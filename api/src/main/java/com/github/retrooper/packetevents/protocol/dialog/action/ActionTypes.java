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

package com.github.retrooper.packetevents.protocol.dialog.action;

import com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ActionTypes {

    private static final VersionedRegistry<ActionType<?>> REGISTRY = new VersionedRegistry<>("dialog_action_type");

    private ActionTypes() {
    }

    @ApiStatus.Internal
    public static <T extends Action> ActionType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticActionType<>(data, decoder, encoder));
    }

    public static final ActionType<StaticAction> OPEN_URL = define("open_url",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> RUN_COMMAND = define("run_command",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> SUGGEST_COMMAND = define("suggest_command",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> SHOW_DIALOG = define("show_dialog",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> CHANGE_PAGE = define("change_page",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> COPY_TO_CLIPBOARD = define("copy_to_clipboard",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> CUSTOM = define("custom",
            StaticAction::decode, StaticAction::encode);
    public static final ActionType<DynamicRunCommandAction> DYNAMIC_RUN_COMMAND = define("dynamic/run_command",
            DynamicRunCommandAction::decode, DynamicRunCommandAction::encode);
    public static final ActionType<DynamicCustomAction> DYNAMIC_CUSTOM = define("dynamic/custom",
            DynamicCustomAction::decode, DynamicCustomAction::encode);

    public static VersionedRegistry<ActionType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
