package io.github.retrooper.packetevents.packetwrappers.in.windowclick;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WrappedPacketInWindowClick extends WrappedPacket {
    private int id;
    private int slot;
    private int button;
    private short actionNumber;
    private int mode;
    private ItemStack clickedItem;

    private static Class<?> packetClass, invClickTypeClass;
    private static boolean isClickModePrimitive = false, clickModeInfoFound = false;
    private static final HashMap<String, Integer> invClickTypeMapCache = new HashMap<String, Integer>();

    private static final HashMap<Integer, ArrayList<WindowClickType>> windowClickTypeCache = new HashMap<Integer, ArrayList<WindowClickType>>();

    public static void load() {
        packetClass = PacketTypeClasses.Client.WINDOW_CLICK;
        invClickTypeClass = NMSUtils.getNMSClassWithoutException("InventoryClickType");

        invClickTypeMapCache.put("PICKUP", 0);
        invClickTypeMapCache.put("QUICK_MOVE", 1);
        invClickTypeMapCache.put("SWAP", 2);
        invClickTypeMapCache.put("CLONE", 3);
        invClickTypeMapCache.put("THROW", 4);
        invClickTypeMapCache.put("QUICK_CRAFT", 5);
        invClickTypeMapCache.put("PICKUP_ALL", 6);

        //MODE 0
        windowClickTypeCache.put(0, getArrayListOfWindowClickTypes(WindowClickType.LEFT_MOUSE_CLICK,
                WindowClickType.RIGHT_MOUSE_CLICK));

        //MODE 1
        windowClickTypeCache.put(1, getArrayListOfWindowClickTypes(WindowClickType.SHIFT_LEFT_MOUSE_CLICK,
                WindowClickType.SHIFT_RIGHT_MOUSE_CLICK));

        //MODE 2
        windowClickTypeCache.put(2, getArrayListOfWindowClickTypes(
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
        windowClickTypeCache.put(3, getArrayListOfWindowClickTypes(WindowClickType.CREATIVE_MIDDLE_CLICK));

        //MODE 4
        windowClickTypeCache.put(4, getArrayListOfWindowClickTypes(WindowClickType.KEY_DROP,
                WindowClickType.KEY_DROP_STACK, WindowClickType.LEFT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING,
                WindowClickType.RIGHT_CLICK_OUTSIDE_WINDOW_HOLDING_NOTHING));

        //MODE 5
        windowClickTypeCache.put(5, getArrayListOfWindowClickTypes(
                WindowClickType.STARTING_LEFT_MOUSE_DRAG,
                WindowClickType.STARTING_RIGHT_MOUSE_DRAG,
                WindowClickType.CREATIVE_STARTING_MIDDLE_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_LEFT_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_RIGHT_MOUSE_DRAG,
                WindowClickType.ADD_SLOT_MIDDLE_MOUSE_DRAG,
                WindowClickType.ENDING_LEFT_MOUSE_DRAG,
                WindowClickType.ENDING_RIGHT_MOUSE_DRAG,
                WindowClickType.ENDING_MIDDLE_MOUSE_DRAG));

        windowClickTypeCache.put(6, getArrayListOfWindowClickTypes(WindowClickType.DOUBLE_CLICK));
    }

    public WrappedPacketInWindowClick(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            this.id = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            this.slot = Reflection.getField(packetClass, int.class, 1).getInt(packet);
            this.button = Reflection.getField(packetClass, int.class, 2).getInt(packet);
            this.actionNumber = Reflection.getField(packetClass, short.class, 0).getShort(packet);
            Object nmsItemStack = Reflection.getField(packetClass, NMSUtils.nmsItemStackClass, 0).get(packet);
            this.clickedItem = NMSUtils.toBukkitItemStack(nmsItemStack);
            Object clickMode = Reflection.getFields(packetClass)[5].get(packet);
            if (!clickModeInfoFound) {
                isClickModePrimitive = clickMode.getClass().isPrimitive();
                clickModeInfoFound = true;
            }

            if (isClickModePrimitive) {
                mode = (int) clickMode;
            } else {
                mode = invClickTypeMapCache.get(clickMode.toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


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

        DOUBLE_CLICK
    }

    public int getWindowID() {
        return id;
    }

    public int getWindowSlot() {
        return slot;
    }

    public int getWindowButton() {
        return button;
    }

    public short getActionNumber() {
        return actionNumber;
    }

    @Nullable
    public WindowClickType getWindowClickType() {
        return windowClickTypeCache.get(mode).get(button);
    }

    public int getMode() {
        return mode;
    }

    public ItemStack getClickedItem() {
        return clickedItem;
    }

    private static ArrayList<WindowClickType> getArrayListOfWindowClickTypes(WindowClickType... types) {
        ArrayList<WindowClickType> arrayList = new ArrayList<WindowClickType>(types.length);
        arrayList.addAll(Arrays.asList(types));
        return arrayList;
    }
}
