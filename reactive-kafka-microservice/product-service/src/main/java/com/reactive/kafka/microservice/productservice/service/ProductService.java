package com.reactive.kafka.microservice.productservice.service;

import com.reactive.kafka.microservice.productservice.dto.ProductDto;
import com.reactive.kafka.microservice.productservice.event.ProductViewEvent;
import com.reactive.kafka.microservice.productservice.repository.ProductRepository;
import com.reactive.kafka.microservice.productservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductViewEventProducer productViewEventProducer;

    public Mono<ProductDto> getProduct(Integer productId) {
        return this.productRepository.findById(productId)
                .doOnNext(e -> this.productViewEventProducer.emitEvent(new ProductViewEvent(e.getId())))
                .map(EntityDtoUtil::toDto);
    }
}
