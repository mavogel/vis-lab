package com.gitlab.mavogel.vislab.dtos.product;

public class SearchDto {

	public SearchDto() {}

	public SearchDto(String text) {
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
