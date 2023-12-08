/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// this file is a mess, but it works
public final class AdventureNBTSerializer {

    public static Component asComponent(NBT nbt) {
        if (nbt instanceof NBTString) {
            return Component.text(((NBTString) nbt).getValue());
        }
        return asComponent((NBTCompound) nbt);
    }

    @SuppressWarnings("PatternValidation")
    public static Component asComponent(NBTCompound nbt) {
        Set<String> tagNames = nbt.getTagNames();

        // specific component type stuff
        ComponentBuilder<?, ?> builder;
        if (tagNames.contains("text")) {
            builder = Component.text()
                    .content(nbt.getStringTagValueOrThrow("text"));
        } else if (tagNames.contains("translate")) {
            TranslatableComponent.Builder i18nBuilder = Component.translatable()
                    .key(nbt.getStringTagValueOrThrow("translate"));

            NBTList<NBTCompound> withNbt = nbt.getTagListOfTypeOrNull("with", NBTCompound.class);
            if (withNbt != null && !withNbt.isEmpty()) {
                List<Component> args = new ArrayList<>(withNbt.size());
                for (NBTCompound argNbt : withNbt.getTags()) {
                    args.add(asComponent(argNbt));
                }
                i18nBuilder.args(args);
            }

            String fallback = nbt.getStringTagValueOrNull("fallback");
            if (fallback != null) {
                i18nBuilder.fallback(fallback);
            }

            builder = i18nBuilder;
        } else if (tagNames.contains("keybind")) {
            builder = Component.keybind()
                    .keybind(nbt.getStringTagValueOrThrow("keybind"));
        } else if (tagNames.contains("score")) {
            NBTCompound scoreNbt = nbt.getCompoundTagOrThrow("score");
            builder = Component.score()
                    .name(scoreNbt.getStringTagValueOrThrow("name"))
                    .objective(scoreNbt.getStringTagValueOrThrow("objective"));
        } else if (tagNames.contains("selector")) {
            SelectorComponent.Builder selectorBuilder = Component.selector()
                    .pattern(nbt.getStringTagValueOrThrow("selector"));

            NBTCompound separatorNbt = nbt.getCompoundTagOrNull("separator");
            if (separatorNbt != null) {
                selectorBuilder.separator(asComponent(separatorNbt));
            }
            builder = selectorBuilder;
        } else if (tagNames.contains("nbt")) {
            NBTComponentBuilder<?, ?> nbtBuilder;
            if (tagNames.contains("block")) {
                BlockNBTComponent.Pos blockPos = BlockNBTComponent.Pos
                        .fromString(nbt.getStringTagValueOrThrow("block"));
                nbtBuilder = Component.blockNBT().pos(blockPos);
            } else if (tagNames.contains("entity")) {
                nbtBuilder = Component.entityNBT()
                        .selector(nbt.getStringTagValueOrThrow("entity"));
            } else if (tagNames.contains("storage")) {
                nbtBuilder = Component.storageNBT()
                        .storage(Key.key(nbt.getStringTagValueOrThrow("storage")));
            } else {
                throw new RuntimeException("Can't determine component nbt type from keys: " + tagNames);
            }

            nbtBuilder.nbtPath(nbt.getStringTagValueOrThrow("nbt"));
            NBT separatorNbt = nbt.getTagOrNull("separator");
            if (separatorNbt != null) {
                nbtBuilder.separator(asComponent(separatorNbt));
            }
            NBTByte interpretNbt = nbt.getTagOfTypeOrNull("interpret", NBTByte.class);
            if (interpretNbt != null) {
                nbtBuilder.interpret(interpretNbt.getAsByte() != 0);
            }
            builder = nbtBuilder;
        } else {
            throw new RuntimeException("Can't determine component type from keys: " + tagNames);
        }

        // style
        Style.Builder style = Style.style();
        if (tagNames.contains("font")) {
            style.font(Key.key(nbt.getStringTagValueOrThrow("font")));
        }
        if (tagNames.contains("color")) {
            String rawColor = nbt.getStringTagValueOrThrow("color");
            if (!rawColor.isEmpty() && rawColor.charAt(0) == '#') {
                int rgb = Integer.parseInt(rawColor.substring(1), 16);
                style.color(TextColor.color(rgb));
            } else {
                NamedTextColor namedColor = NamedTextColor.NAMES.value(rawColor);
                if (namedColor != null) {
                    style.color(namedColor);
                }
            }
        }
        if (tagNames.contains("insertion")) {
            style.insertion(nbt.getStringTagValueOrThrow("insertion"));
        }
        if (tagNames.contains("clickEvent")) {
            NBTCompound clickEventNbt = nbt.getCompoundTagOrThrow("clickEvent");
            ClickEvent.Action action = ClickEvent.Action.NAMES.valueOrThrow(
                    clickEventNbt.getStringTagValueOrThrow("action"));
            if (action.readable()) {
                String value = clickEventNbt.getStringTagValueOrThrow("value");
                style.clickEvent(ClickEvent.clickEvent(action, value));
            }
        }
        if (tagNames.contains("hoverEvent")) {
            NBTCompound hoverEventTag = nbt.getCompoundTagOrThrow("hoverEvent");
            HoverEvent.Action<?> action = HoverEvent.Action.NAMES.valueOrThrow(
                    hoverEventTag.getStringTagValueOrThrow("action"));

            if (action.readable()) {
                if (action == HoverEvent.Action.SHOW_TEXT) {
                    NBT contentsNbt = hoverEventTag.getTagOrThrow("contents");
                    if (contentsNbt != null) {
                        Component text = asComponent(contentsNbt);
                        style.hoverEvent(HoverEvent.showText(text));
                    }
                } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                    NBTCompound contentsNbt = hoverEventTag.getCompoundTagOrThrow("contents");
                    Key type = Key.key(contentsNbt.getStringTagValueOrThrow("type"));
                    UUID id = UUID.fromString(contentsNbt.getStringTagValueOrThrow("id"));
                    NBT nameNbt = contentsNbt.getTagOrNull("name");
                    Component name = nameNbt != null ? asComponent(nameNbt) : null;
                    style.hoverEvent(HoverEvent.showEntity(type, id, name));
                } else if (action == HoverEvent.Action.SHOW_ITEM) {
                    NBT contentsTag = hoverEventTag.getTagOrThrow("contents");
                    if (contentsTag instanceof NBTCompound) {
                        NBTCompound compoundContentsTag = (NBTCompound) contentsTag;
                        Key item = Key.key(compoundContentsTag.getStringTagValueOrThrow("id"));
                        NBTNumber countNbt = compoundContentsTag.getNumberTagOrNull("count");
                        int count = countNbt == null ? 1 : countNbt.getAsInt();
                        String itemNbtStr = compoundContentsTag.getStringTagValueOrNull("tag");
                        BinaryTagHolder itemTagHolder = itemNbtStr != null
                                ? BinaryTagHolder.binaryTagHolder(itemNbtStr) : null;
                        style.hoverEvent(HoverEvent.showItem(item, count, itemTagHolder));
                    } else {
                        String contentsVal = ((NBTString) contentsTag).getValue();
                        style.hoverEvent(HoverEvent.showItem(Key.key(contentsVal), 1));
                    }
                }
            }
        }
        for (Map.Entry<String, TextDecoration> decoEntry : TextDecoration.NAMES.keyToValue().entrySet()) {
            if (!tagNames.contains(decoEntry.getKey())) {
                continue;
            }
            boolean value = nbt.getBoolean(decoEntry.getKey());
            style.decoration(decoEntry.getValue(), value);
        }
        builder.style(style.build());

