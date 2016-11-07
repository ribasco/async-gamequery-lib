/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

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
