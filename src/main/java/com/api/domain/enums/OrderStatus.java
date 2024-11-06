package com.api.domain.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {

    ORDER_1000("ORDER_1000", "배송 준비 중"),
    ORDER_2000("ORDER_2000", "배송 중"),
    ORDER_3000("ORDER_3000", "배송 보류"),
    ORDER_4000("ORDER_4000", "배송 완료"),
    ORDER_5000("ORDER_5000", "구매 확정");

    private final String code;

    @JsonValue
    private final String message;


}
