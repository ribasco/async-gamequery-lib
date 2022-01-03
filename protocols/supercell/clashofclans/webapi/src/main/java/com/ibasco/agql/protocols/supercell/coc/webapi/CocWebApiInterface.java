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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.google.gson.JsonElement;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.core.AbstractWebApiResponse;
import com.ibasco.agql.core.AbstractWebResponse;
import com.ibasco.agql.protocols.supercell.coc.webapi.exceptions.CocIncorrectParametersException;
import com.ibasco.agql.protocols.supercell.coc.webapi.exceptions.CocWebApiException;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocErrorResponse;
import io.netty.handler.codec.http.HttpStatusClass;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
@ApiStatus.ScheduledForRemoval
abstract public class CocWebApiInterface
        extends AbstractWebApiInterface<CocWebApiClient, CocWebApiRequest, CocWebApiResponse> {

    private static final Logger log = LoggerFactory.getLogger(CocWebApiInterface.class);

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link CocWebApiClient} instance
     */
    public CocWebApiInterface(CocWebApiClient client) {
        super(client);
    }

    /**
     * Handle Error Events
     *
     * @param res
     * @param error
     */
    @Override
    protected void handleError(AbstractWebResponse res, Throwable error) {
        if (!(res instanceof AbstractWebApiResponse)) {
            super.handleError(res, error);
            return;
        }
        //noinspection unchecked
        AbstractWebApiResponse<JsonElement> response = (AbstractWebApiResponse<JsonElement>) res;
        if (error != null)
            throw new CocWebApiException(error);
        if (response.getStatus() == HttpStatusClass.CLIENT_ERROR) {
            if (response.getProcessedContent() != null) {
                CocErrorResponse err = builder().fromJson(response.getProcessedContent(), CocErrorResponse.class);
                log.error("[ERROR]: Reason: {}, Message: {}", err.getReason(), err.getMessage());
            }
            switch (response.getMessage().getStatusCode()) {
                case 400:
                    throw new CocIncorrectParametersException("Incorrect parameters provided for request");
                default:
                    break;
            }
            //Let the base class handle the rest
            super.handleError(response, error);
        }
    }
}
