package com.api.core.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    SYSTEM_ERROR("SYSTEM_ERROR", "There's a problem Please try it later"),
    INVALID_PARAMETER("INVALID_PARAMETER", "Please check the parameter value."),
    INVALID_URL("INVALID_URL", "Please check URL."),

    INVALID_NULL_DATA("INVALID_NULL_DATA", "Please check null data"),
    INVALID_DATA_TYPE("INVALID_DATA_TYPE", "Please check data type  : %s");


    private final String code;
    private final String message;

}
