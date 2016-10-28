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

package org.ribasco.asyncgamequerylib.core.session;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public final class SessionId implements Serializable, Comparable<SessionId> {
    private String id;

    public SessionId(SessionId id) {
        this(id.getId());
    }

    public SessionId(String id) {
        this.id = id;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
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
    public final int hashCode() {
        return new HashCodeBuilder(61, 235).append(getId()).hashCode();
    }

    @Override
    public String toString() {
        return "SessionId{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(SessionId o) {
        return new CompareToBuilder().append(getId(), o.getId()).toComparison();
    }
}
