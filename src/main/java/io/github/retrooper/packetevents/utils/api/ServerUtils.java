package io.github.retrooper.packetevents.utils.api;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.SystemOS;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.InvocationTargetException;

public final class ServerUtils {
    @Deprecated
    public ServerVersion getServerVersion() {
        return getVersion();
    }
    @Deprecated
    public double[] getRecentServerTPS() {
       return getRecentTPS();
    }
    @Deprecated
    public double getCurrentServerTPS() {
        return getCurrentTPS();
    }

    public ServerVersion getVersion() {
        return ServerVersion.getVersion();
    }

    public double[] getRecentTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    public double getCurrentTPS() {
        return getRecentTPS()[0];
    }

    public SystemOS getPlatform() {
        return SystemOS.getOperatingSystem();
    }
}
