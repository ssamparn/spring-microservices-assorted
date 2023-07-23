package com.reactive.kafka.microservice.productservice.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String description;
    private Integer price;
}
