package com.github.mavogel.vislab.security;

import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

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

/**
 * Defines a custom redirect strategy for using <pre>grand_type=authorizaton code</pre>
 * and the login page, if no token is given at the request.
 * <br>
 * The redirect strategy or logic is that the service calls itself to redirect
 * to the uaa-service. See README for more details.
 *
 * @author Thibaud Leprêtre
 */
class DynamicOauth2ClientContextFilter extends OAuth2ClientContextFilter {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void redirectUser(UserRedirectRequiredException e, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String redirectUri = e.getRedirectUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);
        Map<String, String> requestParams = e.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            builder.queryParam(param.getKey(), param.getValue());
        }

        if (e.getStateKey() != null) {
            builder.queryParam("state", e.getStateKey());
        }

        String url = getBaseUrl(request) + builder.build().encode().toUriString();
        this.redirectStrategy.sendRedirect(request, response, url);
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    private String getBaseUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return  url.substring(0, url.length() - request.getRequestURI().length() + request.getContextPath().length());
    }
}
