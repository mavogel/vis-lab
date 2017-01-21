package com.felixmohr.microservice.product.controller;

import com.felixmohr.microservice.product.model.Product;
import com.felixmohr.microservice.product.model.ProductRepository;
import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private ModelMapper mapper;

    private static final Logger LOG = LogManager.getLogger(ProductsController.class.getName());

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of all products.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> productDtos = ((List<Product>) repo.findAll()).stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a product", notes = "Returns the created product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal error")})
    @ResponseBody
    public ResponseEntity<ProductDto> create(@RequestBody NewProductDto newProduct) {
        try {
            Product product = new Product(newProduct.getName(), newProduct.getPrice(), newProduct.getCategory(), newProduct.getDetails());
            return ResponseEntity.ok(mapper.map(repo.save(product), ProductDto.class));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edits the product details", notes = "Returns the product that has been edited.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 406, message = "Not acceptable")})
    public ResponseEntity<ProductDto> edit(@RequestBody ProductDto product) {
        try {
            Product productToEdit = repo.findOne(product.getId());
            if (productToEdit == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                productToEdit.setName(product.getName());
                productToEdit.setCategoryId(product.getCategoryId());
                productToEdit.setPrice(product.getPrice());
                productToEdit.setDetails(product.getDetails());

                ProductDto editedProduct = mapper.map(repo.save(productToEdit), ProductDto.class);
                return ResponseEntity.status(HttpStatus.OK).body(editedProduct);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the details of the product with the given id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 404, message = "Not found")})
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        ProductDto p = mapper.map(repo.findOne(id), ProductDto.class);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(p);
        }
    }

    @RequestMapping(value = "/byCategory/{categoryId}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of all products of the given categoryId.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    public ResponseEntity<List<ProductDto>> allProductsByCategoryId(@PathVariable long categoryId) {
        List<ProductDto> productDtos = repo.findByCategoryId(categoryId).stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes the product with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 406, message = "Not acceptable")})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            Product p = repo.findOne(id);
            if (p == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                this.repo.delete(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Accepts a search string, returns all matching products.", notes = "Search string will be split at white spaces. All words in the search string must be found in a product description for the product to be included.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    @ResponseBody
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestBody SearchDto search) {
        List<ProductDto> productDtos = repo
                .findByDetailsContainingAndPriceBetween(search.getText(),
                        mapToDefaultSeachCriteria(search.getSearchMinPrice(), Double.MIN_VALUE),
                        mapToDefaultSeachCriteria(search.getSearchMaxPrice(), Double.MAX_VALUE))
                .stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    private double mapToDefaultSeachCriteria(final double searchCriteria, final double defaultValue) {
        return searchCriteria < 0.00001 ? defaultValue : searchCriteria;
    }

}