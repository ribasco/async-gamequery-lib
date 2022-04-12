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
import java.util.Collection;

/**
 * <p>CollectionParameterizedType class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CollectionParameterizedType implements ParameterizedType {

    private final Type type;
    private final Class<? extends Collection> classListType;

    /**
     * <p>Constructor for CollectionParameterizedType.</p>
     *
     * @param type a {@link java.lang.reflect.Type} object
     * @param classListType a {@link java.lang.Class} object
     */
    public CollectionParameterizedType(Type type, Class<? extends Collection> classListType) {
        this.type = type;
        this.classListType = classListType;
    }

    /** {@inheritDoc} */
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{type};
    }

    /** {@inheritDoc} */
    @Override
    public Type getRawType() {
        return classListType;
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
