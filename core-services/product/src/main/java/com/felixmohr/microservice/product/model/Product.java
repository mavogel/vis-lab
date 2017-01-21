package com.felixmohr.microservice.product.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double price;
    private String details;
    private long categoryId;

    public Product() {
    }

    public Product(String name, double price, long categoryId, String details) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public void setCategoryId(final long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "[" + id + "] - " + name + ": " + price + ": " + categoryId + ": ";
    }

}