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

package io.github.retrooper.packetevents.utils.player;

/**
 * The {@code Hand} enum represents what hand was used in an interaction.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Open_Book">https://wiki.vg/Protocol#Open_Book</a>
 * @since 1.8
 */
public enum Hand {
    /**
     * The right hand in vanilla minecraft.
     * Some clients allow you to render the main hand as the left hand.
     */
    MAIN_HAND,

    /**
     * The left hand in vanilla minecraft.
     * This hand does not exist on 1.7.10 and 1.8.x's protocol.
     * It will always be the {@link Hand#MAIN_HAND} on those protocols.
     */
    OFF_HAND
}
