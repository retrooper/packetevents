package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.SystemOS;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.InvocationTargetException;

public class ServerUtils {
    public ServerVersion getServerVersion() {
        return ServerVersion.getVersion();
    }

    public double[] getRecentServerTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    public double getCurrentServerTPS() {
        return getRecentServerTPS()[0];
    }

    public SystemOS getPlatform(){
        return SystemOS.getOperatingSystem();
    }
}
