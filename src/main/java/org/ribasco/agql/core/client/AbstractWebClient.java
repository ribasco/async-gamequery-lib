package org.ribasco.agql.core.client;

import org.ribasco.agql.core.AbstractWebRequest;
import org.ribasco.agql.core.AbstractWebResponse;
import org.ribasco.agql.core.Client;
import org.ribasco.agql.core.messenger.WebMessenger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * A generic Http Client base
 *
 * @param <Req> Type of {@link AbstractWebRequest}
 * @param <Res> Type of {@link AbstractWebResponse}
 */
abstract class AbstractWebClient<Req extends AbstractWebRequest, Res extends AbstractWebResponse>
        implements Client<Req, Res> {

    private WebMessenger<Req, Res> messenger;

    AbstractWebClient() {
        this.messenger = createWebMessenger();
    }

    abstract public WebMessenger<Req, Res> createWebMessenger();

    @Override
    @SuppressWarnings("unchecked")
    public <V> CompletableFuture<V> sendRequest(Req message) {
        return (CompletableFuture<V>) messenger.send(message);
    }

    @Override
    public void close() throws IOException {
        messenger.close();
    }
}
