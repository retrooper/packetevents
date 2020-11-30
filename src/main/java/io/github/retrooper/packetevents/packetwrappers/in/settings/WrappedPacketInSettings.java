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

package io.github.retrooper.packetevents.packetwrappers.in.settings;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.HashMap;

public class WrappedPacketInSettings extends WrappedPacket {
    private static Class<?> chatVisibilityEnumClass;

    private static boolean isLowerThan_v_1_8;
    private Object chatVisibilityEnumObj;
    private HashMap<DisplayedSkinPart, Boolean> displayedSkinParts = new HashMap<>();

    public WrappedPacketInSettings(final Object packet) {
        super(packet);
    }

    public static void load() {

        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);

        try {
            chatVisibilityEnumClass = NMSUtils.getNMSClass("EnumChatVisibility");
        } catch (ClassNotFoundException e) {
            Class<?> entityHumanClass = NMSUtils.getNMSClassWithoutException("EntityHuman");
            //They are just on an outdated version
            assert entityHumanClass != null;
            chatVisibilityEnumClass = SubclassUtil.getSubClass(entityHumanClass, "EnumChatVisibility");
        }
    }

    /**
     * Get language client setting
     *
     * @return String Locale
     */
    public String getLocale() {
        return readString(0);
    }

    /**
     * Get client view distance.
     *
     * @return View Distance
     */
    public int getViewDistance() {
        return readInt(0);
    }

    /**
     * Get Chat Visibility
     *
     * @return Chat Visibility
     */
    public ChatVisibility getChatVisibility() {
        if (chatVisibilityEnumObj == null) {
            chatVisibilityEnumObj = readObject(0, chatVisibilityEnumClass);
        }
        String enumValueAsString = chatVisibilityEnumObj.toString();
        if (enumValueAsString.equals("FULL")) {
            return ChatVisibility.ENABLED;
        } else if (enumValueAsString.equals("SYSTEM")) {
            return ChatVisibility.COMMANDS_ONLY;
        } else {
            return ChatVisibility.HIDDEN;
        }
    }

    /**
     * Is chat colors
     *
     * @return Chat Colors
     */
    public boolean isChatColors() {
        return readBoolean(0);
    }

    /**
     * Get Displayed skin parts.
     * <p>
     * It is possible for some keys to not exist.
     * If that is the case, the server version is 1.7.10.
     * 1.7.10 only sends the cape skin part.
     *
     * @return A map with a Skin Parts as a key, and a boolean as a value.
     */
    @Nullable
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

    /**
     * Is the skin part enabled.
     * <p>
     * On 1.7.10, some skin parts will default to 'false' as 1.7.10
     * only sends the 'cape' skin part.
     *
     * @param part The skin part to check the status of.
     * @return Is the skin part enabled
     */
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

    /**
     * Enum for the client chat visibility setting
     */
    public enum ChatVisibility {
        ENABLED, COMMANDS_ONLY, HIDDEN
    }

    /**
     * Enum for the client displayed skin parts settings
     */
    public enum DisplayedSkinPart {
        CAPE, JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANTS, RIGHT_PANTS, HAT
    }
}
