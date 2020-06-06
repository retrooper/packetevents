package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.enums.Hand;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.BlockFinder;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private Block block;
    private Hand hand;

    public WrappedPacketInBlockPlace_1_9(final Player player, final Object packet) {
        super(player, packet);
    }

    @Override
    protected void setup() {
        try {
            this.block = BlockFinder.getBlocksInDirection(getPlayer(), 10);

            final Object enumHandObj = enumHandField.get(packet);

            this.hand = Hand.valueOf(enumHandObj.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Hand getHand() {
        return hand;
    }

    public Block getBlock() {
        return block;
    }

    private static Class<?> blockPlaceClass_1_9;

    private static Field enumHandField;


    protected static void setupStaticVars() {
        try {
            blockPlaceClass_1_9 = NMSUtils.getNMSClass("PacketPlayInBlockPlace");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            enumHandField = blockPlaceClass_1_9.getDeclaredField("a");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (!enumHandField.isAccessible()) {
            enumHandField.setAccessible(true);
        }
    }


}
