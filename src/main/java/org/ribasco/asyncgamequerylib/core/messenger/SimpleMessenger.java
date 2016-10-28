package org.ribasco.asyncgamequerylib.core.messenger;

import org.ribasco.asyncgamequerylib.core.AbstractRequest;
import org.ribasco.asyncgamequerylib.core.AbstractResponse;
import org.ribasco.asyncgamequerylib.core.Messenger;
import org.ribasco.asyncgamequerylib.core.Transport;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

abstract public class SimpleMessenger<Req extends AbstractRequest, Res extends AbstractResponse> implements Messenger<Req, Res> {

    private Transport<Req> transport;

    public SimpleMessenger(Transport<Req> transport) {
        this.transport = transport;
    }

    @Override
    public CompletableFuture<Res> send(Req request) {
        CompletableFuture<Res> promise = new CompletableFuture<>();
        this.transport.send(request);
        return promise;
    }

    @Override
    public void accept(Res res, Throwable throwable) {

    }

    @Override
    public void close() throws IOException {
        this.transport.close();
    }
}
