/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibasco.agql.core.exceptions.*;
import com.ibasco.agql.core.reflect.types.CollectionParameterizedType;
import io.netty.handler.codec.http.HttpStatusClass;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>An API Interface containing a set/group of methods that are usually defined by the publisher</p>
 *
 * @param <C>
 *         Any class extending {@link com.ibasco.agql.core.AbstractRestClient}
 * @param <R>
 *         Any class extending {@link com.ibasco.agql.core.AbstractWebRequest}
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractWebApiInterface<C extends AbstractRestClient, R extends AbstractWebApiRequest, S extends AbstractWebApiResponse<JsonElement>> {

    /**
     * Used by the underlying concrete classes for api versioning
     */
    public static final int VERSION_1 = 1, VERSION_2 = 2, VERSION_3 = 3;

    private static final Logger log = LoggerFactory.getLogger(AbstractWebApiInterface.class);

    private final C client;

    private final GsonBuilder gsonBuilder = new GsonBuilder();

    private Gson jsonBuilder;

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.core.AbstractRestClient} instance
     */
    public AbstractWebApiInterface(C client) {
        this.client = client;
    }

    /**
     * <p>fromJson.</p>
     *
     * @param element
     *         a {@link com.google.gson.JsonElement} object
     * @param classTypeOf
     *         a {@link java.lang.Class} object
     * @param <V>
     *         a V class
     *
     * @return a V object
     */
    protected <V> V fromJson(JsonElement element, Class<V> classTypeOf) {
        return builder().fromJson(element, classTypeOf);
    }

    /**
     * Lazy-Inititalization
     *
     * @return A {@link com.google.gson.Gson} instance
     */
    protected Gson builder() {
        if (jsonBuilder == null) {
            configureBuilder(gsonBuilder);
            jsonBuilder = gsonBuilder.create();
        }
        return jsonBuilder;
    }

    /**
     * <p>Override this method if you need to perform additional configurations against the builder (e.g. Register custom deserializers)</p>
     *
     * @param builder
     *         A {@link com.google.gson.GsonBuilder} instance that will be accessed and configured by a concrete {@link com.ibasco.agql.core.AbstractWebApiInterface} implementation
     */
    protected void configureBuilder(GsonBuilder builder) {
        //no implementation
    }

    /**
     * <p>Similar to {@link #asCollectionOf(Class, String, JsonObject, Class, boolean)}  minus the Collection class argument. This also returns a {@link java.util.List} collection type instead.</p>
     *
     * @param itemType
     *         The {@link java.lang.Class} type of the item in the {@link java.util.Collection}
     * @param searchKey
     *         The name of the {@link com.google.gson.JsonArray} element that we will convert
     * @param searchElement
     *         The {@link com.google.gson.JsonObject} that will be used to search for the {@link com.google.gson.JsonArray} element
     * @param strict
     *         If <code>true</code> an exception will be thrown if the listName is not found within the search element specified.
     *         Otherwise no exceptions will be raised and an empty {@link java.util.Collection} instance will be returned.
     * @param <A>
     *         The type of the List to be returned
     *
     * @return A {@link java.util.List} containing the parsed json entities
     */
    protected <A> List<A> asListOf(Class itemType, String searchKey, JsonObject searchElement, boolean strict) {
        return asCollectionOf(itemType, searchKey, searchElement, ArrayList.class, strict);
    }

    /**
     * <p>A Utility function that retrieves the specified json element and converts it to a Parameterized {@link java.util.Collection} instance.</p>
     *
     * @param itemType
     *         The {@link java.lang.Class} type of the item in the {@link java.util.Collection}
     * @param searchKey
     *         The name of the {@link com.google.gson.JsonArray} element that we will convert
     * @param searchElement
     *         The {@link com.google.gson.JsonObject} that will be used to search for the {@link com.google.gson.JsonArray} element
     * @param collectionClass
     *         A {@link java.lang.Class} representing the concrete implementation of the {@link java.util.Collection}
     * @param strict
     *         If <code>true</code> an exception will be thrown if the listName is not found within the search element specified. Otherwise no exceptions will be raised and an empty {@link java.util.Collection} instance will be returned.
     * @param <A>
     *         The internal type of the {@link java.util.Collection} to be returned
     *
     * @return A {@link java.util.Collection} containing the type specified by collectionClass argument
     */
    protected <A extends Collection> A asCollectionOf(Class itemType, String searchKey, JsonObject searchElement, Class<? extends Collection> collectionClass, boolean strict) {
        if (searchElement.has(searchKey) && searchElement.get(searchKey).isJsonArray()) {
            return fromJson(searchElement.getAsJsonArray(searchKey), new CollectionParameterizedType(itemType, collectionClass));
        }
        if (strict)
            throw new JsonElementNotFoundException(searchElement, String.format("Unable to find a JsonArray element '%s' from the search element", searchKey));
        else {
            return null;
        }
    }

    /**
     * <p>fromJson.</p>
     *
     * @param element
     *         a {@link com.google.gson.JsonElement} object
     * @param typeOf
     *         a {@link java.lang.reflect.Type} object
     * @param <V>
     *         a V class
     *
     * @return a V object
     */
    protected <V> V fromJson(JsonElement element, Type typeOf) {
        return builder().fromJson(element, typeOf);
    }

    /**
     * <p>Sends a requests to the internal client.</p>
     *
     * @param request
     *         An instance of {@link com.ibasco.agql.core.AbstractWebRequest}
     * @param <A>
     *         a A class
     *
     * @return A {@link java.util.concurrent.CompletableFuture} that will hold the expected value once a response has been received by the server
     */
    protected <A> CompletableFuture<A> sendRequest(R request) {
        return client.send(request).whenComplete(this::handleError).thenApply(this::convertToJson);
    }

    /**
     * The default error handler. Override this if needed.
     *
     * @param response
     *         An instance of {@link com.ibasco.agql.core.AbstractWebApiResponse} or <code>null</code> if an exception was thrown.
     * @param error
     *         A {@link java.lang.Throwable} instance or <code>null</code> if no error has occured.
     */
    protected void handleError(AbstractWebResponse response, Throwable error) {
        if (error != null)
            throw new WebException(error);
        log.debug("Handling response for {}, with status code = {}", response.getMessage().getUri(), response.getMessage().getStatusCode());
        if (response.getStatus() == HttpStatusClass.SERVER_ERROR || response.getStatus() == HttpStatusClass.CLIENT_ERROR) {
            JsonElement jsonElement = toJsonElement(response);
            if (response.getMessage().getStatusCode() == 403) {
                if (jsonElement != null && jsonElement.isJsonObject()) {
                    JsonObject obj = (JsonObject) jsonElement;
                    if (obj.has("error")) {
                        log.debug("GOT ERROR: {}", obj.get("error"));
                    }
                }
            }
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
                    throw new UnknownWebException("An internal error occured in server");
                case 503:
                    throw new ServiceUnavailableException("Service is temprorarily unavailable. Possible maintenance on-going.");
                default:
                    throw new WebException(String.format("An unknown error occured during request (Status Code: %d)", response.getMessage().getStatusCode()));
            }
        }
    }

    /**
     * Converts the underlying processed content to a {@link com.google.gson.JsonObject} instance
     */
    @SuppressWarnings("unchecked")
    private <A> A convertToJson(AbstractWebResponse res) {
        if (!(res instanceof AbstractWebApiResponse))
            return null;
        AbstractWebApiResponse<?> response = (AbstractWebApiResponse<?>) res;
        log.debug("ConvertToJson for Response = {}, {}", response.getMessage().getStatusCode(), response.getMessage().getHeaders());
        JsonElement processedElement = (JsonElement) response.getProcessedContent();
        if (processedElement != null) {
            if (processedElement.isJsonObject())
                return (A) processedElement.getAsJsonObject();
            else if (processedElement.isJsonArray())
                return (A) processedElement.getAsJsonArray();
        }
        throw new AgqlRuntimeException("No parsed content found for response" + response);
    }

    private JsonElement toJsonElement(AbstractWebResponse response) {
        try {
            return convertToJson(response);
        } catch (Exception e) {
            log.debug("Error converting response to json element", e);
            return null;
        }
    }
}
