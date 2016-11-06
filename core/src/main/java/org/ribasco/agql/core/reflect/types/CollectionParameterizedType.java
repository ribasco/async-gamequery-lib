package org.ribasco.agql.core.reflect.types;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class CollectionParameterizedType implements ParameterizedType {

    private Type type;
    private Class<? extends Collection> classListType;

    public CollectionParameterizedType(Type type, Class<? extends Collection> classListType) {
        this.type = type;
        this.classListType = classListType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{type};
    }

    @Override
    public Type getRawType() {
        return classListType;
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
