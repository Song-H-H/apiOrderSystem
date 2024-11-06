package com.api.token.controller;

import com.api.core.filter.JwtFilter;
import com.api.core.http.HttpConstants;
import com.api.core.jwt.TokenProvider;
import com.api.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @PostMapping("/getToken")
    public TokenDto getToken(@RequestBody TokenDto requestToken) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestToken.getUserId(), requestToken.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String getToken = tokenProvider.createToken(authentication);

        log.info("Security Context '{}'", authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, HttpConstants.HEADER_VALUE_PREFIX + getToken);

        requestToken.setToken(getToken);
        requestToken.setAuth(true);
        return requestToken;
    }
}
