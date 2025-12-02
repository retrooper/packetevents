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

import com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogTypes {

    private static final VersionedRegistry<DialogType<?>> REGISTRY = new VersionedRegistry<>("dialog_type");

    private DialogTypes() {
    }

    public static VersionedRegistry<DialogType<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static <T extends Dialog> DialogType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticDialogType<>(data, decoder, encoder));
    }

    public static final DialogType<NoticeDialog> NOTICE = define("notice",
            NoticeDialog::decode, NoticeDialog::encode);
    public static final DialogType<ServerLinksDialog> SERVER_LINKS = define("server_links",
            ServerLinksDialog::decode, ServerLinksDialog::encode);
    public static final DialogType<DialogListDialog> DIALOG_LIST = define("dialog_list",
            DialogListDialog::decode, DialogListDialog::encode);
    public static final DialogType<MultiActionDialog> MULTI_ACTION = define("multi_action",
            MultiActionDialog::decode, MultiActionDialog::encode);
    public static final DialogType<ConfirmationDialog> CONFIRMATION = define("confirmation",
            ConfirmationDialog::decode, ConfirmationDialog::encode);

    static {
        REGISTRY.unloadMappings();
    }
}
