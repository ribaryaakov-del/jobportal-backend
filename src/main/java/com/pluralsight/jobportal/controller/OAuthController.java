package com.pluralsight.jobportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/oauth")
public class OAuthController {


    @GetMapping("/google")
    public Map<String, Object> getGoogleInfo(
            @AuthenticationPrincipal OAuth2User principal
    ) {
        return principal.getAttributes();
    }


}
