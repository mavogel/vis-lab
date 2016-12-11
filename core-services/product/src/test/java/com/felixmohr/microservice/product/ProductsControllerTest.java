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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixmohr.microservice.product.model.Product;
import com.felixmohr.microservice.product.model.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mavogel on 12/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clear the db before each test
public class ProductsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProductRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void shouldList2Products() throws Exception {
        Product p1 = repo.save(new Product("P1", 2.55, 0, "D1"));
        Product p2 = repo.save(new Product("P2", 6.55, 0, "D2"));

        this.mockMvc.perform(get("/product/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(p1.getId()))
                .andExpect(jsonPath("$[0].name").value(p1.getName()))
                .andExpect(jsonPath("$[0].price").value(p1.getPrice()))
                .andExpect(jsonPath("$[0].categoryId").value(p1.getCategoryId()))
                .andExpect(jsonPath("$[0].details").value(p1.getDetails()))
                .andExpect(jsonPath("$[1].id").value(p2.getId()))
                .andExpect(jsonPath("$[1].name").value(p2.getName()))
                .andExpect(jsonPath("$[1].price").value(p2.getPrice()))
                .andExpect(jsonPath("$[1].categoryId").value(p2.getCategoryId()))
                .andExpect(jsonPath("$[1].details").value(p2.getDetails()));
    }

    @Test
    public void shouldSearchAndFind2Of4Products() throws Exception {
        Product p1 = repo.save(new Product("P1", 2.55, 0, "D1 bla"));
        Product p2 = repo.save(new Product("P2", 6.55, 0, "D2 bla"));
        repo.save(new Product("P3", 7.55, 0, "D3"));
        repo.save(new Product("P4", 33.55, 0, "D4"));

        Map<String, String> searchText = new HashMap<>();
        searchText.put("text", "bla");

        this.mockMvc.perform(post("/product/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(searchText)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(p1.getId()))
                .andExpect(jsonPath("$[0].name").value(p1.getName()))
                .andExpect(jsonPath("$[0].price").value(p1.getPrice()))
                .andExpect(jsonPath("$[0].categoryId").value(p1.getCategoryId()))
                .andExpect(jsonPath("$[0].details").value(p1.getDetails()))
                .andExpect(jsonPath("$[1].id").value(p2.getId()))
                .andExpect(jsonPath("$[1].name").value(p2.getName()))
                .andExpect(jsonPath("$[1].price").value(p2.getPrice()))
                .andExpect(jsonPath("$[1].categoryId").value(p2.getCategoryId()))
                .andExpect(jsonPath("$[1].details").value(p2.getDetails()));
    }

    @Test
    public void shouldCreateANewProduct() throws Exception {
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("name", "P1");
        newProduct.put("price", "2.55");
        newProduct.put("category", "0");
        newProduct.put("details", "D1");

        this.mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("P1"))
                .andExpect(jsonPath("$.price").value(2.55))
                .andExpect(jsonPath("$.categoryId").value(0))
                .andExpect(jsonPath("$.details").value("D1"));
    }

    @Test
    public void shouldEditAProduct() throws Exception {
        Product p1 = repo.save(new Product("P1", 2.55, 0, "D1"));

        Map<String, String> editedProduct = new HashMap<>();
        editedProduct.put("id", String.valueOf(p1.getId()));
        editedProduct.put("name", "P1fancy");
        editedProduct.put("price", "4.55");
        editedProduct.put("category", "0");
        editedProduct.put("details", "D2new");

        this.mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(editedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(p1.getId()))
                .andExpect(jsonPath("$.name").value("P1fancy"))
                .andExpect(jsonPath("$.price").value(4.55))
                .andExpect(jsonPath("$.categoryId").value(0))
                .andExpect(jsonPath("$.details").value("D2new"));
    }

    @Test
    public void shouldGetASingleProduct() throws Exception {
        Product p1 = repo.save(new Product("P1", 2.55, 0, "D1"));

        this.mockMvc.perform(get("/product/" + p1.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("P1"))
                .andExpect(jsonPath("$.price").value(2.55))
                .andExpect(jsonPath("$.categoryId").value(0))
                .andExpect(jsonPath("$.details").value("D1"));
    }

    @Test
    public void shouldDeleteASingleProduct() throws Exception {
        Product p1 = repo.save(new Product("P1", 2.55, 0, "D1"));

        this.mockMvc.perform(delete("/product/" + p1.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
