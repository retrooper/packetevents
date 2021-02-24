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

import java.util.HashMap;

public class WrappedPacketInSettings extends WrappedPacket {
    private static Class<? extends Enum<?>> chatVisibilityEnumClass;

    private static boolean isLowerThan_v_1_8;
    private Object chatVisibilityEnumObj;
    private HashMap<DisplayedSkinPart, Boolean> displayedSkinParts = new HashMap<>();

    public WrappedPacketInSettings(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isLowerThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);

        try {
            chatVisibilityEnumClass = (Class<? extends Enum<?>>) NMSUtils.getNMSClass("EnumChatVisibility");
        } catch (ClassNotFoundException e) {
            Class<?> entityHumanClass = NMSUtils.getNMSClassWithoutException("EntityHuman");
            //They are just on an outdated version
            chatVisibilityEnumClass = (Class<? extends Enum<?>>) SubclassUtil.getSubClass(entityHumanClass, "EnumChatVisibility");
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

    /**
     * Get Chat Visibility
     *
     * @return Chat Visibility
     */
    public ChatVisibility getChatVisibility() {
          Enum<?> enumConst = (Enum<?>) readObject(0, chatVisibilityEnumClass);
         return ChatVisibility.valueOf(enumConst.name());
    }

    public void setChatVisibility(ChatVisibility visibility) {
        Enum<?> enumConst = EnumUtil.valueByIndex(chatVisibilityEnumClass, visibility.ordinal()); //Ordinal is faster than name comparison.
        write(chatVisibilityEnumClass, 0, enumConst);
    }

    public boolean isChatColored() {
        return readBoolean(0);
    }

    public void setIsChatColored(boolean chatColors) {
        writeBoolean(0, chatColors);
    }


    //TODO make setter for this :c
    public HashMap<DisplayedSkinPart, Boolean> getDisplayedSkinPartsMap() {
        if (displayedSkinParts.isEmpty()) {
            if (isLowerThan_v_1_8) {
                //in 1.7.10 only the cape display skin part is sent
                boolean capeEnabled = readBoolean(1);
                displayedSkinParts.put(DisplayedSkinPart.CAPE, capeEnabled);
            } else {
                //in 1.8, all the skin parts are sent
                int skinPartFlags = readInt(1);
                displayedSkinParts.put(DisplayedSkinPart.CAPE, (skinPartFlags & 0x01) != 0);
                displayedSkinParts.put(DisplayedSkinPart.JACKET, (skinPartFlags & 0x02) != 0);
                displayedSkinParts.put(DisplayedSkinPart.LEFT_SLEEVE, (skinPartFlags & 0x04) != 0);
                displayedSkinParts.put(DisplayedSkinPart.RIGHT_SLEEVE, (skinPartFlags & 0x08) != 0);
                displayedSkinParts.put(DisplayedSkinPart.LEFT_PANTS, (skinPartFlags & 0x10) != 0);
                displayedSkinParts.put(DisplayedSkinPart.RIGHT_PANTS, (skinPartFlags & 0x20) != 0);
                displayedSkinParts.put(DisplayedSkinPart.HAT, (skinPartFlags & 0x40) != 0);
            }
        }
        return displayedSkinParts;
    }

    //TODO instead of returning false on 1.7.10 for non-existing skin parts, find out their real value on 1.7.10(although they aren't modifyable)
    public boolean isDisplaySkinPartEnabled(DisplayedSkinPart part) {
        if (displayedSkinParts.isEmpty()) {
            displayedSkinParts = getDisplayedSkinPartsMap();
        }
        //1.7.10, we will default the other skin parts to return false.
        if (!displayedSkinParts.containsKey(part)) {
            return false;
        }
        return displayedSkinParts.get(part);
    }
//TODO finish this
   /* public void setIsDisplaySKinPartEnabled(DisplayedSkinPart part, boolean enabled) throws UnsupportedOperationException {
        if (displayedSkinParts.isEmpty()) {
            displayedSkinParts = getDisplayedSkinPartsMap();
        }

        if (!displayedSkinParts.containsKey(part)) {
            throwUnsupportedOperation(part);
        }

        displayedSkinParts.put(part, enabled);
    }*/

    public enum ChatVisibility {
        FULL, SYSTEM, HIDDEN
    }

    public enum DisplayedSkinPart {
        CAPE,

        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        JACKET,
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        LEFT_SLEEVE,
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        RIGHT_SLEEVE,
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        LEFT_PANTS,
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        RIGHT_PANTS,
        @SupportedVersions(ranges = {ServerVersion.v_1_8, ServerVersion.ERROR})
        HAT
    }
}
