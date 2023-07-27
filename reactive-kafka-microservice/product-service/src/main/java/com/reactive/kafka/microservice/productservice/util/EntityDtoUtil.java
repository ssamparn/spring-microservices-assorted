package com.reactive.kafka.microservice.productservice.util;

import com.reactive.kafka.microservice.productservice.dto.ProductDto;
import com.reactive.kafka.microservice.productservice.entity.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoUtil {
    public static ProductDto toDto(Product product){
        var dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

}
