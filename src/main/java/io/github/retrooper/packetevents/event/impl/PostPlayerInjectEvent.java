/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code PostPlayerInjectEvent} event is fired after a player is successfully injected.
 * One thing to note is PacketEvents can never predict if an injection will be successful before injecting.
 * Older versions used to call this event right after the injection.
 * If you are on an older version of PacketEvents DON'T use this to register player data.
 * The issue was the PacketReceiveEvent and PacketSendEvent could be called before this event resulting in your
 * player data not being initiated(null).
 * To fix this what we do is whenever we receive the FIRST or send the FIRST packet to a player, we call this event right before doing that.
 * This event is called asynchronously always.
 * It is not recommended to register player data using this event unless you know what you are doing.
 * This event assures you that the player was successfully injected.
 * A player is injected by PacketEvents whenever they join the server.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 * @see <a href="https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java">https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java</a>
 * @author retrooper
 * @since 1.3
 */
public class PostPlayerInjectEvent extends PacketEvent implements PlayerEvent {
    private final Player player;

    public PostPlayerInjectEvent(Player player) {
        this.player = player;
    }

    /**
     * This method returns the bukkit player object of the player that has been injected.
     * The player is guaranteed to not be null.
     * @return Injected Player.
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the cached netty channel of the player.
     * @return Netty channel of the injected player.
     */
    @NotNull
    public Object getChannel() {
        return PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
    }

    /**
     * This method returns the ClientVersion of the injected player.
     * @see ClientVersion
     * @return ClientVersion of injected player.
     */
    public ClientVersion getClientVersion() {
        return PacketEvents.get().getPlayerUtils().clientVersionsMap.get(player.getAddress());
    }

    @Override
    public void call(PacketListenerDynamic listener) {
        listener.onPostPlayerInject(this);
    }
}
