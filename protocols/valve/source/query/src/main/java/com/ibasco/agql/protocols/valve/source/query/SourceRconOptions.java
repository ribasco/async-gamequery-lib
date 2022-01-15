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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceRconPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;

/**
 * A class containing the available configuration {@link Option} for the {@link SourceRconClient}
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconOptions {

    /**
     * Enable the use of "terminating packets". A terminator packet allows the library to accurately determine the end of a response.
     * <p/>
     * When enabled, an empty rcon response packet is sent after every command. The game server will then mirror it back
     * at the end of the response, followed by an additional terminator packet with a terminating byte equals to 1 (0x01), thus allowing the library to determine
     * that it has reached the end of the response. This is mostly useful for large response packets that are sent in smaller chunks by the server, which would then
     * have to be re-assembled by the library back into a single {@link SourceRconPacket} instance.
     * <p>
     * <p/>
     * Please note that some games such as <a href="https://wiki.vg/RCON">Minecraft</a> does not support this mode, if that is the case this configuration option should be left disabled. A special heuristics will be used instead to determine the end of the response.
     *
     * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">Multi-Packet Responses</a>
     * @see SourceRconPacket
     * @see SourceRconPacketAssembler
     */
    public static final Option<Boolean> USE_TERMINATOR_PACKET = Option.createOption("rconUseTerminatorPackets", true, true);

    /**
     * Enable strict mode. When {@code false} the library will attempt to recover malformed/corrupted packets.
     * Please note that disabling this option does not guarantee that the decode process will always be successful.
     */
    public static final Option<Boolean> STRICT_MODE = Option.createOption("rconStrictMode", true, true, true);

    /**
     * Automatically send a reauthentication request if the connection is no longer valid. This applies only to successfully authenticated addresses.
     */
    public static final Option<Boolean> REAUTHENTICATE = Option.createOption("rconReauth", true, true, true);

    /**
     * The {@link CredentialsManager} to be used by the rcon authentication module. Default is {@link RconCredentialsManager}.
     *
     * @see Credentials
     * @see CredentialsManager
     * @see SourceRconAuthProxy
     */
    public static final Option<CredentialsManager> CREDENTIALS_MANAGER = Option.createOption("rconCredManager", null);

    /**
     * Automatically close inactive channels/connections once it has reached the threshold value (value is in seconds). Set to -1 to disable.
     */
    public static final Option<Integer> CLOSE_INACTIVE_CHANNELS = Option.createOption("rconClosedUnused", 30);
}
