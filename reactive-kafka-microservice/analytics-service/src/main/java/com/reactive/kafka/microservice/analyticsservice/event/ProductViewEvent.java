package com.reactive.kafka.microservice.analyticsservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewEvent {

    private Integer productId;
}
