/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.netty.handler.codec.http.HttpStatusClass;
import org.ribasco.asyncgamequerylib.core.client.AbstractRestClient;
import org.ribasco.asyncgamequerylib.core.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * <p>An API Interface containing a set/group of methods that are usually defined by the publisher</p>
 *
 * @param <T>   Any class extending {@link AbstractRestClient}
 * @param <Req> Any class extending {@link AbstractWebRequest}
 */
abstract public class AbstractWebApiInterface<T extends AbstractRestClient,
        Req extends AbstractWebApiRequest,
        Res extends AbstractWebApiResponse<JsonElement>> {
    private static final Logger log = LoggerFactory.getLogger(AbstractWebApiInterface.class);
    private T client;
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson jsonBuilder;

    /**
     * Used by the underlying concrete classes for api versioning
     */
    protected static final int VERSION_1 = 1, VERSION_2 = 2, VERSION_3 = 3;

    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public AbstractWebApiInterface(T client) {
        this.client = client;
    }

    /**
     * Lazy-Inititalization
     *
     * @return A {@link Gson} instance
     */
    protected Gson builder() {
        if (jsonBuilder == null) {
            configureBuilder(gsonBuilder);
            jsonBuilder = gsonBuilder.create();
        }
        return jsonBuilder;
    }

    /**
     * <p>Sends a requests to the internal client</p>
     *
     * @param request An instance of {@link AbstractWebRequest}
     *
     * @return A {@link CompletableFuture} that will hold the expected value once a response has been received by the server
     */
    @SuppressWarnings("unchecked")
    protected <A> CompletableFuture<A> sendRequest(Req request) {
        CompletableFuture<Res> responseFuture = client.sendRequest(request);
        return responseFuture.whenComplete(this::errorHandler).thenApply(this::convertToJsonObject);
    }

    /**
     * <p>Override this method if you need to perform additional configurations against the builder (e.g. Register custom deserializers)</p>
     *
     * @param builder A {@link GsonBuilder} instance that will be accessed and configured by a concrete {@link AbstractWebApiInterface} implementation
     */
    protected void configureBuilder(GsonBuilder builder) {
        //no implementation
    }

    /**
     * The default error handler
     *
     * @param response
     * @param error
     */
    protected void errorHandler(Res response, Throwable error) {
        if (error != null)
            throw new WebException(error);
        if (response.getStatus() == HttpStatusClass.CLIENT_ERROR) {
            switch (response.getMessage().getStatusCode()) {
                case 400:
                    throw new BadRequestException("Incorrect parameters provided for request");
                case 403:
                    throw new AccessDeniedException("Access denied, either because of missing/incorrect credentials or used API token does not grant access to the requested resource.");
                case 404:
                    throw new ResourceNotFoundException("Resource was not found.");
                case 429:
                    throw new TooManyRequestsException("Request was throttled, because amount of requests was above the threshold defined for the used API token.");
                case 500:
                    throw new UnknownWebException("Unknown error happened when handling the request.");
                case 503:
                    throw new ServiceUnavailableException("Service is temprorarily unavailable because of maintenance.");
                default:
                    throw new WebException("Unknown error occured on request send");
            }
        }
    }

    /**
     * A convenience method to convert the response to {@link com.google.gson.JsonObject}
     *
     * @param response
     * @param <A>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private <A> A convertToJsonObject(Res response) {
        log.info("ConvertToJson for Response = {}", response);
        JsonElement processedElement = response.getProcessedContent();
        if (processedElement != null)
            return (A) processedElement.getAsJsonObject();
        throw new AsyncGameLibUncheckedException("No parsed content found for response" + response);
    }
}
