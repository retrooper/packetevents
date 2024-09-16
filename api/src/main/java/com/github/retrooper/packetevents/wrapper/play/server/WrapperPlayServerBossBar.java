/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.wrapper.play.server;

import java.util.UUID;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerBossBar extends PacketWrapper<WrapperPlayServerBossBar> {

    private UUID uuid;
    private Action action;
    private Component title;
    private float health;
    private BossBar.Color color;
    private BossBar.Overlay division;
    private short flags;

    public WrapperPlayServerBossBar(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBossBar(UUID uuid, Action action) {
        super(PacketType.Play.Server.BOSS_BAR);
        this.uuid = uuid;
        this.action = action;
    }
    
    @Override
    public void read() {
        uuid = readUUID();
        action = readEnum(Action.class);
        switch (action) {
        case ADD:
            title = readComponent();
            health = readFloat();
            color = readEnum(BossBar.Color.class);
            division = readEnum(BossBar.Overlay.class);
            flags = readUnsignedByte();
            break;
        case REMOVE: // do nothing
            break;
        case UPDATE_HEALTH:
            health = readFloat();
            break;
        case UPDATE_TITLE:
            title = readComponent();
            break;
        case UPDATE_STYLE:
            color = readEnum(BossBar.Color.class);
            division = readEnum(BossBar.Overlay.class);
            break;
        case UPDATE_FLAGS:
            flags = readUnsignedByte();
            break;
        }
    }
    
    @Override
    public void write() {
        writeUUID(uuid);
        writeEnum(action);
        switch (action) {
        case ADD:
            writeComponent(title);
            writeFloat(health);
            writeEnum(color);
            writeEnum(division);
            writeByte(flags);
            break;
        case REMOVE: // do nothing
            break;
        case UPDATE_HEALTH:
            writeFloat(health);
            break;
        case UPDATE_TITLE:
            writeComponent(title);
            break;
        case UPDATE_STYLE:
            writeEnum(color);
            writeEnum(division);
            break;
        case UPDATE_FLAGS:
            writeByte(flags);
            break;
        }
    }
    
    @Override
    public void copy(WrapperPlayServerBossBar wrapper) {
        uuid = wrapper.uuid;
        action = wrapper.action;
        title = wrapper.title;
        health = wrapper.health;
        color = wrapper.color;
        division = wrapper.division;
        flags = wrapper.flags;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Action getAction() {
        return action;
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
    
    public Component getTitle() {
        return title;
    }
    
    public void setTitle(Component title) {
        this.title = title;
    }
    
    public float getHealth() {
        return health;
    }
    
    public void setHealth(float health) {
        this.health = health;
    }
    
    public BossBar.Color getColor() {
        return color;
    }
    
    public void setColor(BossBar.Color color) {
        this.color = color;
    }
    
    public BossBar.Overlay getDivision() {
        return division;
    }
    
    public void setDivision(BossBar.Overlay division) {
        this.division = division;
    }
    
    public short getFlags() {
        return flags;
    }
    
    public void setFlags(short flags) {
        this.flags = flags;
    }
    
    public enum Action {
        ADD, REMOVE, UPDATE_HEALTH, UPDATE_TITLE, UPDATE_STYLE, UPDATE_FLAGS;
    }
}
