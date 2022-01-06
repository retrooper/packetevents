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

package io.github.retrooper.packetevents.packetwrappers.play.in.settings;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class WrappedPacketInSettings extends WrappedPacket {
    private static Class<? extends Enum<?>> chatVisibilityEnumClass;

    private static boolean isLowerThan_v_1_8, v_1_17, v_1_18;

    public WrappedPacketInSettings(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isLowerThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        v_1_18 = version.isNewerThanOrEquals(ServerVersion.v_1_18);
        chatVisibilityEnumClass = NMSUtils.getNMSEnumClassWithoutException("EnumChatVisibility");
        if (chatVisibilityEnumClass == null) {
            //They are on 1.17+
            chatVisibilityEnumClass = NMSUtils.getNMEnumClassWithoutException("world.entity.player.EnumChatVisibility");
            if (chatVisibilityEnumClass == null) {
                //They are just on an outdated version
                chatVisibilityEnumClass = SubclassUtil.getEnumSubClass(NMSUtils.entityHumanClass, "EnumChatVisibility");
            }
        }
    }

    public String getLocale() {
        return readString(0);
    }

    public void setLocale(String locale) {
        writeString(0, locale);
    }

    public int getViewDistance() {
        return readInt((v_1_17 && !v_1_18) ? 1 : 0);
    }

    public void setViewDistance(int viewDistance) {
        writeInt((v_1_17 && !v_1_18) ? 1 : 0, viewDistance);
    }

    public ChatVisibility getChatVisibility() {
        Enum<?> enumConst = readEnumConstant(0, chatVisibilityEnumClass);
        return ChatVisibility.values()[enumConst.ordinal()];
    }

    public void setChatVisibility(ChatVisibility visibility) {
        Enum<?> enumConst = EnumUtil.valueByIndex(chatVisibilityEnumClass, visibility.ordinal());
        writeEnumConstant(0, enumConst);
    }

    public boolean isChatColored() {
        return readBoolean(0);
    }

    public void setChatColored(boolean chatColors) {
        writeBoolean(0, chatColors);
    }

    //Added in 1.17
    public boolean isTextFilteringEnabled() {
        if (v_1_17) {
            return readBoolean(1);
        }
        return false;
    }

    //Added in 1.17
    public void setTextFilteringEnabled(boolean enabled) {
        if (v_1_17) {
            writeBoolean(1, enabled);
        }
    }

    //Added in 1.18
    public boolean isServerListingsAllowed() {
        if (v_1_18) {
            return readBoolean(2);
        }
        return true;
    }

    //Added in 1.18
    public void setServerListingsAllowed(boolean allowed) {
        if (v_1_18) {
            writeBoolean(2, allowed);
        }
    }


    public byte getDisplaySkinPartsMask() {
        byte mask = 0;
        if (isLowerThan_v_1_8) {
            boolean capeEnabled = readBoolean(1);
            if (capeEnabled) {
                mask |= 0x01;
            }
        } else {
            mask = (byte) readInt((v_1_17 && !v_1_18) ? 2 : 1);
        }
        return mask;
    }

    public void setDisplaySkinPartsMask(byte mask) {
        if (isLowerThan_v_1_8) {
            boolean capeEnabled = (mask & 0x01) == 0x01;
            writeBoolean(1, capeEnabled);
        } else {
            writeInt((v_1_17 && !v_1_18) ? 2 : 1, mask);
        }
    }

    public Set<DisplayedSkinPart> getDisplayedSkinParts() {
        Set<DisplayedSkinPart> displayedSkinParts = new HashSet<>();
        byte mask = getDisplaySkinPartsMask();
        for (DisplayedSkinPart part : DisplayedSkinPart.values()) {
            if ((mask & part.maskFlag) == part.maskFlag) {
                displayedSkinParts.add(part);
            }
        }
        return displayedSkinParts;
    }

    public void setDisplayedSkinParts(Set<DisplayedSkinPart> displayedSkinParts) {
        byte mask = 0;
        for (DisplayedSkinPart part : displayedSkinParts) {
            mask |= part.maskFlag;
        }
        setDisplaySkinPartsMask(mask);
    }

    public enum ChatVisibility {
        FULL, SYSTEM, HIDDEN
    }

    public enum DisplayedSkinPart {
        CAPE(0x01),

        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        JACKET(0x02),
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        LEFT_SLEEVE(0x04),
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        RIGHT_SLEEVE(0x08),
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        LEFT_PANTS(0x10),
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        RIGHT_PANTS(0x20),
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        HAT(0x40);

        final byte maskFlag;

        DisplayedSkinPart(int maskFlag) {
            this.maskFlag = (byte) maskFlag;
        }
    }
}
