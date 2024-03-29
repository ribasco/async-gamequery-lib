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
 * <p>StoreGameControllerInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreGameControllerInfo {

    @SerializedName("full_gamepad")
    private boolean fullGamepad;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("fullGamepad", isFullGamepad()).toString();
    }

    /**
     * <p>isFullGamepad.</p>
     *
     * @return a boolean
     */
    public boolean isFullGamepad() {
        return fullGamepad;
    }

    /**
     * <p>Setter for the field <code>fullGamepad</code>.</p>
     *
     * @param fullGamepad
     *         a boolean
     */
    public void setFullGamepad(boolean fullGamepad) {
        this.fullGamepad = fullGamepad;
    }
}
