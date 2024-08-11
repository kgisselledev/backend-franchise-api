package com.api.franchise.infraestructure;

import com.api.franchise.application.FranchiseService;
import com.api.franchise.application.dto.ProductBranchDTO;
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

import java.util.Comparator;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private static final Logger log = LoggerFactory.getLogger(FranchiseServiceImpl.class);

    @Autowired
    private FranchiseRepositoryDB franchiseRepository;

    @Override
    public Mono<Franchise> addFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise)
                .doOnSuccess(savedFranchise -> log.info("Franquicia: {}", savedFranchise))
                .doOnError(error -> log.error("Error creando Franquicia", error));
    }

    @Override
    public Mono<Branch> addBranchToFranchise(String franchiseName, Branch branch) {
        log.info("Adding branch to franchise: {}", franchiseName);
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    log.info("Franquicia: {}", franchise);
                    franchise.getBranches().add(branch);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(savedFranchise -> Mono.just(branch))
                .doOnSuccess(savedBranch -> log.info("Sucursal: {}", savedBranch))
                .doOnError(error -> log.error("Error creando sucursal", error));
    }

    @Override
    public Mono<Product> addProductToBranch(String franchiseName, String branchName, Product product) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    branch.getProducts().removeIf(product -> product.getName().equals(productName));
                    return franchiseRepository.save(franchise).then();
                })
                .doOnSuccess(aVoid -> log.info("Producto eliminado"))
                .doOnError(error -> log.error("Error al eliminar el producto", error));
    }
    @Override
    public Mono<Product> updateProductStock(String franchiseName, String branchName, String productName, Product updatedProduct) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

                    product.setStock(updatedProduct.getStock());
                    return franchiseRepository.save(franchise);
                })
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    return Mono.just(branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")));
                });
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseName, String newName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }

    @Override
    public Mono<Branch> updateBranchName(String franchiseName, String branchName, String newName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    branch.setName(newName);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(newName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    return Mono.just(branch);
                });
    }

    @Override
    public Mono<Product> updateProductName(String franchiseName, String branchName, String productName, String newName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

                    product.setName(newName);
                    return franchiseRepository.save(franchise);
                })
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));

                    return Mono.just(branch.getProducts().stream()
                            .filter(p -> p.getName().equals(newName))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")));
                });
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll()
                .doOnNext(franchise -> log.info("Lista de franquicias: {}", franchise))
                .doOnError(error -> log.error("Error consultando franquicias", error));
    }

    @Override
    public Flux<ProductBranchDTO> getProductsStocks(String franchiseName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> {
                    return Mono.justOrEmpty(branch.getProducts().stream()
                            .max(Comparator.comparingInt(Product::getStock))
                            .map(product -> new ProductBranchDTO(product.getName(), product.getStock(), branch.getName())));
                });
    }
}
