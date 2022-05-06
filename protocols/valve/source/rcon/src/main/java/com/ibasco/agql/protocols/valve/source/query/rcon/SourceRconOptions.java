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
import com.ibasco.agql.core.CredentialsStore;
import com.ibasco.agql.core.util.AbstractOptions;
import com.ibasco.agql.core.util.ConnectOptions;
import com.ibasco.agql.core.util.FailsafeOptions;
import com.ibasco.agql.core.util.Inherit;
import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket;
import java.net.InetSocketAddress;

/**
 * Configuration options container for the Source RCON module.
 *
 * @author Rafael Luis Ibasco
 * @see OptionBuilder
 * @see Options
 * @see SourceRconClient
 */
@Inherit(options = {FailsafeOptions.class, ConnectOptions.class})
public final class SourceRconOptions extends AbstractOptions {

    /**
     * <p>
     * Enable the use of "terminating packets" (Enabled by default).
     * <p/>
     *
     * <p>
     * Simply put, a terminator packet allows the library to accurately determine the end of a response ensuring the integrity of the data received from the game server. If disabled, a special heuristics will be performed to determine the end of response but this is not always accurate in some cases and its possible that you won't be receiving the entire response from the server (especially when dealing with large response packets).
     * </p>
     * <br />
     * <h3>Detailed description</h3>
     * <p>
     * If enabled, an empty <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#SERVERDATA_RESPONSE_VALUE">rcon response packet</a> is sent after every command. The game server will then mirror it back
     * at the end of the response, followed by an additional terminator packet with a terminating byte equals to 1 (0x01). This is mostly useful for large response packets that are sent in smaller chunks by the server, which would then
     * have to be re-assembled by the library back into a single {@link SourceRconPacket} instance.
     * <p/>
     * Keep in mind that some games such as <a href="https://wiki.vg/RCON">Minecraft</a> do not support "terminator packets" as it does not echo back the <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">response packets</a> receives. So if the game server does not support this,  then this configuration option should be <strong>disabled</strong>. A special heuristics will be used instead to determine the end of the response.
     *
     * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">Multi-Packet Responses</a>
     * @see SourceRconPacket
     * @see SourceRconPacketAssembler
     */
    public static final Option<Boolean> USE_TERMINATOR_PACKET = Option.create("rconUseTerminatorPackets", true, true);

    /**
     * Enable strict mode. When {@code false} the library will attempt to recover from malformed/corrupted packets.
     * Please note that disabling this option does not always guarantee that the decode process would succeed.
     */
    public static final Option<Boolean> STRICT_MODE = Option.create("rconStrictMode", true, true, true);

    /**
     * Automatically send a re-authentication request if the connection is no longer valid. This applies only to authenticated and registered addresses (registered via {@link SourceRconClient#authenticate(InetSocketAddress, byte[])}).
     */
    public static final Option<Boolean> REAUTHENTICATE = Option.create("rconReauth", true, true, true);

    /**
     * The {@link CredentialsStore} to be used by the rcon authentication module. Default is {@link InMemoryCredentialsStore}.
     *
     * @see Credentials
     * @see CredentialsStore
     * @see SourceRconMessenger
     */
    public static final Option<CredentialsStore> CREDENTIALS_STORE = Option.create("rconCredentialsStore", null);

    /**
     * Automatically close inactive channels/connections once it has reached the threshold value (value is in seconds). Set to -1 to disable.
     */
    public static final Option<Integer> CLOSE_INACTIVE_CHANNELS = Option.create("rconClosedUnused", 30);

    /**
     * Number of seconds to check for inactive {@link io.netty.channel.Channel}'s/connections (Unit: Seconds, Default Value: 1 sec)
     *
     * @see #CLOSE_INACTIVE_CHANNELS
     */
    public static final Option<Integer> INACTIVE_CHECK_INTERVAL = Option.create("rconInactiveCheckInterval", 1);

    /**
     * <p>Create a new {@link OptionBuilder} for {@link SourceRconOptions}</p>
     *
     * @return a newly instantiated {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder<SourceRconOptions> builder() {
        return OptionBuilder.newBuilder(SourceRconOptions.class);
    }
}
