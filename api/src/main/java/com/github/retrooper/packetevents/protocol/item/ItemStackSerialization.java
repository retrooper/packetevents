/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public final class ItemStackSerialization {

    private ItemStackSerialization() {
    }

    public static ItemStack readOptionalTemplate(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
            return read(wrapper);
        }
        if (wrapper.readBoolean()) {
            return readTemplate0(wrapper);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack readTemplate(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
            return read(wrapper);
        }
        return readTemplate0(wrapper);
    }

    public static ItemStack readTemplate0(PacketWrapper<?> wrapper) {
        ItemType item = wrapper.readMappedEntity(ItemTypes.getRegistry());
        int count = wrapper.readVarInt();
        PatchableComponentMap components = PatchableComponentMap.read(wrapper, item, false);
        ItemStack ret = ItemStack.builder().type(item).amount(count).components(components).build();
        if (ret.isEmpty()) {
            throw new IllegalStateException("Can't read empty item stack template: " + ret);
        }
        return ret;
    }

    public static void writeOptionalTemplate(PacketWrapper<?> wrapper, ItemStack stack) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
            write(wrapper, stack);
            return;
        }
        if (!stack.isEmpty()) {
            wrapper.writeBoolean(true);
            writeTemplate0(wrapper, stack);
        } else {
            wrapper.writeBoolean(false);
        }
    }

    public static void writeTemplate(PacketWrapper<?> wrapper, ItemStack stack) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
            write(wrapper, stack);
        } else {
            writeTemplate0(wrapper, stack);
        }
    }

    private static void writeTemplate0(PacketWrapper<?> wrapper, ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Can't write empty item stack template: " + stack);
        }
        wrapper.writeMappedEntity(stack.getType());
        wrapper.writeVarInt(stack.getAmount());
        PatchableComponentMap.write(wrapper, stack, false);
    }

    public static ItemStack read(PacketWrapper<?> wrapper) {
        return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)
                ? readModern(wrapper) : readLegacy(wrapper);
    }

    public static void write(PacketWrapper<?> wrapper, @Nullable ItemStack stack) {
        ItemStack replacedStack = stack == null ? ItemStack.EMPTY : stack;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            writeModern(wrapper, replacedStack);
        } else {
            writeLegacy(wrapper, replacedStack);
        }
    }

    /**
     * Removed with 1.20.5
     */
    private static ItemStack readLegacy(PacketWrapper<?> wrapper) {
        boolean v1_13_2 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2 && !wrapper.readBoolean()) {
            return ItemStack.EMPTY;
        }
        int typeId = v1_13_2 ? wrapper.readVarInt() : wrapper.readShort();
        if (typeId < 0 && !v1_13_2) { // 1.13.2 doesn't have this logic
            return ItemStack.EMPTY;
        }

        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        ItemType type = ItemTypes.getRegistry().getByIdOrThrow(version, typeId);
        int amount = wrapper.readByte();
        int legacyData = version.isOlderThan(ClientVersion.V_1_13) ? wrapper.readShort() : -1;
        NBTCompound nbt = wrapper.readNBT();
        return ItemStack.builder().type(type).amount(amount)
                .nbt(nbt).legacyData(legacyData)
                .wrapper(wrapper).build();
    }

    /**
     * Removed with 1.20.5
     */
    private static void writeLegacy(PacketWrapper<?> wrapper, ItemStack stack) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_13_2)) {
            int typeId = stack.isEmpty() ? -1 : stack.getType().getId(wrapper.getServerVersion().toClientVersion());
            wrapper.writeShort(typeId);
            if (typeId != -1) {
                wrapper.writeByte(stack.getAmount());
                if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_13)) {
                    wrapper.writeShort(stack.getLegacyData());
                }
                wrapper.writeNBT(stack.getNBT());
            }
        } else if (stack.isEmpty()) {
            wrapper.writeBoolean(false);
        } else {
            wrapper.writeBoolean(true);
            wrapper.writeMappedEntity(stack.getType());
            wrapper.writeByte(stack.getAmount());
            wrapper.writeNBT(stack.getNBT());
        }
    }

    /**
     * Added with 1.20.5
     */
    public static ItemStack readModern(PacketWrapper<?> wrapper) {
        return readModern(wrapper, false);
    }

    /**
     * Added with 1.21.5
     */
    public static ItemStack readUntrusted(PacketWrapper<?> wrapper) {
        return readModern(wrapper, true);
    }

    private static ItemStack readModern(PacketWrapper<?> wrapper, boolean lengthPrefixed) {
        int count = wrapper.readVarInt();
        if (count <= 0) {
            return ItemStack.EMPTY;
        }
        ItemType item = wrapper.readMappedEntity(ItemTypes.getRegistry());
        PatchableComponentMap components = PatchableComponentMap.read(wrapper, item, lengthPrefixed);
        return ItemStack.builder().type(item).amount(count).components(components).wrapper(wrapper).build();
    }

    /**
     * Added with 1.20.5
     */
    public static void writeModern(PacketWrapper<?> wrapper, ItemStack stack) {
        writeModern(wrapper, stack, false);
    }

    /**
     * Added with 1.21.5
     */
    public static void writeUntrusted(PacketWrapper<?> wrapper, ItemStack stack) {
        writeModern(wrapper, stack, true);
    }

    private static void writeModern(PacketWrapper<?> wrapper, ItemStack stack, boolean lengthPrefixed) {
        if (stack.isEmpty()) {
            wrapper.writeByte(0);
            return;
        }
        wrapper.writeVarInt(stack.getAmount());
        wrapper.writeMappedEntity(stack.getType());
        PatchableComponentMap.write(wrapper, stack, lengthPrefixed);
    }
}
