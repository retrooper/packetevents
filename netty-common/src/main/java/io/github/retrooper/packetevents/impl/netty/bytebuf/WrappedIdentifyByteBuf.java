package io.github.retrooper.packetevents.impl.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

public class WrappedIdentifyByteBuf extends ByteBuf {
    private final UUID identifier;
    private final ByteBuf original;

    public WrappedIdentifyByteBuf(ByteBuf original) {
        this.identifier = UUID.randomUUID();
        this.original = original;
    }

    @Override
    public int capacity() {
        return original.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        return original.capacity(newCapacity);
    }

    @Override
    public int maxCapacity() {
        return original.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return original.alloc();
    }

    @Deprecated
    @Override
    public ByteOrder order() {
        return original.order();
    }

    @Deprecated
    @Override
    public ByteBuf order(ByteOrder endianness) {
        return original.order(endianness);
    }

    @Override
    public ByteBuf unwrap() {
        return original.unwrap();
    }

    @Override
    public boolean isDirect() {
        return original.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return original.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return original.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return original.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return original.readerIndex(readerIndex);
    }

    @Override
    public int writerIndex() {
        return original.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return original.writerIndex(writerIndex);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return original.setIndex(readerIndex, writerIndex);
    }

    @Override
    public int readableBytes() {
        return original.readableBytes();
    }

    @Override
    public int writableBytes() {
        return original.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return original.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return original.isReadable();
    }

    @Override
    public boolean isReadable(int numBytes) {
        return original.isReadable(numBytes);
    }

    @Override
    public boolean isWritable() {
        return original.isWritable();
    }

    @Override
    public boolean isWritable(int numBytes) {
        return original.isWritable(numBytes);
    }

    @Override
    public ByteBuf clear() {
        return original.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return original.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return original.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return original.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return original.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return original.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return original.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return original.ensureWritable(minWritableBytes);
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return original.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return original.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return original.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return original.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return original.getShort(index);
    }

    @Override
    public short getShortLE(int index) {
        return original.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return original.getUnsignedShort(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return original.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int index) {
        return original.getMedium(index);
    }

    @Override
    public int getMediumLE(int index) {
        return original.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return original.getUnsignedMedium(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return original.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int index) {
        return original.getInt(index);
    }

    @Override
    public int getIntLE(int index) {
        return original.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return original.getUnsignedInt(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return original.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int index) {
        return original.getLong(index);
    }

    @Override
    public long getLongLE(int index) {
        return original.getLongLE(index);
    }

    @Override
    public char getChar(int index) {
        return original.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return original.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return original.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return original.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex) {
        return original.getBytes(index, dst, dstIndex);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return original.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return original.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return original.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return original.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return original.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return original.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return original.getBytes(index, out, position, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return original.getCharSequence(index, length, charset);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return original.setBoolean(index, value);
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return original.setByte(index, value);
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return original.setShort(index, value);
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        return original.setShortLE(index, value);
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return original.setMedium(index, value);
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        return original.setMediumLE(index, value);
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return original.setInt(index, value);
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        return original.setIntLE(index, value);
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return original.setLong(index, value);
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        return original.setLongLE(index, value);
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return original.setChar(index, value);
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return original.setFloat(index, value);
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return original.setDouble(index, value);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return original.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex) {
        return original.setBytes(index, src, srcIndex);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return original.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return original.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return original.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return original.setBytes(index, src);
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return original.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return original.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return original.setBytes(index, in, position, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        return original.setZero(index, length);
    }

    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return original.setCharSequence(index, sequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return original.readBoolean();
    }

    @Override
    public byte readByte() {
        return original.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return original.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return original.readShort();
    }

    @Override
    public short readShortLE() {
        return original.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return original.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return original.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return original.readMedium();
    }

    @Override
    public int readMediumLE() {
        return original.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return original.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return original.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return original.readInt();
    }

    @Override
    public int readIntLE() {
        return original.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return original.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return original.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return original.readLong();
    }

    @Override
    public long readLongLE() {
        return original.readLongLE();
    }

    @Override
    public char readChar() {
        return original.readChar();
    }

    @Override
    public float readFloat() {
        return original.readFloat();
    }

    @Override
    public double readDouble() {
        return original.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return original.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return original.readSlice(length);
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return original.readRetainedSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return original.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return original.readBytes(dst, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return original.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        return original.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return original.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        return original.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return original.readBytes(out, length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return original.readBytes(out, length);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return original.readCharSequence(length, charset);
    }

    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return original.readBytes(out, position, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return original.skipBytes(length);
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return original.writeBoolean(value);
    }

    @Override
    public ByteBuf writeByte(int value) {
        return original.writeByte(value);
    }

    @Override
    public ByteBuf writeShort(int value) {
        return original.writeShort(value);
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        return original.writeShortLE(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return original.writeMedium(value);
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        return original.writeMediumLE(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return original.writeInt(value);
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        return original.writeIntLE(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return original.writeLong(value);
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        return original.writeLongLE(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return original.writeChar(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return original.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return original.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return original.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return original.writeBytes(src, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return original.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        return original.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return original.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        return original.writeBytes(src);
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return original.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return original.writeBytes(in, length);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return original.writeBytes(in, position, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        return original.writeZero(length);
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return original.writeCharSequence(sequence, charset);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return original.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return original.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return original.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return original.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteProcessor processor) {
        return original.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return original.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return original.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return original.forEachByteDesc(index, length, processor);
    }

    @Override
    public ByteBuf copy() {
        return original.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return original.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return original.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return original.retainedSlice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return original.slice(index, length);
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return original.retainedSlice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return original.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return original.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return original.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return original.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return original.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return original.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return original.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return original.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return original.hasArray();
    }

    @Override
    public byte[] array() {
        return original.array();
    }

    @Override
    public int arrayOffset() {
        return original.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return original.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return original.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return original.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return original.toString(index, length, charset);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return original.compareTo(buffer);
    }

    @Override
    public String toString() {
        return original.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return original.retain(increment);
    }

    @Override
    public int refCnt() {
        return original.refCnt();
    }

    @Override
    public ByteBuf retain() {
        return original.retain();
    }

    @Override
    public ByteBuf touch() {
        return original.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return original.touch(hint);
    }

    @Override
    public boolean release() {
        return original.release();
    }

    @Override
    public boolean release(int decrement) {
        return original.release(decrement);
    }


    /*
     * Need to rewrite this method to use cache identifier
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WrappedIdentifyByteBuf that = (WrappedIdentifyByteBuf) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
