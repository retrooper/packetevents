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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.json.JSONComponentConstants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.nbt.api.BinaryTagHolder.binaryTagHolder;
import static net.kyori.adventure.text.Component.blockNBT;
import static net.kyori.adventure.text.Component.entityNBT;
import static net.kyori.adventure.text.Component.score;
import static net.kyori.adventure.text.Component.storageNBT;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.ClickEvent.clickEvent;
import static net.kyori.adventure.text.event.HoverEvent.ShowEntity.showEntity;
import static net.kyori.adventure.text.event.HoverEvent.ShowItem.showItem;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.COLOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.EXTRA;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.FONT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT_CONTENTS;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.INSERTION;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.KEYBIND;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_BLOCK;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_ENTITY;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_INTERPRET;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_STORAGE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE_NAME;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE_OBJECTIVE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SELECTOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SEPARATOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TEXT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE_FALLBACK;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE_WITH;

// inspired by adventure's gson component deserializer implementation
// this serialization logic is really hard to read and should definitely be cleaned up
public final class AdventureNBTSerialization {

    private static final TagType[] NBT_TYPES = TagType.values();
    private static final int DEPTH_LIMIT = 12;

    // more constants
    // decorations
    private static final String OBFUSCATED = "obfuscated";
    private static final String BOLD = "bold";
    private static final String STRIKETHROUGH = "strikethrough";
    private static final String UNDERLINED = "underlined";
    private static final String ITALIC = "italic";
    // click event actions
    private static final String OPEN_URL = "open_url";
    private static final String RUN_COMMAND = "run_command";
    private static final String SUGGEST_COMMAND = "suggest_command";
    private static final String CHANGE_PAGE = "change_page";
    private static final String COPY_TO_CLIPBOARD = "copy_to_clipboard";
    // hover event actions
    private static final String SHOW_TEXT = "show_text";
    private static final String SHOW_ITEM = "show_item";
    private static final String SHOW_ENTITY = "show_entity";
    // hover event show item
    private static final String ITEM_ID = "id";
    private static final String ITEM_COUNT = "count";
    private static final String ITEM_TAG = "tag";
    // hover event show entity
    private static final String ENTITY_TYPE = "type";
    private static final String ENTITY_ID = "id";
    private static final String ENTITY_NAME = "name";

    private AdventureNBTSerialization() {
    }

    private static TagType resolveNbtType(byte typeId) {
        if (typeId < 0 || typeId >= NBT_TYPES.length) {
            throw new IllegalStateException("Invalid nbt type id read: " + typeId);
        }
        TagType nbtType = NBT_TYPES[typeId];
        if (nbtType == null) {
            throw new IllegalStateException("Invalid nbt type id read: " + typeId);
        }
        return nbtType;
    }

    private static void requireType(TagType type, TagType wantedType) {
        if (type != wantedType) {
            throw new IllegalStateException("Expected nbt type " + wantedType + ", read " + type);
        }
    }

    private static void requireComponentType(TagType type) {
        if (type != TagType.STRING && type != TagType.COMPOUND) {
            throw new IllegalStateException("Expected nbt component type, read " + type);
        }
    }

    private static void requireState(boolean state) {
        if (!state) {
            throw new IllegalStateException();
        }
    }

    public static Style readStyle(Object byteBuf) throws IOException {
        return readStyle(new ByteBufInputStream(byteBuf));
    }

    public static Style readStyle(DataInput input) throws IOException {
        TagType type = resolveNbtType(input.readByte());
        return readStyle(input, type);
    }

    private static Style readStyle(DataInput input, TagType rootType) throws IOException {
        return readStyle(input, rootType, 0);
    }

    private static Style readStyle(DataInput input, TagType rootType, int depth) throws IOException {
        if (depth > DEPTH_LIMIT) {
            throw new RuntimeException("Depth limit reached while decoding style: " + depth + " > " + DEPTH_LIMIT);
        }
        if (rootType != TagType.COMPOUND) {
            throw new RuntimeException("Unsupported nbt tag type for style: " + rootType);
        }

        Style.Builder style = null;

        // read until end
        TagType type;
        while ((type = resolveNbtType(input.readByte())) != TagType.END) {
            String key = input.readUTF();

            if (style == null) {
                style = Style.style();
            }
            readStyle(style, key, type, input, depth);
        }

        return style == null ? Style.empty() : style.build();
    }

