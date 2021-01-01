package io.github.retrooper.packetevents.packetwrappers.play.out.blockchange;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedPacketOutBlockChange extends WrappedPacket {
    private static Method iBlockDataMethodCache = null;
    private static Method getBlockIdMethodCache = null;
    public WrappedPacketOutBlockChange(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        iBlockDataMethodCache = Reflection.getMethod(NMSUtils.iBlockDataClass, NMSUtils.blockClass, 0);
        getBlockIdMethodCache = Reflection.getMethod(NMSUtils.blockClass, "getId", 0);
    }

    public int getBlockX() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(0);
        } else {
            return 0;
        }
    }

    public int getBlockY() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(1);
        } else {
            return 0;
        }
    }

    public int getBlockZ() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            return readInt(2);
        } else {
            return 0;
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
}
