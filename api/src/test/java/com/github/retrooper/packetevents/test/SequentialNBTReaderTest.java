package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SequentialNBTReaderTest {

    private static byte[] list(int elementType, byte[]... elements) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeByte(9); // list tag type
            out.writeByte(elementType);
            out.writeInt(elements.length);
            for (byte[] element : elements) {
                out.write(element);
            }
        }
        return bytes.toByteArray();
    }

    private static byte[] utf(String value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeUTF(value);
        }
        return bytes.toByteArray();
    }

    private static SequentialNBTReader.List reader(byte[] encoded) throws IOException {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(encoded));
        return (SequentialNBTReader.List) SequentialNBTReader.INSTANCE.deserializeTag(NBTLimiter.noop(), input, false);
    }

    @Test
    public void testFixedWidthElements() throws IOException {
        byte[] encoded = list(3, new byte[]{0, 0, 0, 1}, new byte[]{0, 0, 0, 2});
        SequentialNBTReader.List reader = reader(encoded);
        assertArrayEquals(encoded, reader.readFullyAsBinary());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testVariableWidthElements() throws IOException {
        byte[] encoded = list(8, utf("a"), utf("a longer string"));
        SequentialNBTReader.List reader = reader(encoded);
        assertArrayEquals(encoded, reader.readFullyAsBinary());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testEmptyList() throws IOException {
        byte[] encoded = list(0);
        SequentialNBTReader.List reader = reader(encoded);
        assertArrayEquals(encoded, reader.readFullyAsBinary());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testRemainingElementsOnly() throws IOException {
        byte[] first = new byte[]{0, 0, 0, 1};
        byte[] second = new byte[]{0, 0, 0, 2};
        SequentialNBTReader.List reader = reader(list(3, first, second));
        reader.next();
        assertArrayEquals(list(3, second), reader.readFullyAsBinary());
        assertFalse(reader.hasNext());
    }
}
