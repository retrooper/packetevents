/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class WrapperCommonServerServerLinks<T extends WrapperCommonServerServerLinks<T>> extends PacketWrapper<T> {

    private List<ServerLink> links;

    public WrapperCommonServerServerLinks(PacketSendEvent event) {
        super(event);
    }

    public WrapperCommonServerServerLinks(PacketTypeCommon packetType, List<ServerLink> links) {
        super(packetType);
        this.links = links;
    }

    @Override
    public void read() {
        this.links = this.readList(ServerLink::read);
    }

    @Override
    public void write() {
        this.writeList(this.links, ServerLink::write);
    }

    @Override
    public void copy(T wrapper) {
        this.links = wrapper.getLinks();
    }

    public List<ServerLink> getLinks() {
        return this.links;
    }

    public void setLinks(List<ServerLink> links) {
        this.links = links;
    }

    public enum KnownType {
        BUG_REPORT,
        COMMUNITY_GUIDELINES,
        SUPPORT,
        STATUS,
        FEEDBACK,
        COMMUNITY,
        WEBSITE,
        FORUMS,
        NEWS,
        ANNOUNCEMENTS,
    }

    public static final class ServerLink {

        private final @Nullable KnownType knownType;
        private final @Nullable Component customType;
        private final String url;

        public ServerLink(@Nullable KnownType knownType, @Nullable Component customType, String url) {
            if ((knownType == null) == (customType == null)) {
                throw new IllegalStateException("Illegal state of both known type and "
                        + "custom type combined: " + knownType + " / " + customType);
            }

            this.knownType = knownType;
            this.customType = customType;
            this.url = url;
        }

        public static ServerLink read(PacketWrapper<?> wrapper) {
            KnownType knownType;
            Component customType;
            if (wrapper.readBoolean()) {
                knownType = wrapper.readEnum(KnownType.values());
                customType = null;
            } else {
                knownType = null;
                customType = wrapper.readComponent();
            }
            String url = wrapper.readString();
            return new ServerLink(knownType, customType, url);
        }

        public static void write(PacketWrapper<?> wrapper, ServerLink link) {
            if (link.getKnownType() != null) {
                wrapper.writeBoolean(true);
                wrapper.writeEnum(link.getKnownType());
            } else {
                assert link.getCustomType() != null;
                wrapper.writeBoolean(false);
                wrapper.writeComponent(link.getCustomType());
            }
            wrapper.writeString(link.getUrl());
        }

        public ServerLink(KnownType knownType, String url) {
            this(knownType, null, url);
        }

        public ServerLink(Component customType, String url) {
            this(null, customType, url);
        }

        public @Nullable KnownType getKnownType() {
            return this.knownType;
        }

        public @Nullable Component getCustomType() {
            return this.customType;
        }

        public String getUrl() {
            return this.url;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ServerLink)) return false;
            ServerLink that = (ServerLink) obj;
            if (this.knownType != that.knownType) return false;
            if (!Objects.equals(this.customType, that.customType)) return false;
            return this.url.equals(that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.knownType, this.customType, this.url);
        }

        @Override
        public String toString() {
            return "ServerLink{knownType=" + this.knownType + ", customType=" + this.customType + ", url='" + this.url + '\'' + '}';
        }
    }
}
