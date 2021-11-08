/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

package com.ibasco.agql.core.session;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public final class SessionId implements Serializable, Comparable<SessionId> {

    private String id;

    public SessionId(SessionId id) {
        this(id.getId());
    }

    public SessionId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SessionId || o instanceof String))
            return false;
        if (o == this)
            return true;
        if (o instanceof String) {
            return defaultIfNull(getId(), "").equalsIgnoreCase((String) o);
        }
        return defaultIfNull(getId(), "").equalsIgnoreCase(((SessionId) o).getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(61, 235).append(getId()).hashCode();
    }

    @Override
    public int compareTo(SessionId o) {
        return new CompareToBuilder().append(getId(), o.getId()).toComparison();
    }

    @Override
    public String toString() {
        return "SessionId{" +
                "id='" + id + '\'' +
                '}';
    }
}
