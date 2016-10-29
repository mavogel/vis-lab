package com.felixmohr.microservice.product.model;

public class Search {
	
	public Search() {}
	
	public Search(String text) {
		this.text = text;
	}
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
