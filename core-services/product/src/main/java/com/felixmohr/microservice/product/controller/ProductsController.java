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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private ModelMapper mapper;

    static final Logger logger = LogManager.getLogger(ProductsController.class.getName());

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of all products.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    public List<ProductDto> getProducts() {
        return ((List<Product>) repo.findAll()).stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a product", notes = "Returns the created product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal error")})
    @ResponseBody
    public ProductDto create(@RequestBody NewProductDto newProduct, HttpServletResponse response) {
        try {
            Product product = new Product(newProduct.getName(), newProduct.getPrice(), newProduct.getCategory(), newProduct.getDetails());
            return mapper.map(repo.save(product), ProductDto.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edits the product details", notes = "Returns the product that has been edited.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Not found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ProductDto edit(@RequestBody ProductDto product, HttpServletResponse response) {
        try {
            if (repo.findOne(product.getId()) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                Product entity = mapper.map(product, Product.class);
                repo.save(entity);
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
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Not found")})
    public ProductDto get(@PathVariable Long id, HttpServletResponse response) {
        ProductDto p = mapper.map(repo.findOne(id), ProductDto.class);
        if (p == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            return p;
        }
    }

    @RequestMapping(value = "/byCategory/{categoryId}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of all products of the given categoryId.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    public List<ProductDto> allProductsByCategoryId(@PathVariable long categoryId) {
        return repo.findByCategoryId(categoryId).stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
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

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Accepts a search string, returns all matching products.", notes = "Search string will be split at white spaces. All words in the search string must be found in a product description for the product to be included.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ProductDto.class, responseContainer = "List")})
    @ResponseBody
    public List<ProductDto> searchProducts(@RequestBody SearchDto search) {
        return repo.search(search.getText()).stream()
                .map(productEntity -> mapper.map(productEntity, ProductDto.class))
                .collect(Collectors.toList());
    }

}