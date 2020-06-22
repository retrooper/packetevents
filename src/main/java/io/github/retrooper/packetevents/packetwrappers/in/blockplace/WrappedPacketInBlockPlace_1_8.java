package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

final class WrappedPacketInBlockPlace_1_8 extends WrappedPacket {
    private static final Reflection.MethodInvoker[] blockPositionXYZInvoker = new Reflection.MethodInvoker[3];
    private static Class<?> blockPlaceClass, blockPositionClass;
    private static Reflection.FieldAccessor<?> blockPositionAccessor, itemStackFieldAccessor;
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

        itemStackFieldAccessor = Reflection.getField(blockPlaceClass, NMSUtils.nmsItemStackClass, 0);

        blockPositionAccessor = Reflection.getField(blockPlaceClass, blockPositionClass, 1);

        blockPositionXYZInvoker[0] = Reflection.getMethod(blockPositionClass.getSuperclass(), "getX");
        blockPositionXYZInvoker[1] = Reflection.getMethod(blockPositionClass.getSuperclass(), "getY");
        blockPositionXYZInvoker[2] = Reflection.getMethod(blockPositionClass.getSuperclass(), "getZ");
    }

    @Override
    protected void setup() {
        Object nmsBlockPos = blockPositionAccessor.get(packet);

        int x = (int) blockPositionXYZInvoker[0].invoke(nmsBlockPos);
        int y = (int) blockPositionXYZInvoker[1].invoke(nmsBlockPos);
        int z = (int) blockPositionXYZInvoker[2].invoke(nmsBlockPos);

        this.blockPosition = new Vector3i(x, y, z);

        this.itemStack = NMSUtils.toBukkitItemStack(itemStackFieldAccessor.get(packet));
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
