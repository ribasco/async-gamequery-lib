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

package com.ribasco.rglib.protocols.valve.source.client;

import com.ribasco.rglib.core.AbstractClient;
import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.protocols.valve.source.SourceRconMessenger;
import com.ribasco.rglib.protocols.valve.source.SourceRconRequest;
import com.ribasco.rglib.protocols.valve.source.SourceRconResponse;
import com.ribasco.rglib.protocols.valve.source.request.SourceRconAuthRequest;
import com.ribasco.rglib.protocols.valve.source.request.SourceRconCmdRequest;
import io.netty.util.concurrent.Promise;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceRconClient extends AbstractClient<SourceRconRequest, SourceRconResponse, SourceRconMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconClient.class);

    /**
     * Contains a map of authenticated request ids with the server address as the key
     */
    private Map<InetSocketAddress, Integer> authMap;

    public SourceRconClient() {
        super(new SourceRconMessenger());
        authMap = new ConcurrentHashMap<>();
    }

    public Promise<Integer> authenticate(InetSocketAddress address, String password, Callback<Integer> callback) {
        if (StringUtils.isEmpty(password))
            throw new IllegalArgumentException("No password specified");
        int id = createRequestId();
        log.debug("Requesting with id: {}", id);
        Promise<Integer> p = this.getMessenger().getTransport().newPromise();

        Promise<Integer> promise = sendRequest(new SourceRconAuthRequest(address, id, password), (response, sender, error) -> {
            if (error != null) {
                callback.onComplete(null, sender, error);
                return;
            }
            authMap.put(sender, response);
            callback.onComplete(response, sender, error);
        }, RequestPriority.HIGH);
        return promise;
    }

    public Promise<String> execute(InetSocketAddress address, String command, Callback<String> callback) {
        if (!isAuthenticated(address))
            throw new IllegalStateException("You are not yet authorized to access the server's rcon interface. Please authenticate first.");

        final Integer id = createRequestId();
        log.debug("Executing command '{}' using request id: {}", command, id);

        return sendRequest(new SourceRconCmdRequest(address, id, command), callback);
    }

    private Integer getAuthenticationId(InetSocketAddress server) {
        return authMap.get(server);
    }

    private boolean isAuthenticated(InetSocketAddress server) {
        return authMap.containsKey(server) && (authMap.get(server) != null);
    }

    private int createRequestId() {
        return RandomUtils.nextInt(100000000, 999999999);
    }

    /*@Override
    public Object resolveKey(SourceRconRequest message) {
        return message.recipient();
    }*/
}
