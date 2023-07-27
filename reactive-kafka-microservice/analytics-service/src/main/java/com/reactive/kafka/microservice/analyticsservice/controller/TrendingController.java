package com.reactive.kafka.microservice.analyticsservice.controller;

import com.reactive.kafka.microservice.analyticsservice.dto.ProductTrendingDto;
import com.reactive.kafka.microservice.analyticsservice.service.ProductTrendingBroadcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trending")
public class TrendingController {

    private final ProductTrendingBroadcastService trendingBroadcastService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<ProductTrendingDto>> trending() {
        return this.trendingBroadcastService.getTrends();
    }

}
