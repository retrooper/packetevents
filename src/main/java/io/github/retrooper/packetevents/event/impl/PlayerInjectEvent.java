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
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

/**
 * The {@code PlayerInjectEvent} event is fired whenever a player is injected.
 * A player is injected by PacketEvents whenever they join the server.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 *
 * @author retrooper
 * @see <a href="https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java">https://github.com/retrooper/packetevents/blob/dev/src/main/java/io/github/retrooper/packetevents/handler/PacketHandlerInternal.java</a>
 * @since 1.6.9
 */
public final class PlayerInjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent {
    private final Player player;
    private final InetSocketAddress address;
    private final boolean async;
    private boolean cancelled;

    public PlayerInjectEvent(final Player player, final boolean isAsync) {
        this.player = player;
        this.address = ChannelUtils.getSocketAddress(PacketEvents.get().packetProcessorInternal.getChannel(player));
        this.async = isAsync;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public void uncancel() {
        cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    /**
     * This method returns the bukkit player object of the player being injected.
     * This player might not be fully initialized.
     *
     * @return Injected Player.
     */
    @Nullable
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the socket address injecting player.
     *
     * @return Socket address of the injecting player.
     */
    @NotNull
    public InetSocketAddress getSocketAddress() {
        return address;
    }

    /**
     * This method returns if the event has been called asynchronously.
     * If the {@link PacketEventsSettings#shouldInjectAsync()} is enabled, the event will be called asynchronously
     * and the player will be injected asynchronously.
     * The {@link PacketEventsSettings} can be accessed here:
     *
     * @return Is the injection asynchronous.
     * @see PacketEvents#getSettings()
     */
    public boolean isAsync() {
        return async;
    }

    @Override
    public void call(PacketListenerDynamic listener) {
        listener.onPlayerInject(this);
    }
}
