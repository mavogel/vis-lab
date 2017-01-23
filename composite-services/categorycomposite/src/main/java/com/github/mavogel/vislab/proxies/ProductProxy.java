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
import com.gitlab.mavogel.vislab.dtos.product.FullProductDto;
import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the calls and fallbacks for calls to the Product-Service,
 * as well as the authentication for each call.
 *
 * Created by mavogel on 11/1/16.
 */
@RestController
public class ProductProxy {

    private static final Logger LOG = LoggerFactory.getLogger(ProductProxy.class);
    private static Map<Long, FullProductDto> CACHE = new LinkedHashMap<>();
    private static Map<Long, CategoryDto> CATEGORY_CACHE = new LinkedHashMap<>();

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private ProductClient productClient;

    @HystrixCommand(fallbackMethod = "listProductsCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity<List<FullProductDto>> listProducts() {
        ResponseEntity<List<ProductDto>> productDtoEntities = this.productClient.listProducts();
        List<FullProductDto> productDtos = enhanceProductsWithFullCategory(productDtoEntities.getBody());
        productDtos.forEach(p -> CACHE.put(p.getId(), p));
        return ResponseEntity.ok(productDtos);
    }

    @HystrixCommand(fallbackMethod = "listProductCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<FullProductDto> listProduct(@PathVariable long id) {
        ResponseEntity<ProductDto> productDtoEntity = this.productClient.listProduct(id);
        if (HttpStatus.OK.equals(productDtoEntity.getStatusCode())) {
            FullProductDto productDto = enhanceProductsWithFullCategory(Arrays.asList(productDtoEntity.getBody())).get(0);
            CACHE.put(productDto.getId(), productDto);
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.status(productDtoEntity.getStatusCode()).body(null);
        }
    }

    @HystrixCommand(fallbackMethod = "searchProductsCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/product/search", method = RequestMethod.POST)
    public ResponseEntity<List<FullProductDto>> searchProducts(@RequestBody SearchDto search) {
        ResponseEntity<List<ProductDto>> productDtoEntities = this.productClient.searchProducts(search);
        List<FullProductDto> productDtos = enhanceProductsWithFullCategory(productDtoEntities.getBody());
        productDtos.forEach(p -> CACHE.put(p.getId(), p));
        return ResponseEntity.ok(productDtos);
    }

    /**
     * Enhances the product dto with the full category dto. Stores already found categories.
     * Does not update them because in the FE the category can never be edited.
     *
     * @param productDtos the products to enhance
     * @return the enhanced {@link FullProductDto}s
     */
    private List<FullProductDto> enhanceProductsWithFullCategory(List<ProductDto> productDtos) {
        productDtos.stream()
                .map(p -> p.getCategoryId())
                .distinct()
                .filter(cId -> !CATEGORY_CACHE.containsKey(cId))
                .map(cId -> this.categoryClient.listCategory(cId).getBody())
                .forEach(categoryDto -> CATEGORY_CACHE.put(categoryDto.getId(), categoryDto));

        return productDtos.stream()
                .map(p -> new FullProductDto(p.getId(), p.getName(), p.getPrice(), p.getDetails(), CATEGORY_CACHE.get(p.getCategoryId())))
                .collect(Collectors.toList());
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<ProductDto> addProduct(@RequestBody NewProductDto newProduct) {
        CategoryDto category = this.categoryClient.listCategory(newProduct.getCategory()).getBody();
        if (category != null) {
            return this.productClient.addProduct(newProduct);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/product", method = RequestMethod.PATCH)
    public ResponseEntity<ProductDto> edit(@RequestBody ProductDto product) {
        return productClient.edit(product);
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        return this.productClient.deleteProduct(id);
    }

    /////////////////
    // Fallbacks
    /////////////////
    private ResponseEntity<List<FullProductDto>> listProductsCache() {
        LOG.info(">> listProductsCache from CACHE");
        return ResponseEntity.ok(CACHE.entrySet().stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList()));
    }

    private ResponseEntity<FullProductDto> listProductCache(long id) {
        LOG.info(">> listProductCache id={} from CACHE", new Object[]{id});
        return ResponseEntity.ok(CACHE.getOrDefault(id,
                new FullProductDto(id, "dummy", 1.00, "dummy details",
                        new CategoryDto(1l, "TestCat"))));
    }

    private ResponseEntity<List<FullProductDto>> searchProductsCache(SearchDto search) {
        LOG.info(">> searchProductsCache search={} from CACHE", new Object[]{search});
        return ResponseEntity.ok(CACHE.entrySet().stream()
                .map(p -> p.getValue())
                .filter(p -> p.getDetails().contains(search.getText()))
                .filter(p -> p.getPrice() >= search.getSearchMinPrice() && p.getPrice() <= search.getSearchMaxPrice())
                .collect(Collectors.toList()));
    }
}
