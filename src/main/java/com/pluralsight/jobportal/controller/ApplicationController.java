package com.pluralsight.jobportal.controller;


import com.pluralsight.jobportal.model.Application;
import com.pluralsight.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<Application> applyForJob(
            Principal principal,
            @PathVariable Long jobId){
        String username = principal.getName();

        return ResponseEntity.ok(applicationService.applyForJob(username, jobId));
    }
}
