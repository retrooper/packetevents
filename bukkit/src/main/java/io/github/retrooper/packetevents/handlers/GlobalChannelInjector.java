/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import com.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import io.github.retrooper.packetevents.handlers.legacy.early.EarlyChannelInjectorLegacy;
import io.github.retrooper.packetevents.handlers.legacy.late.LateChannelInjectorLegacy;
import io.github.retrooper.packetevents.handlers.modern.early.EarlyChannelInjectorModern;
import io.github.retrooper.packetevents.handlers.modern.late.LateChannelInjectorModern;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import org.jetbrains.annotations.Nullable;

public class GlobalChannelInjector implements ChannelInjector {
    public boolean injectEarly = true;
    private ChannelInjector injector;

    public boolean shouldInjectEarly() {
        return injectEarly;
    }

    public void setShouldInjectEarly(boolean injectEarly) {
        this.injectEarly = injectEarly;
    }

    public void load() {
        if (injectEarly) {
            injector = SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE ? new EarlyChannelInjectorModern() : new EarlyChannelInjectorLegacy();
        } else {
            injector = SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE ? new LateChannelInjectorModern() : new LateChannelInjectorLegacy();
        }
    }

    @Override
    public boolean isBound() {
        return injector.isBound();
    }

    @Override
    public void inject() {
        try {
            //Try inject...
            injector.inject();
        } catch (Exception ex) {
            //Failed to inject! Let us revert to the compatibility injector and re-inject.
            if (injector instanceof EarlyInjector) {
                injectEarly = false;
                load();
                injector.inject();
                PacketEvents.getAPI().getLogger().warning("[packetevents] Failed to inject with the Early Injector. Reverting to the Compatibility/Late Injector... This is just a warning, but please report this!");
                ex.printStackTrace();
            } else {
                PacketEvents.getAPI().getLogger().severe("[packetevents] Failed to inject with all available injectors. Please report this!");
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void eject() {
        injector.eject();
    }

    @Override
    public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            injector.injectPlayer(player, connectionState);
        }
    }

    @Override
    public void ejectPlayer(Object player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            injector.ejectPlayer(player);
        }
    }

    @Override
    public boolean hasInjected(Object player) {
        return injector.hasInjected(player);
    }

    @Override
    @Nullable
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        return injector.getConnectionState(channel);
    }

    @Override
    public void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        injector.changeConnectionState(channel, connectionState);
    }
}
