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

package com.ibasco.agql.protocols.valve.source.query.rcon;

import com.ibasco.agql.core.Credentials;
import com.ibasco.agql.core.util.Platform;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.ApiStatus;
import java.util.Objects;

/**
 * A collection of global constants and utility methods used internally by the RCON module
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class SourceRcon {

    /** Constant <code>DEFAULT_THREAD_GROUP</code> */
    public static final ThreadGroup DEFAULT_THREAD_GROUP = Platform.createThreadGroup(SourceRcon.class);

    /**
     * RCON Auth Request Header
     */
    public static final int RCON_TYPE_REQUEST_AUTH = 0x3;

    /**
     * RCON Command Request Header
     */
    public static final int RCON_TYPE_REQUEST_COMMAND = 0x2;

    /**
     * RCON Auth Response Header
     */
    public static final int RCON_TYPE_RESPONSE_AUTH = 0x2;

    /**
     * RCON Response Value Header
     */
    public static final int RCON_TYPE_RESPONSE_VALUE = 0;

    /**
     * RCON Response Value Header (Rust)
     */
    public static final int RCON_TYPE_RESPONSE_VALUE_RUST = 0x4;

    /**
     * A reserved request id representing a special rcon terminator packet
     */
    public static final int RCON_TERMINATOR_RID = -1;//8445800;

    /**
     * Flag indicating that the channel has been invalidated and needs to be re-authenticated
     */
    public static final AttributeKey<Boolean> INVALIDATED = AttributeKey.valueOf("rconInvalidated");

    /**
     * The minimum allowable value for an rcon request id
     */
    private static final int RCON_ID_MIN_RANGE = 100000000;

    /**
     * Flag indicating that the channel has been successfully authenticated by the remote server
     */
    //public static final AttributeKey<Boolean> AUTHENTICATED = AttributeKey.valueOf("rconAuthenticated");

    /**
     * The maximum allowable value for an rcon request id
     */
    private static final int RCON_ID_MAX_RANGE = 999999999;

    /**
     * Check if the packet is the primary terminator packet (The first terminator packet sent after a request)
     *
     * @param packet
     *         {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the rcon packet is the initial terminator packet (byte terminator value of 0x0)
     */
    public static boolean isInitialTerminatorPacket(SourceRconPacket packet) {
        return isTerminatorPacket(packet) && packet.getTerminator() == 0;
    }

    /**
     * Check if packet is a valid terminator packet
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the packet is a terminator packet
     */
    public static boolean isTerminatorPacket(SourceRconPacket packet) {
        Objects.requireNonNull(packet, "Packet argument must not be null");
        return isTerminatorId(packet.getId()) && isResponseValuePacket(packet);
    }

    /**
     * <p>Checks if the rcon request id represents a terminator packet</p>
     *
     * @param requestId
     *         An integer representing an Rcon Request Id
     *
     * @return <code>true</code> if the given id represents a terminator
     */
    public static boolean isTerminatorId(int requestId) {
        return RCON_TERMINATOR_RID == requestId;
    }

    /**
     * Check if packet is an RCON response value packet
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the packet is a valid response value packet
     */
    public static boolean isResponseValuePacket(SourceRconPacket packet) {
        Objects.requireNonNull(packet, "Packet argument must not be null");
        return RCON_TYPE_RESPONSE_VALUE == packet.getType() || RCON_TYPE_RESPONSE_VALUE_RUST == packet.getType();
    }

    /**
     * Check if the packet is the secondary terminator packet (The secondary terminator packet sent after a request)
     *
     * @param packet
     *         {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the rcon packet is the initial terminator packet (byte terminator value of 0x01)
     */
    public static boolean isSecondaryTerminatorPacket(SourceRconPacket packet) {
        return isTerminatorPacket(packet) && packet.getTerminator() == 0x01;
    }

    /**
     * <p>Checks if the request id is within the valid range</p>
     *
     * @param requestId
     *         An integer representing a request id
     *
     * @return <code>true</code> if the given request id is within the valid range
     */
    public static boolean isValidRequestId(int requestId) {
        return requestId >= RCON_ID_MIN_RANGE && requestId <= RCON_ID_MAX_RANGE;
    }

    /**
     * Check if value is valid RCON terminator
     *
     * @param terminator
     *         The terminator value
     *
     * @return {@code true} if the value is a valid rcon terminators
     */
    public static boolean isValidTerminator(int terminator) {
        return terminator == 0x01 || terminator == 0x00;
    }

    /**
     * Convenience method to determine if terminator packets are enabled for the provided channel
     *
     * @param ctx
     *         The {@link io.netty.channel.ChannelHandlerContext}
     *
     * @return {@code true} if terminator packets are enabled
     */
    public static boolean terminatorPacketEnabled(ChannelHandlerContext ctx) {
        Boolean useTerminatorPackets = ctx.channel().attr(SourceRconOptions.USE_TERMINATOR_PACKET.toAttributeKey()).get();
        return useTerminatorPackets == null ? SourceRconOptions.USE_TERMINATOR_PACKET.getDefaultValue() : useTerminatorPackets;
    }

    /**
     * Get the name of the provided {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket}
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return The name of the {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket}
     */
    public static String getPacketTypeName(SourceRconPacket packet) {
        if (isAuthRequestPacket(packet)) {
            return "SERVERDATA_AUTH";
        } else if (isAuthResponsePacket(packet)) {
            return "SERVERDATA_AUTH_RESPONSE";
        } else if (isCommandResponsePacket(packet)) {
            return "SERVERDATA_EXECCOMMAND";
        } else if (isTerminatorPacket(packet)) {
            return "TERMINATOR";
        } else if (isResponseValuePacket(packet)) {
            return "SERVERDATA_RESPONSE_VALUE";
        } else {
            throw new IllegalStateException("Unrecognized packet type");
        }
    }

    /**
     * Check if packet is an RCON Auth request packet
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if packet is a valid AUTH packet
     */
    public static boolean isAuthRequestPacket(SourceRconPacket packet) {
        Objects.requireNonNull(packet, "Packet argument must not be null");
        return RCON_TYPE_REQUEST_AUTH == packet.getType();
    }

    /**
     * Check if packet is an RCON auth response packet
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the packet is a valid auth response packet
     */
    public static boolean isAuthResponsePacket(SourceRconPacket packet) {
        Objects.requireNonNull(packet, "Packet argument must not be null");
        //a valid auth response packet should have:
        // - an id == -1 or > 0
        // - an empty response body
        return (packet.getId() == -1 || (packet.getId() > 0)) &&
                (packet.content().readableBytes() == 1 && (packet.content().getByte(0) == 0)) &&
                RCON_TYPE_RESPONSE_AUTH == packet.getType();
    }

    /**
     * Check if packet is a Command Response Packet
     *
     * @param packet
     *         The {@link com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket} to check
     *
     * @return {@code true} if the packet is a valid command response packet
     */
    public static boolean isCommandResponsePacket(SourceRconPacket packet) {
        return packet.getId() > 0 && RCON_TYPE_RESPONSE_AUTH == packet.getType() && packet.content().readableBytes() > 1;
    }

    /**
     * <p>getPacketTypeName.</p>
     *
     * @param type
     *         a int
     *
     * @return a {@link java.lang.String} object
     */
    public static String getPacketTypeName(int type) {
        switch (type) {
            case RCON_TYPE_REQUEST_AUTH: {
                return "SERVERDATA_AUTH";
            }
            case RCON_TYPE_RESPONSE_AUTH: {
                return "SERVERDATA_AUTH_RESPONSE/SERVERDATA_EXECCOMMAND";
            }
            case RCON_TYPE_RESPONSE_VALUE: {
                return "SERVERDATA_RESPONSE_VALUE";
            }
            case RCON_TYPE_RESPONSE_VALUE_RUST: {
                return "SERVERDATA_RESPONSE_VALUE_RUST";
            }
            default: {
                return "Unknown";
            }
        }
    }

    /**
     * <p>createAuthRequest.</p>
     *
     * @param credentials
     *         a {@link com.ibasco.agql.core.Credentials} object
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest} object
     */
    public static SourceRconAuthRequest createAuthRequest(Credentials credentials) {
        return createAuthRequest(credentials.getPassphrase());
    }

    /**
     * <p>createAuthRequest.</p>
     *
     * @param password
     *         an array of {@code byte} objects
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest} object
     */
    public static SourceRconAuthRequest createAuthRequest(byte[] password) {
        if (password == null || password.length == 0)
            throw new IllegalStateException("Password must not be null or empty");
        SourceRconAuthRequest request = new SourceRconAuthRequest(password);
        request.setRequestId(SourceRcon.createRequestId());
        return request;
    }

    /**
     * A utility method to generate random request ids
     *
     * @return An random integer ranging from 100000000 to 999999999
     */
    public static int createRequestId() {
        return RandomUtils.nextInt(RCON_ID_MIN_RANGE, RCON_ID_MAX_RANGE);
    }
}
