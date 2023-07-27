package com.reactive.kafka.microservice.analyticsservice.service;

import com.reactive.kafka.microservice.analyticsservice.entity.ProductViewCount;
import com.reactive.kafka.microservice.analyticsservice.event.ProductViewEvent;
import com.reactive.kafka.microservice.analyticsservice.repository.ProductViewRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductViewEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductViewEventConsumer.class);

    private final ReactiveKafkaConsumerTemplate<String, ProductViewEvent> kafkaConsumerTemplate;
    private final ProductViewRepository productViewRepository;

    private static Integer apply(ProductViewEvent id) {
        return new ProductViewCount().getId();
    }

    @PostConstruct
    public void subscribe() {
        this.kafkaConsumerTemplate
                .receive()
                .bufferTimeout(1000, Duration.ofSeconds(1))
                .flatMap(this::process)
                .subscribe();
    }

    /*
     * We have created the following map as eventsMap
     * {
     *       2 : 5
     *      10 : 172
     *       8 : 120
     * }
     *
     * database:
     *
     * {
     *      10 : 3
     *       8 : 2
     * }
     *
     * update the database:
     *
     * {
     *       2 : 5
     *      10 : 175
     *       8 : 122
     * }
     * */

    private Mono<Void> process(List<ReceiverRecord<String, ProductViewEvent>> events) {
        Map<Integer, Long> eventsMap = events.stream()
                .map(r -> r.value().getProductId())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return this.productViewRepository.findAllById(eventsMap.keySet()) // what if there are no records
                .collectMap(productViewCount -> productViewCount.getId())
                .defaultIfEmpty(Collections.emptyMap())
                .map(dbMap -> eventsMap.keySet().stream().map(productId -> updateViewCount(dbMap, eventsMap, productId)).collect(Collectors.toList()))
                .flatMapMany(this.productViewRepository::saveAll)
                .doOnComplete(() -> events.get(events.size() - 1).receiverOffset().acknowledge())
                .doOnError(ex -> log.error(ex.getMessage()))
                .then();
    }

    private ProductViewCount updateViewCount(Map<Integer, ProductViewCount> dbMap, Map<Integer, Long> eventMap, int productId){
        ProductViewCount productViewCount = dbMap.getOrDefault(productId, new ProductViewCount(productId, 0L, true));
        productViewCount.setCount(productViewCount.getCount() + eventMap.get(productId));
        return productViewCount;
    }

}
