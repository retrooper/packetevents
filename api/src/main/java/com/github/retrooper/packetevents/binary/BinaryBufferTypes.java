package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;

import java.nio.charset.StandardCharsets;

final class BinaryBufferTypes {

    private BinaryBufferTypes() {}

    static final class Float implements BinaryBufferType<java.lang.Float> {
        @Override
        public java.lang.Float read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readFloat(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Float value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeFloat(buffer.getBuffer(), value);
        }
    }
    static final class Double implements BinaryBufferType<java.lang.Double> {
            @Override
            public java.lang.Double read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
                return ByteBufHelper.readDouble(buffer.getBuffer());
            }

            @Override
            public void write(BinaryBuffer buffer, java.lang.Double value, ServerVersion serverVersion, ClientVersion clientVersion) {
                ByteBufHelper.writeDouble(buffer.getBuffer(), value);
            }
    }
    static final class Bool implements BinaryBufferType<Boolean> {

        @Override
        public Boolean read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readBoolean(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, Boolean value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeBoolean(buffer.getBuffer(), value);
        }
    }
    static final class Byte implements BinaryBufferType<java.lang.Byte> {

        @Override
        public java.lang.Byte read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readByte(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Byte value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeByte(buffer.getBuffer(), value);
        }
    }
    static final class UByte implements BinaryBufferType<java.lang.Short> {

        @Override
        public java.lang.Short read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readUnsignedByte(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Short value, ServerVersion serverVersion, ClientVersion clientVersion) {
            int s = value & 0xFF;
            ByteBufHelper.writeByte(buffer.getBuffer(), s);
        }
    }
    static final class Int implements BinaryBufferType<Integer> {

        @Override
        public Integer read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readInt(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, Integer value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeInt(buffer.getBuffer(), value);
        }
    }
    static final class Long implements BinaryBufferType<java.lang.Long> {

        @Override
        public java.lang.Long read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readLong(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Long value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeLong(buffer.getBuffer(), value);
        }
    }
    static final class Short implements BinaryBufferType<java.lang.Short> {

        @Override
        public java.lang.Short read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readShort(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Short value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeShort(buffer.getBuffer(), value);
        }
    }
    static final class VarInt implements BinaryBufferType<Integer> {

        @Override
        public Integer read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            int value = 0;
            int length = 0;
            byte currentByte;
            do {
                currentByte = ByteBufHelper.readByte(buffer.getBuffer());
                value |= (currentByte & 0x7F) << (length * 7);
                length++;
                if (length > 5) {
                    throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
                }
            } while ((currentByte & 0x80) == 0x80);
            return value;
        }

        @Override
        public void write(BinaryBuffer buffer, Integer value, ServerVersion serverVersion, ClientVersion clientVersion) {
            /* Got this code/optimization from https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
             * Copyright and permission notice above (above the class).
             * Steinborn's post says that the code is under the MIT, last accessed 29.06.2024.
             */
            if ((value & (0xFFFFFFFF << 7)) == 0) {
                ByteBufHelper.writeByte(buffer.getBuffer(), value);
            } else if ((value & (0xFFFFFFFF << 14)) == 0) {
                int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
                ByteBufHelper.writeShort(buffer.getBuffer(), w);
            } else if ((value & (0xFFFFFFFF << 21)) == 0) {
                int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
                ByteBufHelper.writeMedium(buffer.getBuffer(), w);
            } else if ((value & (0xFFFFFFFF << 28)) == 0) {
                int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
                        | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
                ByteBufHelper.writeInt(buffer.getBuffer(), w);
            } else {
                int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                        | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
                ByteBufHelper.writeInt(buffer.getBuffer(), w);
                ByteBufHelper.writeByte(buffer.getBuffer(), value >>> 28);
            }
        }
    }
    static final class Medium implements BinaryBufferType<Integer> {

        @Override
        public Integer read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readMedium(buffer.getBuffer());
        }

        @Override
        public void write(BinaryBuffer buffer, Integer value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeMedium(buffer.getBuffer(), value);
        }
    }
    static final class VarLong implements BinaryBufferType<java.lang.Long> {

        @Override
        public java.lang.Long read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            long value = 0;
            int size = 0;
            int b;
            while (((b = ByteBufHelper.readByte(buffer.getBuffer())) & 0x80) == 0x80) {
                value |= (long) (b & 0x7F) << (size++ * 7);
            }
            return value | ((long) (b & 0x7F) << (size * 7));        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Long value, ServerVersion serverVersion, ClientVersion clientVersion) {
            while ((value & ~0x7F) != 0) {
                ByteBufHelper.writeByte(buffer.getBuffer(), (int) (value & 0x7F) | 0x80);
                value >>>= 7;
            }

            ByteBufHelper.writeByte(buffer.getBuffer(), value.intValue());
        }
    }
    static final class String implements BinaryBufferType<java.lang.String> {

        static final int MAX_LENGTH = java.lang.Short.MAX_VALUE;

        private int maxLength = MAX_LENGTH;

        String(int length) {
            if (length < 0 || length > MAX_LENGTH) {
                throw new IllegalArgumentException("String length must be between 0 and " + MAX_LENGTH + ", but was " + length);
            }
            this.maxLength = length;
        }

        String() {}

        @Override
        public java.lang.String read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            int j = buffer.read(BinaryBuffer.VAR_INT);
            // TODO: Don't throw an exception if the string is too long (but still cut it off and probably kick the player)
            if (j > maxLength * 4) {
                throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLength * 4 + ")");
            } else if (j < 0) {
                throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
            } else {
                java.lang.String s = ByteBufHelper.toString(buffer, ByteBufHelper.readerIndex(buffer), j, StandardCharsets.UTF_8);
                ByteBufHelper.readerIndex(buffer, ByteBufHelper.readerIndex(buffer) + j);
                if (s.length() > maxLength) {
                    throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLength + ")");
                } else {
                    return s;
                }
            }
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.String value, ServerVersion serverVersion, ClientVersion clientVersion) {
            buffer.write(BinaryBuffer.VAR_INT, value.length());
            ByteBufHelper.writeBytes(buffer.getBuffer(), value.getBytes(StandardCharsets.UTF_8));
        }
    }
    static final class Uuid implements BinaryBufferType<java.util.UUID> {

        @Override
        public java.util.UUID read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            long mostSigBits = ByteBufHelper.readLong(buffer.getBuffer());
            long leastSigBits = ByteBufHelper.readLong(buffer.getBuffer());
            return new java.util.UUID(mostSigBits, leastSigBits);
        }

        @Override
        public void write(BinaryBuffer buffer, java.util.UUID value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeLong(buffer.getBuffer(), value.getMostSignificantBits());
            ByteBufHelper.writeLong(buffer.getBuffer(), value.getLeastSignificantBits());
        }
    }
    static final class BlockPos implements BinaryBufferType<Vector3i> {

        @Override
        public Vector3i read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            long l = ByteBufHelper.readLong(buffer.getBuffer());
            return new Vector3i(l, serverVersion);
        }

        @Override
        public void write(BinaryBuffer buffer, Vector3i value, ServerVersion serverVersion, ClientVersion clientVersion) {
            long val = value.getSerializedPosition(serverVersion);
            ByteBufHelper.writeLong(buffer.getBuffer(), val);
        }
    }
    static final class Identifier implements BinaryBufferType<ResourceLocation> {

        @Override
        public ResourceLocation read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return new ResourceLocation(buffer.read(BinaryBuffer.STRING));
        }

        @Override
        public void write(BinaryBuffer buffer, ResourceLocation value, ServerVersion serverVersion, ClientVersion clientVersion) {
            buffer.write(BinaryBuffer.STRING, value.toString());
        }
    }
    static final class Nbt implements BinaryBufferType<NBTCompound> {

        @Override
        public NBTCompound read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return (NBTCompound) buffer.read(BinaryBuffer.NBT_RAW);
        }

        @Override
        public void write(BinaryBuffer buffer, NBTCompound value, ServerVersion serverVersion, ClientVersion clientVersion) {
            buffer.write(BinaryBuffer.NBT_RAW, value, serverVersion, clientVersion);
        }
    }
    static final class NbtRaw implements BinaryBufferType<NBT> {

        @Override
        public NBT read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return NBTCodec.readNBTFromBuffer(buffer.getBuffer(), serverVersion);
        }

        @Override
        public void write(BinaryBuffer buffer, NBT value, ServerVersion serverVersion, ClientVersion clientVersion) {
            NBTCodec.writeNBTToBuffer(buffer.getBuffer(), serverVersion, value);
        }
    }
    static final class NbtUnlimited implements BinaryBufferType<NBTCompound> {

        @Override
        public NBTCompound read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return (NBTCompound) buffer.read(BinaryBuffer.NBT_RAW_UNLIMITED);
        }

        @Override
        public void write(BinaryBuffer buffer, NBTCompound value, ServerVersion serverVersion, ClientVersion clientVersion) {
            buffer.write(BinaryBuffer.NBT_RAW, value, serverVersion, clientVersion);
        }
    }
    static final class NbtRawUnlimited implements BinaryBufferType<NBT> {

        @Override
        public NBT read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return NBTCodec.readNBTFromBuffer(buffer.getBuffer(), serverVersion, NBTLimiter.noop());
        }

        @Override
        public void write(BinaryBuffer buffer, NBT value, ServerVersion serverVersion, ClientVersion clientVersion) {
            NBTCodec.writeNBTToBuffer(buffer.getBuffer(), serverVersion, value);
        }
    }
    static final class GameMode implements BinaryBufferType<com.github.retrooper.packetevents.protocol.player.GameMode> {

        @Override
        public com.github.retrooper.packetevents.protocol.player.GameMode read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            byte b = ByteBufHelper.readByte(buffer.getBuffer());
            return com.github.retrooper.packetevents.protocol.player.GameMode.getById(b);
        }

        @Override
        public void write(BinaryBuffer buffer, com.github.retrooper.packetevents.protocol.player.GameMode value, ServerVersion serverVersion, ClientVersion clientVersion) {
            int id = value == null ? -1 : value.getId();
            ByteBufHelper.writeByte(buffer.getBuffer(), id);
        }
    }
    static final class Dimension implements BinaryBufferType<com.github.retrooper.packetevents.protocol.world.Dimension> {

        @Override
        public com.github.retrooper.packetevents.protocol.world.Dimension read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                return new com.github.retrooper.packetevents.protocol.world.Dimension(
                        buffer.read(BinaryBuffer.VAR_INT));
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                    || serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
                com.github.retrooper.packetevents.protocol.world.Dimension dimension = new com.github.retrooper.packetevents.protocol.world.Dimension(new NBTCompound());
                dimension.setDimensionName(buffer.read(BinaryBuffer.IDENTIFIER).toString());
                return dimension;
            } else {
                NBTCompound attrib = buffer.read(BinaryBuffer.NBT);
                return new com.github.retrooper.packetevents.protocol.world.Dimension(attrib);
            }
        }

        @Override
        public void write(BinaryBuffer buffer, com.github.retrooper.packetevents.protocol.world.Dimension value, ServerVersion serverVersion, ClientVersion clientVersion) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                buffer.write(BinaryBuffer.VAR_INT, value.getId());
                return;
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                    || serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
                buffer.write(BinaryBuffer.STRING, value.getDimensionName());
            } else {
                buffer.write(BinaryBuffer.NBT, value.getAttributes());
            }
        }
    }



}
