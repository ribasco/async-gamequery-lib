/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core;

import com.ibasco.agql.core.transport.http.ContentTypeProcessor;
import com.ibasco.agql.core.transport.http.processors.JsonContentTypeProcessor;
import com.ibasco.agql.core.transport.http.processors.XmlContentTypeProcessor;
import com.ibasco.agql.core.util.Options;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

abstract public class AbstractRestClient extends AsyncHttpClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractRestClient.class);

    private final Map<String, ContentTypeProcessor> contentProcessorMap = new HashMap<>();

    private String authToken;

    /**
     * Some rest clients do not require authentication
     */
    public AbstractRestClient() {
        this(null);
    }

    /**
     * Constructor accepting a {@link String} representing the authentication token for the provider
     *
     * @param authToken
     *         A {@link String} containing the authentication token for the provider
     */
    public AbstractRestClient(String authToken) {
        super(null);
        this.authToken = authToken;
        //Register default content-type processors
        registerContentTypeProcessor("application/json", new JsonContentTypeProcessor());
        registerContentTypeProcessor("application/xml", new XmlContentTypeProcessor());
    }

    /**
     * <p>Factory method for creating the default {@link HttpMessenger}</p>
     *
     * @return A {@link HttpMessenger} instance
     */
    @Override
    protected HttpMessenger createMessenger(Options options) {
        Function<Response, AbstractWebApiResponse> responseFactory = this::createWebApiResponse;
        return new HttpMessenger(responseFactory.andThen(this::applyContentTypeProcessor));
    }

    /**
     * <p>Function that is responsible for parsing the internal response of the message (e.g. JSON or XML)</p>
     *
     * @return Returns instance of {@link AbstractWebApiResponse}
     */
    private AbstractWebApiResponse applyContentTypeProcessor(AbstractWebApiResponse response) {
        if (response != null && response.getMessage() != null) {
            Response msg = response.getMessage();
            String body = msg.getResponseBody();
            ContentTypeProcessor processor = contentProcessorMap.get(parseContentType(msg.getContentType()));
            if (log.isDebugEnabled() && processor == null)
                log.debug("No Content-Type processor found for {}. Please register a ContentTypeProcessor using registerContentProcessor()", parseContentType(msg.getContentType()));
            //noinspection unchecked
            response.setProcessedContent((processor != null) ? processor.apply(body) : body);
        }
        return response;
    }

    /**
     * <p>Register custom content processors based on the value defined in the Content-Type header</p>
     *
     * @param contentType
     *         A {@link String} Content-Type identifier
     * @param processor
     *         The {@link ContentTypeProcessor} that will handle the response body conversion
     */
    protected void registerContentTypeProcessor(String contentType, ContentTypeProcessor processor) {
        String type = parseContentType(contentType);
        log.debug("Registering Content-Type: {}", type);
        this.contentProcessorMap.put(type, processor);
    }

    protected void removeContentTypeProcessor(String contentType) {
        this.contentProcessorMap.remove(parseContentType(contentType));
    }

    /**
     * <p>A Simply utility method for parsing Content-Type which contains parameters</p>
     *
     * @param contentType
     *         A {@link String} containing the Content-Type
     *
     * @return The parsed content-type {@link String} excluding the parameters
     */
    private String parseContentType(String contentType) {
        if (!StringUtils.isEmpty(contentType) && contentType.contains(";")) {
            String[] types = StringUtils.splitByWholeSeparatorPreserveAllTokens(contentType, ";", 2);
            if (types != null && types.length > 1)
                return types[0].trim();
        }
        return contentType;
    }

    @Override
    protected CompletableFuture<AbstractWebResponse> send(AbstractWebRequest message) {
        //Before sending the request, make sure we make some last minute
        // modifications to the request by applying the api token provided
        if (!StringUtils.isEmpty(this.authToken))
            applyAuthenticationScheme(message.request(), this.authToken);
        log.debug("Sending request : {}", message);
        return super.send(message);
    }

    /**
     * <p>Override this method if the client requires authentication.</p>
     *
     * @param requestBuilder
     *         The {@link RequestBuilder} containing the request parameters of the concrete {@link org.asynchttpclient.Request}
     * @param authToken
     *         A {@link String} representing the authetntication token to be passed to the provider
     */
    protected void applyAuthenticationScheme(RequestBuilder requestBuilder, String authToken) {
        //Optional implementation
    }

    /**
     * <p>A factory method that creates an {@link AbstractWebApiResponse} instance based on the Http {@link Response}</p>
     *
     * @param response
     *         The Http {@link Response} received by the transport
     *
     * @return A Concrete implementation of {@link AbstractWebApiResponse}
     */
    abstract protected AbstractWebApiResponse createWebApiResponse(Response response);

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
