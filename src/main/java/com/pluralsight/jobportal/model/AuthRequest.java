package com.pluralsight.jobportal.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    public String email;
    public String password;
}
