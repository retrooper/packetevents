package io.github.retrooper.packetevents.packetwrappers.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;

class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private Block block;

    public WrappedPacketInBlockPlace_1_9(final Player player, final Object packet) {
        super(player, packet);
    }

    @Override
    protected void setup() {
        this.block = getPlayer().getTargetBlock((HashSet<Material>)null, 3);

    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlock() {
        return block;
    }

    private static Class<?> blockPlaceClass_1_9;
    protected static void initStatic() {
        try {
            blockPlaceClass_1_9 = NMSUtils.getNMSClass("PacketPlayInUseItem");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
