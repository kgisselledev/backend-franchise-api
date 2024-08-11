package com.api.franchise.application;

import com.api.franchise.application.dto.ProductBranchDTO;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises")
public class FranchiseController {

    @Autowired
    private FranchiseService franchiseService;

    @PostMapping
    public Mono<Franchise> addFranchise(@RequestBody Franchise franchise) {
        return franchiseService.addFranchise(franchise);
    }

    @PostMapping("/{franchiseName}/branches")
    public Mono<Branch> addBranchToFranchise(@PathVariable String franchiseName, @RequestBody Branch branch) {
        return franchiseService.addBranchToFranchise(franchiseName, branch);
    }

    @PostMapping("/{franchiseName}/branches/{branchName}/products")
    public Mono<Product> addProductToBranch(@PathVariable String franchiseName, @PathVariable String branchName, @RequestBody Product product) {
        return franchiseService.addProductToBranch(franchiseName, branchName, product);
    }

    @DeleteMapping("/{franchiseName}/branches/{branchName}/products/{productName}")
    public Mono<Void> removeProductFromBranch(@PathVariable String franchiseName, @PathVariable String branchName, @PathVariable String productName) {
        return franchiseService.removeProductFromBranch(franchiseName, branchName, productName);
    }

    @PutMapping("/{franchiseName}/branches/{branchName}/products/{productName}/stock")
    public Mono<Product> updateProductStock(@PathVariable String franchiseName, @PathVariable String branchName, @PathVariable String productName, @RequestBody Product updatedProduct) {
        return franchiseService.updateProductStock(franchiseName, branchName, productName, updatedProduct);
    }

    @PutMapping("/{franchiseName}")
    public Mono<Franchise> updateFranchiseName(@PathVariable String franchiseName, @RequestBody Franchise updatedFranchise) {
        return franchiseService.updateFranchiseName(franchiseName, updatedFranchise.getName());
    }

    @PutMapping("/{franchiseName}/branches/{branchName}")
    public Mono<Branch> updateBranchName(@PathVariable String franchiseName, @PathVariable String branchName, @RequestBody Branch updatedBranch) {
        return franchiseService.updateBranchName(franchiseName, branchName, updatedBranch.getName());
    }

    @PutMapping("/{franchiseName}/branches/{branchName}/products/{productName}/name")
    public Mono<Product> updateProductName(@PathVariable String franchiseName, @PathVariable String branchName, @PathVariable String productName, @RequestBody Product updatedProduct) {
        return franchiseService.updateProductName(franchiseName, branchName, productName, updatedProduct.getName());
    }

    @GetMapping
    public Flux<Franchise> getAllFranchises() {
        return franchiseService.getAllFranchises();
    }

    @GetMapping("/{franchiseName}/products")
    public Flux<ProductBranchDTO> getProductsStocks(@PathVariable String franchiseName){
        return franchiseService.getProductsStocks(franchiseName);
    }
}

