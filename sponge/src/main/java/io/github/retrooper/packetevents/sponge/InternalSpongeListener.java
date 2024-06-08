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
package io.github.retrooper.packetevents.sponge;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import io.github.retrooper.packetevents.sponge.injector.SpongeChannelInjector;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class InternalSpongeListener {

    @Listener(order = Order.LATE)
    public void onJoin(ServerSideConnectionEvent.Join event, @Getter("player") ServerPlayer player) {
        SpongeChannelInjector injector = (SpongeChannelInjector) PacketEvents.getAPI().getInjector();

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) {
            // We did not inject this user
            Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
            // Check if it is a fake connection...
            if (!FakeChannelUtil.isFakeChannel(channel)) {
                // Kick them, if they are not a fake player.
                player.kick(Component.text("PacketEvents 2.0 failed to inject"));
            }
            return;
        }

        // Set player object in the injectors
        injector.updatePlayer(user, player);
    }

    @Listener(order = Order.EARLY)
    public void onStart(StartedEngineEvent<Server> event) {
        if (PacketEvents.getAPI().getSettings().shouldCheckForUpdates()) {
            PacketEvents.getAPI().getUpdateChecker().handleUpdateCheck();
        }
    }
}
