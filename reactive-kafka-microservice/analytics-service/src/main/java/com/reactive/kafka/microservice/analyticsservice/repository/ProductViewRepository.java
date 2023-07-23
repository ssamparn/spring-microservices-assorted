package com.reactive.kafka.microservice.analyticsservice.repository;

import com.reactive.kafka.microservice.analyticsservice.entity.ProductViewCount;
import com.reactive.kafka.microservice.analyticsservice.event.ProductViewEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductViewRepository extends ReactiveCrudRepository<ProductViewEvent, Integer> {
    Flux<ProductViewCount> findTop5ByOrderByCountDesc();

}
