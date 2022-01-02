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

package com.ibasco.agql.protocols.valve.source.query.packets;

import com.ibasco.agql.protocols.valve.source.query.SourceRcon;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A factory class for creating {@link SourceRconPacket} instances
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconPacketFactory {

    private static final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

    /**
     * Do not allow instantiation. This class is meant to be accessed statically
     */
    private SourceRconPacketFactory() {
    }

    /**
     * Create a new Authentication Request Packet
     *
     * @param id
     *         The request id to be associated with this request
     * @param password
     *         The rcon password to use to authenticate with the server
     *
     * @return A {@link SourceRconPacket}
     */
    public static SourceRconPacket createAuth(final int id, byte[] password) {
        Objects.requireNonNull(password, "Password is null");
        ByteBuf payload = allocator.directBuffer(password.length + 1);
        payload.writeBytes(password);
        payload.writeByte(0);
        return createPacket(id, SourceRcon.RCON_TYPE_REQUEST_AUTH, payload);
    }

    /**
     * Create an authentication response packet
     *
     * @param id
     *         A positive integer indicating a successful authentication or -1 if authentication failed
     *
     * @return An authentication response packet wrapped in {@link SourceRconPacket}
     */
    public static SourceRconPacket createAuthResponse(int id) {
        return createPacket(id, SourceRcon.RCON_TYPE_RESPONSE_AUTH, allocator.directBuffer(1).writeByte(0));
    }

    /**
     * Create a command request packet
     *
     * @param id
     *         A unique positive integer
     * @param command
     *         The command to be sent to the server
     *
     * @return A command request packet wrapped in {@link SourceRconPacket}
     */
    public static SourceRconPacket createCommand(int id, String command) {
        Objects.requireNonNull(command, "Command is null");
        ByteBuf payload = allocator.directBuffer(command.length() + 1);
        payload.writeCharSequence(command, StandardCharsets.US_ASCII);
        payload.writeByte(0);
        return createPacket(id, SourceRcon.RCON_TYPE_REQUEST_COMMAND, payload);
    }

    /**
     * Create a response value packet
     *
     * @param id
     *         A positive or negative value
     * @param response
     *         The message payload of the response in the form of a {@link String}
     *
     * @return A {@link SourceRconPacket}
     */
    public static SourceRconPacket createResponseValue(int id, String response) {
        ByteBuf payload = allocator.directBuffer(response.length() + 1);
        payload.writeCharSequence(response, StandardCharsets.US_ASCII);
        payload.writeByte(0);
        return createResponseValue(id, payload);
    }

    /**
     * Create a response value packet
     *
     * @param id
     *         A positive or negative value
     * @param payload
     *         The message payload of the response
     *
     * @return A {@link SourceRconPacket}
     */
    public static SourceRconPacket createResponseValue(int id, ByteBuf payload) {
        return createPacket(id, SourceRcon.RCON_TYPE_RESPONSE_VALUE, payload);
    }

    /**
     * Create a new terminator packet. A terminator packet is a special type of response value packet which sent after every command issued to the server.
     *
     * @return A special response value packet with an id of -1 wrapped in {@link SourceRconPacket}
     */
    public static SourceRconPacket createTerminator() {
        return createResponseValue(SourceRcon.RCON_TERMINATOR_RID, StringUtils.EMPTY);
    }

    public static SourceRconPacket createPacket(int id, int type, ByteBuf payload) {
        int size = payload.capacity() + 9;
        assert size >= 10;
        return createPacket(size, id, type, 0, payload);
    }

    public static SourceRconPacket createPacket(int size, int id, int type, int terminator, ByteBuf payload) {
        SourceRconPacket packet = new SourceRconPacket(type, payload);
        packet.setSize(size);
        packet.setId(id);
        packet.setTerminator(terminator);
        return packet;
    }
}
