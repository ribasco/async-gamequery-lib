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

import java.util.Map;

/**
 * Created by raffy on 9/26/2016.
 */
public abstract class AbstractSessionKeyFactory<A extends SessionKey, Req extends AbstractRequest, Res extends AbstractResponse> implements SessionKeyFactory<A, Req, Res> {
    private Map<Class<? extends Req>, Class<? extends Res>> lookup;

    protected Class<? extends Res> findResponseClass(Req request) {
        if (lookup == null)
            throw new IllegalStateException("Lookup map has not been initialized");
        if (request != null)
            return lookup.get(request.getClass());
        return null;
    }

    public Map<Class<? extends Req>, Class<? extends Res>> getLookup() {
        return lookup;
    }

    public void setLookup(Map<Class<? extends Req>, Class<? extends Res>> lookup) {
        this.lookup = lookup;
    }
}
