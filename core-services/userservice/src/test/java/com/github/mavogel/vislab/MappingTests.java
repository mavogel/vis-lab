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

import com.github.mavogel.vislab.user.model.Role;
import com.github.mavogel.vislab.user.model.User;
import com.gitlab.mavogel.vislab.dtos.user.RoleDto;
import com.gitlab.mavogel.vislab.dtos.user.UserDto;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

/**
 * Created by mavogel on 12/11/16.
 */
public class MappingTests {

    private ModelMapper mapper = new ModelMapper();

    @Test
    public void shouldMapRoleEntitiyToDTO() throws Exception {
        Role entity = new Role("type", 1);

        // == go
        RoleDto roleDto = mapper.map(entity, RoleDto.class);

        // == verify
//        assertEquals(entity.getId(), productDto.getId());
        assertEquals(entity.getType(), roleDto.getType());
        assertEquals(entity.getLevel(), roleDto.getLevel());
    }

    @Test
    public void shouldMapUserEntitiyToDTO() throws Exception {
        Role role = new Role("type", 1);
        User entity = new User("username", "firstname", "lastname", "pwd", role);

        // == go
        UserDto userDto = mapper.map(entity, UserDto.class);

        // == verify
//        assertEquals(entity.getId(), productDto.getId());
        assertEquals(entity.getUsername(), userDto.getUsername());
        assertEquals(entity.getFirstname(), userDto.getFirstname());
        assertEquals(entity.getLastname(), userDto.getLastname());
        assertEquals(entity.getPassword(), userDto.getPassword());
        assertEquals(entity.getRole().getType(), userDto.getRole().getType());
        assertEquals(entity.getRole().getLevel(), userDto.getRole().getLevel());
    }
}
