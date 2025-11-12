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

package com.github.retrooper.packetevents.protocol.chat.clickevent;

import com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ClickEventActions {

    private static final VersionedRegistry<ClickEventAction<?>> REGISTRY = new VersionedRegistry<>("click_event_action");

    private ClickEventActions() {
    }

    @ApiStatus.Internal
    public static <T extends ClickEvent> ClickEventAction<T> define(
            String name, boolean allowFromServer,
            NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder
    ) {
        return REGISTRY.define(name, data -> new StaticClickEventAction<>(
                data, allowFromServer, decoder, encoder));
    }

    public static VersionedRegistry<ClickEventAction<?>> getRegistry() {
        return REGISTRY;
    }

    public static final ClickEventAction<OpenUrlClickEvent> OPEN_URL = define("open_url", true,
            OpenUrlClickEvent::decode, OpenUrlClickEvent::encode);
    public static final ClickEventAction<OpenFileClickEvent> OPEN_FILE = define("open_file", false,
            OpenFileClickEvent::decode, OpenFileClickEvent::encode);
    public static final ClickEventAction<RunCommandClickEvent> RUN_COMMAND = define("run_command", true,
            RunCommandClickEvent::decode, RunCommandClickEvent::encode);
    /**
     * Removed in 1.9
     */
    @ApiStatus.Obsolete
    public static final ClickEventAction<TwitchUserInfoClickEvent> TWITCH_USER_INFO = define("twitch_user_info", false,
            TwitchUserInfoClickEvent::decode, TwitchUserInfoClickEvent::encode);
    public static final ClickEventAction<SuggestCommandClickEvent> SUGGEST_COMMAND = define("suggest_command", true,
            SuggestCommandClickEvent::decode, SuggestCommandClickEvent::encode);

    /**
     * Added with 1.8
     */
    public static final ClickEventAction<ChangePageClickEvent> CHANGE_PAGE = define("change_page", true,
            ChangePageClickEvent::decode, ChangePageClickEvent::encode);

    /**
     * Added with 1.15
     */
    public static final ClickEventAction<CopyToClipboardClickEvent> COPY_TO_CLIPBOARD = define("copy_to_clipboard", true,
            CopyToClipboardClickEvent::decode, CopyToClipboardClickEvent::encode);

    /**
     * Added with 1.21.6
     */
    public static final ClickEventAction<ShowDialogClickEvent> SHOW_DIALOG = define("show_dialog", true,
            ShowDialogClickEvent::decode, ShowDialogClickEvent::encode);
    /**
     * Added with 1.21.6
     */
    public static final ClickEventAction<CustomClickEvent> CUSTOM = define("custom", true,
            CustomClickEvent::decode, CustomClickEvent::encode);

    static {
        REGISTRY.unloadMappings();
    }
}
