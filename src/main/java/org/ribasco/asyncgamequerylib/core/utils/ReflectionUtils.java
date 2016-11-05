package org.ribasco.asyncgamequerylib.core.utils;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T createInstance(Class<T> classOf) {
        try {
            return ConstructorUtils.invokeConstructor(classOf);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.debug("Error on createInstance()", e);
        }
        return null;
    }
}
