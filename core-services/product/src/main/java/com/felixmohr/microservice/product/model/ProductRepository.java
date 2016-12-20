package com.felixmohr.microservice.product.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom  {

    List<Product> categoryId(long categoryId);
}
