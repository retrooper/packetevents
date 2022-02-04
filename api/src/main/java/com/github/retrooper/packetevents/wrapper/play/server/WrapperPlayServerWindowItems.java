package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerWindowItems extends PacketWrapper<WrapperPlayServerWindowItems> {
    private int windowID;
    private int stateID;
    private List<ItemStack> items;
    private Optional<ItemStack> carriedItem;

    public WrapperPlayServerWindowItems(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWindowItems(int windowID, int stateID, List<ItemStack> items, @Nullable ItemStack carriedItem) {
        super(PacketType.Play.Server.WINDOW_ITEMS);
        this.windowID = windowID;
        this.stateID = stateID;
        this.items = items;
        this.carriedItem = Optional.ofNullable(carriedItem);
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

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            carriedItem = Optional.of(readItemStack());
        }
        else {
            carriedItem = Optional.empty();
        }
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
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
            writeItemStack(carriedItem.orElse(ItemStack.AIR));
        }
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

    public Optional<ItemStack> getCarriedItem() {
        return carriedItem;
    }

    public void setCarriedItem(@Nullable ItemStack carriedItem) {
        this.carriedItem = Optional.ofNullable(carriedItem);
    }
}