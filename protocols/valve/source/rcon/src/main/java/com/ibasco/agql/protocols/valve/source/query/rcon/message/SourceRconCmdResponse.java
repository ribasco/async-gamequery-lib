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

import org.apache.commons.lang3.StringUtils;

/**
 * <p>SourceRconCmdResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconCmdResponse extends SourceRconResponse<String> {

    /**
     * <p>Constructor for SourceRconCmdResponse.</p>
     *
     * @param result a {@link java.lang.String} object
     */
    public SourceRconCmdResponse(String result) {
        super(result);
    }

    /**
     * <p>getCommand.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCommand() {
        SourceRconCmdRequest request = (SourceRconCmdRequest) getRequest();
        return request.getCommand();
    }

    /**
     * <p>getResult.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public final String getResult() {
        return super.getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("%s = %s", super.toString(), StringUtils.truncate(getResult(), 32));
    }
}
