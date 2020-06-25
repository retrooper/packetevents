package io.github.retrooper.packetevents.packetwrappers.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

final class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private Block block;

    private static Method getTargetBlockMethod;

    public WrappedPacketInBlockPlace_1_9(final Player player, final Object packet) {
        super(player, packet);
    }

    protected static void initStatic() {
        try {
            getTargetBlockMethod = Player.class.getMethod("getTargetBlock", HashSet.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setup() {
        try {
            this.block = (Block) getTargetBlockMethod.invoke(getPlayer(), null, 3);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlock() {
        return block;
    }


}
