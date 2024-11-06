package com.api.core.jwt;

import java.util.Arrays;
import java.util.List;

public enum JwtAuthUser {
    API_USER("API_USER","$2a$10$YvwT9XUn8..NLVCVnMu5GeukKZkCIzjllg2AZAyG.5cTK6rh2oka6", Arrays.asList("ROLE")),
    API_USER2("API_USER2","$2a$10$YvwT9XUn8..NLVCVnMu5GeukKZkCIzjllg2AZAyG.5cTK6rh2oka6", Arrays.asList("ROLE"));

    private final String userName;
    private final String password;
    private final List<String> roles;

    JwtAuthUser(String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public String getKey() {
        return name();
    }

    public String getPassword() {
        return this.password;
    }

    public List<String> getRoles() { return this.roles;}
}
