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

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.WINDOW_CLICK;
        invClickTypeClass = (Class<? extends Enum<?>>) NMSUtils.getNMSClassWithoutException("InventoryClickType");

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

    private static ArrayList<WindowClickType> getArrayListOfWindowClickTypes(WindowClickType... types) {
        ArrayList<WindowClickType> arrayList = new ArrayList<WindowClickType>(types.length);
        arrayList.addAll(Arrays.asList(types));
        return arrayList;
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
            Enum<?> enumConst = (Enum<?>) readObject(0, invClickTypeClass);
            return enumConst.ordinal();
        }
    }

    public void setMode(int mode) {
        if (isClickModePrimitive) {
            writeInt(3, mode);
        } else {
            Enum<?> enumConst = EnumUtil.valueByIndex(invClickTypeClass, mode);
            write(invClickTypeClass, 0, enumConst);
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
