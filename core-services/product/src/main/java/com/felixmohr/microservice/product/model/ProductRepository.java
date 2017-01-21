package com.felixmohr.microservice.product.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * Finds product by their category id
     *
     * @param categoryId the category id
     * @return the Products fulfilling this criteria
     */
    List<Product> findByCategoryId(long categoryId);

    /**
     * Finds products by its details containing a string (LIKE %details%) and the price
     * between a definde range.
     *
     * @param details the details to contain
     * @param minPrice the min price
     * @param maxPrice the max price
     * @return the Products fulfilling these criteria
     */
    List<Product> findByDetailsContainingAndPriceBetween(String details, double minPrice, double maxPrice);
}
