package com.github.mavogel.vislab.proxies;/*
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

import com.github.mavogel.vislab.clients.CategoryClient;
import com.github.mavogel.vislab.clients.ProductClient;
import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.category.NewCategoryDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
public class CategoryProxy {

    private static Map<Long, CategoryDto> CACHE = new LinkedHashMap<>();

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private ProductClient productClient;

    @HystrixCommand(fallbackMethod = "listCategoriesCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> listCategories() {
        ResponseEntity<List<CategoryDto>> categoriesToCache = categoryClient.listCategories();
        categoriesToCache.getBody().forEach(c -> CACHE.put(c.getId(), c));
        return categoriesToCache;
    }

    private ResponseEntity<List<CategoryDto>> listCategoriesCache() {
        return ResponseEntity.ok(CACHE.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList()));
    }

    @HystrixCommand(fallbackMethod = "listCategoryCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> listCategory(@PathVariable long id) {
        ResponseEntity<CategoryDto> categoryToCache = categoryClient.listCategory(id);
        CACHE.put(categoryToCache.getBody().getId(), categoryToCache.getBody());
        return categoryToCache;
    }

    private ResponseEntity<CategoryDto> listCategoryCache(long id) {
        return ResponseEntity.ok(CACHE.getOrDefault(id, new CategoryDto(1, "dummyCategory")));
    }


    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody NewCategoryDto newCategory) {
        this.categoryClient.addCategory(newCategory);
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        CategoryDto categoryToDelete = this.categoryClient.listCategory(id).getBody();
        if (categoryToDelete != null) {
            try {
                this.productClient.allProductsByCategoryId(id)
                        .forEach(product -> productClient.deleteProduct(product.getId()));
                this.categoryClient.deleteCategory(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
