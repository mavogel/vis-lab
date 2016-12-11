package com.github.mavogel.vislab.category.controller;/*
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

import com.github.mavogel.vislab.Category;
import com.github.mavogel.vislab.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
@RequestMapping("/catcomp")
public class CategoryCompositeController {

    @RequestMapping(value = "/category", method = RequestMethod.GET, headers = {"Authorization: Basic"})
    public Iterable<Category> listCategories() {
        // TODO dummy
        return Arrays.asList(new Category(1L, "TestCategory"));
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET, headers = {"Authorization: Basic"})
    public Category listCategory(@PathVariable Long id) {
        // TODO
        return new Category(id, "TestCategory");
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET, headers = {"Authorization: Basic"})
    public Iterable<Product> listProducts() {
        // TODO
        return Arrays.asList(new Product(1L, "TestProduct", "TestProduct Details...", 6.55, 4L));
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, headers = {"Authorization: Basic"})
    public Product listProduct(@PathVariable Long id) {
        // TODO
        return new Product(id, "TestProduct", "TestProduct Details...", 6.55, 4L);
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody String category) {
        // TODO
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody String product) {
        // TODO
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        // TODO
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        // TODO
    }
}
