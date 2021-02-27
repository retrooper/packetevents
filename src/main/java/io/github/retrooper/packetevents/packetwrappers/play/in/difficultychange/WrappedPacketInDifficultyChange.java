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

package io.github.retrooper.packetevents.packetwrappers.play.in.difficultychange;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

/**
 * Wrapper for the DifficultyChange packet.
 *
 * @author Tecnio
 * @since 1.8
 */
public final class WrappedPacketInDifficultyChange extends WrappedPacket {

    private static Class<? extends Enum<?>> enumDifficultyClass;

    public WrappedPacketInDifficultyChange(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            enumDifficultyClass = NMSUtils.getNMSEnumClass("EnumDifficulty");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Difficulty getDifficulty() {
        Enum<?> enumConstant = readEnumConstant(0, enumDifficultyClass);
        return Difficulty.values()[enumConstant.ordinal()];
    }

    public void setDifficulty(Difficulty difficulty) {
        Enum<?> enumConstant = EnumUtil.valueByIndex(enumDifficultyClass, difficulty.ordinal());
        writeEnumConstant(0, enumConstant);
    }

    public enum Difficulty {
        PEACEFUL,
        EASY,
        NORMAL,
        HARD
    }
}
