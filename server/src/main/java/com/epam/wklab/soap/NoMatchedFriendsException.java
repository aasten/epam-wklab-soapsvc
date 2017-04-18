package com.epam.wklab.soap;

/**
 * Created by sten on 11.04.17.
 */
public class NoMatchedFriendsException extends Exception {
    /**
     * @param msg message to be shown to client
     */
    public NoMatchedFriendsException(final String msg) {
        super(msg);
    }
}
