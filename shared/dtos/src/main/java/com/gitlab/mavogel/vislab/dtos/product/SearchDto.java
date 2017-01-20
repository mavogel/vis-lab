package com.gitlab.mavogel.vislab.dtos.product;

public class SearchDto {
    private String text;
    private double searchMinPrice;
    private double searchMaxPrice;

    public SearchDto() {
    }

    public SearchDto(final String text, final double searchMinPrice, final double searchMaxPrice) {
        this.text = text;
        this.searchMinPrice = searchMinPrice;
        this.searchMaxPrice = searchMaxPrice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSearchMinPrice() {
        return searchMinPrice;
    }

    public void setSearchMinPrice(final double searchMinPrice) {
        this.searchMinPrice = searchMinPrice;
    }

    public double getSearchMaxPrice() {
        return searchMaxPrice;
    }

    public void setSearchMaxPrice(final double searchMaxPrice) {
        this.searchMaxPrice = searchMaxPrice;
    }
}
