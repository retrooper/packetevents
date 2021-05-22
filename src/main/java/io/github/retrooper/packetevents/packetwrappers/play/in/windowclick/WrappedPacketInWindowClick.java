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

package io.github.retrooper.packetevents.packetwrappers.play.in.windowclick;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WrappedPacketInWindowClick extends WrappedPacket {
    private static final HashMap<Integer, ArrayList<WindowClickType>> WINDOW_CLICK_TYPE_CACHE = new HashMap<>();
    private static Class<? extends Enum<?>> invClickTypeClass;
    private static boolean isClickModePrimitive;


    public WrappedPacketInWindowClick(NMSPacket packet) {
        super(packet);
    }

    private static ArrayList<WindowClickType> getArrayListOfWindowClickTypes(WindowClickType... types) {
        ArrayList<WindowClickType> arrayList = new ArrayList<>(types.length);
        arrayList.addAll(Arrays.asList(types));
        return arrayList;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.WINDOW_CLICK;
        invClickTypeClass = NMSUtils.getNMSEnumClassWithoutException("InventoryClickType");

        //MODE 0
        WINDOW_CLICK_TYPE_CACHE.put(0, getArrayListOfWindowClickTypes(WindowClickType.LEFT_MOUSE_CLICK,
                WindowClickType.RIGHT_MOUSE_CLICK));

        //MODE 1
        WINDOW_CLICK_TYPE_CACHE.put(1, getArrayListOfWindowClickTypes(WindowClickType.SHIFT_LEFT_MOUSE_CLICK,
                WindowClickType.SHIFT_RIGHT_MOUSE_CLICK));

        //MODE 2
        WINDOW_CLICK_TYPE_CACHE.put(2, getArrayListOfWindowClickTypes(
                WindowClickType.KEY_NUMBER1,
                WindowClickType.KEY_NUMBER2,
                WindowClickType.KEY_NUMBER3,
                WindowClickType.KEY_NUMBER4,
                WindowClickType.KEY_NUMBER5,
                WindowClickType.KEY_NUMBER6,
                WindowClickType.KEY_NUMBER7,
                WindowClickType.KEY_NUMBER8,
                WindowClickType.KEY_NUMBER9));

        //MODE 3
        WINDOW_CLICK_TYPE_CACHE.put(3, getArrayListOfWindowClickTypes(WindowClickType.UNKNOWN, WindowClickType.UNKNOWN, WindowClickType.CREATIVE_MIDDLE_CLICK));

        //MODE 4
        WINDOW_CLICK_TYPE_CACHE.put(4, getArrayListOfWindowClickTypes(WindowClickType.KEY_DROP,
                WindowClickType.KEY_DROP_STACK));

        //MODE 5
        WINDOW_CLICK_TYPE_CACHE.put(5, getArrayListOfWindowClickTypes(
                WindowClickType.STARTING_LEFT_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_LEFT_MOUSE_DRAG,
                WindowClickType.ENDING_LEFT_MOUSE_DRAG,
                WindowClickType.UNKNOWN,
                WindowClickType.STARTING_RIGHT_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_RIGHT_MOUSE_DRAG,
                WindowClickType.CREATIVE_STARTING_MIDDLE_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_MIDDLE_MOUSE_DRAG,
                WindowClickType.ENDING_MIDDLE_MOUSE_DRAG));

        WINDOW_CLICK_TYPE_CACHE.put(6, getArrayListOfWindowClickTypes(WindowClickType.DOUBLE_CLICK));
        isClickModePrimitive = Reflection.getField(packetClass, int.class, 3) != null;
    }

    public int getWindowId() {
        return readInt(0);
    }

    public void setWindowId(int windowID) {
        writeInt(0, windowID);
    }

    public int getWindowSlot() {
        return readInt(1);
    }

    public void setWindowSlot(int slot) {
        writeInt(1, slot);
    }

    public int getWindowButton() {
        return readInt(2);
    }

    public void setWindowButton(int button) {
        writeInt(2, button);
    }

    public short getActionNumber() {
        return readShort(0);
    }

    public void setActionNumber(short actionNumber) {
        writeShort(0, actionNumber);
    }

    @Deprecated
    public WindowClickType getWindowClickType() {
        int mode = getMode();
        if (WINDOW_CLICK_TYPE_CACHE.get(mode) == null) {
            return WindowClickType.UNKNOWN;
        }
        int windowButton = getWindowButton();
        if (windowButton + 1 > WINDOW_CLICK_TYPE_CACHE.size()) {
            return WindowClickType.UNKNOWN;
        }

        if (mode == 4) {
            int windowSlot = getWindowSlot();
            if (windowSlot == -999) {
                if (windowButton == 0) {
                    return WindowClickType.LEFT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING;
                } else if (windowButton == 1) {
                    return WindowClickType.RIGHT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING;
                }
            }
        }
        return WINDOW_CLICK_TYPE_CACHE.get(mode).get(windowButton);
    }

    public int getMode() {
        if (isClickModePrimitive) {
            return readInt(3);
        } else {
            Enum<?> enumConst = readEnumConstant(0, invClickTypeClass);
            return enumConst.ordinal();
        }
    }

    public void setMode(int mode) {
        if (isClickModePrimitive) {
            writeInt(3, mode);
        } else {
            Enum<?> enumConst = EnumUtil.valueByIndex(invClickTypeClass, mode);
            writeEnumConstant(0, enumConst);
        }
    }

    /**
     * Get the clicked item.
     *
     * @return Get Clicked ItemStack
     */
    public ItemStack getClickedItemStack() {
        Object nmsItemStack = readObject(0, NMSUtils.nmsItemStackClass);
        return NMSUtils.toBukkitItemStack(nmsItemStack);
    }

    public void setClickedItemStack(ItemStack stack) {
        Object nmsItemStack = NMSUtils.toNMSItemStack(stack);
        write(NMSUtils.nmsItemStackClass, 0, nmsItemStack);
    }

    @Deprecated
    public enum WindowClickType {
        LEFT_MOUSE_CLICK, RIGHT_MOUSE_CLICK,
        SHIFT_LEFT_MOUSE_CLICK, SHIFT_RIGHT_MOUSE_CLICK,

        CREATIVE_MIDDLE_CLICK, CREATIVE_STARTING_MIDDLE_MOUSE_DRAG,

        KEY_NUMBER1, KEY_NUMBER2, KEY_NUMBER3, KEY_NUMBER4,
        KEY_NUMBER5, KEY_NUMBER6, KEY_NUMBER7, KEY_NUMBER8,
        KEY_NUMBER9, KEY_DROP, KEY_DROP_STACK,

        LEFT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING,
        RIGHT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING,

        STARTING_LEFT_MOUSE_DRAG,
        STARTING_RIGHT_MOUSE_DRAG,

        ADD_SLOT_LEFT_MOUSE_DRAG,
        ADD_SLOT_RIGHT_MOUSE_DRAG,
        ADD_SLOT_MIDDLE_MOUSE_DRAG,

        ENDING_LEFT_MOUSE_DRAG,
        ENDING_RIGHT_MOUSE_DRAG,
        ENDING_MIDDLE_MOUSE_DRAG,

        DOUBLE_CLICK,

        UNKNOWN
    }
}