        // children
        NBTList<NBTCompound> extraNbt = nbt.getTagListOfTypeOrNull("extra", NBTCompound.class);
        if (extraNbt != null) {
            for (NBTCompound argNbt : extraNbt.getTags()) {
                builder.append(asComponent(argNbt));
            }
        }

        return builder.build();
    }

    public static NBT asNbt(Component component) {
        if (component instanceof TextComponent
                && !component.hasStyling()
                && component.children().isEmpty()) {
            return new NBTString(((TextComponent) component).content());
        }
        return asCompoundNbt(component);
    }

    public static NBTCompound asCompoundNbt(Component component) {
        NBTCompound tag = new NBTCompound();

        // specific component type stuff
        if (component instanceof TextComponent) {
            tag.setTag("text", new NBTString(((TextComponent) component).content()));
        } else if (component instanceof TranslatableComponent) {
            TranslatableComponent i18nComponent = (TranslatableComponent) component;
            tag.setTag("translate", new NBTString(i18nComponent.key()));

            List<Component> args = i18nComponent.args();
            if (!args.isEmpty()) {
                // see comment at children serialization for why this is a compound
                NBTList<NBTCompound> withTag = new NBTList<>(NBTType.COMPOUND);
                for (Component arg : args) {
                    withTag.addTag(asCompoundNbt(arg));
                }
                tag.setTag("with", withTag);
            }

            String fallback = i18nComponent.fallback();
            if (fallback != null) {
                tag.setTag("fallback", new NBTString(fallback));
            }
        } else if (component instanceof KeybindComponent) {
            tag.setTag("keybind", new NBTString(((KeybindComponent) component).keybind()));
        } else if (component instanceof ScoreComponent) {
            ScoreComponent scoreComponent = (ScoreComponent) component;
            NBTCompound scoreNbt = new NBTCompound();
            scoreNbt.setTag("name", new NBTString(scoreComponent.name()));
            scoreNbt.setTag("objective", new NBTString(scoreComponent.objective()));
            tag.setTag("score", scoreNbt);
        } else if (component instanceof SelectorComponent) {
            SelectorComponent selectorComponent = (SelectorComponent) component;
            tag.setTag("selector", new NBTString(selectorComponent.pattern()));
            Component separator = selectorComponent.separator();
            if (separator != null) {
                tag.setTag("separator", asNbt(separator));
            }
        } else if (component instanceof NBTComponent<?, ?>) {
            NBTComponent<?, ?> nbtComponent = (NBTComponent<?, ?>) component;

            tag.setTag("nbt", new NBTString(nbtComponent.nbtPath()));
            if (nbtComponent.interpret()) {
                tag.setTag("interpret", new NBTByte(true));
            }
            Component separator = nbtComponent.separator();
            if (separator != null) {
                tag.setTag("separator", asNbt(separator));
            }

            if (component instanceof BlockNBTComponent) {
                tag.setTag("block", new NBTString(((BlockNBTComponent) component).pos().asString()));
            } else if (component instanceof EntityNBTComponent) {
                tag.setTag("entity", new NBTString(((EntityNBTComponent) component).selector()));
            } else if (component instanceof StorageNBTComponent) {
                tag.setTag("storage", new NBTString(((StorageNBTComponent) component).storage().asString()));
            }
        }

        // style
        if (component.hasStyling()) {
            Style style = component.style();

            Key font = style.font();
            if (font != null) {
                tag.setTag("font", new NBTString(font.asString()));
            }

            TextColor color = style.color();
            if (color instanceof NamedTextColor) {
                tag.setTag("color", new NBTString(color.toString()));
            } else if (color != null) {
                StringBuilder colorBuilder = new StringBuilder(7);
                colorBuilder.append(Integer.toHexString(color.value())).reverse();
                while (colorBuilder.length() < 6) {
                    colorBuilder.append('0');
                }
                colorBuilder.append('#').reverse();
                tag.setTag("color", new NBTString(colorBuilder.toString()));
            }

            String insertion = style.insertion();
            if (insertion != null) {
                tag.setTag("insertion", new NBTString(insertion));
            }

            ClickEvent clickEvent = style.clickEvent();
            if (clickEvent != null) {
                ClickEvent.Action action = clickEvent.action();
                if (action.readable()) {
                    NBTCompound clickNbt = new NBTCompound();
                    clickNbt.setTag("action", new NBTString(action.toString()));
                    clickNbt.setTag("value", new NBTString(clickEvent.value()));
                    tag.setTag("clickEvent", clickNbt);
                }
            }

            HoverEvent<?> hoverEvent = style.hoverEvent();
            if (hoverEvent != null) {
                HoverEvent.Action<?> action = hoverEvent.action();
                if (action.readable()) {
                    tag.setTag("action", new NBTString(action.toString()));
                    Object hoverValue = hoverEvent.value();
                    if (hoverValue instanceof Component) {
                        tag.setTag("contents", asNbt((Component) hoverValue));
                    } else if (hoverValue instanceof HoverEvent.ShowEntity) {
                        HoverEvent.ShowEntity showEntity = (HoverEvent.ShowEntity) hoverValue;
                        NBTCompound entityNbt = new NBTCompound();
                        entityNbt.setTag("type", new NBTString(showEntity.type().asString()));
                        entityNbt.setTag("id", new NBTString(showEntity.id().toString()));
                        Component name = showEntity.name();
                        if (name != null) {
                            entityNbt.setTag("name", asNbt(name));
                        }
                        tag.setTag("contents", entityNbt);
                    } else if (hoverValue instanceof HoverEvent.ShowItem) {
                        HoverEvent.ShowItem showItem = (HoverEvent.ShowItem) hoverValue;
                        NBTCompound itemNbt = new NBTCompound();
                        itemNbt.setTag("id", new NBTString(showItem.item().asString()));
                        if (showItem.count() != 1) {
                            itemNbt.setTag("count", new NBTInt(showItem.count()));
                        }
                        BinaryTagHolder tagHolder = showItem.nbt();
                        if (tagHolder != null) {
                            itemNbt.setTag("tag", new NBTString(tagHolder.string()));
                        }
                        tag.setTag("contents", itemNbt);
                    }
                }
            }

            for (TextDecoration decoration : TextDecoration.values()) {
                TextDecoration.State state = style.decoration(decoration);
                if (state != TextDecoration.State.NOT_SET) {
                    boolean value = state == TextDecoration.State.TRUE;
                    tag.setTag(decoration.toString(), new NBTByte(value));
                }
            }
        }

        // children
        List<Component> children = component.children();
        if (!children.isEmpty()) {
            // nbt doesn't support different tag types in the same list,
            // so just make everything a compound
            NBTList<NBTCompound> extraTag = new NBTList<>(NBTType.COMPOUND);
            for (Component child : children) {
                extraTag.addTag(asCompoundNbt(child));
            }
            tag.setTag("extra", extraTag);
        }

        return tag;
    }
}
