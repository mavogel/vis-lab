package com.felixmohr.microservice.product.model;


 
public class NewProduct {
    
    private String title;
    
	private Long price;
	
	private long category;
	
	private String description;

 	public NewProduct() {}
 
    public NewProduct(String title, Long price, long category, String description) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.description = description;
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	
    public long getCategory() {
		return category;
	}

	public void setCategory(long category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return title + ": " + price + ": " + category + ": ";
	}
 
}