/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.message;

import org.apache.commons.lang3.StringUtils;

public final class SourceRconCmdResponse extends SourceRconResponse {

    private final String result;

    private final String command;

    public SourceRconCmdResponse(int requestId, String command, String result, boolean success) {
        this(requestId, command, result, success, null);
    }

    public SourceRconCmdResponse(int requestId, String command, String result, boolean success, Throwable error) {
        super(requestId, success, error);
        this.command = command;
        this.result = result;
    }

    public SourceRconCmdResponse(SourceRconCmdResponse copy) {
        super(copy.getRequestId(), copy.isSuccess(), copy.getError());
        this.command = copy.command;
        this.result = copy.result;
    }

    public String getCommand() {
        return command;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", super.toString(), StringUtils.truncate(getResult(), 32));
    }
}
