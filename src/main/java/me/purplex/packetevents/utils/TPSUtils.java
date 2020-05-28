package me.purplex.packetevents.utils;
import java.lang.reflect.InvocationTargetException;

public class TPSUtils {
    public static double[] getRecentTPS() {
        try {
            return NMSUtils.recentTPS();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
