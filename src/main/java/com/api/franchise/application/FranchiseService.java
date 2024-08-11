package com.api.franchise.application;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {
    Mono<Franchise> addFranchise(Franchise franchise);
    Mono<Branch> addBranchToFranchise(String franchiseName, Branch branch);
    Mono<Product> addProductToBranch(String franchiseName, String branchName, Product product);
    Mono<Void> removeProductFromBranch(String franchiseName, String branchName, String productName);
    Mono<Product> updateProductStock(String franchiseName, String branchName, String productName, int newStock);
    Flux<Franchise> getAllFranchises();
}
