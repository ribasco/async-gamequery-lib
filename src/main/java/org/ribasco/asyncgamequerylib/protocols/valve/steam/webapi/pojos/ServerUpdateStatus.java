/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raffy on 10/26/2016.
 */
public class ServerUpdateStatus extends SteamResponse {
    @SerializedName("up_to_date")
    private boolean upToDate;

    @SerializedName("version_is_listable")
    private boolean versionListable;

    @SerializedName("required_version")
    private int requiredVersion;

    private String message;

    public boolean isUpToDate() {
        return upToDate;
    }

    public void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    public boolean isVersionListable() {
        return versionListable;
    }

    public void setVersionListable(boolean versionListable) {
        this.versionListable = versionListable;
    }

    public int getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(int requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
