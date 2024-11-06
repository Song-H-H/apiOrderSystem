package com.api.order.service;


import com.api.core.exception.BadRequestException;
import com.api.core.exceptionhandler.ErrorCode;
import com.api.core.http.HttpUtil;
import com.api.core.http.HttpUtilService;
import com.api.dto.OrderDto;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class InOrderService {

    private final HttpUtilService httpUtilService;

    public static final Map<String, OrderDto> orderMemory = new LinkedHashMap<>();

    public List<OrderDto> saveOrderDataByOut(){
        List<OrderDto> orderDtoList = requestOrderData();
        saveOrderData(orderDtoList);

        return orderDtoList;
    }

    public List<OrderDto> findByOrderNumber(List<String> orderNumber){
        List<OrderDto> orderList = new ArrayList<>();

        for(String orderDto : orderNumber){
            OrderDto order = orderMemory.getOrDefault(orderDto, OrderDto.builder().orderNumber(orderDto).build());
            orderList.add(order);
        }

        return orderList;
    }

    public List<OrderDto> findByCustomerId(String customerId){
        return orderMemory.values().stream().filter(x -> x.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }

    public void saveOrderData(List<OrderDto> orderDtoList){
        for(OrderDto orderDto : orderDtoList){
            String orderNumber = orderDto.getOrderNumber();

            if(ObjectUtils.isEmpty(orderNumber)){
                throw new BadRequestException(ErrorCode.INVALID_NULL_DATA);
            }
            if(ObjectUtils.isEmpty(orderDto.getOrderStatus())){
                throw new BadRequestException(ErrorCode.INVALID_DATA_TYPE, orderNumber);
            }
            log.info("saveOrderData OrderNumber : {}", orderNumber);

            if(orderMemory.containsKey(orderNumber)){
                orderDto.setCreatedDateTime(orderMemory.get(orderNumber).getOrderDateTime());
                orderDto.setCreatedBy(orderMemory.get(orderNumber).getCreatedBy());
                orderMemory.remove(orderNumber);
            }else{
                orderDto.setCreatedBy(orderDto.currentAuditor());
            }
            orderDto.setLastModifiedBy(orderDto.currentAuditor());

            orderMemory.put(orderNumber, orderDto);
        }
    }

    public List<OrderDto> requestOrderData(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/out/order/makeTempData")
                .typeReference("orderList", new TypeReference<List<OrderDto>>() {})
                .build();

        HashMap<String, Object> result = httpUtilService.exchangeWithRequestToken(httpUtil);
        return (List<OrderDto>) result.get("orderList");
    }

    public void sendDataToOut(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/out/order/loggingDataByIn")
                .body("orderList", orderMemory.values())
                .build();

        httpUtilService.exchangeWithRequestToken(httpUtil);
    }

}
