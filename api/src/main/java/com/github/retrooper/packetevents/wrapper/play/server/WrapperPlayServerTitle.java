/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class WrapperPlayServerTitle extends PacketWrapper<WrapperPlayServerTitle> {

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private TitleAction action;
    @Nullable
    private Component title;
    @Nullable
    private Component subtitle;
    @Nullable
    private Component actionBar;

    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;

    public WrapperPlayServerTitle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTitle(TitleAction action, @Nullable Component title, @Nullable Component subtitle,
                                  @Nullable Component actionBar, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        super(PacketType.Play.Server.TITLE);
        this.action = action;
        this.title = title;
        this.subtitle = subtitle;
        this.actionBar = actionBar;
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    @Deprecated
    public WrapperPlayServerTitle(TitleAction action, @Nullable String titleJson, @Nullable String subtitleJson,
                                  @Nullable String actionBarJson, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        this(action, AdventureSerializer.parseComponent(titleJson), AdventureSerializer.parseComponent(subtitleJson),
                AdventureSerializer.parseComponent(actionBarJson), fadeInTicks, stayTicks, fadeOutTicks);
    }

    @Override
    public void read() {
        boolean modern = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11);
        int id = readVarInt();
        if (modern) {
            action = TitleAction.fromId(id);
        } else {
            action = TitleAction.fromLegacyId(id);
        }
        switch (Objects.requireNonNull(action)) {
            case SET_TITLE:
                title = readComponent();
                break;
            case SET_SUBTITLE:
                subtitle = readComponent();
                break;
            case SET_ACTION_BAR:
                actionBar = readComponent();
                break;
            case SET_TIMES_AND_DISPLAY:
                fadeInTicks = readInt();
                stayTicks = readInt();
                fadeOutTicks = readInt();
                break;
        }
    }

    @Override
    public void copy(WrapperPlayServerTitle wrapper) {
        action = wrapper.action;
        title = wrapper.title;
        subtitle = wrapper.subtitle;
        actionBar = wrapper.actionBar;
        fadeInTicks = wrapper.fadeInTicks;
        stayTicks = wrapper.stayTicks;
        fadeOutTicks = wrapper.fadeOutTicks;
    }

    @Override
    public void write() {
        boolean modern = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11);
        int id = modern ? action.getId() : action.getLegacyId();
        writeVarInt(id);
        switch (action) {
            case SET_TITLE:
                writeComponent(title);
                break;
            case SET_SUBTITLE:
                writeComponent(subtitle);
                break;
            case SET_ACTION_BAR:
                writeComponent(actionBar);
                break;
            case SET_TIMES_AND_DISPLAY:
                writeInt(fadeInTicks);
                writeInt(stayTicks);
                writeInt(fadeOutTicks);
                break;
        }
    }

    public TitleAction getAction() {
        return action;
    }

    public void setAction(TitleAction action) {
        this.action = action;
    }

    public @Nullable Component getTitle() {
        return title;
    }

    public void setTitle(@Nullable Component title) {
        this.title = title;
    }

    @Deprecated
    public @Nullable String getTitleJson() {
        return AdventureSerializer.toJson(this.getTitle());
    }

    @Deprecated
    public void setTitleJson(@Nullable String titleJson) {
        this.setTitle(AdventureSerializer.parseComponent(titleJson));
    }

    public @Nullable Component getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable Component subtitle) {
        this.subtitle = subtitle;
    }

    @Deprecated
    public @Nullable String getSubtitleJson() {
        return AdventureSerializer.toJson(this.getSubtitle());
    }

    @Deprecated
    public void setSubtitleJson(@Nullable String subtitleJson) {
        this.setSubtitle(AdventureSerializer.parseComponent(subtitleJson));
    }

    public @Nullable Component getActionBar() {
        return actionBar;
    }

    public void setActionBar(@Nullable Component actionBar) {
        this.actionBar = actionBar;
    }

    @Deprecated
    public @Nullable String getActionBarJson() {
        return AdventureSerializer.toJson(this.getActionBar());
    }

    @Deprecated
    public void setActionBarJson(@Nullable String actionBarJson) {
        this.setActionBar(AdventureSerializer.parseComponent(actionBarJson));
    }

    public int getFadeInTicks() {
        return fadeInTicks;
    }

    public void setFadeInTicks(int fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
    }

    public int getStayTicks() {
        return stayTicks;
    }

    public void setStayTicks(int stayTicks) {
        this.stayTicks = stayTicks;
    }

    public int getFadeOutTicks() {
        return fadeOutTicks;
    }

    public void setFadeOutTicks(int fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
    }

    public enum TitleAction {
        SET_TITLE(0),
        SET_SUBTITLE(1),
        SET_ACTION_BAR,
        SET_TIMES_AND_DISPLAY(2),
        HIDE(3),
        RESET(4);
        private final int legacyId;

        TitleAction() {
            this(-1);
        }

        TitleAction(int legacyId) {
            this.legacyId = legacyId;
        }

        public static TitleAction fromId(int id) {
            return values()[id];
        }

        public static TitleAction fromLegacyId(int legacyId) {
            for (TitleAction action : values()) {
                if (action.legacyId == legacyId) {
                    return action;
                }
            }
            return null;
        }

        public int getId() {
            return ordinal();
        }

        public int getLegacyId() {
            return legacyId;
        }
    }
}