    @SuppressWarnings({"PatternValidation", "unchecked"}) // Key and HoverEvent
    private static void readStyle(Style.Builder style, String key, TagType type,
                                  DataInput input, int depth) throws IOException {
        switch (key) {
            case FONT:
                requireType(type, TagType.STRING);
                style.font(key(input.readUTF()));
                break;
            case COLOR:
                requireType(type, TagType.STRING);
                style.color(parseColor(input.readUTF()));
                break;
            case BOLD:
                requireType(type, TagType.BYTE);
                style.decoration(TextDecoration.BOLD, State.byBoolean(input.readBoolean()));
                break;
            case ITALIC:
                requireType(type, TagType.BYTE);
                style.decoration(TextDecoration.ITALIC, State.byBoolean(input.readBoolean()));
                break;
            case UNDERLINED:
                requireType(type, TagType.BYTE);
                style.decoration(TextDecoration.UNDERLINED, State.byBoolean(input.readBoolean()));
                break;
            case STRIKETHROUGH:
                requireType(type, TagType.BYTE);
                style.decoration(TextDecoration.STRIKETHROUGH, State.byBoolean(input.readBoolean()));
                break;
            case OBFUSCATED:
                requireType(type, TagType.BYTE);
                style.decoration(TextDecoration.OBFUSCATED, State.byBoolean(input.readBoolean()));
                break;
            case INSERTION:
                requireType(type, TagType.STRING);
                style.insertion(input.readUTF());
                break;
            case CLICK_EVENT:
                requireType(type, TagType.COMPOUND);

                ClickEvent.Action clickEventAction = null;
                String clickEventValue = null;

                TagType clickType;
                while ((clickType = resolveNbtType(input.readByte())) != TagType.END) {
                    String clickKey = input.readUTF();
                    switch (clickKey) {
                        case CLICK_EVENT_ACTION:
                            requireType(clickType, TagType.STRING);
                            requireState(clickEventAction == null);

                            String actionId = input.readUTF();
                            switch (actionId) {
                                case OPEN_URL:
                                    clickEventAction = ClickEvent.Action.OPEN_URL;
                                    break;
                                case RUN_COMMAND:
                                    clickEventAction = ClickEvent.Action.RUN_COMMAND;
                                    break;
                                case SUGGEST_COMMAND:
                                    clickEventAction = ClickEvent.Action.SUGGEST_COMMAND;
                                    break;
                                case CHANGE_PAGE:
                                    clickEventAction = ClickEvent.Action.CHANGE_PAGE;
                                    break;
                                case COPY_TO_CLIPBOARD:
                                    clickEventAction = ClickEvent.Action.COPY_TO_CLIPBOARD;
                                    break;
                                default:
                                    throw new IllegalStateException("Illegal click event action read: '" + actionId + "'");
                            }
                            break;
                        case CLICK_EVENT_VALUE:
                            requireType(clickType, TagType.STRING);
                            requireState(clickEventValue == null);
                            clickEventValue = input.readUTF();
                            break;
                        default:
                            throw new IllegalStateException("Illegal click event nbt key read: '" + clickKey + "'");
                    }
                }
                requireState(clickEventAction != null && clickEventValue != null);
                style.clickEvent(clickEvent(clickEventAction, clickEventValue));
                break;
            case HOVER_EVENT:
                requireType(type, TagType.COMPOUND);

                HoverEvent.Action<?> hoverEventAction = null;
                Object hoverEventContents = null;

                TagType hoverType;
                while ((hoverType = resolveNbtType(input.readByte())) != TagType.END) {
                    String hoverKey = input.readUTF();
                    switch (hoverKey) {
                        case HOVER_EVENT_ACTION:
                            requireType(hoverType, TagType.STRING);
                            requireState(hoverEventAction == null);

                            String actionId = input.readUTF();
                            switch (actionId) {
                                case SHOW_TEXT:
                                    hoverEventAction = HoverEvent.Action.SHOW_TEXT;
                                    break;
                                case SHOW_ITEM:
                                    hoverEventAction = HoverEvent.Action.SHOW_ITEM;
                                    break;
                                case SHOW_ENTITY:
                                    hoverEventAction = HoverEvent.Action.SHOW_ENTITY;
                                    break;
                                default:
                                    throw new IllegalStateException("Illegal hover event action read: '" + actionId + "'");
                            }
                            break;
                        case HOVER_EVENT_CONTENTS:
                            requireState(hoverEventContents == null);
                            requireState(hoverEventAction != null);

                            switch (hoverEventAction.toString()) {
                                case SHOW_TEXT:
                                    requireComponentType(hoverType);
                                    hoverEventContents = readComponent(input, hoverType, depth + 1);
                                    break;
                                case SHOW_ITEM:
                                    if (hoverType == TagType.STRING) {
                                        String itemId = input.readUTF();
                                        hoverEventContents = showItem(key(itemId), 1);
                                    } else {
                                        requireType(hoverType, TagType.COMPOUND);

                                        String itemId = null;
                                        int count = 1;
                                        String tag = null;

                                        TagType itemType;
                                        while ((itemType = resolveNbtType(input.readByte())) != TagType.END) {
                                            String itemKey = input.readUTF();
                                            switch (itemKey) {
                                                case ITEM_ID:
                                                    requireType(itemType, TagType.STRING);
                                                    requireState(itemId == null);
                                                    itemId = input.readUTF();
                                                    break;
                                                case ITEM_COUNT:
                                                    requireType(itemType, TagType.INT);
                                                    count = input.readInt();
                                                    break;
                                                case ITEM_TAG:
                                                    requireType(itemType, TagType.STRING);
                                                    tag = input.readUTF();
                                                    break;
                                            }
                                        }

                                        requireState(itemId != null);
                                        hoverEventContents = showItem(key(itemId), count,
                                                tag == null ? null : binaryTagHolder(tag));
                                    }
                                    break;
                                case SHOW_ENTITY:
                                    requireType(hoverType, TagType.COMPOUND);

                                    String entityType = null;
                                    UUID entityId = null;
                                    Component entityName = null;

                                    TagType itemType;
                                    while ((itemType = resolveNbtType(input.readByte())) != TagType.END) {
                                        String itemKey = input.readUTF();
                                        switch (itemKey) {
                                            case ENTITY_TYPE:
                                                requireType(itemType, TagType.STRING);
                                                requireState(entityType == null);
                                                entityType = input.readUTF();
                                                break;
                                            case ENTITY_ID:
                                                requireType(itemType, TagType.INT_ARRAY);
                                                requireState(entityId == null);
                                                entityId = readUniqueId(input);
                                                break;
                                            case ENTITY_NAME:
                                                requireComponentType(itemType);
                                                requireState(entityName == null);
                                                entityName = readComponent(input, itemType, depth + 1);
                                                break;
                                        }
                                    }

                                    requireState(entityType != null && entityId != null);
                                    hoverEventContents = showEntity(key(entityType), entityId, entityName);
                                    break;
                            }
                            break;
                        default:
                            throw new IllegalStateException("Illegal hover event nbt key read: '" + hoverKey + "'");
                    }
                }
                requireState(hoverEventContents != null);

                // this is not unchecked, as it will 100% never fail - validated while reading
                HoverEvent.Action<? super Object> unsafeAction = (HoverEvent.Action<? super Object>) hoverEventAction;
                style.hoverEvent(HoverEvent.hoverEvent(unsafeAction, hoverEventContents));
                break;
            default:
                throw new IllegalStateException("Illegal component nbt key read: '" + key + "'");
        }
    }

