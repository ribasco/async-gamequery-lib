package com.ibasco.agql.examples.base;

import java.io.Closeable;

public interface BaseExample extends Closeable {
    void run() throws Exception;
}
