package com.pluralsight.jobportal.config;

import com.pluralsight.jobportal.model.Job;
import com.pluralsight.jobportal.model.Role;
import com.pluralsight.jobportal.model.User;
import com.pluralsight.jobportal.repository.JobRepository;
import com.pluralsight.jobportal.repository.RoleRepository;
import com.pluralsight.jobportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               JobRepository jobRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder){
       return args -> {
           Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                   .orElseGet(() -> roleRepository.save(
                           Role.builder().name("ROLE_ADMIN").build()));

           Role applicantRole = roleRepository.findByName("ROLE_APPLICANT")
                   .orElseGet(() -> roleRepository.save(
                           Role.builder().name("ROLE_APPLICANT").build()));

           if(!userRepository.existsByEmail("user@example.com")){
               User user = User.builder()
                       .name("User")
                       .email("user@example.com")
                       .password(passwordEncoder.encode("user123"))
                       .provider(User.AuthProvider.LOCAL)
                       .build();

               user.getRoles().add(applicantRole);

               userRepository.save(user);
           }

           if(!userRepository.existsByEmail("admin@example.com")){
               User admin = User.builder()
                       .name("Admin")
                       .email("admin@example.com")
                       .password(passwordEncoder.encode("admin123"))
                       .provider(User.AuthProvider.LOCAL)
                       .build();

               admin.getRoles().add(adminRole);

               userRepository.save(admin);
           }

           if(!jobRepository.existsByTitle("Backend Developer")){
               Job backendJob = Job.builder()
                       .title("Backend Developer")
                       .description("Develop and maintain backend systems using Java and Spring")
                       .company("Tech Corp")
                       .postedDate(LocalDate.now())
                       .build();

               jobRepository.save(backendJob);
           }

           if(!jobRepository.existsByTitle("Security Analyst")){
               Job securityJob = Job.builder()
                       .title("Security Analyst")
                       .description("Conduct security assessments and implement security measures")
                       .company("CyberX")
                       .postedDate(LocalDate.now())
                       .build();

               jobRepository.save(securityJob);
           }

           if(!jobRepository.existsByTitle("Frontend Developer")){
               Job frontendJob = Job.builder()
                       .title("Frontend Developer")
                       .description("Design and develop user interfaces using React and TypeScript")
                       .company("Frontier Tech")
                       .postedDate(LocalDate.now())
                       .build();

               jobRepository.save(frontendJob);
           }

           if(!jobRepository.existsByTitle("Software Tester")) {
               Job softwareTesterJob = Job.builder()
                       .title("Software Tester")
                       .description("Conduct software testing and identify bugs and issues")
                       .company("Quality Assurance Inc.")
                       .postedDate(LocalDate.now())
                       .build();

               jobRepository.save(softwareTesterJob);
           }
       };
    }
}
