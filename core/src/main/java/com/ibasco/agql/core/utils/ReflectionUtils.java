/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.utils;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

@Deprecated
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
