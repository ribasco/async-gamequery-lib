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

package com.ibasco.agql.core.reflect.types;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>MapParameterizedType class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MapParameterizedType implements ParameterizedType {

    private final Type type;
    private final Class<? extends Map> classType;

    /**
     * <p>Constructor for MapParameterizedType.</p>
     *
     * @param type a {@link java.lang.reflect.Type} object
     * @param classType a {@link java.lang.Class} object
     */
    public MapParameterizedType(Type type, Class<? extends Map> classType) {
        this.type = type;
        this.classType = classType;
    }

    /** {@inheritDoc} */
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[0];
    }

    /** {@inheritDoc} */
    @Override
    public Type getRawType() {
        return classType;
    }

    /** {@inheritDoc} */
    @Override
    public Type getOwnerType() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }
}
