package com.ribasco.gamecrawler.protocols;

/**
 * Created by raffy on 9/5/2016.
 */
public interface GameResponse<T> {
    T toObject();
    Class<? extends GameRequest> getRequestClass();
}
