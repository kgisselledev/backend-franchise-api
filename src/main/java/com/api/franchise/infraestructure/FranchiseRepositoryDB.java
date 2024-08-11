package com.api.franchise.infraestructure;

import com.api.franchise.domain.model.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryDB extends ReactiveMongoRepository<Franchise, String> {
    Mono<Franchise> findByName(String name);
}
