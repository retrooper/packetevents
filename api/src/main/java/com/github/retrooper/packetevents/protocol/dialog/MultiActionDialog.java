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

package com.github.retrooper.packetevents.protocol.dialog;

import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

@NullMarked
public class MultiActionDialog extends AbstractMappedEntity implements Dialog {

    private final CommonDialogData common;
    private final List<ActionButton> actions;
    private final @Nullable ActionButton exitAction;
    private final int columns;

    public MultiActionDialog(
            CommonDialogData common,
            List<ActionButton> actions,
            @Nullable ActionButton exitAction,
            int columns
    ) {
        this(null, common, actions, exitAction, columns);
    }

    @ApiStatus.Internal
    public MultiActionDialog(
            @Nullable TypesBuilderData data,
            CommonDialogData common,
            List<ActionButton> actions,
            @Nullable ActionButton exitAction,
            int columns
    ) {
        super(data);
        this.common = common;
        this.actions = actions;
        this.exitAction = exitAction;
        this.columns = columns;
    }

    public static MultiActionDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        List<ActionButton> actions = compound.getListOrThrow("actions", ActionButton::decode, wrapper);
        ActionButton action = compound.getOrNull("exit_action", ActionButton::decode, wrapper);
        int columns = compound.getNumberTagValueOrDefault("columns", 2).intValue();
        int buttonWidth = compound.getNumberTagValueOrDefault("button_width", 150).intValue();
        return new MultiActionDialog(null, common, actions, action, columns);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, MultiActionDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        compound.setList("actions", dialog.actions, ActionButton::encode, wrapper);
        if (dialog.exitAction != null) {
            compound.set("exit_action", dialog.exitAction, ActionButton::encode, wrapper);
        }
        if (dialog.columns != 2) {
            compound.setTag("columns", new NBTInt(dialog.columns));
        }
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new MultiActionDialog(newData, this.common, this.actions, this.exitAction, this.columns);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public List<ActionButton> getActions() {
        return this.actions;
    }

    public @Nullable ActionButton getExitAction() {
        return this.exitAction;
    }

    public int getColumns() {
        return this.columns;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.MULTI_ACTION;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof MultiActionDialog)) return false;
        if (!super.equals(obj)) return false;
        MultiActionDialog that = (MultiActionDialog) obj;
        if (this.columns != that.columns) return false;
        if (!this.common.equals(that.common)) return false;
        if (!this.actions.equals(that.actions)) return false;
        return Objects.equals(this.exitAction, that.exitAction);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.common, this.actions, this.exitAction, this.columns);
    }
}
