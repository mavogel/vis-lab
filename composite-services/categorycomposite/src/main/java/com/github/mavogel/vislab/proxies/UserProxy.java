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
import com.github.mavogel.vislab.clients.UserClient;
import com.gitlab.mavogel.vislab.dtos.category.CategoryDto;
import com.gitlab.mavogel.vislab.dtos.category.NewCategoryDto;
import com.gitlab.mavogel.vislab.dtos.product.NewProductDto;
import com.gitlab.mavogel.vislab.dtos.product.ProductDto;
import com.gitlab.mavogel.vislab.dtos.product.SearchDto;
import com.gitlab.mavogel.vislab.dtos.user.NewUserDto;
import com.gitlab.mavogel.vislab.dtos.user.RoleDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
public class UserProxy {

    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username){
        return userClient.getUserByUsername(username);
    }

    @RequestMapping(value = "/user/exists/{name}", method = RequestMethod.GET)
    public boolean doesUserAlreadyExist(@PathVariable String name) {
        return userClient.doesUserAlreadyExist(name);
    }

    @RequestMapping(value = "/user/level/{levelId}", method = RequestMethod.GET)
    public ResponseEntity<RoleDto> getRoleByLevel(@PathVariable int levelId) {
        return userClient.getRoleByLevel(levelId);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<UserDto> registerUser(@RequestBody NewUserDto userDto) {
        return userClient.registerUser(userDto);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        return userClient.deleteUser(id);
    }
}
