package com.api.order.service;


import com.api.core.exception.BadRequestException;
import com.api.core.exceptionhandler.ErrorCode;
import com.api.core.http.HttpUtil;
import com.api.core.http.HttpUtilService;
import com.api.domain.enums.OrderStatus;
import com.api.dto.OrderDto;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutOrderService {

    private final HttpUtilService httpUtilService;

    public Map<String, List> makeTempData(){
        List<OrderDto> orderList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            UUID uuid = UUID.randomUUID();
            Random random = new Random();

            OrderDto order = OrderDto.builder()
                    .orderNumber(uuid.toString())
                    .orderStatus(OrderStatus.ORDER_1000)
                    .customerId(String.valueOf(random.nextInt(10)))
                    .customerName("customerName" + i)
                    .orderDateTime(LocalDateTime.now())
                    .build();
            orderList.add(order);
        }

        Map<String, List> detailInfo = new HashMap<>();
        detailInfo.put("orderList", orderList);

        return detailInfo;
    }

}
