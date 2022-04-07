package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
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
    public void read() {
        item = ItemTypes.getById(serverVersion.toClientVersion(), readVarInt());
        cooldownTicks = readVarInt();
    }

    @Override
    public void copy(WrapperPlayServerSetCooldown wrapper) {
        item = wrapper.item;
        cooldownTicks = wrapper.cooldownTicks;
    }

    @Override
    public void write() {
        writeVarInt(item.getId(serverVersion.toClientVersion()));
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
