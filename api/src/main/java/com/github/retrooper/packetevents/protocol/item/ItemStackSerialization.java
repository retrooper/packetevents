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
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ItemStackSerialization {

    private ItemStackSerialization() {
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
        int legacyData = v1_13_2 ? -1 : wrapper.readShort();
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
                wrapper.writeShort(stack.getLegacyData());
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

    @SuppressWarnings("unchecked")
    private static ItemStack readModern(PacketWrapper<?> wrapper, boolean lengthPrefixed) {
        int count = wrapper.readVarInt();
        if (count <= 0) {
            return ItemStack.EMPTY;
        }
        ItemType itemType = wrapper.readMappedEntity(ItemTypes.getRegistry());

        // read component patch counts
        int presentCount = wrapper.readVarInt();
        int absentCount = wrapper.readVarInt();
        if (presentCount == 0 && absentCount == 0) {
            return ItemStack.builder().type(itemType).amount(count).wrapper(wrapper).build();
        }

        PatchableComponentMap components = new PatchableComponentMap(
                itemType.getComponents(wrapper.getServerVersion().toClientVersion()),
                new HashMap<>(presentCount + absentCount));
        for (int i = 0; i < presentCount; i++) {
            ComponentType<?> type = wrapper.readMappedEntity(ComponentTypes.getRegistry());
            // this is not 1:1 how vanilla decodes the length prefix, vanilla slices the buffer and
            // restricts reading to only the slice; packetevents just verifies the length isn't too large
            // and throws an error if we actually read more/less than expected
            int expectedReaderIndex;
            if (lengthPrefixed) {
                int size = wrapper.readVarInt();
                if (size > ByteBufHelper.readableBytes(wrapper.buffer)) {
                    throw new RuntimeException("Component size " + size + " for " + type.getName() + " out of bounds");
                }
                expectedReaderIndex = ByteBufHelper.readerIndex(wrapper.buffer) + size;
            } else {
                expectedReaderIndex = -1;
            }
            // read component value
            Object value = type.read(wrapper);
            // if this component is length-prefixed, verify the reader index changed to the expected value
            if (expectedReaderIndex != -1) {
                int readerIndex = ByteBufHelper.readerIndex(wrapper.buffer);
                if (readerIndex != expectedReaderIndex) {
                    throw new RuntimeException("Invalid component read for " + type.getName() + "; expected reader index "
                            + expectedReaderIndex + ", got reader index " + readerIndex);
                }
            }
            // set component value in component patch-map
            components.set((ComponentType<Object>) type, value);
        }
        for (int i = 0; i < absentCount; i++) {
            components.unset(wrapper.readMappedEntity(ComponentTypes.getRegistry()));
        }

        return ItemStack.builder().type(itemType).amount(count).components(components).wrapper(wrapper).build();
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

    @SuppressWarnings("unchecked")
    private static void writeModern(PacketWrapper<?> wrapper, ItemStack stack, boolean lengthPrefixed) {
        if (stack.isEmpty()) {
            wrapper.writeByte(0);
            return;
        }
        wrapper.writeVarInt(stack.getAmount());
        wrapper.writeMappedEntity(stack.getType());

        if (!stack.hasComponentPatches()) {
            wrapper.writeShort(0);
            return; // early return
        }

        // write component patch counts
        Map<ComponentType<?>, Optional<?>> allPatches = stack.getComponents().getPatches();
        int presentCount = 0, absentCount = 0;
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                presentCount++;
            } else {
                absentCount++;
            }
        }
        wrapper.writeVarInt(presentCount);
        wrapper.writeVarInt(absentCount);

        // write present patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                wrapper.writeVarInt(patch.getKey().getId(wrapper.getServerVersion().toClientVersion()));
                Runnable writer = () -> ((ComponentType<Object>) patch.getKey()).write(wrapper, patch.getValue().get());
                if (lengthPrefixed) {
                    // easiest solution is to just temporarily replace the buffer
                    Object originalBuffer = wrapper.buffer;
                    wrapper.buffer = ByteBufHelper.allocateNewBuffer(originalBuffer);
                    writer.run();
                    Object componentBuffer = wrapper.buffer;
                    wrapper.buffer = originalBuffer;
                    // after writing to new buffer, write length of newly written buffer to original buffer
                    wrapper.writeVarInt(ByteBufHelper.readableBytes(componentBuffer));
                    // copy component buffer bytes to original buffer
                    ByteBufHelper.writeBytes(wrapper.buffer, componentBuffer);
                    // release component buffer
                    ByteBufHelper.release(componentBuffer);
                } else {
                    writer.run();
                }
            }
        }

        // write absent patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (!patch.getValue().isPresent()) {
                wrapper.writeVarInt(patch.getKey().getId(wrapper.getServerVersion().toClientVersion()));
            }
        }
    }
}
