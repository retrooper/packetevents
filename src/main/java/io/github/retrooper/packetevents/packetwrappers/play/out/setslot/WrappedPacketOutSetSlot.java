package io.github.retrooper.packetevents.packetwrappers.play.out.setslot;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutSetSlot extends WrappedPacket implements SendableWrapper {

    private static Constructor<?> packetConstructor;

    public WrappedPacketOutSetSlot(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutSetSlot(int windowId, int slot, ItemStack item) {
        this.windowId = windowId;
        this.slot = slot;
        this.item = item;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.SET_SLOT;
        try {
            packetConstructor = packetClass.getConstructor(int.class, int.class, NMSUtils.nmsItemStackClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private int windowId;
    private int slot;
    private ItemStack item;

    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return windowId;
        }
    }

    public int getSlot() {
        if (packet != null) {
            return readInt(1);
        } else {
            return slot;
        }
    }

    public ItemStack getItem() {
        if (packet != null) {
            Object nmsItemStack = readObject(0, NMSUtils.nmsItemStackClass);
            return NMSUtils.toBukkitItemStack(nmsItemStack);
        } else {
            return item;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getWindowId(), getSlot(), NMSUtils.toNMSItemStack(getItem()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
