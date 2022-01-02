/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StoreAppPlatform {
    @SerializedName("windows")
    private boolean windowsSupported;
    @SerializedName("mac")
    private boolean macSupported;
    @SerializedName("linux")
    private boolean linuxSupported;

    public boolean isWindowsSupported() {
        return windowsSupported;
    }

    public void setWindowsSupported(boolean windowsSupported) {
        this.windowsSupported = windowsSupported;
    }

    public boolean isMacSupported() {
        return macSupported;
    }

    public void setMacSupported(boolean macSupported) {
        this.macSupported = macSupported;
    }

    public boolean isLinuxSupported() {
        return linuxSupported;
    }

    public void setLinuxSupported(boolean linuxSupported) {
        this.linuxSupported = linuxSupported;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("windows", isWindowsSupported())
                .append("mac", isMacSupported())
                .append("linux", isLinuxSupported())
                .toString();
    }
}
