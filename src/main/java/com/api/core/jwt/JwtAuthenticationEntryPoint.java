package com.api.core.jwt;

import com.api.core.exceptionhandler.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("JwtAuthenticationEntryPoint : {} ",authException.getMessage());
        ErrorResult errorResult = new ErrorResult(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(errorResult);

        response.setStatus(errorResult.getResponseCode());
        response.getWriter().write(responseBody);
    }
}