    public static void writeStyle(Object byteBuf, Style style) throws IOException {
        writeStyle(new ByteBufOutputStream(byteBuf), style);
    }

    public static void writeStyle(DataOutput output, Style style) throws IOException {
        TagType tagType = TagType.COMPOUND;
        output.writeByte(tagType.getId());
        writeStyle(output, style, tagType);
        output.writeByte(TagType.END.getId()); // ends style tag
    }

    private static void writeStyle(DataOutput output, Style style, TagType rootType) throws IOException {
        if (rootType != TagType.COMPOUND) {
            throw new UnsupportedEncodingException();
        }
        if (style.isEmpty()) {
            return;
        }

        // font
        Key font = style.font();
        if (font != null) {
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(FONT);
            output.writeUTF(font.asString());
        }

        // color
        TextColor color = style.color();
        if (color != null) {
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(COLOR);
            output.writeUTF(stringifyColor(color));
        }

        // bold
        State bold = style.decoration(TextDecoration.BOLD);
        if (bold != State.NOT_SET) {
            output.writeByte(TagType.BYTE.getId());
            output.writeUTF(BOLD);
            output.writeBoolean(bold == State.TRUE);
        }
        // italic
        State italic = style.decoration(TextDecoration.ITALIC);
        if (italic != State.NOT_SET) {
            output.writeByte(TagType.BYTE.getId());
            output.writeUTF(ITALIC);
            output.writeBoolean(italic == State.TRUE);
        }
        // underlined
        State underlined = style.decoration(TextDecoration.UNDERLINED);
        if (underlined != State.NOT_SET) {
            output.writeByte(TagType.BYTE.getId());
            output.writeUTF(UNDERLINED);
            output.writeBoolean(underlined == State.TRUE);
        }
        // strikethrough
        State strikethrough = style.decoration(TextDecoration.STRIKETHROUGH);
        if (strikethrough != State.NOT_SET) {
            output.writeByte(TagType.BYTE.getId());
            output.writeUTF(STRIKETHROUGH);
            output.writeBoolean(strikethrough == State.TRUE);
        }
        // obfuscated
        State obfuscated = style.decoration(TextDecoration.OBFUSCATED);
        if (obfuscated != State.NOT_SET) {
            output.writeByte(TagType.BYTE.getId());
            output.writeUTF(OBFUSCATED);
            output.writeBoolean(obfuscated == State.TRUE);
        }

        // insertion
        String insertion = style.insertion();
        if (insertion != null) {
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(INSERTION);
            output.writeUTF(insertion);
        }

        // click event
        ClickEvent clickEvent = style.clickEvent();
        if (clickEvent != null) {
            // nested compound
            output.writeByte(TagType.COMPOUND.getId());
            output.writeUTF(CLICK_EVENT);

            // click event action
            ClickEvent.Action action = clickEvent.action();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(CLICK_EVENT_ACTION);
            output.writeUTF(action.toString());

            // click event value
            String value = clickEvent.value();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(CLICK_EVENT_VALUE);
            output.writeUTF(value);

            output.writeByte(TagType.END.getId()); // ends compound
        }

        // hover event
        HoverEvent<?> hoverEvent = style.hoverEvent();
        if (hoverEvent != null) {
            // nested compound
            output.writeByte(TagType.COMPOUND.getId());
            output.writeUTF(HOVER_EVENT);

            // hover event action
            HoverEvent.Action<?> action = hoverEvent.action();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(HOVER_EVENT_ACTION);
            output.writeUTF(action.toString());

            // hover event contents
            switch (action.toString()) {
                case SHOW_TEXT:
                    Component text = (Component) hoverEvent.value();
                    TagType textTagType = getComponentTagType(text);
                    output.writeByte(textTagType.getId());
                    output.writeUTF(HOVER_EVENT_CONTENTS);
                    writeComponent(output, text, textTagType);
                    break;
                case SHOW_ITEM:
                    HoverEvent.ShowItem item = (HoverEvent.ShowItem) hoverEvent.value();
                    Key itemId = item.item();
                    int count = item.count();
                    BinaryTagHolder nbt = item.nbt();

                    if (count == 1 && nbt == null) {
                        output.writeByte(TagType.STRING.getId());
                        output.writeUTF(HOVER_EVENT_CONTENTS);
                        output.writeUTF(itemId.asString());
                    } else {
                        // nested compound
                        output.writeByte(TagType.COMPOUND.getId());
                        output.writeUTF(HOVER_EVENT_CONTENTS);

                        // item id
                        output.writeByte(TagType.STRING.getId());
                        output.writeUTF(ITEM_ID);
                        output.writeUTF(itemId.asString());

                        // item count
                        if (count != 1) {
                            output.writeByte(TagType.INT.getId());
                            output.writeUTF(ITEM_COUNT);
                            output.writeInt(count);
                        }

                        // item nbt
                        if (nbt != null) {
                            output.writeByte(TagType.STRING.getId());
                            output.writeUTF(ITEM_TAG);
                            output.writeUTF(nbt.string());
                        }

                        output.writeByte(TagType.END.getId()); // ends compound
                    }
                    break;
                case SHOW_ENTITY:
                    HoverEvent.ShowEntity entity = (HoverEvent.ShowEntity) hoverEvent.value();
                    Key entityType = entity.type();
                    UUID entityId = entity.id();
                    Component entityName = entity.name();

                    // nested compound
                    output.writeByte(TagType.COMPOUND.getId());
                    output.writeUTF(HOVER_EVENT_CONTENTS);

                    // entity type
                    output.writeByte(TagType.STRING.getId());
                    output.writeUTF(ENTITY_TYPE);
                    output.writeUTF(entityType.asString());

                    // entity uuid
                    output.writeByte(TagType.INT_ARRAY.getId());
                    output.writeUTF(ENTITY_ID);
                    writeUniqueId(output, entityId);

                    // entity name
                    if (entityName != null) {
                        TagType nameTagType = getComponentTagType(entityName);
                        output.writeByte(nameTagType.getId());
                        output.writeUTF(ENTITY_NAME);
                        writeComponent(output, entityName, nameTagType);
                    }

                    output.writeByte(TagType.END.getId()); // ends compound
                    break;
            }

            output.writeByte(TagType.END.getId()); // ends compound
        }
    }

