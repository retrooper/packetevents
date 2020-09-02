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

import io.github.retrooper.packetevents.event.CancellableEvent;
import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

/**
 * This event is called each time you eject a player.
 */
public final class PlayerUninjectEvent extends PacketEvent implements CancellableEvent {
    private final Player player;
    private final boolean async;
    private boolean cancelled;

    public PlayerUninjectEvent(final Player player, final boolean isAsync) {
        this.player = player;
        this.async = isAsync;
    }

    public PlayerUninjectEvent(final Player player) {
        this(player, false);
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean val) {
        this.cancelled = val;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAsync() {
        return async;
    }
}
