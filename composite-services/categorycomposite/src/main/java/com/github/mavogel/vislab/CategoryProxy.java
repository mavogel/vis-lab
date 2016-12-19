package com.github.mavogel.vislab;/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Manuel Vogel
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 *  https://opensource.org/licenses/MIT
 */

import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.category.NewCategoryDto;
import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
@RequestMapping("")//category-proxy")
public class CategoryProxy {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private ProductClient productClient;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public List<CategoryDto> listCategories() {
        return categoryClient.listCategories();
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody NewCategoryDto newCategory) {
        this.categoryClient.addCategory(newCategory);
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        try {
            this.productClient.allProductsByCategoryId(id)
                    .forEach(product -> productClient.deleteProduct(product.getId()));
            this.categoryClient.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public List<ProductDto> listProducts() {
        return this.productClient.listProducts();
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ProductDto listProduct(@PathVariable long id) {
        return this.productClient.listProduct(id);
    }

    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public List<ProductDto> searchProducts(@RequestBody SearchDto search) {
        return this.productClient.searchProducts(search);
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<Void> addProduct(@RequestBody NewProductDto newProduct) {
        List<CategoryDto> category = this.categoryClient.listCategory(newProduct.getCategory());
        if (category != null) {
            this.productClient.addProduct(newProduct);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/product", method = RequestMethod.PATCH)
    public ProductDto edit(@RequestBody ProductDto product) {
        return productClient.edit(product);
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable long id) {
        this.productClient.deleteProduct(id);
    }
}
