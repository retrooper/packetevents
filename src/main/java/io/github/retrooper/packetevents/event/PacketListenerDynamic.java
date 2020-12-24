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

package io.github.retrooper.packetevents.event;

import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;

/**
 * Dynamic packet event listener.
 * Implement whatever event method you need.
 * Nothing will happen if you don't implement an event method.
 *
 * @author retrooper
 * @since 1.7.7
 */
public abstract class PacketListenerDynamic {
    private final PacketEventPriority priority;

    public PacketListenerDynamic(final PacketEventPriority priority) {
        this.priority = priority;
    }

    public PacketListenerDynamic() {
        this(PacketEventPriority.NORMAL);
    }

    public PacketEventPriority getPriority() {
        return priority;
    }

    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {

    }

    public void onPacketStatusSend(PacketStatusSendEvent event) {
    }

    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
    }

    public void onPacketLoginSend(PacketLoginSendEvent event) {

    }

    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
    }

    public void onPacketPlaySend(PacketPlaySendEvent event) {
    }

    public void onPostPacketPlayReceive(PostPacketPlayReceiveEvent event) {
    }

    public void onPostPacketPlaySend(PostPacketPlaySendEvent event) {
    }

    public void onPostPlayerInject(PostPlayerInjectEvent event) {

    }

    public void onPlayerInject(PlayerInjectEvent event) {
    }

    public void onPlayerEject(PlayerEjectEvent event) {
    }

    public void onPacketEvent(PacketEvent event) {
    }
}
