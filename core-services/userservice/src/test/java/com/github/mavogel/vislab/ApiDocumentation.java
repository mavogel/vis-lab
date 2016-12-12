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
import com.github.mavogel.vislab.user.controller.UserLevel;
import com.github.mavogel.vislab.user.model.Role;
import com.github.mavogel.vislab.user.model.User;
import com.github.mavogel.vislab.user.repository.RoleRepository;
import com.github.mavogel.vislab.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mavogel on 11/1/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clear the db before each test
public class ApiDocumentation {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MockMvc mockMvc;

    private RestDocumentationResultHandler documentationHandler;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
    public void getUserByName() throws Exception {
        Role role = roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId()));
        User user = userRepository.save(new User("jdoe", "john", "doe", "acbd", role));

        this.mockMvc.perform(get("/user/" + user.getUsername()).accept(MediaType.APPLICATION_JSON)
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42"))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("id").description("The user ID"),
                                fieldWithPath("username").description("The name of the user"),
                                fieldWithPath("firstname").description("The firstname of the user"),
                                fieldWithPath("lastname").description("The lastname of the user"),
                                fieldWithPath("password").description("The password of the user"),
                                fieldWithPath("role").description("The role of the user")
                        )
                ));
    }

    @Test
    public void doesUserAlreadyExist() throws Exception {
        this.mockMvc.perform(get("/user/exists/jdoe").accept(MediaType.APPLICATION_JSON)
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRoleByLevel() throws Exception {
        Role role = roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId()));

        this.mockMvc.perform(get("/user/level/" + role.getLevel()).accept(MediaType.APPLICATION_JSON)
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42"))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("id").description("The user ID"),
                                fieldWithPath("type").description("The type of the role"),
                                fieldWithPath("level").description("The level of the role")
                        )
                ));
    }

    @Test
    public void registerUser() throws Exception {
        final Role initRole = new Role("roletype", 1);
        roleRepository.save(initRole);

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", "1");
        newUser.put("username", "jdoe");
        newUser.put("firstname", "John");
        newUser.put("lastname", "Doe");
        newUser.put("password", "sdkfjld%3§");
        Map<String, String> newRole = new HashMap<>();
        newRole.put("id", "1");
        newRole.put("type", "roletype");
        newRole.put("level", "1");
        newUser.put("role", newRole);

        this.mockMvc.perform(post("/user")
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("id").description("The user ID"),
                                fieldWithPath("username").description("The name of the user"),
                                fieldWithPath("firstname").description("The firstname of the user"),
                                fieldWithPath("lastname").description("The lastname of the user"),
                                fieldWithPath("password").description("The password of the user"),
                                fieldWithPath("role").description("The role of the user")
                        )
                ));

        userRepository.delete(1L);
        roleRepository.delete(1L);
    }

    @Test
    public void deleteUser() throws Exception {
        User originalUser = createSampleUser("jdoe", "John", "Doe");

        this.mockMvc.perform(delete("/user/" + originalUser.getId())
                .header("Authorization: Basic", "0b79bab50daca910b000d4f1a2b675d604257e42")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    private User createSampleUser(final String username, final String firstname, final String lastname) {
        final Role initRole = new Role("roletype", 1);
        roleRepository.save(initRole);
        return userRepository.save(new User(username, firstname, lastname, "password123", initRole));
    }
}
