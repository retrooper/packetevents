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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.Parsers.Parser;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Node {

    private byte flags;
    private List<Integer> children;
    private int redirectNodeIndex;
    private Optional<String> name;
    private Optional<Parser> parser;
    private Optional<List<Object>> properties;
    private Optional<ResourceLocation> suggestionsType;

    public Node(
            byte flags, List<Integer> children, int redirectNodeIndex, @Nullable String name,
            @Nullable Integer parserID, @Nullable List<Object> properties, @Nullable ResourceLocation suggestionsType
    ) {
        this(flags, children, redirectNodeIndex, name, parserID == null ? null : Parsers.getById(
                        PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), parserID),
                properties, suggestionsType);
    }

    public Node(
            byte flags, List<Integer> children, int redirectNodeIndex, @Nullable String name, @Nullable Parser parser,
            @Nullable List<Object> properties, @Nullable ResourceLocation suggestionsType
    ) {
        this.flags = flags;
        this.children = children;
        this.redirectNodeIndex = redirectNodeIndex;
        this.name = Optional.ofNullable(name);
        this.parser = Optional.ofNullable(parser);
        this.properties = Optional.ofNullable(properties);
        this.suggestionsType = Optional.ofNullable(suggestionsType);
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public List<Integer> getChildren() {
        return children;
    }

    public void setChildren(List<Integer> children) {
        this.children = children;
    }

    public int getRedirectNodeIndex() {
        return redirectNodeIndex;
    }

    public void setRedirectNodeIndex(int redirectNodeIndex) {
        this.redirectNodeIndex = redirectNodeIndex;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<Parser> getParser() {
        return this.parser;
    }

    public void setParser(Optional<Parser> parser) {
        this.parser = parser;
    }

    public Optional<Integer> getParserID() {
        return this.parser.map(parser -> parser.getId(PacketEvents.getAPI()
                .getServerManager().getVersion().toClientVersion()));
    }

    public void setParserID(Optional<Integer> parserID) {
        this.parser = parserID.map(id -> Parsers.getById(PacketEvents.getAPI()
                .getServerManager().getVersion().toClientVersion(), id));
    }

    public Optional<List<Object>> getProperties() {
        return properties;
    }

    public void setProperties(Optional<List<Object>> properties) {
        this.properties = properties;
    }

    public Optional<ResourceLocation> getSuggestionsType() {
        return suggestionsType;
    }

    public void setSuggestionsType(Optional<ResourceLocation> suggestionsType) {
        this.suggestionsType = suggestionsType;
    }
}
