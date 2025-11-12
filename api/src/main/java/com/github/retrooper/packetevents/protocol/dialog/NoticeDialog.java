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
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class NoticeDialog extends AbstractMappedEntity implements Dialog {

    public static final ActionButton DEFAULT_ACTION = new ActionButton(
            new CommonButtonData(Component.translatable("gui.ok"), null, 150),
            null
    );

    private final CommonDialogData common;
    private final ActionButton action;

    public NoticeDialog(CommonDialogData common, ActionButton action) {
        this(null, common, action);
    }

    @ApiStatus.Internal
    public NoticeDialog(@Nullable TypesBuilderData data, CommonDialogData common, ActionButton action) {
        super(data);
        this.common = common;
        this.action = action;
    }

    public static NoticeDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        ActionButton action = compound.getOr("action", ActionButton::decode, DEFAULT_ACTION, wrapper);
        return new NoticeDialog(null, common, action);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, NoticeDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        if (dialog.action != DEFAULT_ACTION) {
            compound.set("action", dialog.action, ActionButton::encode, wrapper);
        }
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new NoticeDialog(newData, this.common, this.action);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public ActionButton getAction() {
        return this.action;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.NOTICE;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (!(obj instanceof NoticeDialog)) return false;
        if (!super.equals(obj)) return false;
        NoticeDialog that = (NoticeDialog) obj;
        if (!this.common.equals(that.common)) return false;
        return this.action.equals(that.action);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.common, this.action);
    }
}
