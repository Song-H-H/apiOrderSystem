package com.api.token.controller;

import com.api.core.http.HttpConstants;
import com.api.core.http.HttpUtil;
import com.api.core.http.HttpUtilService;
import com.api.core.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TokenControllerTest {

    @Autowired
    private HttpUtilService httpUtilService;

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    public void 올바른토큰요청(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/getToken")
                .body("userId", "API_USER")
                .body("password", "2024-11-04-API-TOKEN-CREATED-USER-1")
                .build();

        HashMap<String, Object> exchange = httpUtilService.exchange(httpUtil);
        String token = (String) exchange.get("token");

        boolean validateToken = tokenProvider.validateToken(token);

        assertTrue(validateToken);
    }


    @Test
    public void 다른사용자토큰요청(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/getToken")
                .body("userId", "API_USER3")
                .body("password", "2024-11-04-API-TOKEN-CREATED-USER-1")
                .build();

        assertThrows(Exception.class, () -> httpUtilService.exchange(httpUtil));
    }

    @Test
    public void 토큰없이접근제한확인(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/order/findByCustomerId")
                .build();

        assertThrows(Exception.class, () -> httpUtilService.exchange(httpUtil));
    }

    @Test
    public void 잘못된URL요청(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/order/findByCustomerId2")
                .build();

        assertThrows(Exception.class, () -> httpUtilService.exchangeWithRequestToken(httpUtil));
    }

}