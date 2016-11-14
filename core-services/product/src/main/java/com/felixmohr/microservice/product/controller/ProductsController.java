package com.felixmohr.microservice.product.controller;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.felixmohr.microservice.product.model.NewProduct;
import com.felixmohr.microservice.product.model.ProductRepository;
import com.felixmohr.microservice.product.model.Product;
import com.felixmohr.microservice.product.model.Search;

@RestController
public class ProductsController {

    @Autowired
    private ProductRepository repo;

    static final Logger logger = LogManager.getLogger(ProductsController.class.getName());

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of all products.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Product.class, responseContainer = "List")})
    public List<Product> getProducts() {
        List<Product> products = (List<Product>) repo.findAll();
        return products;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Accepts a search string, returns all matching products.", notes = "Search string will be split at white spaces. All words in the search string must be found in a product description for the product to be included.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Product.class, responseContainer = "List")})
    @ResponseBody
    public List<Product> searchProducts(@RequestBody Search search) {
        return repo.search(search.getText());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a product", notes = "Returns the created product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal error")})
    @ResponseBody
    public Product create(@RequestBody NewProduct newProduct, HttpServletResponse response) {
        try {
            Product product = new Product(newProduct.getTitle(), newProduct.getPrice(), newProduct.getCategory(), newProduct.getDescription());
            repo.save(product);
            return product;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @ApiOperation(value = "Edits the product details", notes = "Returns the product that has been edited.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 400, message = "Not found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Product edit(@RequestBody Product product, HttpServletResponse response) {
        try {
            if (repo.findOne(product.getId()) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                repo.save(product);
                return product;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the details of the product with the given id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 400, message = "Not found")})
    public Product get(@PathVariable Long id, HttpServletResponse response) {
        Product p = repo.findOne(id);
        if (p == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            return p;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes the product with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Not found"),
            @ApiResponse(code = 500, message = "Failure")})
    public String delete(@PathVariable Long id, HttpServletResponse response) {
        try {
            Product p = repo.findOne(id);
            if (p == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "Not found. Nothing deleted.";
            } else {
                this.repo.delete(id);
                return "Deletion successful.";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
    }

}