    public static Component readComponent(Object byteBuf) throws IOException {
        return readComponent(new ByteBufInputStream(byteBuf));
    }

    public static Component readComponent(DataInput input) throws IOException {
        TagType type = resolveNbtType(input.readByte());
        return readComponent(input, type);
    }

    private static Component readComponent(DataInput input, TagType rootType) throws IOException {
        return readComponent(input, rootType, 0);
    }

    @SuppressWarnings({"PatternValidation"}) // Key
    private static Component readComponent(DataInput input, TagType rootType, int depth) throws IOException {
        if (depth > DEPTH_LIMIT) {
            throw new RuntimeException("Depth limit reached while decoding component: " + depth + " > " + DEPTH_LIMIT);
        }

        if (rootType == TagType.STRING) {
            return text(input.readUTF());
        }
        if (rootType != TagType.COMPOUND) {
            throw new RuntimeException("Unsupported nbt tag type for component: " + rootType);
        }

        // component parts
        List<Component> extra = null;
        String text = null;
        String translate = null;
        String translateFallback = null;
        List<Component> translateWith = null;
        String scoreName = null;
        String scoreObjective = null;
        String selector = null;
        String keybind = null;
        String nbt = null;
        boolean nbtInterpret = false;
        String nbtBlock = null;
        String nbtEntity = null;
        String nbtStorage = null;
        Component separator = null;

        Style.Builder style = null;

        // read until end
        TagType type;
        while ((type = resolveNbtType(input.readByte())) != TagType.END) {
            String key = input.readUTF();
            switch (key) {
                // this case resolves an edge-case where a simple component would
                // be serialized to an unnamed compound because of nbt lists
                case "":
                case TEXT:
                    requireType(type, TagType.STRING);
                    requireState(text == null);
                    text = input.readUTF();
                    break;
                case TRANSLATE:
                    requireType(type, TagType.STRING);
                    requireState(translate == null);
                    translate = input.readUTF();
                    break;
                case TRANSLATE_FALLBACK:
                    requireType(type, TagType.STRING);
                    requireState(translateFallback == null);
                    translateFallback = input.readUTF();
                    break;
                case TRANSLATE_WITH:
                    requireType(type, TagType.LIST);
                    requireState(translateWith == null);
                    translateWith = readComponentList(input, depth + 1);
                    break;
                case SCORE:
                    requireType(type, TagType.COMPOUND);
                    requireState(scoreName == null && scoreObjective == null);

                    TagType scoreType;
                    while ((scoreType = resolveNbtType(input.readByte())) != TagType.END) {
                        String scoreKey = input.readUTF();
                        switch (scoreKey) {
                            case SCORE_NAME:
                                requireType(scoreType, TagType.STRING);
                                requireState(scoreName == null);
                                scoreName = input.readUTF();
                                break;
                            case SCORE_OBJECTIVE:
                                requireType(scoreType, TagType.STRING);
                                requireState(scoreObjective == null);
                                scoreObjective = input.readUTF();
                                break;
                            default:
                                throw new IllegalStateException("Invalid nbt key read for score key: '" + scoreKey + "'");
                        }
                    }
                    requireState(scoreName != null && scoreObjective != null);
                    break;
                case SELECTOR:
                    requireType(type, TagType.STRING);
                    requireState(selector == null);
                    selector = input.readUTF();
                    break;
                case KEYBIND:
                    requireType(type, TagType.STRING);
                    requireState(keybind == null);
                    keybind = input.readUTF();
                    break;
                case JSONComponentConstants.NBT:
                    requireType(type, TagType.STRING);
                    requireState(nbt == null);
                    nbt = input.readUTF();
                    break;
                case NBT_INTERPRET:
                    requireType(type, TagType.BYTE);
                    nbtInterpret = input.readBoolean();
                    break;
                case NBT_BLOCK:
                    requireType(type, TagType.STRING);
                    requireState(nbtBlock == null);
                    nbtBlock = input.readUTF();
                    break;
                case NBT_ENTITY:
                    requireType(type, TagType.STRING);
                    requireState(nbtEntity == null);
                    nbtEntity = input.readUTF();
                    break;
                case NBT_STORAGE:
                    requireType(type, TagType.STRING);
                    requireState(nbtStorage == null);
                    nbtStorage = input.readUTF();
                    break;
                case EXTRA:
                    requireType(type, TagType.LIST);
                    requireState(extra == null);
                    extra = readComponentList(input, depth + 1);
                    break;
                case SEPARATOR:
                    requireComponentType(type);
                    requireState(separator == null);
                    separator = readComponent(input, type, depth + 1);
                    break;
                default:
                    if (style == null) {
                        style = Style.style();
                    }
                    readStyle(style, key, type, input, depth);
                    break;
            }
        }

        // build component from read values
        ComponentBuilder<?, ?> builder;
        if (text != null) {
            builder = text().content(text);
        } else if (translate != null) {
            if (translateWith != null) {
                builder = translatable().key(translate).fallback(translateFallback).args(translateWith);
            } else {
                builder = translatable().key(translate).fallback(translateFallback);
            }
        } else if (scoreName != null && scoreObjective != null) {
            builder = score().name(scoreName).objective(scoreObjective);
        } else if (selector != null) {
            builder = Component.selector().pattern(selector).separator(separator);
        } else if (keybind != null) {
            builder = Component.keybind().keybind(keybind);
        } else if (nbt != null) {
            if (nbtBlock != null) {
                builder = blockNBT()
                        .nbtPath(nbt).interpret(nbtInterpret).separator(separator)
                        .pos(BlockNBTComponent.Pos.fromString(nbtBlock));
            } else if (nbtEntity != null) {
                builder = entityNBT()
                        .nbtPath(nbt).interpret(nbtInterpret).separator(separator)
                        .selector(nbtEntity);
            } else if (nbtStorage != null) {
                builder = storageNBT()
                        .nbtPath(nbt).interpret(nbtInterpret).separator(separator)
                        .storage(key(nbtStorage));
            } else {
                throw new IllegalStateException("Illegal nbt component, block/entity/storage is missing");
            }
        } else {
            throw new IllegalStateException("Illegal nbt component, component type could not be determined");
        }

        if (style != null) {
            builder.style(style.build());
        }

        if (extra != null) {
            builder.append(extra);
        }
        return builder.build();
    }

