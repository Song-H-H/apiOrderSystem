package com.api.order.controller;


import com.api.dto.OrderDto;
import com.api.dto.OrderDtoList;
import com.api.order.service.OutOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/out/order")
@RequiredArgsConstructor
public class OutOrderController {

    private final OutOrderService orderService;


    @PostMapping("/makeTempData")
    public Map<String, List> makeTempData(){
        return orderService.makeTempData();
    }

    @PostMapping("/loggingDataByIn")
    public void loggingDataByIn(@RequestBody OrderDtoList orderDtoList){
        for (OrderDto orderDto : orderDtoList.getOrderList()) {
            log.info("orderNumber : {}", orderDto.toString());
        }
    }

}
