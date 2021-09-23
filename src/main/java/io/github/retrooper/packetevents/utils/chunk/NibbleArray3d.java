/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.utils.chunk;

import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstractInputStream;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstractOutputStream;

import java.io.IOException;

public class NibbleArray3d {
    private final byte[] data;

    public NibbleArray3d(byte[] data) {
        this.data = data;
    }

    public NibbleArray3d(int size) {
        this(new byte[size >> 1]);
    }

    public NibbleArray3d(ByteBufAbstractInputStream in, int size) throws IOException {
        this(in.readBytes(size));
    }

    public byte[] getData() {
        return data;
    }

    public void write(ByteBufAbstractOutputStream out) throws IOException {
        out.write(this.data);
    }

    public int get(int x, int y, int z) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        return part == 0 ? this.data[index] & 15 : this.data[index] >> 4 & 15;
    }

    public void set(int x, int y, int z, int val) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        if(part == 0) {
            this.data[index] = (byte) (this.data[index] & 240 | val & 15);
        } else {
            this.data[index] = (byte) (this.data[index] & 15 | (val & 15) << 4);
        }
    }

    public void fill(int val) {
        for(int index = 0; index < this.data.length << 1; index++) {
            int ind = index >> 1;
            int part = index & 1;
            if(part == 0) {
                this.data[ind] = (byte) (this.data[ind] & 240 | val & 15);
            } else {
                this.data[ind] = (byte) (this.data[ind] & 15 | (val & 15) << 4);
            }
        }
    }
}
