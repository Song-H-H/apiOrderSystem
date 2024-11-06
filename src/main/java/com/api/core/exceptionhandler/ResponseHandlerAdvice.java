package com.api.core.exceptionhandler;

import com.api.core.http.HttpConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ResponseHandlerAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof ErrorResult){
            return body;
        }

        Map map = new HashMap<>();
        map.putIfAbsent(HttpConstants.RESPONSE_CODE, HttpStatus.OK.value());
        map.putIfAbsent(HttpConstants.RESPONSE_MESSAGE, HttpStatus.OK.getReasonPhrase());

        if(!ObjectUtils.isEmpty(body)){
            map.putIfAbsent("data", body);
        }

        return map;
    }
}
