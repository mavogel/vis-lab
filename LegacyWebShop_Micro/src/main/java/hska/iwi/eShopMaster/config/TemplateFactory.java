package hska.iwi.eShopMaster.config;/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017 Manuel Vogel
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

import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mavogel on 1/17/17.
 */
public final class TemplateFactory {

    public static final String API_GATEWAY = "http://api-gateway:8765";
    public static final String TOKEN_URI = API_GATEWAY +"/uaa/oauth/token";
    public static final String CLIENT_ID= "acme";
    public static final String CLIENT_SECRET = "acmesecret";
    private static OAuth2RestTemplate CONFIGURED_OAUTH2_REST_TEMPLATE;

    private TemplateFactory() {
        throw new AssertionError("do not instantiate");
    }

    /**
     * Creates a rest template.
     *
     * @return the {@link RestTemplate}
     */
    public static RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

//    /**
//     * Creates a rest template for the specified path and call type.
//     *
//     * @param path the path
//     * @param httpMethod the call type
//     * @return the configured {@link RestTemplate}
//     */
//    public static RestTemplate getRestTemplate(final String path, final HttpMethod httpMethod) {
//        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        try {
//            requestFactory.createRequest(new URI(API_GATEWAY + path), httpMethod);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return new RestTemplate(requestFactory);
//    }

    /**
     * The configured OAuth2 rest template.
     *
     * @return the {@link OAuth2RestTemplate}
     */
    public static OAuth2RestTemplate getOAuth2RestTemplate() {
        // TODO null check
        return CONFIGURED_OAUTH2_REST_TEMPLATE;
    }

    /**
     * Performs the logout
     */
    public static void nullTheOAuth2RestTemplate() {
        CONFIGURED_OAUTH2_REST_TEMPLATE = null;
    }

    /**
     * Creates an OAuth2 rest template with grand type 'password' for the given credentials.
     *
     * @param username the username credential
     * @param password the password credential
     * @return the configured OAuth2 rest template
     */
    public static OAuth2RestTemplate createAndGetOAuth2RestTemplate(String username, String password) {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        CONFIGURED_OAUTH2_REST_TEMPLATE = new OAuth2RestTemplate(createResource(username, password), new DefaultOAuth2ClientContext(atr));
        return CONFIGURED_OAUTH2_REST_TEMPLATE;
    }

    private static OAuth2ProtectedResourceDetails createResource(String username, String password) {

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

        List<String> scopes = new ArrayList<String>(2);
        scopes.add("openid");
        resource.setAccessTokenUri(TOKEN_URI);
        resource.setClientId(CLIENT_ID);
        resource.setClientSecret(CLIENT_SECRET);
        resource.setGrantType("password");
        resource.setScope(scopes);

        resource.setUsername(username);
        resource.setPassword(password);

        return resource;
    }
}
