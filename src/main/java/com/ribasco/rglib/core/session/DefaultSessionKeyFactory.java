/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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

package com.ribasco.rglib.core.session;

import com.ribasco.rglib.core.AbstractRequest;
import com.ribasco.rglib.core.AbstractResponse;

/**
 * Created by raffy on 9/26/2016.
 */
public class DefaultSessionKeyFactory extends AbstractSessionKeyFactory<DefaultSessionKey, AbstractRequest, AbstractResponse> {

    @Override
    public DefaultSessionKey createKey(AbstractRequest request) {
        //Look for the response class associated with this request
        final Class<? extends AbstractResponse> responseClass = findResponseClass(request);
        if (responseClass == null)
            throw new IllegalStateException(String.format("Unable to create session key for request. No response handler found for request '%s'. Please add mapping for this request using addMapping()", request.getClass().getSimpleName()));
        return new DefaultSessionKey(responseClass, request.recipient());
    }

    @Override
    public DefaultSessionKey createKey(AbstractResponse response) {
        return new DefaultSessionKey(response);
    }

    @Override
    public DefaultSessionKey duplicate(DefaultSessionKey key) {
        return new DefaultSessionKey(key);
    }
}
