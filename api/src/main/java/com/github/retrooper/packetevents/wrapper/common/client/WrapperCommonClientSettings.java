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

package com.github.retrooper.packetevents.wrapper.common.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.protocol.player.SkinSection;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

public class WrapperCommonClientSettings<T extends WrapperCommonClientSettings<T>> extends PacketWrapper<T> {

    private String locale;
    private int viewDistance;
    private ChatVisibility chatVisibility;
    private boolean chatColors;
    private byte skinMask;
    private HumanoidArm mainHand;
    private boolean textFilteringEnabled;
    private boolean allowServerListings;
    /**
     * Added with 1.21.2
     */
    private ParticleStatus particleStatus;
    /**
     * Removed with 1.8
     */
    @ApiStatus.Obsolete
    private byte ignoredDifficulty;

    public WrapperCommonClientSettings(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperCommonClientSettings(
            PacketTypeCommon packetType,
            String locale, int viewDistance, ChatVisibility chatVisibility,
            boolean chatColors, byte skinMask, HumanoidArm mainHand,
            boolean textFilteringEnabled, boolean allowServerListings,
            ParticleStatus particleStatus, byte ignoredDifficulty
    ) {
        super(packetType);
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatVisibility = chatVisibility;
        this.chatColors = chatColors;
        this.skinMask = skinMask;
        this.mainHand = mainHand;
        this.textFilteringEnabled = textFilteringEnabled;
        this.allowServerListings = allowServerListings;
        this.particleStatus = particleStatus;
        this.ignoredDifficulty = ignoredDifficulty;
    }

    @Override
    public void read() {
        this.locale = this.readString(this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7);
        this.viewDistance = this.readByte();
        this.chatVisibility = this.readEnum(ChatVisibility.values());
        this.chatColors = this.readBoolean();
        if (this.serverVersion == ServerVersion.V_1_7_10) {
            this.ignoredDifficulty = this.readByte();
            if (this.readBoolean()) { // show cape
                this.skinMask = SkinSection.CAPE.getMask();
            }
        } else {
            this.skinMask = (byte) this.readUnsignedByte();
        }
        this.mainHand = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                ? this.readEnum(HumanoidArm.values()) : HumanoidArm.RIGHT;
        this.textFilteringEnabled = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) && this.readBoolean();
        this.allowServerListings = this.serverVersion.isOlderThan(ServerVersion.V_1_18) || this.readBoolean();
        this.particleStatus = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? this.readEnum(ParticleStatus.values()) : ParticleStatus.ALL;
    }

    @Override
    public void write() {
        this.writeString(this.locale, this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12) ? 16 : 7);
        this.writeByte(this.viewDistance);
        this.writeEnum(this.chatVisibility);
        this.writeBoolean(this.chatColors);
        if (this.serverVersion == ServerVersion.V_1_7_10) {
            this.writeByte(this.ignoredDifficulty);
            this.writeBoolean(SkinSection.CAPE.isSet(this.skinMask));
        } else {
            this.writeByte(this.skinMask);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.writeEnum(this.mainHand);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.writeBoolean(this.textFilteringEnabled);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.writeBoolean(this.allowServerListings);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeEnum(this.particleStatus);
        }
    }

    @Override
    public void copy(T wrapper) {
        this.locale = wrapper.getLocale();
        this.viewDistance = wrapper.getViewDistance();
        this.chatVisibility = wrapper.getChatVisibility();
        this.chatColors = wrapper.isChatColors();
        this.skinMask = wrapper.getSkinMask();
        this.mainHand = wrapper.getMainHand();
        this.textFilteringEnabled = wrapper.isTextFilteringEnabled();
        this.allowServerListings = wrapper.isServerListingAllowed();
        this.particleStatus = wrapper.getParticleStatus();
        this.ignoredDifficulty = wrapper.getIgnoredDifficulty();
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

    public ChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public void setChatVisibility(ChatVisibility chatVisibility) {
        this.chatVisibility = chatVisibility;
    }

    public boolean isChatColors() {
        return this.chatColors;
    }

    public void setChatColors(boolean chatColors) {
        this.chatColors = chatColors;
    }

    public byte getSkinMask() {
        return this.skinMask;
    }

    public void setSkinMask(byte skinMask) {
        this.skinMask = skinMask;
    }

    public SkinSection getVisibleSkinSection() {
        return new SkinSection(this.skinMask);
    }

    public void setVisibleSkinSections(SkinSection visibleSkinSection) {
        this.skinMask = visibleSkinSection.getMask();
    }

    public boolean isSkinSectionVisible(SkinSection section) {
        return section.isSet(this.skinMask);
    }

    public void setSkinSectionVisible(SkinSection section, boolean visible) {
        this.skinMask = section.set(this.skinMask, visible);
    }

    public HumanoidArm getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(HumanoidArm mainHand) {
        this.mainHand = mainHand;
    }

    public boolean isTextFilteringEnabled() {
        return this.textFilteringEnabled;
    }

    public void setTextFilteringEnabled(boolean textFilteringEnabled) {
        this.textFilteringEnabled = textFilteringEnabled;
    }

    public boolean isServerListingAllowed() {
        return this.allowServerListings;
    }

    public void setServerListingAllowed(boolean allowServerListings) {
        this.allowServerListings = allowServerListings;
    }

    public ParticleStatus getParticleStatus() {
        return this.particleStatus;
    }

    public void setParticleStatus(ParticleStatus particleStatus) {
        this.particleStatus = particleStatus;
    }

    /**
     * Removed with 1.8
     */
    @ApiStatus.Obsolete
    public byte getIgnoredDifficulty() {
        return this.ignoredDifficulty;
    }

    /**
     * Removed with 1.8
     */
    @ApiStatus.Obsolete
    public void setIgnoredDifficulty(byte ignoredDifficulty) {
        this.ignoredDifficulty = ignoredDifficulty;
    }

    public enum ChatVisibility {
        FULL,
        SYSTEM,
        HIDDEN,
    }

    public enum ParticleStatus {
        ALL,
        DECREASED,
        MINIMAL,
    }
}
