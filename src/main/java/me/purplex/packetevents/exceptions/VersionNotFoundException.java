package me.purplex.packetevents.exceptions;

public class VersionNotFoundException extends Exception {
    public VersionNotFoundException() {
        super("VersionNotFound! Failed to access an NMS Field.");
    }
}
