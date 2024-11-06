package com.api.order.controller;


import com.api.dto.OrderDto;
import com.api.dto.SearchOrderDto;
import com.api.order.service.InOrderService;
import com.api.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/in/order")
@RequiredArgsConstructor
public class InOrderController {

    private final InOrderService orderService;

    @PostMapping("/saveOrderDataByOut")
    public Map saveOrderDataByOut(){
        List<OrderDto> orderDtos = orderService.saveOrderDataByOut();
        return ObjectMapperUtil.grabMap("orderList", orderDtos);
    }

    @PostMapping("/findByOrderNumber")
    public Map findByOrderNumber(@RequestBody SearchOrderDto searchOrderDto){
        List<OrderDto> orderDtos =  orderService.findByOrderNumber(searchOrderDto.getSearchOrderNumber());
        return ObjectMapperUtil.grabMap("orderList", orderDtos);
    }

    @PostMapping("/findByCustomerId")
    public Map findByCustomerId(@RequestBody SearchOrderDto searchOrderDto){
        List<OrderDto> orderDtos =  orderService.findByCustomerId(searchOrderDto.getSearchCustomerId());
        return ObjectMapperUtil.grabMap("orderList", orderDtos);
    }

    @PostMapping("/sendDataToOut")
    public void sendDataToOut(){
         orderService.sendDataToOut();
    }

}
