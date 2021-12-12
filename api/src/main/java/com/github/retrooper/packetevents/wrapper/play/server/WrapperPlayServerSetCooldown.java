package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetCooldown extends PacketWrapper<WrapperPlayServerSetCooldown> {
    ItemType item;
    int cooldownTicks;

    public WrapperPlayServerSetCooldown(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetCooldown(ItemType item, int cooldownTicks) {
        super(PacketType.Play.Server.SET_COOLDOWN);
        this.item = item;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void readData() {
        item = ItemTypes.getById(readVarInt());
        cooldownTicks = readVarInt();
    }

    @Override
    public void readData(WrapperPlayServerSetCooldown wrapper) {
        item = wrapper.item;
        cooldownTicks = wrapper.cooldownTicks;
    }

    @Override
    public void writeData() {
        writeVarInt(item.getId());
        writeVarInt(cooldownTicks);
    }

    public ItemType getItem() {
        return item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }
}
