package io.github.retrooper.packetevents.packetwrappers.play.in.setcreativeslot;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.inventory.ItemStack;

public class WrappedPacketInSetCreativeSlot extends WrappedPacket {

    public WrappedPacketInSetCreativeSlot(final NMSPacket packet) {
        super(packet);
    }

    /**
     * Get the clicked slot
     *
     * @return the slot
     */
    public int getSlot() {
        return readInt(0);
    }

    /**
     * Set the clicked slot
     *
     * @param value the slot
     */
    public void setSlot(int value) {
        writeInt(0, value);
    }

    /**
     * Get the clicked item
     *
     * @return the clicked item
     */
    public ItemStack getClickedItem() {
        Object nmsItemStack = readObject(0, NMSUtils.nmsItemStackClass);
        return NMSUtils.toBukkitItemStack(nmsItemStack);
    }
}