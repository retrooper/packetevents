/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.in.settings;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.HashSet;
import java.util.Set;

public class WrappedPacketInSettings extends WrappedPacket {
    private static Class<? extends Enum<?>> chatVisibilityEnumClass;

    private static boolean isLowerThan_v_1_8;
    private Object chatVisibilityEnumObj;

    public WrappedPacketInSettings(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isLowerThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);

        try {
            chatVisibilityEnumClass = NMSUtils.getNMSEnumClass("EnumChatVisibility");
        } catch (ClassNotFoundException e) {
            Class<?> entityHumanClass = NMSUtils.getNMSClassWithoutException("EntityHuman");
            //They are just on an outdated version
            chatVisibilityEnumClass = SubclassUtil.getEnumSubClass(entityHumanClass, "EnumChatVisibility");
        }
    }

    public String getLocale() {
        return readString(0);
    }

    public void setLocale(String locale) {
        writeString(0, locale);
    }

    public int getViewDistance() {
        return readInt(0);
    }

    public void setViewDistance(int viewDistance) {
        writeInt(0, viewDistance);
    }

    public ChatVisibility getChatVisibility() {
        Enum<?> enumConst = readEnumConstant(0, chatVisibilityEnumClass);
        return ChatVisibility.valueOf(enumConst.name());
    }

    public void setChatVisibility(ChatVisibility visibility) {
        Enum<?> enumConst = EnumUtil.valueByIndex(chatVisibilityEnumClass, visibility.ordinal()); //Ordinal is faster than name comparison.
        writeEnumConstant(0, enumConst);
    }

    public boolean isChatColored() {
        return readBoolean(0);
    }

    public void setChatColored(boolean chatColors) {
        writeBoolean(0, chatColors);
    }

    public byte getDisplaySkinPartsMask() {
        byte mask = 0;
        if (isLowerThan_v_1_8) {
            boolean capeEnabled = readBoolean(1);
            if (capeEnabled) {
                mask |= 0x01;
            }
        } else {
            mask = (byte) readInt(1);
        }
        return mask;
    }

    public void setDisplaySkinPartsMask(byte mask) {
        if (isLowerThan_v_1_8) {
            boolean capeEnabled = (mask & 0x01) == 0x01;
            writeBoolean(1, capeEnabled);
        } else {
            writeInt(1, mask);
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
