package com.felixmohr.microservice.product.model;

import java.util.List;

public interface ProductRepositoryCustom {

	public List<Product> search(String search);
	
}
