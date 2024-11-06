package com.api.core.http;

import com.api.core.exception.BadRequestException;
import com.api.core.exceptionhandler.ErrorCode;
import com.api.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpUtilService {

    private final RestTemplateConfig restTemplateConfig;

    public HashMap<String, Object> exchangeWithRequestToken(HttpUtil httpUtil) {
        String token = getToken();
        httpUtil.header(HttpConstants.AUTHORIZATION, HttpConstants.HEADER_VALUE_PREFIX + token);

        return exchange(httpUtil);
    }

    public String getToken() {
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/getToken")
                .body("userId", "API_USER")
                .body("password", "2024-11-04-API-TOKEN-CREATED-USER-1")
                .build();

        HashMap<String, Object> exchange = exchange(httpUtil);
        return  (String) exchange.get("token");
    }

    public HashMap<String, Object> exchange(HttpUtil httpUtil) {
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate();

        RequestEntity<String> requestEntity = RequestEntity
                .post(httpUtil.getUrl())
                .headers(httpUtil.getHeaders())
                .body(httpUtil.getJsonBody());

        try{
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            return convertResponseBoby(response, httpUtil.getTypeReference());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    HashMap<String, Object> convertResponseBoby(ResponseEntity<String> response, HashMap<String, TypeReference> typeReference){
        try {
            ObjectMapper mapper = ObjectMapperUtil.objectMapper();
            HashMap readValue = mapper.readValue(response.getBody(), HashMap.class);

            int responseCode = (int) readValue.get(HttpConstants.RESPONSE_CODE);
            if(ObjectUtils.isEmpty(responseCode)) throw new BadRequestException(String.valueOf(readValue.get(HttpConstants.RESPONSE_MESSAGE)));
            if(HttpStatus.OK.value() != responseCode) throw new BadRequestException(String.valueOf(readValue.get(HttpConstants.RESPONSE_MESSAGE)));

            readValue = (HashMap) readValue.get(HttpConstants.RESPONSE_DATA);

            if(typeReference != null){
                HashMap<String, TypeReference> typeReferenceMap = typeReference;

                for( String strKey : typeReferenceMap.keySet() ){
                    Object detailInfo = readValue.get(strKey);

                    if(!ObjectUtils.isEmpty(detailInfo)){
                        String writeValueAsString = mapper.writeValueAsString(detailInfo);
                        TypeReference classType = typeReferenceMap.get(strKey);
                        Object convertData = mapper.readValue(writeValueAsString, classType);
                        readValue.put(strKey, convertData);
                    }
                }
            }

            return readValue;
        } catch (IOException e) {
            log.error("HttpClientErrorException : {}", e.getMessage());
            throw new BadRequestException(ErrorCode.INVALID_PARAMETER);
        }
    }
}
