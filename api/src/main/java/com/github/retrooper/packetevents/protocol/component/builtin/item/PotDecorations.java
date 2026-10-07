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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.ItemStackSerialization;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * @versions 1.20.5+
 */
public class PotDecorations {

    private @Nullable ItemStack back;
    private @Nullable ItemStack left;
    private @Nullable ItemStack right;
    private @Nullable ItemStack front;

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    private PotDecorations(Queue<Optional<ItemType>> items) {
        this(
                items.isEmpty() ? null : items.remove().orElse(null),
                items.isEmpty() ? null : items.remove().orElse(null),
                items.isEmpty() ? null : items.remove().orElse(null),
                items.isEmpty() ? null : items.remove().orElse(null)
        );
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public PotDecorations(
            @Nullable ItemType back,
            @Nullable ItemType left,
            @Nullable ItemType right,
            @Nullable ItemType front
    ) {
        this(
                back != null ? ItemStack.builder().type(back).build() : null,
                left != null ? ItemStack.builder().type(left).build() : null,
                right != null ? ItemStack.builder().type(right).build() : null,
                front != null ? ItemStack.builder().type(front).build() : null
        );
    }

    /**
     * @versions 26.3+
     */
    public PotDecorations(
            @Nullable ItemStack back,
            @Nullable ItemStack left,
            @Nullable ItemStack right,
            @Nullable ItemStack front
    ) {
        this.back = back;
        this.left = left;
        this.right = right;
        this.front = front;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    private List<Optional<ItemType>> asList() {
        return Arrays.asList(
                Optional.ofNullable(this.back).map(ItemStack::getType),
                Optional.ofNullable(this.left).map(ItemStack::getType),
                Optional.ofNullable(this.right).map(ItemStack::getType),
                Optional.ofNullable(this.front).map(ItemStack::getType)
        );
    }

    public static PotDecorations read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            return new PotDecorations(
                    wrapper.readOptional(ItemStackSerialization::readTemplate),
                    wrapper.readOptional(ItemStackSerialization::readTemplate),
                    wrapper.readOptional(ItemStackSerialization::readTemplate),
                    wrapper.readOptional(ItemStackSerialization::readTemplate)
            );
        }
        Queue<Optional<ItemType>> items = wrapper.readCollection(ArrayDeque::new, ew -> {
            ItemType type = wrapper.readMappedEntity(ItemTypes.getRegistry());
            return type == ItemTypes.BRICK ? Optional.empty() : Optional.of(type);
        });
        return new PotDecorations(items);
    }

    public static void write(PacketWrapper<?> wrapper, PotDecorations decorations) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
            wrapper.writeOptional(decorations.back, ItemStackSerialization::writeTemplate);
            wrapper.writeOptional(decorations.left, ItemStackSerialization::writeTemplate);
            wrapper.writeOptional(decorations.right, ItemStackSerialization::writeTemplate);
            wrapper.writeOptional(decorations.front, ItemStackSerialization::writeTemplate);
        } else {
            wrapper.writeList(decorations.asList(), (ew, type) ->
                    ew.writeMappedEntity(type.orElse(ItemTypes.BRICK)));
        }
    }

    /**
     * @versions 26.3+
     */
    public @Nullable ItemStack getBackStack() {
        return this.back;
    }

    /**
     * @versions 26.3+
     */
    public void setBackStack(@Nullable ItemStack back) {
        this.back = back;
    }

    /**
     * @versions 26.3+
     */
    public @Nullable ItemStack getLeftStack() {
        return this.left;
    }

    /**
     * @versions 26.3+
     */
    public void setLeftStack(@Nullable ItemStack left) {
        this.left = left;
    }

    /**
     * @versions 26.3+
     */
    public @Nullable ItemStack getRightStack() {
        return this.right;
    }

    /**
     * @versions 26.3+
     */
    public void setRightStack(@Nullable ItemStack right) {
        this.right = right;
    }

    /**
     * @versions 26.3+
     */
    public @Nullable ItemStack getFrontStack() {
        return this.front;
    }

    /**
     * @versions 26.3+
     */
    public void setFrontStack(@Nullable ItemStack front) {
        this.front = front;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public @Nullable ItemType getBack() {
        return this.back != null ? this.back.getType() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public void setBack(@Nullable ItemType back) {
        this.back = back != null ? ItemStack.builder().type(back).build() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public @Nullable ItemType getLeft() {
        return this.left != null ? this.left.getType() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public void setLeft(@Nullable ItemType left) {
        this.left = left != null ? ItemStack.builder().type(left).build() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public @Nullable ItemType getRight() {
        return this.right != null ? this.right.getType() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public void setRight(@Nullable ItemType right) {
        this.right = right != null ? ItemStack.builder().type(right).build() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public @Nullable ItemType getFront() {
        return this.front != null ? this.front.getType() : null;
    }

    /**
     * @versions 1.20.5-26.2
     */
    @ApiStatus.Obsolete
    public void setFront(@Nullable ItemType front) {
        this.front = front != null ? ItemStack.builder().type(front).build() : null;
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
