package com.api.franchise.infraestructure;

import com.api.franchise.application.FranchiseService;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private static final Logger log = LoggerFactory.getLogger(FranchiseServiceImpl.class);

    @Autowired
    private FranchiseRepositoryDB franchiseRepository;

    @Override
    public Mono<Franchise> addFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise)
                .doOnSuccess(savedFranchise -> log.info("Franchise added: {}", savedFranchise))
                .doOnError(error -> log.error("Error adding franchise", error));
    }

    @Override
    public Mono<Branch> addBranchToFranchise(String franchiseName, Branch branch) {
        log.info("Adding branch to franchise: {}", franchiseName);
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMap(franchise -> {
                    log.info("Found franchise: {}", franchise);
                    franchise.getBranches().add(branch);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(savedFranchise -> Mono.just(branch))
                .doOnSuccess(savedBranch -> log.info("Branch added: {}", savedBranch))
                .doOnError(error -> log.error("Error adding branch", error));
    }

    @Override
    public Mono<Product> addProductToBranch(String franchiseName, String branchName, Product product) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));

                    branch.getProducts().add(product);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(savedFranchise -> Mono.just(product))
                .doOnSuccess(savedProduct -> log.info("Product added: {}", savedProduct))
                .doOnError(error -> log.error("Error adding product", error));
    }

    @Override
    public Mono<Void> removeProductFromBranch(String franchiseName, String branchName, String productName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));

                    branch.getProducts().removeIf(product -> product.getName().equals(productName));
                    return franchiseRepository.save(franchise).then(); // Use .then() to return Mono<Void>
                })
                .doOnSuccess(aVoid -> log.info("Product removed"))
                .doOnError(error -> log.error("Error removing product", error));
    }

    @Override
    public Mono<Product> updateProductStock(String franchiseName, String branchName, String productName, int newStock) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

                    product.setStock(newStock);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(updatedFranchise -> {
                    Branch branch = updatedFranchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));

                    return Mono.just(branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
                })
                .doOnSuccess(updatedProduct -> log.info("Product stock updated: {}", updatedProduct))
                .doOnError(error -> log.error("Error updating product stock", error));
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll()
                .doOnNext(franchise -> log.info("Retrieved franchise: {}", franchise))
                .doOnError(error -> log.error("Error retrieving franchises", error));
    }
}
