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

package com.github.retrooper.packetevents.protocol.dialog.body;

import com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogBodyTypes {

    private static final VersionedRegistry<DialogBodyType<?>> REGISTRY = new VersionedRegistry<>("dialog_body_type");

    private DialogBodyTypes() {
    }

    @ApiStatus.Internal
    public static <T extends DialogBody> DialogBodyType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticDialogBodyType<>(data, decoder, encoder));
    }

    public static VersionedRegistry<DialogBodyType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final DialogBodyType<ItemDialogBody> ITEM = define("item",
            ItemDialogBody::decode, ItemDialogBody::encode);
    public static final DialogBodyType<PlainMessageDialogBody> PLAIN_MESSAGE = define("plain_message",
            PlainMessageDialogBody::decode, PlainMessageDialogBody::encode);

    static {
        REGISTRY.unloadMappings();
    }
}
