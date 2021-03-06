package com.github.mavogel.vislab.clients;/*
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

import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by mavogel on 12/19/16.
 */
@FeignClient("productservice")
public interface ProductClient {

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    ResponseEntity<List<ProductDto>> listProducts();

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    ResponseEntity<ProductDto> listProduct(@PathVariable("id") long id);

    @RequestMapping(value = "/product/byCategory/{categoryId}", method = RequestMethod.GET)
    ResponseEntity<List<ProductDto>> allProductsByCategoryId(@PathVariable("categoryId") long categoryId);

    @RequestMapping(value = "/product/search", method = RequestMethod.POST)
    ResponseEntity<List<ProductDto>> searchProducts(@RequestBody SearchDto search);

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    ResponseEntity<ProductDto> addProduct(@RequestBody NewProductDto product);

    @RequestMapping(value = "/product", method = RequestMethod.PATCH)
    ResponseEntity<ProductDto> edit(@RequestBody ProductDto product);

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteProduct(@PathVariable("id") long id);
}
