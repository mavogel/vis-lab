package com.felixmohr.microservice.product.model;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> search(String search);

    List<Product> findByCategoryId(long categoryId);
}
