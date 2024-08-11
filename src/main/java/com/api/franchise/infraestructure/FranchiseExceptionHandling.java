package com.api.franchise.infraestructure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class FranchiseExceptionHandling {
    @ExceptionHandler(RuntimeException.class)
    public Mono<Void> handleRuntimeException(RuntimeException ex) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
}
