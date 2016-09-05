package com.ribasco.gamecrawler.protocols;

/**
 * Created by raffy on 9/1/2016.
 */
public interface Callback<T> {
    void onReceive(T msg);
}
