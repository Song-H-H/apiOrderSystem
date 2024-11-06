package com.api.core.http;

import com.api.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;


@Slf4j
@Getter
public class HttpUtil {

    private HttpHeaders headers;
    private HashMap<String, Object> body;
    private StringBuilder urlBuilder;
    private String url;
    private String method;

    private HashMap<String, TypeReference> typeReference;

    public HttpUtil(){
        this.headers = new HttpHeaders();
        this.body = new HashMap<>();
        this.typeReference = new HashMap<>();
    }

    private HttpUtil(HttpHeaders headers, HashMap body, String url, String method, HashMap<String, TypeReference> typeReference) {
        this.headers = headers;
        this.body = body;
        this.url = url;
        this.method = method;
        this.typeReference = typeReference;
        this.contentType("application", "json", "UTF-8");
    }
    public HttpUtil contentType(String type, String subType, String charSet){
        this.headers.setContentType(new MediaType(type, subType, Charset.forName(charSet)));
        return this;
    }

    public HttpUtil url(String uri) {
        this.urlBuilder = new StringBuilder();
        urlBuilder.append(uri);
        this.url = urlBuilder.toString();
        return this;
    }

    public HttpUtil method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public HttpUtil header(String name, String value){
        headers.set(name, value);
        return this;
    }

    public HttpUtil body(String key, String value){
        this.body.put(key, value);
        return this;
    }

    public HttpUtil body(String key, Object value){
        this.body.put(key, value);
        return this;
    }

    public HttpUtil body(HashMap<String, Object> params){
        Iterator<String> itr = params.keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            body.put(key, (String)params.get(key));
        }
        return this;
    }

    public HttpUtil body(Object obejct){
        Class<?> clazz = obejct.getClass();
        List<Field> fields = new ArrayList<>();
        while(clazz != null){	// 1. 상위 클래스가 null 이 아닐때까지 모든 필드를 list 에 담는다.
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        for (Field field : fields) {
            Object value = null;
            field.setAccessible(true);

            try {
                // Field Value를 참조한다.
                value = field.get(obejct);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (!ObjectUtils.isEmpty(value)) {
                body.put(field.getName(), String.valueOf(value));
            }
        }

        return this;
    }

    public HttpUtil typeReference(String key, TypeReference typeReference){
        this.typeReference.put(key, typeReference);
        return this;
    }

    public HttpUtil build(){
        return new HttpUtil(headers, body, url, method, typeReference);
    }

    public String getJsonBody() {
        try {
            return ObjectMapperUtil.objectMapper().writeValueAsString(body).toString();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json 변환 실패");
        }
    }

}
