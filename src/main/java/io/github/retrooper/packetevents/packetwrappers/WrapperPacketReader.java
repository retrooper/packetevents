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

package io.github.retrooper.packetevents.packetwrappers;

public interface WrapperPacketReader {

    boolean readBoolean(int index);

    byte readByte(int index);

    short readShort(int index);

    int readInt(int index);

    long readLong(int index);

    float readFloat(int index);

    double readDouble(int index);

    boolean[] readBooleanArray(int index);

    byte[] readByteArray(int index);

    short[] readShortArray(int index);

    int[] readIntArray(int index);

    long[] readLongArray(int index);

    float[] readFloatArray(int index);

    double[] readDoubleArray(int index);

    String[] readStringArray(int index);

    String readString(int index);

    Object readObject(int index, Class<?> type);

    Object readAnyObject(int index);

    Object[] readObjectArray(int index, Class<?> type);
}
