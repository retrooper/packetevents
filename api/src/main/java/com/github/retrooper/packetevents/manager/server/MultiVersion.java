package com.github.retrooper.packetevents.manager.server;

/**
 * This enum contains all possible comparison types for server versions.
 */
public enum MultiVersion {
    /*
    The server version equals the compared server version.
     */
    EQUALS,
    /*
    The server version is newer than the compared server version.
     */
    NEWER_THAN,
    /*
    The server version is newer than or equal to the compared server version.
     */
    NEWER_THAN_OR_EQUALS,
    /*
    The server version is older than the compared server version.
     */
    OLDER_THAN,
    /*
    The server version is older than or equal to the compared server version.
     */
    OLDER_THAN_OR_EQUALS;
}