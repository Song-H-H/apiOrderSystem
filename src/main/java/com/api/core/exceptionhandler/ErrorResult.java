package com.api.core.exceptionhandler;

import com.api.core.http.HttpConstants;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResult {

    private int responseCode;
    private String responseMessage;


    @JsonGetter(HttpConstants.RESPONSE_CODE)
    public int getResponseCode() {
        return responseCode;
    }

    @JsonGetter(HttpConstants.RESPONSE_MESSAGE)
    public String getResponseMessage() {
        return responseMessage;
    }

}