    public static void writeComponent(Object byteBuf, Component component) throws IOException {
        writeComponent(new ByteBufOutputStream(byteBuf), component);
    }

    public static void writeComponent(DataOutput output, Component component) throws IOException {
        TagType tagType = getComponentTagType(component);
        output.writeByte(tagType.getId());
        writeComponent(output, component, tagType);
    }

    private static void writeComponent(DataOutput output, Component component, TagType rootType) throws IOException {
        if (rootType == TagType.STRING) {
            output.writeUTF(((TextComponent) component).content());
            return;
        }
        if (rootType != TagType.COMPOUND) {
            throw new UnsupportedEncodingException();
        }

        // component parts
        if (component instanceof TextComponent) {
            // text content
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(TEXT);
            output.writeUTF(((TextComponent) component).content());
        } else if (component instanceof TranslatableComponent) {
            // translation key
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(TRANSLATE);
            output.writeUTF(((TranslatableComponent) component).key());

            // translation fallback
            String fallback = ((TranslatableComponent) component).fallback();
            if (fallback != null) {
                output.writeByte(TagType.STRING.getId());
                output.writeUTF(TRANSLATE_FALLBACK);
                output.writeUTF(fallback);
            }

            // translation arguments
            List<Component> args = ((TranslatableComponent) component).args();
            if (!args.isEmpty()) {
                output.writeByte(TagType.LIST.getId());
                output.writeUTF(TRANSLATE_WITH);
                writeComponentList(output, args);
            }
        } else if (component instanceof ScoreComponent) {
            // nested compound
            output.writeByte(TagType.COMPOUND.getId());
            output.writeUTF(SCORE);

            // score name
            String scoreName = ((ScoreComponent) component).name();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(SCORE_NAME);
            output.writeUTF(scoreName);

            // score objective
            String scoreObjective = ((ScoreComponent) component).objective();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(SCORE_OBJECTIVE);
            output.writeUTF(scoreObjective);

            output.writeByte(TagType.END.getId()); // ends compound
        } else if (component instanceof SelectorComponent) {
            // selector
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(SELECTOR);
            output.writeUTF(((SelectorComponent) component).pattern());

            // separator
            Component separator = ((SelectorComponent) component).separator();
            if (separator != null) {
                TagType componentTagType = getComponentTagType(separator);
                output.writeByte(componentTagType.getId());
                output.writeUTF(SEPARATOR);
                writeComponent(output, separator, componentTagType);
            }
        } else if (component instanceof KeybindComponent) {
            // keybind
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(KEYBIND);
            output.writeUTF(((KeybindComponent) component).keybind());
        } else if (component instanceof NBTComponent<?, ?>) {
            // nbt path
            String nbtPath = ((NBTComponent<?, ?>) component).nbtPath();
            output.writeByte(TagType.STRING.getId());
            output.writeUTF(JSONComponentConstants.NBT);
            output.writeUTF(nbtPath);

            // interpret
            boolean interpret = ((NBTComponent<?, ?>) component).interpret();
            if (interpret) { // defaults to true
                output.writeByte(TagType.BYTE.getId());
                output.writeUTF(NBT_INTERPRET);
                output.writeBoolean(true);
            }

            // separator
            Component separator = ((NBTComponent<?, ?>) component).separator();
            if (separator != null) {
                TagType componentTagType = getComponentTagType(separator);
                output.writeByte(componentTagType.getId());
                output.writeUTF(SEPARATOR);
                writeComponent(output, separator, componentTagType);
            }

            if (component instanceof BlockNBTComponent) {
                // nbt block
                BlockNBTComponent.Pos pos = ((BlockNBTComponent) component).pos();
                output.writeByte(TagType.STRING.getId());
                output.writeUTF(NBT_BLOCK);
                output.writeUTF(pos.asString());
            } else if (component instanceof EntityNBTComponent) {
                // nbt entity
                String selector = ((EntityNBTComponent) component).selector();
                output.writeByte(TagType.STRING.getId());
                output.writeUTF(NBT_ENTITY);
                output.writeUTF(selector);
            } else if (component instanceof StorageNBTComponent) {
                // nbt storage key
                Key storage = ((StorageNBTComponent) component).storage();
                output.writeByte(TagType.STRING.getId());
                output.writeUTF(NBT_STORAGE);
                output.writeUTF(storage.asString());
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }

        if (component.hasStyling()) {
            writeStyle(output, component.style(), TagType.COMPOUND);
        }

        // component children
        List<Component> children = component.children();
        if (!children.isEmpty()) {
            output.writeByte(TagType.LIST.getId());
            output.writeUTF(EXTRA);
            writeComponentList(output, children);
        }

        output.writeByte(TagType.END.getId()); // ends compound
    }

    private static List<Component> readComponentList(DataInput input, int depth) throws IOException {
        byte typeId = input.readByte();
        int length = input.readInt();
        if (typeId == TagType.END.getId() && length > 0) {
            throw new IllegalStateException("Non-empty list with no specified type read");
        }

        TagType type = resolveNbtType(typeId);
        requireComponentType(type);
        if (length == 0) {
            return Collections.emptyList();
        }

        List<Component> components = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            components.add(readComponent(input, type, depth));
        }
        return components;
    }

