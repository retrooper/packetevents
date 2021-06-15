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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityequipment;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.pair.MojangPairUtils;
import io.github.retrooper.packetevents.utils.pair.Pair;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class WrappedPacketOutEntityEquipment extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_17;
    private static Class<? extends Enum<?>> enumItemSlotClass;
    private static Constructor<?> packetConstructor;
    private List<Pair<EquipmentSlot, ItemStack>> equipment;
    private EquipmentSlot legacySlot;
    private ItemStack legacyItemStack;

    public WrappedPacketOutEntityEquipment(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityEquipment(int entityID, EquipmentSlot slot, ItemStack itemStack) {
        setEntityId(entityID);
        Pair<EquipmentSlot, ItemStack> pair = new Pair<>(slot, itemStack);
        this.equipment = new ArrayList<>();
        this.equipment.add(pair);
        this.legacySlot = slot;
        this.legacyItemStack = itemStack;
    }

    public WrappedPacketOutEntityEquipment(Entity entity, EquipmentSlot slot, ItemStack itemStack) {
        setEntity(entity);
        Pair<EquipmentSlot, ItemStack> pair = new Pair<>(slot, itemStack);
        this.equipment = new ArrayList<>();
        this.equipment.add(pair);
        this.legacySlot = slot;
        this.legacyItemStack = itemStack;
    }


    public WrappedPacketOutEntityEquipment(int entityID, List<Pair<EquipmentSlot, ItemStack>> equipment) {
        setEntityId(entityID);
        this.equipment = equipment;
        this.legacySlot = equipment.get(0).getFirst();
        this.legacyItemStack = equipment.get(0).getSecond();
    }

    public WrappedPacketOutEntityEquipment(Entity entity, List<Pair<EquipmentSlot, ItemStack>> equipment) {
        setEntity(entity);
        this.equipment = equipment;
        this.legacySlot = equipment.get(0).getFirst();
        this.legacyItemStack = equipment.get(0).getSecond();
    }


    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT.getConstructor(NMSUtils.packetDataSerializerClass);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        enumItemSlotClass = NMSUtils.getNMSEnumClassWithoutException("EnumItemSlot");
        if (enumItemSlotClass == null) {
            enumItemSlotClass = NMSUtils.getNMEnumClassWithoutException("world.entity.EnumItemSlot");
        }
    }

    //LEGACY
    private EquipmentSlot getSingleSlot() {
        if (packet != null) {
            byte id;
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                id = (byte) readInt(1);
            } else {
                Enum<?> nmsEnumItemSlot = readEnumConstant(0, enumItemSlotClass);
                id = (byte) nmsEnumItemSlot.ordinal();
            }
            return EquipmentSlot.getById(id);
        } else {
            return legacySlot;
        }
    }

    private void setSingleSlot(EquipmentSlot slot) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                writeInt(1, slot.getId());
            } else {
                Enum<?> nmsEnumConstant = EnumUtil.valueByIndex(enumItemSlotClass, slot.getId());
                writeEnumConstant(0, nmsEnumConstant);
            }
        } else {
            this.legacySlot = slot;
        }
    }

    private ItemStack getSingleItemStack() {
        if (packet != null) {
            return readItemStack(0);
        } else {
            return legacyItemStack;
        }
    }

    private void setSingleItemStack(ItemStack itemStack) {
        if (packet != null) {
            writeItemStack(0, itemStack);
        } else {
            this.legacyItemStack = itemStack;
        }
    }

    //1.16+ only methods
    private List<Pair<EquipmentSlot, ItemStack>> getListPair() {
        List<Object> listMojangPairObject = readList(0);
        List<Pair<EquipmentSlot, ItemStack>> pairList = new ArrayList<>();
        for (Object mojangPair : listMojangPairObject) {
            Pair<Object, Object> abstractedPair = MojangPairUtils.extractPair(mojangPair);
            Enum<?> nmsItemSlot = (Enum<?>) abstractedPair.getFirst();
            Object nmsItemStack = abstractedPair.getSecond();
            Pair<EquipmentSlot, ItemStack> pair = new Pair<>(EquipmentSlot.getById((byte) nmsItemSlot.ordinal()), NMSUtils.toBukkitItemStack(nmsItemStack));
            pairList.add(pair);
        }
        return pairList;
    }

    private void setListPair(List<Pair<EquipmentSlot, ItemStack>> pairList) {
        List<Object> mojangPairList = new ArrayList<>(pairList.size());
        for (Pair<EquipmentSlot, ItemStack> pair : pairList) {
            EquipmentSlot slot = pair.getFirst();
            ItemStack itemStack = pair.getSecond();
            Enum<?> nmsItemSlotEnumConstant = EnumUtil.valueByIndex(enumItemSlotClass, slot.getId());
            Object nmsItemStack = NMSUtils.toNMSItemStack(itemStack);
            Object mojangPair = MojangPairUtils.getMojangPair(nmsItemSlotEnumConstant, nmsItemStack);
            mojangPairList.add(mojangPair);
        }
        writeList(0, mojangPairList);
    }

    //UNIVERSAL METHOD
    public List<Pair<EquipmentSlot, ItemStack>> getEquipment() {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_16)) {
                List<Pair<EquipmentSlot, ItemStack>> pair = new ArrayList<>(1);
                pair.add(new Pair<>(getSingleSlot(), getSingleItemStack()));
                return pair;
            } else {
                return getListPair();
            }
        } else {
            return equipment;
        }
    }

    public void setEquipment(List<Pair<EquipmentSlot, ItemStack>> equipment) throws UnsupportedOperationException {
        boolean olderThan_v_1_16 = version.isOlderThan(ServerVersion.v_1_16);
        if (olderThan_v_1_16 && equipment.size() > 1) {
            throw new UnsupportedOperationException("The equipment pair list size cannot be greater than one on server versions older than 1.16!");
        }
        if (packet != null) {
            if (olderThan_v_1_16) {
                EquipmentSlot equipmentSlot = equipment.get(0).getFirst();
                ItemStack itemStack = equipment.get(0).getSecond();
                setSingleSlot(equipmentSlot);
                setSingleItemStack(itemStack);
            } else {
                setListPair(equipment);
            }
        } else {
            this.equipment = equipment;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] {0, 0, 0, 0}));
            packetInstance = packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = packetConstructor.newInstance();
        }
        WrappedPacketOutEntityEquipment wrappedPacketOutEntityEquipment = new WrappedPacketOutEntityEquipment(new NMSPacket(packetInstance));
        wrappedPacketOutEntityEquipment.setEntityId(getEntityId());
        wrappedPacketOutEntityEquipment.setEquipment(getEquipment());
        return packetInstance;
    }

    public enum EquipmentSlot {
        MAINHAND,
        OFFHAND,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET;

        public byte id;

        @Nullable
        public static EquipmentSlot getById(byte id) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.id == id) {
                    return slot;
                }
            }
            return null;
        }

        public byte getId() {
            return id;
        }
    }
}
