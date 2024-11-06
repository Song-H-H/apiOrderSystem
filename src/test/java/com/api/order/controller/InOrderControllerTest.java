package com.api.order.controller;

import com.api.core.http.HttpUtil;
import com.api.core.http.HttpUtilService;
import com.api.dto.OrderDto;
import com.api.dto.SearchOrderDto;
import com.api.order.service.InOrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InOrderControllerTest {

    @Autowired
    HttpUtilService httpUtilService;
    @Autowired
    InOrderService inOrderService;

    @Test
    public void 주문데이터저장(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/in/order/saveOrderDataByOut")
                .typeReference("orderList", new TypeReference<List<OrderDto>>() {})
                .build();

        HashMap<String, Object> result = httpUtilService.exchangeWithRequestToken(httpUtil);
        List<OrderDto> orderList = (List<OrderDto>) result.get("orderList");

        assertNotNull(orderList);
    }

    @Test
    public void 주문데이터검색(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/in/order/saveOrderDataByOut")
                .typeReference("orderList", new TypeReference<List<OrderDto>>() {})
                .build();

        HashMap<String, Object> result = httpUtilService.exchangeWithRequestToken(httpUtil);
        List<OrderDto> orderList = (List<OrderDto>) result.get("orderList");

        List<OrderDto> byOrderNumber = inOrderService.findByOrderNumber(Collections.singletonList(orderList.get(0).getOrderNumber()));

        assertNotNull(byOrderNumber);
    }

    @Test
    public void 고객주문데이터검색(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/in/order/saveOrderDataByOut")
                .typeReference("orderList", new TypeReference<List<OrderDto>>() {})
                .build();

        HashMap<String, Object> result = httpUtilService.exchangeWithRequestToken(httpUtil);
        List<OrderDto> orderList = (List<OrderDto>) result.get("orderList");

        SearchOrderDto searchOrderDto = SearchOrderDto.builder().searchCustomerId(orderList.get(0).getCustomerId()).build();

        HttpUtil httpUtil2 = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/in/order/findByCustomerId")
                .body(searchOrderDto)
                .typeReference("orderList", new TypeReference<List<OrderDto>>() {})
                .build();

        HashMap<String, Object> result2 = httpUtilService.exchangeWithRequestToken(httpUtil2);
        List<OrderDto> orderList2 = (List<OrderDto>) result2.get("orderList");

        long count = orderList2.stream().map(x -> x.getCustomerId()).distinct().count();
        assertEquals(1, count);
    }

    @Test
    public void 주문데이터외부전송(){
        HttpUtil httpUtil = new HttpUtil()
                .method(HttpMethod.POST.name())
                .url("http://localhost:8080/api/in/order/sendDataToOut")
                .build();

        HashMap<String, Object> result = httpUtilService.exchangeWithRequestToken(httpUtil);

    }

}