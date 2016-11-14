package com.felixmohr.microservice.product.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class ProductRepositoryImpl implements ProductRepositoryCustom {
	
	@Autowired
	private ProductRepository repo;

	public List<Product> search(String search) {
		List<Product> products = (List<Product>) repo.findAll();
		List<Product> ret = new ArrayList<Product>();
		for (Product p : products) {
			String[] splitDescription = p.getDetails().split(" ");
			String[] splitSearch = search.split(" ");
			boolean add = true;
			for (String i : splitSearch) {
				boolean contained = false;
				for (String j : splitDescription) {
					if (i.equals(j)) {
						contained = true;
						break;
					}
				}
				if (!contained) {
					add = false;
					break;
				}
			}
			if (add) {
				ret.add(p);
			}
		}
		return ret;
	}

}
