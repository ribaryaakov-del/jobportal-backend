package com.pluralsight.jobportal.config;

import com.pluralsight.jobportal.model.Role;
import com.pluralsight.jobportal.model.User;
import com.pluralsight.jobportal.repository.RoleRepository;
import com.pluralsight.jobportal.repository.UserRepository;
import com.pluralsight.jobportal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${app.cors.protocol}")
    private String protocol;

    @Value("${app.cors.host}")
    private String host;

    @Value("${app.cors.frontend.port}")
    private String frontendPort;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFiler) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/all").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/application/apply/**").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.GET, "/api/auth/details").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req,res,e)->res.sendError(401))
                )
                .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .oauth2Login((oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(userRequest -> {
                                    var delegate = new DefaultOAuth2UserService();
                                    var oauth2User = delegate.loadUser(userRequest);

                                    return new DefaultOAuth2User(
                                            List.of(new SimpleGrantedAuthority("ROLE_APPLICANT")),
                                            oauth2User.getAttributes(),
                                            "email"
                                    );
                                })
                        ).successHandler((request, response, authentication) -> {
                            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");
                            String name = oAuth2User.getAttribute("name");

                            Role applicantRole = roleRepository.findByName("ROLE_APPLICANT")
                                            .orElseThrow(() -> new IllegalArgumentException("Role not found: ROLE_APPLICANT"));

                            userRepository.findByEmail(email).orElseGet(() -> {
                                User newUser = User.builder()
                                        .name(name)
                                        .email(email)
                                        .password(null)
                                        .provider(User.AuthProvider.GOOGLE)
                                        .build();
                                return userRepository.save(newUser);
                            });

                            String token = jwtUtil.generateToken(email);

                            String redirectUrl = protocol + "://" + host +
                                    (frontendPort.isEmpty() ? "" : ":" + frontendPort) +
                                    "/login?token=" + token;

                            response.sendRedirect(redirectUrl);
                        })
                    )
                )
                .build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource(@Value("${app.cors.host}") String corsHost) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://" + corsHost,
                "http://" + corsHost + ":*",
                "https://" + corsHost,
                "https://" + corsHost + ":*"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