    private static void writeComponentList(DataOutput output, List<Component> components) throws IOException {
        if (components.isEmpty()) {
            output.writeByte(TagType.END.getId());
            output.writeInt(0);
            return;
        }

        boolean simple = true;
        for (Component component : components) {
            if (!isSimpleComponent(component)) {
                simple = false;
                break;
            }
        }

        TagType tagType = getComponentTagType(simple);
        output.writeByte(tagType.getId());
        output.writeInt(components.size());
        for (Component component : components) {
            writeComponent(output, component, tagType);
        }
    }

    private static UUID readUniqueId(DataInput input) throws IOException {
        // decoded from an int array

        int arrayLength = input.readInt();
        if (arrayLength != 4) {
            throw new IllegalStateException("Invalid encoded uuid length: " + arrayLength + " != 4");
        }
        return new UUID(
                (long) input.readInt() << 32 | (long) input.readInt() & 0xFFFFFFFFL,
                (long) input.readInt() << 32 | (long) input.readInt() & 0xFFFFFFFFL
        );
    }

    private static void writeUniqueId(DataOutput output, UUID uniqueId) throws IOException {
        // encoded as an int array

        long mostBits = uniqueId.getMostSignificantBits();
        long leastBits = uniqueId.getLeastSignificantBits();

        output.writeInt(4); // length
        output.writeInt((int) (mostBits >> 32));
        output.writeInt((int) mostBits);
        output.writeInt((int) (leastBits >> 32));
        output.writeInt((int) leastBits);
    }

    private static TagType getComponentTagType(Component component) {
        return getComponentTagType(isSimpleComponent(component));
    }

    private static TagType getComponentTagType(boolean simple) {
        return simple ? TagType.STRING : TagType.COMPOUND;
    }

    private static boolean isSimpleComponent(Component component) {
        return component instanceof TextComponent
                && !component.hasStyling()
                && component.children().isEmpty();
    }

    private static TextColor parseColor(String colorStr) {
        if (colorStr.isEmpty()) {
            throw new IllegalStateException("Tried parsing empty color string");
        }

        TextColor color;
        if (colorStr.charAt(0) == '#') {
            color = TextColor.fromHexString(colorStr);
        } else {
            color = NamedTextColor.NAMES.value(colorStr);
        }

        if (color == null) {
            throw new IllegalStateException("Can't parse color from: " + colorStr);
        }
        return color;
    }

    private static String stringifyColor(TextColor color) {
        if (color instanceof NamedTextColor) {
            return color.toString();
        }
        return color.asHexString();
    }

    private enum TagType {
        END,
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BYTE_ARRAY,
        STRING,
        LIST,
        COMPOUND,
        INT_ARRAY,
        LONG_ARRAY;

        public int getId() {
            return this.ordinal();
        }
    }
}
