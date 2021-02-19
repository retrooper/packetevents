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

package io.github.retrooper.packetevents.event.eventtypes;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

/**
 * Every event that supports cancellation should implement this interface.
 * PacketEvents' event system lets the highest priority listener be have the highest priority
 * in deciding whether the event will cancel.
 * This means an event with a lower priority than the higher priority one would not be able to decide.
 * Cancelling the event means the action assosiated with the event will be cancelled.
 * For example, cancelling the {@link PacketPlayReceiveEvent}
 * will prevent minecraft from processing the incoming packet.
 *
 * @author retrooper
 * @see PacketPlayReceiveEvent
 * @since 1.7
 */
public interface CancellableEvent {

    /**
     * This method returns if the event will be cancelled.
     *
     * @return Will the event be cancelled.
     */
    boolean isCancelled();

    /**
     * Cancel or proceed with the event.
     *
     * @param val Is the event cancelled
     */
    void setCancelled(boolean val);
}
