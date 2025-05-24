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

package com.github.retrooper.packetevents.protocol.component.predicates;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * This implementation is experimental and not done yet. As I don't expect
 * component predicates to be used in packetevents at all, it is currently not
 * read from/to NBT; if you want/need this though, feel free to mention it
 * via GitHub/Discord - or open a PR!
 */
public final class ComponentPredicateTypes {

    private static final VersionedRegistry<ComponentPredicateType<?>> REGISTRY =
            new VersionedRegistry<>("data_component_predicate_type");

    private ComponentPredicateTypes() {
    }

    @ApiStatus.Internal
    public static <T extends IComponentPredicate> ComponentPredicateType<T> define(
            String key, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(key, data ->
                new StaticComponentPredicateType<>(data, reader, writer));
    }

    public static VersionedRegistry<ComponentPredicateType<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> DAMAGE =
            define("damage", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> ENCHANTMENTS =
            define("enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> STORED_ENCHANTMENTS =
            define("stored_enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> POTION_CONTENTS =
            define("potion_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> CUSTOM_DATA =
            define("custom_data", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> CONTAINER =
            define("container", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> BUNDLE_CONTENTS =
            define("bundle_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> FIREWORK_EXPLOSION =
            define("firework_explosion", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> FIREWORKS =
            define("fireworks", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> WRITABLE_BOOK_CONTENT =
            define("writable_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> WRITTEN_BOOK_CONTENT =
            define("written_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> ATTRIBUTE_MODIFIERS =
            define("attribute_modifiers", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> TRIM =
            define("trim", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> JUKEBOX_PLAYABLE =
            define("jukebox_playable", NbtComponentPredicate::read, NbtComponentPredicate::write);

    static {
        REGISTRY.unloadMappings();
    }
}
