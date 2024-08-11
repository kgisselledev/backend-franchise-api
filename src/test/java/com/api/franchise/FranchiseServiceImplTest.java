package com.api.franchise;

import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Product;
import com.api.franchise.infraestructure.FranchiseRepositoryDB;
import com.api.franchise.infraestructure.FranchiseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FranchiseServiceImplTest {
    @Mock
    private FranchiseRepositoryDB franchiseRepository;

    @InjectMocks
    private FranchiseServiceImpl franchiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFranchise() {
        Franchise franchise = new Franchise();
        franchise.setName("Franchise1");

        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseService.addFranchise(franchise);

        StepVerifier.create(result)
                .expectNextMatches(savedFranchise -> savedFranchise.getName().equals("Franchise1"))
                .verifyComplete();
    }
}
