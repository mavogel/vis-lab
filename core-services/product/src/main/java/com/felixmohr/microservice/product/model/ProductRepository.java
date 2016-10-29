package com.felixmohr.microservice.product.model;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom  {

}
