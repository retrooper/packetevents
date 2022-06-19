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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
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

    //Not accessible, only for 1.7
    private byte ignoredDifficulty;

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
        int localeLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7;
        locale = readString(localeLength);
        viewDistance = readByte();
        int visibilityIndex = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) ? readVarInt() : readByte();
        visibility = ChatVisibility.VALUES[visibilityIndex];
        chatColorable = readBoolean();
        if (serverVersion == ServerVersion.V_1_7_10) {
            //Ignored
            ignoredDifficulty = readByte();
            //We use this for the skin sections
            boolean showCape = readBoolean();
            if (showCape) {
                visibleSkinSectionMask = SkinSection.CAPE.getMask();
            }
        }
        else {
            visibleSkinSectionMask = readByte();
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            hand = HumanoidArm.VALUES[readVarInt()];
        }
        else {
            hand = HumanoidArm.RIGHT;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            textFilteringEnabled = readBoolean();
        }
        else {
            textFilteringEnabled = false;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            allowServerListings = readBoolean();
        }
        else {
            allowServerListings = true;
        }
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
        ignoredDifficulty = wrapper.ignoredDifficulty;
    }

    @Override
    public void write() {
        int localeLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7;
        writeString(locale, localeLength);
        writeByte(viewDistance);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(visibility.ordinal());
        }
        else {
            writeByte(visibility.ordinal());
        }
        writeBoolean(chatColorable);
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeByte(ignoredDifficulty);
            //Show cape
            boolean showCape = SkinSection.CAPE.isSet(visibleSkinSectionMask);
            writeBoolean(showCape);
        }
        else {
            writeByte(visibleSkinSectionMask);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(hand.ordinal());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeBoolean(textFilteringEnabled);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            writeBoolean(allowServerListings);
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
