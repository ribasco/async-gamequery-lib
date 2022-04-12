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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>StoreAppPlatform class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppPlatform {
    @SerializedName("windows")
    private boolean windowsSupported;
    @SerializedName("mac")
    private boolean macSupported;
    @SerializedName("linux")
    private boolean linuxSupported;

    /**
     * <p>isWindowsSupported.</p>
     *
     * @return a boolean
     */
    public boolean isWindowsSupported() {
        return windowsSupported;
    }

    /**
     * <p>Setter for the field <code>windowsSupported</code>.</p>
     *
     * @param windowsSupported a boolean
     */
    public void setWindowsSupported(boolean windowsSupported) {
        this.windowsSupported = windowsSupported;
    }

    /**
     * <p>isMacSupported.</p>
     *
     * @return a boolean
     */
    public boolean isMacSupported() {
        return macSupported;
    }

    /**
     * <p>Setter for the field <code>macSupported</code>.</p>
     *
     * @param macSupported a boolean
     */
    public void setMacSupported(boolean macSupported) {
        this.macSupported = macSupported;
    }

    /**
     * <p>isLinuxSupported.</p>
     *
     * @return a boolean
     */
    public boolean isLinuxSupported() {
        return linuxSupported;
    }

    /**
     * <p>Setter for the field <code>linuxSupported</code>.</p>
     *
     * @param linuxSupported a boolean
     */
    public void setLinuxSupported(boolean linuxSupported) {
        this.linuxSupported = linuxSupported;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("windows", isWindowsSupported())
                .append("mac", isMacSupported())
                .append("linux", isLinuxSupported())
                .toString();
    }
}
