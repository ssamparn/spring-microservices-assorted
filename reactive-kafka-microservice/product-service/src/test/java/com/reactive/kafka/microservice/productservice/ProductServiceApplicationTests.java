package com.reactive.kafka.microservice.productservice;

import com.reactive.kafka.microservice.productservice.event.ProductViewEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
class ProductServiceApplicationTests extends AbstractIntegrationTest {

	@Autowired
	private WebTestClient client;

	@Test
	void contextLoads() {

	}

	@Test
	void productViewAndEventTest() {
		// view products
		viewProductSuccess(1);
		viewProductSuccess(2);
		viewProductError(1000);
		viewProductSuccess(5);
		viewProductSuccess(10);

		// check if the events are emitted
		var flux = this.<ProductViewEvent>createReceiver(PRODUCT_VIEW_EVENTS)
				.receive()
				.take(4);

		StepVerifier.create(flux)
				.consumeNextWith(r -> Assertions.assertEquals(1, r.value().getProductId()))
				.consumeNextWith(r -> Assertions.assertEquals(2, r.value().getProductId()))
				.consumeNextWith(r -> Assertions.assertEquals(5, r.value().getProductId()))
				.consumeNextWith(r -> Assertions.assertEquals(10, r.value().getProductId()))
				.verifyComplete();

	}

	private void viewProductSuccess(int id) {
		this.client
				.get()
				.uri("/product/" + id)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody()
				.jsonPath("$.id").isEqualTo(id)
				.jsonPath("$.description").isEqualTo("product-" + id);
	}

	private void viewProductError(int id) {
		this.client
				.get()
				.uri("/product/" + id)
				.exchange()
				.expectStatus().is4xxClientError();
	}

}
