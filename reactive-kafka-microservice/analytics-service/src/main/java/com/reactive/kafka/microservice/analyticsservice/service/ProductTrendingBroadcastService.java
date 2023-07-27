package com.reactive.kafka.microservice.analyticsservice.service;

import com.reactive.kafka.microservice.analyticsservice.dto.ProductTrendingDto;
import com.reactive.kafka.microservice.analyticsservice.repository.ProductViewRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ProductTrendingBroadcastService {

    private final ProductViewRepository productViewRepository;
    private Flux<List<ProductTrendingDto>> trends;

    @PostConstruct
    private void init() {
        this.trends = this.productViewRepository.findTop5ByOrderByCountDesc()
                .map(pvc -> new ProductTrendingDto(pvc.getId(), pvc.getCount()))
                .collectList()
                .filter(Predicate.not(List::isEmpty))
                .repeatWhen(l -> l.delayElements(Duration.ofSeconds(3)))
                .distinctUntilChanged()
                .cache(1);
    }

    public Flux<List<ProductTrendingDto>> getTrends(){
        return this.trends;
    }

}
