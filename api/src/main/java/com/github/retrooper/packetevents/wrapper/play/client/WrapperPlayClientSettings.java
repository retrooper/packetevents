/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.protocol.player.Hand;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

import java.util.HashSet;
import java.util.Set;

public class WrapperPlayClientSettings extends PacketWrapper<WrapperPlayClientSettings> {
    private String locale;
    private int viewDistance;
    private ChatVisibility visibility;
    private boolean chatColors;
    private byte visibleSkinSectionMask;
    private Hand hand;
    private boolean disableTextFiltering;

    //Not accessible, only for 1.7
    private byte ignoredDifficulty;

    public enum ChatVisibility {
        FULL, SYSTEM, HIDDEN;

        public static final ChatVisibility[] VALUES = values();
    }

    public enum SkinSection {
        CAPE(0x01),

        JACKET(0x02),

        LEFT_SLEEVE(0x04),

        RIGHT_SLEEVE(0x08),

        LEFT_PANTS(0x10),

        RIGHT_PANTS(0x20),

        HAT(0x40);

        public static final SkinSection[] VALUES = values();

        final byte maskFlag;

        SkinSection(int maskFlag) {
            this.maskFlag = (byte) maskFlag;
        }

        public static boolean isSectionPresent(byte mask, SkinSection section) {
            return (mask & section.maskFlag) != 0;
        }
    }

    public WrapperPlayClientSettings(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSettings(String locale, int viewDistance, ChatVisibility visibility,
                                     boolean chatColors, byte visibleSkinSectionMask, Hand hand,
                                     boolean disableTextFiltering) {
        super(PacketType.Play.Client.CLIENT_SETTINGS);
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.visibility = visibility;
        this.chatColors = chatColors;
        this.visibleSkinSectionMask = visibleSkinSectionMask;
        this.hand = hand;
        this.disableTextFiltering = disableTextFiltering;
    }

    @Override
    public void readData() {
        int localeLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7;
        locale = readString(localeLength);
        viewDistance = readByte();
        int visibilityIndex = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) ? readVarInt() : readByte();
        visibility = ChatVisibility.VALUES[visibilityIndex];
        chatColors = readBoolean();
        if (serverVersion == ServerVersion.V_1_7_10) {
            //Ignored
            ignoredDifficulty = readByte();
            //We use this for the skin sections
            boolean showCape = readBoolean();
            if (showCape) {
                visibleSkinSectionMask = SkinSection.CAPE.maskFlag;
            }
        }
        else {
            visibleSkinSectionMask = readByte();
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            hand = Hand.VALUES[readVarInt()];
        }
        else {
            hand = Hand.RIGHT;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            disableTextFiltering = readBoolean();
        }
        else {
            disableTextFiltering = true;
        }
    }

    @Override
    public void readData(WrapperPlayClientSettings wrapper) {
        locale = wrapper.locale;
        viewDistance = wrapper.viewDistance;
        visibility = wrapper.visibility;
        chatColors = wrapper.chatColors;
        visibleSkinSectionMask = wrapper.visibleSkinSectionMask;
        hand = wrapper.hand;
        disableTextFiltering = wrapper.disableTextFiltering;
    }

    @Override
    public void writeData() {
        int localeLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7;
        writeString(locale, localeLength);
        writeByte(viewDistance);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(visibility.ordinal());
        }
        else {
            writeByte(visibility.ordinal());
        }
        writeBoolean(chatColors);
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeByte(ignoredDifficulty);
            //Show cape
            boolean showCape = SkinSection.isSectionPresent(visibleSkinSectionMask, SkinSection.CAPE);
            writeBoolean(showCape);
        }
        else {
            writeByte(visibleSkinSectionMask);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(hand.ordinal());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeBoolean(disableTextFiltering);
        }
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

    //TODO Rethink name
    public boolean isChatColored() {
        return chatColors;
    }

    //TODO Rethink name
    public void setChatColored(boolean chatColors) {
        this.chatColors = chatColors;
    }

    public byte getVisibleSkinSectionMask() {
        return visibleSkinSectionMask;
    }

    public void setVisibleSkinSectionMask(byte visibleSkinSectionMask) {
        this.visibleSkinSectionMask = visibleSkinSectionMask;
    }

    public Set<SkinSection> getVisibleSkinSections() {
        byte mask = getVisibleSkinSectionMask();
        Set<SkinSection> visibleSkinSections = new HashSet<>();
        for (SkinSection skinSection : SkinSection.VALUES) {
            if (SkinSection.isSectionPresent(mask, skinSection)) {
                visibleSkinSections.add(skinSection);
            }
        }
        return visibleSkinSections;
    }

    public void setVisibleSkinSections(Set<SkinSection> visibleSkinSections) {
        byte mask = 0x00;
        for (SkinSection skinSection : visibleSkinSections) {
            mask |= skinSection.maskFlag;
        }
        setVisibleSkinSectionMask(mask);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public boolean isTextFilteringDisabled() {
        return disableTextFiltering;
    }

    public void setTextFilteringDisabled(boolean disableTextFiltering) {
        this.disableTextFiltering = disableTextFiltering;
    }
}
