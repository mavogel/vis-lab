package com.github.mavogel.vislab.user.controller;/*
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

import com.github.mavogel.vislab.user.model.User;
import com.github.mavogel.vislab.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 9d6f806e8a84474f76dc3e3925718b02a5940bba

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "", method = RequestMethod.GET, headers = {"Authorization: Basic"})
    public Iterable<User> listUsers() {
        return userRepository.findAll();
    }

<<<<<<< HEAD
    @RequestMapping(value = "", method = RequestMethod.POST, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User user){
        userRepository.save(new User(user.getId(),user.getName()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = {"Authorization: Basic"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userRepository.delete(id);
    }
=======
//    @RequestMapping(value = "", method = RequestMethod.POST, headers = {"Authorization: Basic"})
//    @ResponseStatus(HttpStatus.CREATED)
//    public void addCategory(@RequestBody Category category) {
//        categoryRepository.save(new Category(category.getId(), category.getName()));
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = {"Authorization: Basic"})
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCategory(@PathVariable Long id) {
//        categoryRepository.delete(id);
//    }
>>>>>>> 9d6f806e8a84474f76dc3e3925718b02a5940bba
}
