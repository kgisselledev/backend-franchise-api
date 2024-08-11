package com.api.franchise.domain.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
//@Document(collection = "branches")
public class Branch {
    private String name;
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
