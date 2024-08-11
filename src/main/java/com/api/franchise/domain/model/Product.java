package com.api.franchise.domain.model;


import java.util.Objects;

public class Product {
    private String name;
    private int stock;

    public Product() {}

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return stock == product.stock &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stock);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}
