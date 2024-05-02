/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PotDecorations {

    private @Nullable ItemType back;
    private @Nullable ItemType left;
    private @Nullable ItemType right;
    private @Nullable ItemType front;

    private PotDecorations(Queue<ItemType> items) {
        this(items.poll(), items.poll(), items.poll(), items.poll());
    }

    public PotDecorations(
            @Nullable ItemType back,
            @Nullable ItemType left,
            @Nullable ItemType right,
            @Nullable ItemType front
    ) {
        this.back = back;
        this.left = left;
        this.right = right;
        this.front = front;
    }

    private List<ItemType> asList() {
        return Arrays.asList(this.back, this.left, this.right, this.front);
    }

    private static @Nullable ItemType readItem(PacketWrapper<?> wrapper) {
        ItemType type = wrapper.readMappedEntity(ItemTypes::getById);
        return type == ItemTypes.BRICK ? null : type;
    }

    public static PotDecorations read(PacketWrapper<?> wrapper) {
        Queue<ItemType> items = wrapper.<ItemType, Queue<ItemType>>readCollection(
                ArrayDeque::new, PotDecorations::readItem);
        return new PotDecorations(items);
    }

    private static void writeItem(PacketWrapper<?> wrapper, @Nullable ItemType type) {
        wrapper.writeMappedEntity(type == null ? ItemTypes.BRICK : type);
    }

    public static void write(PacketWrapper<?> wrapper, PotDecorations decorations) {
        wrapper.writeList(decorations.asList(), PotDecorations::writeItem);
    }

    public @Nullable ItemType getBack() {
        return this.back;
    }

    public void setBack(@Nullable ItemType back) {
        this.back = back;
    }

    public @Nullable ItemType getLeft() {
        return this.left;
    }

    public void setLeft(@Nullable ItemType left) {
        this.left = left;
    }

    public @Nullable ItemType getRight() {
        return this.right;
    }

    public void setRight(@Nullable ItemType right) {
        this.right = right;
    }

    public @Nullable ItemType getFront() {
        return this.front;
    }

    public void setFront(@Nullable ItemType front) {
        this.front = front;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PotDecorations)) return false;
        PotDecorations that = (PotDecorations) obj;
        if (!Objects.equals(this.back, that.back)) return false;
        if (!Objects.equals(this.left, that.left)) return false;
        if (!Objects.equals(this.right, that.right)) return false;
        return Objects.equals(this.front, that.front);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.back, this.left, this.right, this.front);
    }
}
