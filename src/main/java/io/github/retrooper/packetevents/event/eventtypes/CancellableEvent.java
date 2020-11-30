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

/**
 * This indicates that it is possible to cancel an event,
 * which should result in cancelling the planned action.
 */
public interface CancellableEvent {

    /**
     * Has the event been cancelled?
     *
     * @return is event cancelled
     */
    boolean isCancelled();

    /**
     * Cancel or proceed with the event.
     *
     * @param val
     */
    void setCancelled(boolean val);

    /**
     * Cancel the event.
     * You can achieve the same result by just using {@link #setCancelled(boolean)}
     */
    void cancel();

    /**
     * Uncancel the event.
     * You can achieve the same result by just using {@link #setCancelled(boolean)}
     */
    void uncancel();
}
