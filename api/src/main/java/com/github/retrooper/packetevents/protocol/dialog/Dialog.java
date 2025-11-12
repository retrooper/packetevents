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

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.dialog.DialogLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Dialog extends MappedEntity, DeepComparableEntity, CopyableEntity<Dialog>, DialogLike {

    static Dialog read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(Dialogs.getRegistry(), Dialog::readDirect);
    }

    static void write(PacketWrapper<?> wrapper, Dialog dialog) {
        wrapper.writeMappedEntityOrDirect(dialog, Dialog::writeDirect);
    }

    static Dialog readDirect(PacketWrapper<?> wrapper) {
        return decodeDirect(wrapper.readNBTRaw(), wrapper, null);
    }

    static void writeDirect(PacketWrapper<?> wrapper, Dialog dialog) {
        wrapper.writeNBTRaw(encodeDirect(dialog, wrapper));
    }

    static Dialog decode(NBT nbt, PacketWrapper<?> wrapper) {
        if (nbt instanceof NBTString) {
            return wrapper.replaceRegistry(Dialogs.getRegistry()).getByNameOrThrow(((NBTString) nbt).getValue());
        }
        return decodeDirect(nbt, wrapper, null);
    }

    static NBT encode(PacketWrapper<?> wrapper, Dialog dialog) {
        if (dialog.isRegistered()) {
            return new NBTString(dialog.getName().toString());
        }
        return encodeDirect(dialog, wrapper);
    }

    @ApiStatus.Internal
    static Dialog decodeDirect(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        String dialogTypeName = compound.getStringTagValueOrThrow("type");
        DialogType<?> dialogType = DialogTypes.getRegistry().getByNameOrThrow(dialogTypeName);
        // TODO pass down data instead of copying
        return dialogType.decode(compound, wrapper).copy(data);
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked") // not unchecked
    static NBT encodeDirect(Dialog dialog, PacketWrapper<?> wrapper) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("type", new NBTString(dialog.getType().getName().toString()));
        ((DialogType<? super Dialog>) dialog.getType()).encode(compound, wrapper, dialog);
        return compound;
    }

    DialogType<?> getType();
}
