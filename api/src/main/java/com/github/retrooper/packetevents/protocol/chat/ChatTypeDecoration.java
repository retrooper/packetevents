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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.CONTENT;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.SENDER;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.TARGET;
import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;
import static java.util.Arrays.asList;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.Style.empty;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class ChatTypeDecoration {

    private final String translationKey;
    private final List<Parameter> parameters;
    private final Style style;

    public ChatTypeDecoration(String translationKey, List<Parameter> parameters, Style style) {
        this.translationKey = translationKey;
        this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
        this.style = style;
    }

    public static ChatTypeDecoration read(PacketWrapper<?> wrapper) {
        String translationKey = wrapper.readString();
        List<Parameter> parameters = wrapper.readList(ew -> ew.readEnum(Parameter.values()));
        Style style = wrapper.readStyle();
        return new ChatTypeDecoration(translationKey, parameters, style);
    }

    public static void write(PacketWrapper<?> wrapper, ChatTypeDecoration decoration) {
        wrapper.writeString(decoration.translationKey);
        wrapper.writeList(decoration.parameters, PacketWrapper::writeEnum);
        wrapper.writeStyle(decoration.style);
    }

    public static ChatTypeDecoration decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;
        String translationKey = compound.getStringTagValueOrThrow("translation_key");
        NBT paramsTag = compound.getTagOrThrow("parameters");
        List<Parameter> params;
        if (paramsTag instanceof NBTList<?>) {
            params = new ArrayList<>();
            NBTList<?> paramsTagList = (NBTList<?>) paramsTag;
            for (NBT paramTag : paramsTagList.getTags()) {
                String paramId = ((NBTString) paramTag).getValue();
                params.add(indexValueOrThrow(Parameter.ID_INDEX, paramId));
            }
        } else {
            String paramId = ((NBTString) paramsTag).getValue();
            params = Collections.singletonList(indexValueOrThrow(Parameter.ID_INDEX, paramId));
        }
        NBTCompound styleTag = compound.getCompoundTagOrNull("style");
        Style style = styleTag == null ? empty() :
                AdventureSerializer.getNBTSerializer().deserializeStyle(styleTag);
        return new ChatTypeDecoration(translationKey, params, style);
    }

    public static NBT encode(ChatTypeDecoration decoration, ClientVersion version) {
        NBTList<NBTString> paramsTag = NBTList.createStringList();
        for (Parameter param : decoration.parameters) {
            paramsTag.addTag(new NBTString(param.getId()));
        }

        NBTCompound compound = new NBTCompound();
        compound.setTag("translation_key", new NBTString(decoration.translationKey));
        compound.setTag("parameters", paramsTag);
        if (!decoration.style.isEmpty()) {
            compound.setTag("style", AdventureSerializer.getNBTSerializer()
                    .serializeStyle(decoration.style));
        }
        return compound;
    }

    public static ChatTypeDecoration withSender(String translationKey) {
        return new ChatTypeDecoration(translationKey, asList(SENDER, CONTENT), empty());
    }

    public static ChatTypeDecoration incomingDirectMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, asList(SENDER, CONTENT),
                style(GRAY, ITALIC));
    }

    public static ChatTypeDecoration outgoingDirectMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, asList(TARGET, CONTENT),
                style(GRAY, ITALIC));
    }

    public static ChatTypeDecoration teamMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, asList(TARGET, SENDER, CONTENT), empty());
    }

    public Component decorate(Component component, ChatType.Bound chatType) {
        ComponentLike[] components = new ComponentLike[this.parameters.size()];
        for (int i = 0; i < components.length; i++) {
            Parameter parameter = this.parameters.get(i);
            components[i] = parameter.selector.apply(component, chatType);
        }
        return Component.translatable(this.translationKey, null, this.style, components);
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public Style getStyle() {
        return this.style;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatTypeDecoration)) return false;
        ChatTypeDecoration that = (ChatTypeDecoration) obj;
        if (!this.translationKey.equals(that.translationKey)) return false;
        if (!this.parameters.equals(that.parameters)) return false;
        return this.style.equals(that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.translationKey, this.parameters, this.style);
    }

    public enum Parameter {

        SENDER("sender", (component, type) -> type.getName()),
        TARGET("target", (component, type) -> type.getTargetName() != null
                ? type.getTargetName() : Component.empty()),
        CONTENT("content", (component, type) -> component);

        public static final Index<String, Parameter> ID_INDEX = Index.create(
                Parameter.class, Parameter::getId);

        private final String id;
        private final BiFunction<Component, ChatType.Bound, Component> selector;

        Parameter(String id, BiFunction<Component, ChatType.Bound, Component> selector) {
            this.id = id;
            this.selector = selector;
        }

        public String getId() {
            return this.id;
        }

        @Deprecated
        public static @Nullable Parameter valueByName(String id) {
            return ID_INDEX.value(id);
        }
    }
}

