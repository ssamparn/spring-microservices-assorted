package com.reactive.kafka.microservice.productservice.repository;

import com.reactive.kafka.microservice.productservice.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
