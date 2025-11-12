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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class DialogListDialog extends AbstractMappedEntity implements Dialog {

    private final CommonDialogData common;
    private final MappedEntityRefSet<Dialog> dialogs;
    private final @Nullable ActionButton exitAction;
    private final int columns;
    private final int buttonWidth;

    public DialogListDialog(
            CommonDialogData common,
            MappedEntityRefSet<Dialog> dialogs,
            @Nullable ActionButton exitAction,
            int columns, int buttonWidth
    ) {
        this(null, common, dialogs, exitAction, columns, buttonWidth);
    }

    @ApiStatus.Internal
    public DialogListDialog(
            @Nullable TypesBuilderData data,
            CommonDialogData common,
            MappedEntityRefSet<Dialog> dialogs,
            @Nullable ActionButton exitAction,
            int columns, int buttonWidth
    ) {
        super(data);
        this.common = common;
        this.dialogs = dialogs;
        this.exitAction = exitAction;
        this.columns = columns;
        this.buttonWidth = buttonWidth;
    }

    public static DialogListDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        MappedEntityRefSet<Dialog> dialogs = compound.getOrThrow("dialogs", MappedEntitySet::decodeRefSet, wrapper);
        ActionButton action = compound.getOrNull("exit_action", ActionButton::decode, wrapper);
        int columns = compound.getNumberTagValueOrDefault("columns", 2).intValue();
        int buttonWidth = compound.getNumberTagValueOrDefault("button_width", 150).intValue();
        return new DialogListDialog(null, common, dialogs, action, columns, buttonWidth);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DialogListDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        compound.set("dialogs", dialog.dialogs, MappedEntitySet::encodeRefSet, wrapper);
        if (dialog.exitAction != null) {
            compound.set("exit_action", dialog.exitAction, ActionButton::encode, wrapper);
        }
        if (dialog.columns != 2) {
            compound.setTag("columns", new NBTInt(dialog.columns));
        }
        if (dialog.buttonWidth != 150) {
            compound.setTag("button_width", new NBTInt(dialog.buttonWidth));
        }
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new DialogListDialog(newData, this.common, this.dialogs, this.exitAction, this.columns, this.buttonWidth);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public MappedEntityRefSet<Dialog> getDialogs() {
        return this.dialogs;
    }

    public @Nullable ActionButton getExitAction() {
        return this.exitAction;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getButtonWidth() {
        return this.buttonWidth;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.DIALOG_LIST;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof DialogListDialog)) return false;
        if (!super.equals(obj)) return false;
        DialogListDialog that = (DialogListDialog) obj;
        if (this.columns != that.columns) return false;
        if (this.buttonWidth != that.buttonWidth) return false;
        if (!this.common.equals(that.common)) return false;
        if (!this.dialogs.equals(that.dialogs)) return false;
        return Objects.equals(this.exitAction, that.exitAction);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.common, this.dialogs, this.exitAction, this.columns, this.buttonWidth);
    }
}
