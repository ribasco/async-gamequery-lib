package org.ribasco.asyncgamequerylib.core.reflect;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapParameterizedType implements ParameterizedType {

    private Type type;
    private Class<? extends Map> classType;

    public MapParameterizedType(Type type, Class<? extends Map> classType) {
        this.type = type;
        this.classType = classType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[0];
    }

    @Override
    public Type getRawType() {
        return classType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }
}
