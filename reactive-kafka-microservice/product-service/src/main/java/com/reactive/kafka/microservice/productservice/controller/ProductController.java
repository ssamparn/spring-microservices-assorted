package com.reactive.kafka.microservice.productservice.controller;

import com.reactive.kafka.microservice.productservice.dto.ProductDto;
import com.reactive.kafka.microservice.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductDto>> viewProduct(@PathVariable(name = "productId") Integer productId) {
        return this.productService.getProduct(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
