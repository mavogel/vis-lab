package com.felixmohr.microservice.product;/*
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

import com.felixmohr.microservice.product.model.Product;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import org.modelmapper.ModelMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mavogel on 12/11/16.
 */
public class MappingTests {

    private ModelMapper mapper = new ModelMapper();

    @Test
    public void shouldMapEntitiyToDTO() throws Exception {
        Product entity = new Product("P1", 2.55, 0, "D1");

        // == go
        ProductDto productDto = mapper.map(entity, ProductDto.class);

        // == verify
//        assertEquals(entity.getId(), productDto.getId());
        assertEquals(entity.getName(), productDto.getName());
        assertEquals(entity.getPrice(), productDto.getPrice(), 0.0001);
        assertEquals(entity.getCategoryId(), productDto.getCategoryId());
        assertEquals(entity.getDetails(), productDto.getDetails());
    }
}
