package com.reactive.kafka.microservice.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTrendingDto {
    private Integer productId;
    private Long viewCount;
}
