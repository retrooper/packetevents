package io.github.explored.packetevents.utils;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class TPSUtils {
    @Nullable
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
