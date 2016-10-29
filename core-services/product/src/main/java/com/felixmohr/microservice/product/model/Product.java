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
    
    private String title;
    
	private Long price;
	
	private long category;
	
	private String description;

 	public Product() {}
 
    public Product(String title, Long price, long category, String description) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.description = description;
    }
    
    public String getTitle() {
		return title;
	}
    
    public Long getId() {
    	return this.id;
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