/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.configuration.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientSettings extends PacketWrapper<WrapperConfigClientSettings> {

    private String locale;
    private int viewDistance;
    private ChatVisibility visibility;
    private boolean chatColorable;
    private byte visibleSkinSectionMask;
    private HumanoidArm hand;
    private boolean textFilteringEnabled;
    private boolean allowServerListings;

    public WrapperConfigClientSettings(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperConfigClientSettings(String locale, int viewDistance, ChatVisibility visibility,
                                       boolean chatColorable, byte visibleSkinSectionMask, HumanoidArm hand,
                                       boolean textFilteringEnabled, boolean allowServerListings) {
        super(PacketType.Configuration.Client.CLIENT_SETTINGS);
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.visibility = visibility;
        this.chatColorable = chatColorable;
        this.visibleSkinSectionMask = visibleSkinSectionMask;
        this.hand = hand;
        this.textFilteringEnabled = textFilteringEnabled;
        this.allowServerListings = allowServerListings;
    }

    @Override
    public void read() {
        this.locale = this.readString(16);
        this.viewDistance = this.readByte();
        this.visibility = ChatVisibility.VALUES[this.readVarInt()];
        this.chatColorable = this.readBoolean();
        this.visibleSkinSectionMask = (byte) this.readUnsignedByte();
        this.hand = HumanoidArm.VALUES[this.readVarInt()];
        this.textFilteringEnabled = this.readBoolean();
        this.allowServerListings = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeString(this.locale, 16);
        this.writeByte(this.viewDistance);
        this.writeVarInt(this.visibility.ordinal());
        this.writeBoolean(this.chatColorable);
        this.writeByte(this.visibleSkinSectionMask);
        this.writeVarInt(this.hand.ordinal());
        this.writeBoolean(this.textFilteringEnabled);
        this.writeBoolean(this.allowServerListings);
    }

    @Override
    public void copy(WrapperConfigClientSettings wrapper) {
        this.locale = wrapper.locale;
        this.viewDistance = wrapper.viewDistance;
        this.visibility = wrapper.visibility;
        this.chatColorable = wrapper.chatColorable;
        this.visibleSkinSectionMask = wrapper.visibleSkinSectionMask;
        this.hand = wrapper.hand;
        this.textFilteringEnabled = wrapper.textFilteringEnabled;
        this.allowServerListings = wrapper.allowServerListings;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public ChatVisibility getVisibility() {
        return this.visibility;
    }

    public void setVisibility(ChatVisibility visibility) {
        this.visibility = visibility;
    }

    public boolean isChatColorable() {
        return this.chatColorable;
    }

    public void setChatColorable(boolean chatColorable) {
        this.chatColorable = chatColorable;
    }

    public byte getVisibleSkinSectionMask() {
        return this.visibleSkinSectionMask;
    }

    public void setVisibleSkinSectionMask(byte visibleSkinSectionMask) {
        this.visibleSkinSectionMask = visibleSkinSectionMask;
    }

    public HumanoidArm getHand() {
        return this.hand;
    }

    public void setHand(HumanoidArm hand) {
        this.hand = hand;
    }

    public boolean isTextFilteringEnabled() {
        return this.textFilteringEnabled;
    }

    public void setTextFilteringEnabled(boolean textFilteringEnabled) {
        this.textFilteringEnabled = textFilteringEnabled;
    }

    public boolean isAllowServerListings() {
        return this.allowServerListings;
    }

    public void setAllowServerListings(boolean allowServerListings) {
        this.allowServerListings = allowServerListings;
    }

    public enum ChatVisibility {

        FULL,
        SYSTEM,
        HIDDEN;

        public static final ChatVisibility[] VALUES = values();
    }
}
