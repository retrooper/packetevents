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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.CONTENT;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.SENDER;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.Parameter.TARGET;
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

    public ChatTypeDecoration(NBTCompound nbt) {
        //Read the translation key
        this.translationKey = nbt.getStringTagValueOrDefault("translation_key", null);

        //Read parameters
        List<Parameter> temporaryList = new ArrayList<>();
        NBTList<NBTString> parametersTag = nbt.getStringListTagOrNull("parameters");
        if (parametersTag != null) {
            for (NBTString p : parametersTag.getTags()) {
                Parameter parameter = Parameter.valueByName(p.getValue().toUpperCase());
                if (parameter != null) {
                    temporaryList.add(parameter);
                }
            }
        }
        this.parameters = Collections.unmodifiableList(temporaryList);

        //Read style
        NBTCompound styleTag = nbt.getCompoundTagOrNull("style");
        if (styleTag != null) {
            //TODO Read style! Must we hard-code this or is there already a way?
            style = empty();
        }
        else {
            style = empty();
        }
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

    public String getTranslationKey() {
        return this.translationKey;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public Style getStyle() {
        return this.style;
    }

    public enum Parameter {
        SENDER, TARGET, CONTENT;

        @Nullable
        public static Parameter valueByName(String name) {
            try {
                return valueOf(name);
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
}

