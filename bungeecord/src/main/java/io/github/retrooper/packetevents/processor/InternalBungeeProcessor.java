/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.player.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Field;

public class InternalBungeeProcessor implements Listener {

    private static final Field CHANNEL_WRAPPER, CHANNEL;

    static {
        try {
            CHANNEL_WRAPPER = Class.forName("net.md_5.bungee.UserConnection")
                    .getDeclaredField("ch");
            CHANNEL_WRAPPER.setAccessible(true);

            CHANNEL = Class.forName("net.md_5.bungee.netty.ChannelWrapper")
                    .getDeclaredField("ch");
            CHANNEL.setAccessible(true);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error looking up channel fields", exception);
        }
    }

    // Called right after LoginSuccess is sent to the user.
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Object channel;
        try {
            channel = CHANNEL.get(CHANNEL_WRAPPER.get(player));
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Error looking up channel from " + player, exception);
        }

        ProtocolManager.CHANNELS.put(player.getUniqueId(), channel);
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);

        User user = PacketEvents.getAPI().getPlayerManager().getUser(event.getPlayer());
        if (user == null) {
            return;
        }
        UserLoginEvent loginEvent = new UserLoginEvent(user, player);
        PacketEvents.getAPI().getEventManager().callEvent(loginEvent);
    }
}
