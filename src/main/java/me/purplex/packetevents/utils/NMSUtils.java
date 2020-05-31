package me.purplex.packetevents.utils;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.utils.entityfinder.EntityFinderUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtils {
    private static final ServerVersion version = ServerVersion.getVersion();


    private static final String nettyPrefix = version == ServerVersion.v_1_7_10 ? "net.minecraft.util.io.netty" : "io.netty";
    private static final String nmsDir = ServerVersion.getNMSDirectory();
    private static final String obcDir = ServerVersion.getOBCDirectory();

    public static Class<?> minecraftServerClass;
    private static Class<?> craftWorldsClass;
    private static Method getServerMethod;
    private static Field recentTPSField;

    private static Class<?> worldServerClass;


    private static Method getCraftWorldHandleMethod;
    private static Method entityMethod;

    static {

        try {
            minecraftServerClass = getNMSClass("MinecraftServer");
            worldServerClass = getNMSClass("WorldServer");
            craftWorldsClass = getOBCClass("CraftWorld");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //METHODS
        try {
            getServerMethod = minecraftServerClass.getMethod("getServer");
            getCraftWorldHandleMethod = craftWorldsClass.getMethod("getHandle");
            // entityMethod = worldServerClass.getMethod(is_1_8() ? "a" : "getEntity");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        try {
            recentTPSField = minecraftServerClass.getField("recentTps");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object getMinecraftServerInstance() throws InvocationTargetException, IllegalAccessException {
        return getServerMethod.invoke(null);
    }

    public static double[] recentTPS() throws IllegalAccessException, InvocationTargetException {
        final Object minecraftServerObj = getMinecraftServerInstance();
        double[] recentTPS = (double[]) recentTPSField.get(minecraftServerObj);
        return recentTPS;
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(nmsDir + "." + name);
    }

    public static Class<?> getOBCClass(String name) throws ClassNotFoundException {
        return Class.forName(obcDir + "." + name);
    }

    public static Class<?> getNettyClass(String name) throws ClassNotFoundException {
        return Class.forName(nettyPrefix + "." + name);
    }

    public static Entity getNearByEntityById(final World w, final int id) throws InvocationTargetException, IllegalAccessException {
        return EntityFinderUtils.getEntityById(w, id);
    }

    public static boolean is_1_8() {
        return version.isHigherThan(ServerVersion.v_1_7_10)
                && version.isLowerThan(ServerVersion.v_1_9);
    }

    public static String getServerConnectionFieldName() {
        return "p";
    }
}
