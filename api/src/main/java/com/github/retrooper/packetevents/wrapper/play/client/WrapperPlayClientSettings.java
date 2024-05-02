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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.protocol.player.SkinSection;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSettings extends PacketWrapper<WrapperPlayClientSettings> {
    private String locale;
    private int viewDistance;
    private ChatVisibility visibility;
    private boolean chatColorable;
    private byte visibleSkinSectionMask;
    private HumanoidArm hand;
    private boolean textFilteringEnabled;
    private boolean allowServerListings;

    public enum ChatVisibility {
        FULL, SYSTEM, HIDDEN;

        public static final ChatVisibility[] VALUES = values();
    }

    public WrapperPlayClientSettings(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSettings(String locale, int viewDistance, ChatVisibility visibility,
                                     boolean chatColorable, byte visibleSkinSectionMask, HumanoidArm hand,
                                     boolean textFilteringEnabled, boolean allowServerListings) {
        super(PacketType.Play.Client.CLIENT_SETTINGS);
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
        int localeLength = 16;
        locale = readString(localeLength);
        viewDistance = readByte();
        int visibilityIndex = readVarInt();
        visibility = ChatVisibility.VALUES[visibilityIndex];
        chatColorable = readBoolean();
        visibleSkinSectionMask = (byte) readUnsignedByte();
        hand = HumanoidArm.VALUES[readVarInt()];
        textFilteringEnabled = readBoolean();
        allowServerListings = readBoolean();
    }

    @Override
    public void write() {
        int localeLength = 16;
        writeString(locale, localeLength);
        writeByte(viewDistance);
        writeVarInt(visibility.ordinal());
        writeBoolean(chatColorable);
        writeByte(visibleSkinSectionMask);
        writeVarInt(hand.ordinal());
        writeBoolean(textFilteringEnabled);
        writeBoolean(allowServerListings);
    }

    @Override
    public void copy(WrapperPlayClientSettings wrapper) {
        locale = wrapper.locale;
        viewDistance = wrapper.viewDistance;
        visibility = wrapper.visibility;
        chatColorable = wrapper.chatColorable;
        visibleSkinSectionMask = wrapper.visibleSkinSectionMask;
        hand = wrapper.hand;
        textFilteringEnabled = wrapper.textFilteringEnabled;
        allowServerListings = wrapper.allowServerListings;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public ChatVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(ChatVisibility visibility) {
        this.visibility = visibility;
    }

    public boolean isChatColorable() {
        return chatColorable;
    }

    public void setChatColorable(boolean chatColorable) {
        this.chatColorable = chatColorable;
    }

    public byte getVisibleSkinSectionMask() {
        return visibleSkinSectionMask;
    }

    public void setVisibleSkinSectionMask(byte visibleSkinSectionMask) {
        this.visibleSkinSectionMask = visibleSkinSectionMask;
    }

    public SkinSection getVisibleSkinSection() {
        return new SkinSection(getVisibleSkinSectionMask());
    }

    public void setVisibleSkinSections(SkinSection visibleSkinSection) {
        this.visibleSkinSectionMask = visibleSkinSection.getMask();
    }

    public boolean isSkinSectionVisible(SkinSection section) {
        return section.isSet(visibleSkinSectionMask);
    }

    public void setSkinSectionVisible(SkinSection section, boolean visible) {
        visibleSkinSectionMask = section.set(visibleSkinSectionMask, visible);
    }

    public HumanoidArm getMainHand() {
        return hand;
    }

    public void setMainHand(HumanoidArm hand) {
        this.hand = hand;
    }

    public boolean isTextFilteringEnabled() {
        return textFilteringEnabled;
    }

    public void setTextFilteringEnabled(boolean textFilteringEnabled) {
        this.textFilteringEnabled = textFilteringEnabled;
    }

    public boolean isServerListingAllowed() {
        return allowServerListings;
    }

    public void setServerListingAllowed(boolean allowServerListings) {
        this.allowServerListings = allowServerListings;
    }
}
