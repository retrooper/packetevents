package me.purplex.packetevents.utils;

import me.purplex.packetevents.enums.ServerVersion;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class NMSUtils {
    private static final ServerVersion version = ServerVersion.getVersion();


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
        //CLASSES
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

        //FIELDS
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

    public static Object getEntityById(final int id) throws InvocationTargetException, IllegalAccessException {
        //1.13.2 worlds field -> getWorlds()
        //1.8->1.8.8, world.a(ID); rest is getEntity(ID);
        final List<World> worlds = Bukkit.getWorlds();
        for (World w : worlds) {
           /* Object craftWorld = craftWorldsClass.cast(w);

            Object worldServer = getCraftWorldHandleMethod.invoke(craftWorld);
            try {
                return entityMethod.invoke(worldServer, id);
            } catch (NullPointerException e) {
                continue;
            }*/
            for(Entity e : w.getEntities()) {
                if(e.getEntityId() == id) {
                    return e;
                }
            }
        }
        return null;
    }

    public static boolean is_1_8() {
        return version.isHigherThan(ServerVersion.v_1_7_10)
                && version.isLowerThan(ServerVersion.v_1_9);
    }
}
