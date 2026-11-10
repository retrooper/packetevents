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

package io.github.retrooper.packetevents.velocity.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Synchronized registry cache key for velocity: one backend server (by its {@link ServerInfo}, which
 * compares by name and address) and the packet version.
 * <p>
 * Cached registries are only reused for identical registry data (see
 * {@link SynchronizedRegistriesHandler.RegistryEntry#computeSyncedRegistry(Object, java.util.List,
 * com.github.retrooper.packetevents.wrapper.PacketWrapper)}), so a backend restarted with different
 * registry contents under the same name and address replaces its entry instead of serving stale data.
 */
@ApiStatus.Internal
final class VelocityRegistryCacheKey {

    private final ServerInfo server;
    private final ClientVersion version;

    VelocityRegistryCacheKey(ServerInfo server, ClientVersion version) {
        this.server = server;
        this.version = version;
    }

    /**
     * Returns the cache key for registries received from the given backend server, or null if the server
     * isn't registered with the proxy (e.g. a server created with
     * {@link ProxyServer#createRawRegisteredServer(ServerInfo)}): no unregister event will ever fire for
     * those, so caching their registries could keep them forever.
     */
    static @Nullable VelocityRegistryCacheKey of(ProxyServer proxy, ServerInfo server, ClientVersion version) {
        RegisteredServer registered = proxy.getServer(server.getName()).orElse(null);
        if (registered == null || !registered.getServerInfo().equals(server)) {
            return null;
        }
        return new VelocityRegistryCacheKey(server, version);
    }

    /**
     * Drops the cached registries of backend servers once they are unregistered, as the registry cache
     * would otherwise keep them (and the user which decoded them) forever on networks which register
     * servers dynamically. Does nothing on velocity versions without {@code ServerUnregisteredEvent}.
     */
    static void registerInvalidation(ProxyServer proxy, PluginContainer plugin) {
        // looked up reflectively: the event is a record, which can't be referenced when compiling for java 8
        Class<?> eventClass;
        try {
            eventClass = Class.forName("com.velocitypowered.api.event.proxy.server.ServerUnregisteredEvent");
        } catch (ClassNotFoundException exception) {
            return; // older velocity version without this event
        }
        Method serverGetter;
        try {
            serverGetter = eventClass.getMethod("unregisteredServer");
        } catch (NoSuchMethodException exception) {
            // the event exists, but its api has changed; registries of unregistered servers will stay cached
            PacketEvents.getAPI().getLogManager().warn("Can't read the server from " + eventClass.getName()
                    + ", cached registries of unregistered servers won't be dropped: " + exception);
            return;
        }
        register(proxy, plugin, eventClass, serverGetter);
    }

    @SuppressWarnings("unchecked")
    private static <E> void register(ProxyServer proxy, PluginContainer plugin, Class<?> eventClass, Method serverGetter) {
        proxy.getEventManager().register(plugin.getInstance().orElse(null), (Class<E>) eventClass, event -> {
            try {
                invalidate(((RegisteredServer) serverGetter.invoke(event)).getServerInfo());
            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException("Error while reading unregistered server from " + event, exception);
            }
        });
    }

    static void invalidate(ServerInfo server) {
        SynchronizedRegistriesHandler.invalidateSyncedRegistries(key ->
                key instanceof VelocityRegistryCacheKey
                        && ((VelocityRegistryCacheKey) key).server.equals(server));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VelocityRegistryCacheKey)) {
            return false;
        }
        VelocityRegistryCacheKey that = (VelocityRegistryCacheKey) obj;
        return this.server.equals(that.server) && this.version == that.version;
    }

    @Override
    public int hashCode() {
        return 31 * this.server.hashCode() + this.version.hashCode();
    }

    @Override
    public String toString() {
        return "VelocityRegistryCacheKey{server=" + this.server + ", version=" + this.version + '}';
    }
}
