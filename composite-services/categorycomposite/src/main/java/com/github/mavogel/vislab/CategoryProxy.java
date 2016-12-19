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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public void deleteCategory(final long id) {
        this.categoryClient.deleteCategory(id);
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public List<ProductDto> listProducts() {
        return this.productClient.listProducts();
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ProductDto listProduct(final long id) {
        return this.productClient.listProduct(id);
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public void addProduct(@RequestBody NewProductDto newProduct) {
        this.productClient.addProduct(newProduct);
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(long id) {
        this.productClient.deleteProduct(id);
    }
}
