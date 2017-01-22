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

import com.github.mavogel.vislab.clients.UserClient;
import com.gitlab.mavogel.vislab.dtos.user.NewUserDto;
import com.gitlab.mavogel.vislab.dtos.user.RoleDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mavogel on 11/1/16.
 */
@RestController
public class UserProxy {

    private static final Logger LOG = LoggerFactory.getLogger(UserProxy.class);
    private static Map<Long, UserDto> USER_CACHE = new LinkedHashMap<>();
    private static Map<Integer, RoleDto> ROLE_CACHE = new LinkedHashMap<>();

    @Autowired
    private UserClient userClient;

    @HystrixCommand(fallbackMethod = "getUserByUsernameCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        ResponseEntity<UserDto> userByUsername = userClient.getUserByUsername(username);
        USER_CACHE.put(userByUsername.getBody().getId(), userByUsername.getBody());
        return userByUsername;
    }

    // TODO delete maybe
    @HystrixCommand(fallbackMethod = "doesUserAlreadyExistCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/user/exists/{name}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> doesUserAlreadyExist(@PathVariable String name) {
        return userClient.doesUserAlreadyExist(name);
    }

    @HystrixCommand(fallbackMethod = "getRoleByLevelCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")
    })
    @RequestMapping(value = "/user/level/{levelId}", method = RequestMethod.GET)
    public ResponseEntity<RoleDto> getRoleByLevel(@PathVariable int levelId) {
        ResponseEntity<RoleDto> roleByLevel = userClient.getRoleByLevel(levelId);
        ROLE_CACHE.put(roleByLevel.getBody().getLevel(), roleByLevel.getBody());
        return roleByLevel;
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<UserDto> registerUser(@RequestBody NewUserDto userDto) {
        return userClient.registerUser(userDto);
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        return userClient.deleteUser(id);
    }

    /////////////////
    // Fallbacks
    /////////////////
    private ResponseEntity<UserDto> getUserByUsernameCache(String username) {
        LOG.info(">> getUserByUsernameCache {} from CACHE", new Object[]{username});
        Optional<UserDto> user = USER_CACHE.entrySet().stream()
                .map(e -> e.getValue())
                .findFirst();

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private ResponseEntity<Boolean> doesUserAlreadyExistCache(String name) {
        LOG.info(">> doesUserAlreadyExistCache {} from CACHE", new Object[]{name});
        if (getUserByUsername(name).getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    private ResponseEntity<RoleDto> getRoleByLevelCache(int levelId) {
        LOG.info(">> getRoleByLevelCache {} from CACHE", new Object[]{levelId});
        return ResponseEntity.ok(ROLE_CACHE.getOrDefault(levelId, new RoleDto(1, "user", levelId)));
    }
}
