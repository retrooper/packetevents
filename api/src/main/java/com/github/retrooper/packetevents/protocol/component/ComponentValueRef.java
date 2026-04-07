/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.WeakHashMap;

@NullMarked
public interface ComponentValueRef<T> {

    @ApiStatus.Internal
    static <T> ComponentValueRef<T> ofBytes(ComponentType<T> type, byte[] bytes, ClientVersion version) {
        ServerVersion serverVersion = version.toServerVersion();
        return asCached(registries -> {
            Object byteBuf = UnpooledByteBufAllocationHelper.wrappedBuffer(bytes);
            try {
                PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(byteBuf, serverVersion);
                wrapper.setRegistryHolder(registries);
                return type.read(wrapper);
            } finally {
                ByteBufHelper.release(byteBuf);
            }
        });
    }

    static <T> ComponentValueRef<T> ofStatic(T value) {
        return registries -> value;
    }

    static <T> ComponentValueRef<T> asCached(ComponentValueRef<T> delegate) {
        return new ComponentValueRef<T>() {
            // I would rather want to use a weak, concurrent, identity-based hashmap, but java doesn't
            // have this datatype built-in and I don't want to write it myself
            private final Map<IRegistryHolder, T> values = new WeakHashMap<>();

            @Override
            public synchronized T resolve(IRegistryHolder registries) {
                return this.values.computeIfAbsent(registries, delegate::resolve);
            }
        };
    }

    @Deprecated
    default T resolveUsingGlobals() {
        return this.resolve(GlobalRegistryHolder.INSTANCE);
    }

    T resolve(IRegistryHolder registries);
}
