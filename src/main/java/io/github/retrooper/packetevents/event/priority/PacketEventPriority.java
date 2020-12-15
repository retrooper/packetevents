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

package io.github.retrooper.packetevents.event.priority;

/**
 * Event priority bytes for the new and legacy event system.
 * Please use these constant variables, as it is possible that the
 * values of the bytes might change.
 * @author retrooper
 * @since 1.6.9
 */
public enum PacketEventPriority {
    /**
     * The weakest event priority.
     * The first to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you need to be the first processing the event and
     * need no power in cancelling an event or preventing an event cancellation.
     */
    LOWEST((byte)0),

    /**
     * A weak event priority.
     * Second to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you would prefer to be one of the first to process the event,
     * but don't mind if some other listener processes before you.
     */
    LOW((byte)1),

    /**
     * Default event priority.
     * Third to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you don't really care/know when you process or just want to
     * be in the middle.
     */
    NORMAL((byte)2),

    /**
     * Higher than the {@link PacketEventPriority#NORMAL} event priority.
     * Fourth to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you want to process before the default event prioritized listeners.
     */
    HIGH((byte)3),

    /**
     * Second most powerful event priority.
     * Fifth to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you prefer to be one of the last to process,
     * but don't mind if some other listener really needs to process after you.
     * Also use this if you prefer deciding if the event cancelled or not, but don't
     * mind if some other listener urgently needs to decide over you.
     * {@link PacketEventPriority#MONITOR} is rarely ever recommended to use.
     */
    HIGHEST((byte)4),

    /**
     * Most powerful event priority.
     * Last(Sixth) to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you urgently need to be the last to process or urgently need to decide if the event cancelled or not.
     * This is rarely recommended.
     */
    MONITOR((byte)5);

    final byte priorityValue;
    PacketEventPriority(byte priorityValue) {
        this.priorityValue = priorityValue;
    }

    public byte getPriorityValue() {
        return priorityValue;
    }

    public static PacketEventPriority getPacketEventPriority(final byte bytePriority) {
        for(PacketEventPriority priority : values()) {
            if(priority.priorityValue == bytePriority) {
                return priority;
            }
        }
        return NORMAL;
    }
}
