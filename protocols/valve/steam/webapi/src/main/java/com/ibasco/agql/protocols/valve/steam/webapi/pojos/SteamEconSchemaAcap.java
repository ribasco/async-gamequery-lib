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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * "Acap" stands for Attribute Controlled Attached Particles. An object containing a list
 * of objects that describe the defined particle effects.
 * </p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaAcap {

    /**
     * The name of the particle system.
     */
    private String system;

    /**
     * The effect's ID, referred to by the attached particle effect attribute.
     */
    private int id;

    /**
     * A boolean that indicates whether or not the effect is attached to the "root" bone.
     * That is the bone of the item with no parent bones used for rotation and animation calculations.
     */
    private boolean attach_to_rootbone;

    /**
     * (Optional) A string indicating where the effect is attached.
     */
    private String attachment;

    /**
     * The localized name of the effect.
     */
    private String name;

    /**
     * <p>Getter for the field <code>system</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSystem() {
        return system;
    }

    /**
     * <p>Setter for the field <code>system</code>.</p>
     *
     * @param system
     *         a {@link java.lang.String} object
     */
    public void setSystem(String system) {
        this.system = system;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a int
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>isAttach_to_rootbone.</p>
     *
     * @return a boolean
     */
    public boolean isAttach_to_rootbone() {
        return attach_to_rootbone;
    }

    /**
     * <p>Setter for the field <code>attach_to_rootbone</code>.</p>
     *
     * @param attach_to_rootbone
     *         a boolean
     */
    public void setAttach_to_rootbone(boolean attach_to_rootbone) {
        this.attach_to_rootbone = attach_to_rootbone;
    }

    /**
     * <p>Getter for the field <code>attachment</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * <p>Setter for the field <code>attachment</code>.</p>
     *
     * @param attachment
     *         a {@link java.lang.String} object
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
