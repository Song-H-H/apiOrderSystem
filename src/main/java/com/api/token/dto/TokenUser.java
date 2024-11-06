package com.api.token.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;


public class TokenUser extends User {
    public TokenUser(String username, String password, List<String> authorities) {
        super(username, password, authorities.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
    }
}
