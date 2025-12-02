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
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class ConfirmationDialog extends AbstractMappedEntity implements Dialog {

    private final CommonDialogData common;
    private final ActionButton yesButton;
    private final ActionButton noButton;

    public ConfirmationDialog(CommonDialogData common, ActionButton yesButton, ActionButton noButton) {
        this(null, common, yesButton, noButton);
    }

    @ApiStatus.Internal
    public ConfirmationDialog(
            @Nullable TypesBuilderData data, CommonDialogData common,
            ActionButton yesButton, ActionButton noButton
    ) {
        super(data);
        this.common = common;
        this.yesButton = yesButton;
        this.noButton = noButton;
    }

    public static ConfirmationDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        ActionButton yesButton = compound.getOrThrow("yes", ActionButton::decode, wrapper);
        ActionButton noButton = compound.getOrThrow("no", ActionButton::decode, wrapper);
        return new ConfirmationDialog(null, common, yesButton, noButton);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ConfirmationDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        compound.set("yes", dialog.yesButton, ActionButton::encode, wrapper);
        compound.set("no", dialog.noButton, ActionButton::encode, wrapper);
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new ConfirmationDialog(newData, this.common, this.yesButton, this.noButton);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public ActionButton getYesButton() {
        return this.yesButton;
    }

    public ActionButton getNoButton() {
        return this.noButton;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.CONFIRMATION;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof ConfirmationDialog)) return false;
        if (!super.equals(obj)) return false;
        ConfirmationDialog that = (ConfirmationDialog) obj;
        if (!this.common.equals(that.common)) return false;
        if (!this.yesButton.equals(that.yesButton)) return false;
        return this.noButton.equals(that.noButton);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.common, this.yesButton, this.noButton);
    }
}
