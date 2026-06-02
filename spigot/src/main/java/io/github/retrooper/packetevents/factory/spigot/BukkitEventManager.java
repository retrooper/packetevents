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

package io.github.retrooper.packetevents.factory.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventContainer;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.manager.ClientVersionInternalListener;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BukkitEventManager extends EventManager {

    public BukkitEventManager() {
        // register internal listener first
        this.registerListener(new ClientVersionInternalListener());
    }

    @Override
    protected void handleNewListener(EventContainer container, PacketListenerCommon listener) {
        if (container != this.clientListeners || !container.isEmpty()) {
            return; // useless
        } else if (listener instanceof ClientVersionInternalListener) {
            return; // don't inject pre-via if this is only our internal listener
        }

        if (ViaVersionUtil.isAvailable()) {
            SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
            injector.activatePreViaVersion();
        }
    }
}
