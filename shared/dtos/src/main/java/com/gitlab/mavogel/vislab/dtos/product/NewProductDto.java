package com.gitlab.mavogel.vislab.dtos.product;


/**
 * Used for the creation of a new product.
 */
public class NewProductDto {

    private String name;
    private double price;
    private long category;
    private String details;

    public NewProductDto() {
    }

    public NewProductDto(String name, Long price, long category, String details) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name + ": " + price + ": " + category + ": ";
    }

}