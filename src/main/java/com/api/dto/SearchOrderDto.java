package com.api.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchOrderDto {

    private List<String> searchOrderNumber;

    private String searchCustomerId;
}
