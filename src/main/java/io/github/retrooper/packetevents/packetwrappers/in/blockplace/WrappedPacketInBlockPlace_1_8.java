package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

final class WrappedPacketInBlockPlace_1_8 extends WrappedPacket {
    private static Class<?> blockPlaceClass, blockPositionClass;
    private Vector3i blockPosition;
    private ItemStack itemStack;

    WrappedPacketInBlockPlace_1_8(final Object packet) {
        super(packet);
    }

    protected static void initStatic() {
        try {
            blockPlaceClass = NMSUtils.getNMSClass("PacketPlayInBlockPlace");

            blockPositionClass = NMSUtils.getNMSClass("BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setup() {
        try {
            Object nmsBlockPos = Reflection.getField(blockPlaceClass, blockPositionClass, 1).get(packet);

            final int[] coords = new int[3];
            for (int i = 0; i < 3; i++) {
                final String s = "get" + (i == 0 ? "X" : i == 1 ? "Y" : "Z");
                coords[i] = (int) Reflection.getMethod(blockPositionClass.getSuperclass(), s, 0).invoke(nmsBlockPos);
            }

            this.blockPosition = new Vector3i(coords[0], coords[1], coords[2]);

            this.itemStack = NMSUtils.toBukkitItemStack(Reflection.getField(blockPlaceClass, NMSUtils.nmsItemStackClass, 0).get(packet));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
