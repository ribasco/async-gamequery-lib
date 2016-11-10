/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.supercell.coc.webapi;

import io.netty.handler.codec.http.HttpStatusClass;
import org.ribasco.agql.core.AbstractWebApiInterface;
import org.ribasco.agql.protocols.supercell.coc.webapi.exceptions.CocIncorrectParametersException;
import org.ribasco.agql.protocols.supercell.coc.webapi.exceptions.CocWebApiException;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param response
     * @param error
     */
    @Override
    protected void interceptResponse(CocWebApiResponse response, Throwable error) {
        if (error != null)
            throw new CocWebApiException(error);
        if (response.getStatus() == HttpStatusClass.CLIENT_ERROR) {
            if (response.getProcessedContent() != null) {
                CocErrorResponse err = builder().fromJson(response.getProcessedContent(), CocErrorResponse.class);
                log.error("[ERROR FROM {}]: Reason: {}, Message: {}", response.sender(), err.getReason(), err.getMessage());
            }
            switch (response.getMessage().getStatusCode()) {
                case 400:
                    throw new CocIncorrectParametersException("Incorrect parameters provided for request");
            }
            //Let the base class handle the rest
            super.interceptResponse(response, error);
        }
    }
}
