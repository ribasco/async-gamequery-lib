package com.ribasco.gamecrawler.protocols.valve.source;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.GameResponse;
import io.netty.buffer.ByteBuf;

/**
 * Created by raffy on 9/5/2016.
 */
public abstract class SourceResponsePacket<T> implements GameResponse<T> {

    private final ByteBuf buffer;

    public SourceResponsePacket(ByteBuf buffer) {
        this.buffer = buffer;
    }

    protected abstract T createFromBuffer();

    @Override
    public T toObject() {
        return createFromBuffer();
    }

    /**
     * Returns the associated request/query class for this GameResponse
     *
     * @return Class
     */
    public abstract Class<? extends GameRequest> getRequestClass();

    public ByteBuf getBuffer() {
        return buffer;
    }
}
