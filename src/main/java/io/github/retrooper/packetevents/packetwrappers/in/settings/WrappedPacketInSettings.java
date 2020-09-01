/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.packetwrappers.in.settings;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.util.HashMap;

public class WrappedPacketInSettings extends WrappedPacket {
    private static Class<?> packetClass;
    private static Class<?> chatVisibilityEnumClass;

    private static boolean isLowerThan_v_1_8;
    private String locale;
    private byte viewDistance;
    private ChatVisibility chatVisibility;
    private boolean chatColors;
    private HashMap<DisplayedSkinPart, Boolean> displayedSkinParts;
    public WrappedPacketInSettings(final Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.SETTINGS;

        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);

        try {
            chatVisibilityEnumClass = NMSUtils.getNMSClass("EnumChatVisibility");
        } catch (ClassNotFoundException e) {
            Class<?> entityHumanClass = NMSUtils.getNMSClassWithoutException("EntityHuman");
            //They are just on an outdated version
            chatVisibilityEnumClass = Reflection.getSubClass(entityHumanClass, "EnumChatVisibility");
        }
    }

    public static Class<?> getChatVisibilityEnumClass() {
        return chatVisibilityEnumClass;
    }

    @Override
    protected void setup() {
        try {
            //LOCALE
            this.locale = Reflection.getField(packetClass, String.class, 0).get(packet).toString();
            //VIEW DISTANCE
            this.viewDistance = (byte) Reflection.getField(packetClass, int.class, 0).getInt(packet);

            //CHAT VISIBILITY
            Object chatVisibilityEnumObject = Reflection.getField(packetClass, chatVisibilityEnumClass, 0).get(packet);
            String enumValueAsString = chatVisibilityEnumObject.toString();
            if (enumValueAsString.equals("FULL")) {
                chatVisibility = ChatVisibility.ENABLED;
            } else if (enumValueAsString.equals("SYSTEM")) {
                chatVisibility = ChatVisibility.COMMANDS_ONLY;
            } else {
                chatVisibility = ChatVisibility.HIDDEN;
            }

            //CHAT COLORS
            this.chatColors = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);

            //DISPLAYED SKIN PARTS
            this.displayedSkinParts = new HashMap<DisplayedSkinPart, Boolean>();

            if (isLowerThan_v_1_8) {
                //in 1.7.10 only the cape display skin part is sent
                boolean capeEnabled = Reflection.getField(packetClass, boolean.class, 1).getBoolean(packet);
                displayedSkinParts.put(DisplayedSkinPart.CAPE, capeEnabled);
            } else {
                //in 1.8, all the skin parts are sent
                int skinPartFlags = Reflection.getField(packetClass, int.class, 1).getInt(packet);
                displayedSkinParts.put(DisplayedSkinPart.CAPE, (skinPartFlags & 0x01) != 0);
                displayedSkinParts.put(DisplayedSkinPart.JACKET, (skinPartFlags & 0x02) != 0);
                displayedSkinParts.put(DisplayedSkinPart.LEFT_SLEEVE, (skinPartFlags & 0x04) != 0);
                displayedSkinParts.put(DisplayedSkinPart.RIGHT_SLEEVE, (skinPartFlags & 0x08) != 0);
                displayedSkinParts.put(DisplayedSkinPart.LEFT_PANTS, (skinPartFlags & 0x10) != 0);
                displayedSkinParts.put(DisplayedSkinPart.RIGHT_PANTS, (skinPartFlags & 0x20) != 0);
                displayedSkinParts.put(DisplayedSkinPart.HAT, (skinPartFlags & 0x40) != 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getLocale() {
        return locale;
    }

    public byte getViewDistance() {
        return viewDistance;
    }

    public ChatVisibility getChatVisibility() {
        return chatVisibility;
    }

    public boolean isChatColors() {
        return chatColors;
    }

    /**
     * It is possible for some keys to not exist.
     * If that is the case, the server version is 1.7.10.
     * 1.7.10 only sends the cape skin part.
     *
     * @return A map associating skin parts with whether or not they are enabled.
     */
    @Nullable
    public HashMap<DisplayedSkinPart, Boolean> getDisplayedSkinPartsMap() {
        return displayedSkinParts;
    }

    /**
     * On 1.7.10, some skin parts will default to 'false' as 1.7.10
     * only sends the 'cape' skin part.
     *
     * @param part The skin part to check the status of.
     * @return True if the specified skin part is enabled, otherwise false.
     */
    public boolean isDisplaySkinPartEnabled(DisplayedSkinPart part) {
        //1.7.10, we will default the other skin parts to return false.
        if (!displayedSkinParts.containsKey(part)) {
            return false;
        }
        return displayedSkinParts.get(part);
    }

    public enum ChatVisibility {
        ENABLED, COMMANDS_ONLY, HIDDEN
    }

    public enum DisplayedSkinPart {
        CAPE, JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANTS, RIGHT_PANTS, HAT
    }
}
