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
import com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonClientSettings;
import net.kyori.adventure.util.Index;

public class WrapperConfigClientSettings extends WrapperCommonClientSettings<WrapperConfigClientSettings> {

    public WrapperConfigClientSettings(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperConfigClientSettings(
            String locale, int viewDistance, WrapperCommonClientSettings.ChatVisibility chatVisibility,
            boolean chatColors, byte skinMask, HumanoidArm mainHand, boolean textFilteringEnabled,
            boolean allowServerListings, ParticleStatus particleStatus
    ) {
        super(PacketType.Configuration.Client.CLIENT_SETTINGS, locale, viewDistance,
                chatVisibility, chatColors, skinMask, mainHand, textFilteringEnabled,
                allowServerListings, particleStatus, (byte) 0);
    }

    @Deprecated
    public WrapperConfigClientSettings(
            String locale, int viewDistance, ChatVisibility visibility,
            boolean chatColorable, byte visibleSkinSectionMask, HumanoidArm hand,
            boolean textFilteringEnabled, boolean allowServerListings
    ) {
        this(locale, viewDistance, visibility.modern, chatColorable,
                visibleSkinSectionMask, hand, textFilteringEnabled,
                allowServerListings, ParticleStatus.ALL);
    }

    @Deprecated
    public ChatVisibility getVisibility() {
        return ChatVisibility.MODERN_INDEX.valueOrThrow(this.getChatVisibility());
    }

    @Deprecated
    public void setVisibility(ChatVisibility visibility) {
        this.setChatVisibility(visibility.modern);
    }

    @Deprecated
    public boolean isChatColorable() {
        return this.isChatColors();
    }

    @Deprecated
    public void setChatColorable(boolean chatColorable) {
        this.setChatColors(chatColorable);
    }

    @Deprecated
    public byte getVisibleSkinSectionMask() {
        return this.getSkinMask();
    }

    @Deprecated
    public void setVisibleSkinSectionMask(byte visibleSkinSectionMask) {
        this.setSkinMask(visibleSkinSectionMask);
    }

    @Deprecated
    public HumanoidArm getHand() {
        return this.getMainHand();
    }

    @Deprecated
    public void setHand(HumanoidArm hand) {
        this.setMainHand(hand);
    }

    @Deprecated
    public boolean isAllowServerListings() {
        return this.isServerListingAllowed();
    }

    @Deprecated
    public void setAllowServerListings(boolean allowServerListings) {
        this.setServerListingAllowed(allowServerListings);
    }

    @Deprecated
    public enum ChatVisibility {

        FULL(WrapperCommonClientSettings.ChatVisibility.FULL),
        SYSTEM(WrapperCommonClientSettings.ChatVisibility.SYSTEM),
        HIDDEN(WrapperCommonClientSettings.ChatVisibility.HIDDEN);

        public static final ChatVisibility[] VALUES = values();
        private static final Index<WrapperCommonClientSettings.ChatVisibility, ChatVisibility> MODERN_INDEX =
                Index.create(ChatVisibility.class, visibility -> visibility.modern);

        private final WrapperCommonClientSettings.ChatVisibility modern;

        ChatVisibility(WrapperCommonClientSettings.ChatVisibility modern) {
            this.modern = modern;
        }
    }
}
