package org.ribasco.asyncgamequerylib.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract public class CriteriaBuilder<T extends CriteriaBuilder> {
    private Map<String, Object> criteriaMap = new HashMap<>();

    protected T put(String name, Object value) {
        criteriaMap.put(name, value);
        return (T) this;
    }

    public Set<Map.Entry<String, Object>> getCriteriaSet() {
        return criteriaMap.entrySet();
    }
}
