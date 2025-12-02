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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;

@NullMarked
public final class Dialogs {

    private static final ActionButton DEFAULT_BACK_BUTTON = new ActionButton(
            new CommonButtonData(Component.translatable("gui.back"), null, 200),
            null);

    private static final VersionedRegistry<Dialog> REGISTRY = new VersionedRegistry<>("dialog");

    private Dialogs() {
    }

    @ApiStatus.Internal
    public static Dialog define(String name, Dialog dialog) {
        return REGISTRY.define(name, dialog::copy);
    }

    public static VersionedRegistry<Dialog> getRegistry() {
        return REGISTRY;
    }

    public static final Dialog SERVER_LINKS = define("server_links", new ServerLinksDialog(
            new CommonDialogData(
                    Component.translatable("menu.server_links.title"),
                    Component.translatable("menu.server_links"),
                    true,
                    true,
                    DialogAction.CLOSE,
                    Collections.emptyList(),
                    Collections.emptyList()
            ),
            DEFAULT_BACK_BUTTON,
            1,
            310
    ));
    public static final Dialog CUSTOM_OPTIONS = define("custom_options", new DialogListDialog(
            new CommonDialogData(
                    Component.translatable("menu.custom_options.title"),
                    Component.translatable("menu.custom_options"),
                    true,
                    true,
                    DialogAction.CLOSE,
                    Collections.emptyList(),
                    Collections.emptyList()
            ),
            new MappedEntitySet<>(new ResourceLocation("pause_screen_additions")),
            DEFAULT_BACK_BUTTON,
            1,
            310
    ));
    public static final Dialog QUICK_ACTIONS = define("quick_actions", new DialogListDialog(
            new CommonDialogData(
                    Component.translatable("menu.quick_actions.title"),
                    Component.translatable("menu.quick_actions"),
                    true,
                    true,
                    DialogAction.CLOSE,
                    Collections.emptyList(),
                    Collections.emptyList()
            ),
            new MappedEntitySet<>(new ResourceLocation("quick_actions")),
            DEFAULT_BACK_BUTTON,
            1,
            310
    ));

    static {
        REGISTRY.unloadMappings();
    }
}
