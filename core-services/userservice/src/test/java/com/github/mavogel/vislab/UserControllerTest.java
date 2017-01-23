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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mavogel.vislab.user.controller.UserLevel;
import com.github.mavogel.vislab.user.model.Role;
import com.github.mavogel.vislab.user.model.User;
import com.github.mavogel.vislab.user.repository.RoleRepository;
import com.github.mavogel.vislab.user.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mavogel on 12/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clear the db before each test
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void shouldFindOneRoleForLevel3() throws Exception {
        Role role = roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId()));

        this.mockMvc.perform(get("/user/level/" + role.getLevel()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.type").value(role.getType()))
                .andExpect(jsonPath("$.level").value(role.getLevel()));
    }

    @Test
    public void shouldRegisterARegularUser() throws Exception {
        Role role = roleRepository.save(new Role("type", UserLevel.REGULAR.getLevelId()));

        Map<String, String> newUser = new HashMap<>();
        newUser.put("username", "jdoe");
        newUser.put("firstname", "john");
        newUser.put("lastname", "doe");
        newUser.put("password", "acbd");

        this.mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("john"))
                .andExpect(jsonPath("$.lastname").value("doe"))
                .andExpect(jsonPath("$.password").value("acbd"))
                .andExpect(jsonPath("$.role.id").value(role.getId()))
                .andExpect(jsonPath("$.role.type").value(role.getType()))
                .andExpect(jsonPath("$.role.level").value(role.getLevel()));
    }

    @Test
    public void shouldFindUserByUsername() throws Exception {
        Role role = roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId()));
        User user = userRepository.save(new User("jdoe", "john", "doe", "acbd", role));

        this.mockMvc.perform(get("/user/" + user.getUsername()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("john"))
                .andExpect(jsonPath("$.lastname").value("doe"))
                .andExpect(jsonPath("$.password").value("acbd"))
                .andExpect(jsonPath("$.role.id").value(role.getId()))
                .andExpect(jsonPath("$.role.type").value(role.getType()))
                .andExpect(jsonPath("$.role.level").value(role.getLevel()));
    }

    @Test
    public void shouldNotFindUserByUsername() throws Exception {
        userRepository.save(new User("jdoe", "john", "doe", "acbd", roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId()))));

        this.mockMvc.perform(get("/user/345").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAUser() throws Exception {
        User user = new User("jdoe", "john", "doe", "acbd", roleRepository.save(new Role("type", UserLevel.ADMIN.getLevelId())));
        userRepository.save(user);

        this.mockMvc.perform(delete("/user/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}
