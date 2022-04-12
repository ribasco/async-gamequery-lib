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

package com.ibasco.agql.protocols.valve.source.query.rcon.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>SourceRconCmdRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconCmdRequest extends SourceRconRequest {

    private final String command;

    /**
     * <p>Constructor for SourceRconCmdRequest.</p>
     *
     * @param command a {@link java.lang.String} object
     */
    public SourceRconCmdRequest(String command) {
        this.command = command;
    }

    /**
     * <p>Getter for the field <code>command</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public final String getCommand() {
        return command;
    }

    /** {@inheritDoc} */
    @Override
    protected void buildToString(ToStringBuilder builder) {
        super.buildToString(builder);
        builder.append("command", getCommand());
    }
}
