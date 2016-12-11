package com.github.mavogel.vislab;
/*
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mavogel on 11/1/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDocumentation {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MockMvc mockMvc;

    private RestDocumentationResultHandler documentationHandler;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.documentationHandler = document("{method-name}",
                preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.documentationHandler)
                .build();
    }

    @Test
    public void listCategories() throws Exception {
        this.mockMvc.perform(get("/catcomp/category")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("[].id").description("The category ID"),
                                fieldWithPath("[].name").description("The name of the category")
                        )
                ));
    }

    @Test
    public void listCategory() throws Exception {

        this.mockMvc.perform(get("/catcomp/category/2")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("id").description("The category ID"),
                                fieldWithPath("name").description("The name of the category")
                        )
                ));
    }

    @Test
    public void listProducts() throws Exception {
        this.mockMvc.perform(get("/catcomp/product")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("[].id").description("The product ID"),
                                fieldWithPath("[].details").description("The details of the product"),
                                fieldWithPath("[].name").description("The name of the product"),
                                fieldWithPath("[].price").description("The price of the product"),
                                fieldWithPath("[].categoryId").description("The ID of the associated category")
                        )
                ));
    }

    @Test
    public void listProduct() throws Exception {

        this.mockMvc.perform(get("/catcomp/product/2")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("id").description("The product ID"),
                                fieldWithPath("details").description("The details of the product"),
                                fieldWithPath("name").description("The name of the product"),
                                fieldWithPath("price").description("The price of the product"),
                                fieldWithPath("categoryId").description("The ID of the associated category")
                        )
                ));
    }

    @Test
    public void addCategory() throws Exception {
        Map<String, String> newCategory = new HashMap<>();
        newCategory.put("id", "1");
        newCategory.put("name", "TestCategory");

        this.mockMvc.perform(post("/catcomp/category")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("id").description("The category ID"),
                                fieldWithPath("name").description("The name of the category")
                        )
                ));
    }

    @Test
    public void deleteCategory() throws Exception {
//        Category originalCategory = createSampleCategory(1L, "TestCategory");

        this.mockMvc.perform(delete("/catcomp/category/1")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addProduct() throws Exception {
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("id", "1");
        newProduct.put("details", "TestProduct Details...");
        newProduct.put("name", "TestProduct");
        newProduct.put("price", "6.77");
        newProduct.put("categoryId", "3");

        this.mockMvc.perform(post("/catcomp/product")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("id").description("The product ID"),
                                fieldWithPath("details").description("The details of the product"),
                                fieldWithPath("name").description("The name of the product"),
                                fieldWithPath("price").description("The price of the product"),
                                fieldWithPath("categoryId").description("The ID of the associated category")
                        )
                ));
    }

    @Test
    public void deleteProduct() throws Exception {
//        Category originalCategory = createSampleCategory(1L, "TestCategory");

        this.mockMvc.perform(delete("/catcomp/product/1")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
