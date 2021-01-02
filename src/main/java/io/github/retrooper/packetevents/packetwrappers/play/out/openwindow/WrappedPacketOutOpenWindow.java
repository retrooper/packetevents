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

package io.github.retrooper.packetevents.packetwrappers.play.out.openwindow;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public class WrappedPacketOutOpenWindow extends WrappedPacket {
    private static boolean legacyMode = false;
    private static boolean ultraLegacyMode = false;
    private int windowID;
    private int windowSize;
    private String windowTitle;

    public WrappedPacketOutOpenWindow(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutOpenWindow(int windowID, int windowSize, String windowTitle) {
        this.windowID = windowID;
        this.windowSize = windowSize;
        this.windowTitle = windowTitle;
    }

    @Override
    protected void load() {
        //Older versions (like 1.13.2 and lower) contain a String,
        legacyMode = Reflection.getField(PacketTypeClasses.Play.Server.OPEN_WINDOW, String.class, 0) != null;
        //1.7.10
        ultraLegacyMode = Reflection.getField(PacketTypeClasses.Play.Server.OPEN_WINDOW, boolean.class, 0) != null;
    }

    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        }
        return windowID;
    }

    public int getWindowType() {
        if (packet != null) {
            if (legacyMode && !ultraLegacyMode) {
                String windowType = readString(0);
                return getWindowTypeFromString(windowType);
            }
            return readInt(1);
        }
        return windowSize;
    }

    public String getWindowTitle() {
        if (packet != null) {
            if (ultraLegacyMode) {
                return readString(0);
            }
            Object iChatBaseComp = readObject(0, NMSUtils.iChatBaseComponentClass);
            return WrappedPacketOutChat.toStringFromIChatBaseComponent(iChatBaseComp);
        }
        return windowTitle;
    }

    public int getWindowTypeFromString(String type) {
        switch (type) {
            case "minecraft:chest":
            case "minecraft:container":
                return 2;
            case "minecraft:dropper":
            case "minecraft:dispenser":
                return 6;
            case "minecraft:anvil":
                return 7;
            case "minecraft:beacon":
                return 8;
            case "minecraft:blast_furnace":
                return 9;
            case "minecraft:brewing_stand":
                return 10;
            case "minecraft:crafting":
            case "minecraft:crafting_table":
                return 11;
            case "minecraft:enchantment":
            case "minecraft:enchanting_table":
                return 12;
            case "minecraft:furnace":
                return 13;
            case "minecraft:grindstone":
                return 14;
            case "minecraft:hopper":
                return 15;
            case "minecraft:lectern":
                return 16;
            case "minecraft:loom":
                return 17;
            case "minecraft:merchant":
                return 18;
            case "minecraft:shulker_box":
                return 19;
            case "minecraft:smoker":
                return 21;
            case "minecraft:cartography":
            case "minecraft:cartography_table":
                return 22;
            case "minecraft:stonecutter":
                return 23;
        }
        return -1;
    }
}
