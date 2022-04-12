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

package com.ibasco.agql.core;

import com.ibasco.agql.core.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * <p>Abstract AbstractMessage class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractMessage implements Message {

    private final UUID id = UUID.create();

    /** {@inheritDoc} */
    @Override
    public final UUID id() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMessage that = (AbstractMessage) o;
        return id().equals(that.id());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id());
    }

    /**
     * <p>buildToString.</p>
     *
     * @param builder a {@link org.apache.commons.lang3.builder.ToStringBuilder} object
     */
    protected void buildToString(ToStringBuilder builder) {
        builder.append("id", this.id());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        buildToString(builder);
        return builder.toString();
    }
}
