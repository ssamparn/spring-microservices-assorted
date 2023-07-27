package com.reactive.kafka.microservice.productservice.service;

import com.reactive.kafka.microservice.productservice.event.ProductViewEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderRecord;

@RequiredArgsConstructor
public class ProductViewEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductViewEventProducer.class);

    private final ReactiveKafkaProducerTemplate<String, ProductViewEvent> reactiveKafkaProducerTemplate;
    private final Sinks.Many<ProductViewEvent> productViewEventSink;
    private final Flux<ProductViewEvent> productViewEventFlux;
    private final String topicName;

    public void subscribe() {
        var srFlux = this.productViewEventFlux
                .map(e -> new ProducerRecord<>(topicName, e.getProductId().toString(), e))
                .map(pr -> SenderRecord.create(pr, pr.key()));

        this.reactiveKafkaProducerTemplate.send(srFlux)
                .doOnNext(r -> log.info("emitted event: {}", r.correlationMetadata()))
                .subscribe();
    }

    public void emitEvent(ProductViewEvent event) {
        this.productViewEventSink.tryEmitNext(event);
    }


}
