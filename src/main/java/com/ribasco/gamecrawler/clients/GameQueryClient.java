package com.ribasco.gamecrawler.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * Created by raffy on 8/27/2016.
 */
public abstract class GameQueryClient implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(GameQueryClient.class);
}
