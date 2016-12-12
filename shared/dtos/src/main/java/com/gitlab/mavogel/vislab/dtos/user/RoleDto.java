package com.gitlab.mavogel.vislab.dtos.user;/*
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

/**
 * Created by mavogel on 12/12/16.
 */
public class RoleDto {

    private long id;
    private String type;
    private int level;

    private RoleDto() {
    }

    public RoleDto(final long id, final String type, final int level) {
        this.id = id;
        this.type = type;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoleDto{");
        sb.append("id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }
}
