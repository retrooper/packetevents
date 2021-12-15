package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerWindowItems extends PacketWrapper<WrapperPlayServerWindowItems> {
    private int windowID;
    private int stateID;
    private List<ItemStack> items;
    private ItemStack carriedItem;

    public WrapperPlayServerWindowItems(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWindowItems(int windowID, int stateID, List<ItemStack> items, ItemStack carriedItem) {
        super(PacketType.Play.Server.WINDOW_ITEMS);
        this.windowID = windowID;
        this.stateID = stateID;
        this.items = items;
        this.carriedItem = carriedItem;
    }

    @Override
    public void readData() {
        windowID = readByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            stateID = readVarInt();
        }

        int count = readVarInt();
        items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            items.add(readItemStack());
        }

        carriedItem = readItemStack();
    }

    @Override
    public void readData(WrapperPlayServerWindowItems wrapper) {
        windowID = wrapper.windowID;
        stateID = wrapper.stateID;
        items = wrapper.items;
        carriedItem = wrapper.carriedItem;
    }

    @Override
    public void writeData() {
        writeByte(windowID);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            writeVarInt(stateID);
        }

        writeVarInt(items.size());
        for (ItemStack item : items) {
            writeItemStack(item);
        }

        writeItemStack(carriedItem);
    }

    public int getWindowId() {
        return windowID;
    }

    public void setWindowId(int windowID) {
        this.windowID = windowID;
    }

    public int getStateId() {
        return stateID;
    }

    public void setStateId(int stateID) {
        this.stateID = stateID;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public ItemStack getCarriedItem() {
        return carriedItem;
    }

    public void setCarriedItem(ItemStack carriedItem) {
        this.carriedItem = carriedItem;
    }
}