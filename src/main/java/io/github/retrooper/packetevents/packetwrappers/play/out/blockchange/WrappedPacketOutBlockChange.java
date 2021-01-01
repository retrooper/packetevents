package io.github.retrooper.packetevents.packetwrappers.play.out.blockchange;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * UNFINISHED, PLEASE DONT USE
 */
public class WrappedPacketOutBlockChange extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private static Method iBlockDataMethodCache = null;
    private static Method getBlockIdMethodCache = null;
    private Object blockPosObj = null;
    private int blockPosX;
    private int blockPosY;
    private int blockPosZ;
    private World world;
    public WrappedPacketOutBlockChange(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockChange(int blockPosX, int blockPosY, int blockPosZ, World world) {

    }

    @Override
    protected void load() {
        iBlockDataMethodCache = Reflection.getMethod(NMSUtils.iBlockDataClass, NMSUtils.blockClass, 0);
        getBlockIdMethodCache = Reflection.getMethod(NMSUtils.blockClass, "getId", 0);

        if (version.equals(ServerVersion.v_1_7_10)) {
            try {
                packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor(int.class, int.class, int.class, NMSUtils.nmsWorldClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public int getBlockX() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(0);
        } else {
            if(blockPosObj == null) {
                blockPosObj =  readObject(0, NMSUtils.blockPosClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getX", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public int getBlockY() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(1);
        } else {
            if(blockPosObj == null) {
                blockPosObj =  readObject(0, NMSUtils.blockPosClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getY", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public int getBlockZ() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(2);
        } else {
            if(blockPosObj == null) {
                blockPosObj =  readObject(0, NMSUtils.blockPosClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getZ", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public int getBlockId() {
        Object nmsBlock = null;
        if (version.equals(ServerVersion.v_1_7_10)) {
            nmsBlock = readObject(0, NMSUtils.blockClass);
        } else {
            Object iBlockDataObj = readObject(0, NMSUtils.iBlockDataClass);
            try {
                nmsBlock = iBlockDataMethodCache.invoke(iBlockDataObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        try {
            return (int) getBlockIdMethodCache.invoke(nmsBlock);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Object asNMSPacket() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            try {
                return packetConstructor.newInstance(getBlockX(), getBlockY(), getBlockZ(), world);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
