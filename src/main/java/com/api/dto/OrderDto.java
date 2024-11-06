package com.api.dto;

import com.api.domain.enums.OrderStatus;
import com.api.dto.Base.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class OrderDto extends BaseDto {

    private String orderNumber;

    private OrderStatus orderStatus;

    private String customerId;

    private String customerName;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime orderDateTime;

}
