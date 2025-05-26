package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.PublicProfileKey;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Either;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.util.crypto.SignatureData;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.security.PublicKey;
import java.time.Instant;
import java.util.*;

/**
 * BinaryBuffer is a utility class for reading and writing protocol data from the underlying buffer
 * This class was created to replace heavy usage of {@link PacketWrapper} for reading and writing data.
 * {@link BinaryBufferType} is the format used to read and write data from the buffer.
 * It provides methods to read and write various data types, including primitive types, collections, maps, and custom types.
 * Custom classes that are not defined in the {@link BinaryBufferTypes} class can be added by creating a new class that extends {@link BinaryBufferType}.
 * This is the encouraged way to add new types to the buffer.
 * Example usage:
 * <pre>
 *     class Person {
 *         public static final BinaryBufferType<Person> TYPE = new Serializer();
 *
 *          public static Person read({@link BinaryBuffer} buffer, {@link ServerVersion} serverVersion, {@link ClientVersion} clientVersion) {
 *              return buffer.read(TYPE, serverVersion, clientVersion);
 *          }
 *
 *        private int id;
 *         private String name;
 *
 *         public Person(int id, String name) {
 *              this.id = id;
 *              this.name = name;
 *         }
 *
 *
 *
 *         class Serializer implements {@link BinaryBufferType<Person>} {
 *
 *           {@literal @}Override
 *           public Person read({@link BinaryBuffer} buffer, {@link ServerVersion} serverVersion, {@link ClientVersion} clientVersion) {
 *              int id = buffer.read(BinaryBuffer.INT, serverVersion, clientVersion);
 *              String name = buffer.read(BinaryBuffer.STRING, serverVersion, clientVersion);
 *              return new Person(id, name);
 *         }
 *
 *         {@literal @}Override
 *         public void write({@link BinaryBuffer} buffer, Person value, {@link ServerVersion} serverVersion, {@link ClientVersion} clientVersion) {
 *              buffer.write(BinaryBuffer.INT, value.getId(), serverVersion, clientVersion);
 *              buffer.write(BinaryBuffer.STRING, value.getName(), serverVersion, clientVersion);
 *         }
 *
 *     }
 *
 * </pre>
 *
 * TODO's:
 *   - Add more types to the {@link BinaryBufferTypes} class.
 *   - Convert the types like BYTE_ARRAY, INT_ARRAY, etc to a more reusable method, same with collections
 */
public final class BinaryBuffer {

    public static final BinaryBufferType<byte[]> BYTE_ARRAY = new BinaryBufferTypes.ByteArray();
    public static BinaryBufferType<byte[]> BYTE_ARRAY(int maxSize) {
        return new BinaryBufferTypes.ByteArray(maxSize);
    }

    public static final BinaryBufferType<Byte> BYTE = new BinaryBufferTypes.Byte();
    public static final BinaryBufferType<Short> UBYTE = new BinaryBufferTypes.UByte();
    public static final BinaryBufferType<Boolean> BOOLEAN = new BinaryBufferTypes.Bool();

    public static final BinaryBufferType<Float> FLOAT = new BinaryBufferTypes.Float();
    public static final BinaryBufferType<Double> DOUBLE = new BinaryBufferTypes.Double();

    public static final BinaryBufferType<Short> SHORT = new BinaryBufferTypes.Short();
    public static final BinaryBufferType<Integer> INT = new BinaryBufferTypes.Int();
    public static final BinaryBufferType<Long> LONG = new BinaryBufferTypes.Long();
    public static final BinaryBufferType<Integer> MEDIUM = new BinaryBufferTypes.Medium();

    public static final BinaryBufferType<Integer> VAR_INT = new BinaryBufferTypes.VarInt();
    public static final BinaryBufferType<Long> VAR_LONG = new BinaryBufferTypes.VarLong();

    public static final BinaryBufferType<UUID> UUID = new BinaryBufferTypes.Uuid();
    public static final BinaryBufferType<Vector3i> BLOCK_POSITION = new BinaryBufferTypes.BlockPos();
    public static final BinaryBufferType<GameMode> GAME_MODE = new BinaryBufferTypes.GameMode();
    public static final BinaryBufferType<ResourceLocation> IDENTIFIER = new BinaryBufferTypes.Identifier();
    public static final BinaryBufferType<SaltSignature> SALT_SIGNATURE = new BinaryBufferTypes.Salt();
    public static final BinaryBufferType<PublicKey> PUBLIC_KEY = new BinaryBufferTypes.Publickey();
    public static final BinaryBufferType<Instant> TIMESTAMP = new BinaryBufferTypes.Timestamp();
    public static final BinaryBufferType<PublicProfileKey> PUBLIC_PROFILE_KEY = new BinaryBufferTypes.ProfileKey();
    public static final BinaryBufferType<SignatureData> SIGNATURE_DATA = new BinaryBufferTypes.SignatureData();

    public static <T extends MappedEntity> BinaryBufferType<T> MAPPED_ENTITY(IRegistry<T> registry) {
        return new BinaryBufferTypes.MappedEntity<>(registry);
    }

    /**
     * Use {@link BinaryBuffer#DIMENSION_TYPE} instead
     */
    @Deprecated()
    public static final BinaryBufferType<Dimension> DIMENSION = new BinaryBufferTypes.Dimension();

    public static final BinaryBufferType<NBT> NBT_RAW = new BinaryBufferTypes.NbtRaw();
    public static final BinaryBufferType<NBTCompound> NBT = new BinaryBufferTypes.Nbt();
    public static final BinaryBufferType<NBT> NBT_RAW_UNLIMITED = new BinaryBufferTypes.NbtRawUnlimited();
    public static final BinaryBufferType<NBTCompound> NBT_UNLIMITED = new BinaryBufferTypes.NbtUnlimited();

    public static final BinaryBufferType<String> STRING = new BinaryBufferTypes.String();
    public static BinaryBufferType<String> STRING(int maxSize) {
        return new BinaryBufferTypes.String(maxSize);
    }

    private Object buffer;

    /**
     * Creates a new BinaryBuffer instance with the specified buffer object. This object is specified by the platform's netty ByteBuf
     * @param buffer the underlying buffer object, typically a ByteBuf or similar.
     */
    public BinaryBuffer(Object buffer) {
        this.buffer = buffer;
    }

    /**
     * Creates a new BinaryBuffer instance with the specified capacity and whether it should be IO or direct.
     * @param capacity the initial capacity of the buffer
     * @param io whether the buffer should be an IO buffer (true) or a direct buffer (false)
     */
    public BinaryBuffer(int capacity, boolean io) {
        if (io) {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer(capacity);
        }
        else {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer(capacity);
        }
    }

    /**
     * Creates a new BinaryBuffer instance with the specified capacity, using an IO buffer by default.
     * @param capacity the initial capacity of the buffer
     */
    public BinaryBuffer(int capacity) {
        this(capacity, true);
    }

    /**
     * Creates a new BinaryBuffer instance that wraps the provided byte array.
     * @param wrappedData the byte array to wrap in the buffer
     */
    public BinaryBuffer(byte[] wrappedData) {
        this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(wrappedData);
    }

    /**
     * Resets the reader index of the underlying buffer.
     */
    public void resetReaderIndex() {
        ByteBufHelper.resetReaderIndex(buffer);
    }

    /**
     * Resets the writer index of the underlying buffer.
     */
    public void resetWriterIndex() {
        ByteBufHelper.resetWriterIndex(buffer);
    }

    /**
     * Clears the underlying buffer, resetting both reader and writer indices.
     */
    public void reset() {
        ByteBufHelper.clear(buffer);
    }

    /**
     * Reads a raw byte array from the underlying buffer.
     * @param length the length of the byte array to read
     * @return the byte array read from the buffer, or an empty array if a read is unsuccessful.
     */
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        ByteBufHelper.readBytes(buffer, bytes);
        return bytes;
    }

    /**
     * Writes a raw byte array to the underlying buffer.
     * @param array the byte array to write to the buffer
     */
    public void writeBytes(byte[] array) {
        ByteBufHelper.writeBytes(buffer, array);
    }

    /**
     * Reads a {@link BinaryBufferType}
     * @param type the type to read
     * @param serverVersion the server version to read the type for, these are important in specific cases.
     * @param clientVersion the client version to read the type for, these are important in specific cases.
     * @return The value read from the buffer. Will always return an instance of the type specified's generic, or throw an exception if the read fails.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> T read(BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        return type.read(this, serverVersion, clientVersion);
    }

    /**
     * Reads a {@link BinaryBufferType} while automatically determining the server version based on the current server manager.
     * @param type the type to read
     * @param clientVersion the client version to read the type for, these are important in specific cases.
     * @return The value read from the buffer. Will always return an instance of the type specified's generic, or throw an exception if the read fails.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> T read(BinaryBufferType<T> type, ClientVersion clientVersion) {
        return type.read(this, clientVersion);
    }

    /**
     * Reads a {@link BinaryBufferType} while automatically determining the server and client versions based on the current server manager.
     * @param type the type to read
     * @return The value read from the buffer. Will always return an instance of the type specified's generic, or throw an exception if the read fails.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> T read(BinaryBufferType<T> type) {
        return type.read(this);
    }


    /** Reads an {@link Either} from the underlying buffer, where the first value is read as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is read using the specified leftType, otherwise the right value is read using the specified rightType.
     * @param leftType the type to read the left value from the buffer
     * @param rightType the type to read the right value from the buffer
     * @param serverVersion the server version to read the type for, these are important in specific cases.
     * @param clientVersion the client version to read the type for, these are important in specific cases.
     * @return an Either containing a left or right value based on the boolean read from the buffer.
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> Either<L, R> readEither(BinaryBufferType<L> leftType, BinaryBufferType<R> rightType, ServerVersion serverVersion, ClientVersion clientVersion) {
        boolean isLeft = read(BOOLEAN, serverVersion, clientVersion);
        if (isLeft) {
            return Either.createLeft(read(leftType, serverVersion, clientVersion));
        } else {
            return Either.createRight(read(rightType, serverVersion, clientVersion));
        }
    }

    /** Reads an {@link Either} from the underlying buffer, where the first value is read as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is read using the specified leftType, otherwise the right value is read using the specified rightType.
     * while automatically determining the server version based on the current server manager.
     * @param leftType the type to read the left value from the buffer
     * @param rightType the type to read the right value from the buffer
     * @param clientVersion the client version to read the type for, these are important in specific cases.
     * @return an Either containing a left or right value based on the boolean read from the buffer.
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> Either<L, R> readEither(BinaryBufferType<L> leftType, BinaryBufferType<R> rightType, ClientVersion clientVersion) {
        return readEither(leftType, rightType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /** Reads an {@link Either} from the underlying buffer, where the first value is read as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is read using the specified leftType, otherwise the right value is read using the specified rightType.
     * while automatically determining the server and client versions based on the current server manager.
     * @param leftType the type to read the left value from the buffer
     * @param rightType the type to read the right value from the buffer
     * @return an Either containing a left or right value based on the boolean read from the buffer.
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> Either<L, R> readEither(BinaryBufferType<L> leftType, BinaryBufferType<R> rightType) {
        return readEither(leftType, rightType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /**
     * Writes a value to the underlying buffer using the specified {@link BinaryBufferType}.
     * @param type the type to write
     * @param value the value to write to the buffer
     * @param serverVersion the server version to write the type for, these are important in specific cases.
     * @param clientVersion the client version to write the type for, these are important in specific cases.
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void write(BinaryBufferType<T> type, T value, ServerVersion serverVersion, ClientVersion clientVersion) {
        type.write(this, value, serverVersion, clientVersion);
    }

    /**
     * Writes a value to the underlying buffer using the specified {@link BinaryBufferType} while automatically determining the server version based on the current server manager.
     * @param type the type to write
     * @param value the value to write to the buffer
     * @param clientVersion the client version to write the type for, these are important in specific cases.
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void write(BinaryBufferType<T> type, T value, ClientVersion clientVersion) {
        type.write(this, value, clientVersion);
    }

    /**
     * Writes a value to the underlying buffer using the specified {@link BinaryBufferType} while automatically determining the server and client versions based on the current server manager.
     * @param type the type to write
     * @param value the value to write to the buffer
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void write(BinaryBufferType<T> type, T value) {
        type.write(this, value);
    }

    /** Writes an {@link Either} to the underlying buffer, where the first value is written as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is written using the specified leftType, otherwise the right value is written using the specified rightType.
     * @param either the Either to write to the buffer
     * @param leftType the type to write the left value to the buffer
     * @param rightType the type to write the right value to the buffer
     * @param serverVersion the server version to write the type for, these are important in specific cases.
     * @param clientVersion the client version to write the type for, these are important in specific cases.
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> void writeEither(Either<L, R> either, BinaryBufferType<L> leftType, BinaryBufferType<R> rightType, ServerVersion serverVersion, ClientVersion clientVersion) {
        if (either.isLeft()) {
            write(BOOLEAN, true, serverVersion, clientVersion);
            write(leftType, either.getLeft(), serverVersion, clientVersion);
        } else {
            write(BOOLEAN, false, serverVersion, clientVersion);
            write(rightType, either.getRight(), serverVersion, clientVersion);
        }
    }

    /** Writes an {@link Either} to the underlying buffer, where the first value is written as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is written using the specified leftType, otherwise the right value is written using the specified rightType.
     * while automatically determining the server version based on the current server manager.
     * @param either the Either to write to the buffer
     * @param leftType the type to write the left value to the buffer
     * @param rightType the type to write the right value to the buffer
     * @param clientVersion the client version to write the type for, these are important in specific cases.
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> void writeEither(Either<L, R> either, BinaryBufferType<L> leftType, BinaryBufferType<R> rightType, ClientVersion clientVersion) {
        writeEither(either, leftType, rightType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /** Writes an {@link Either} to the underlying buffer, where the first value is written as a boolean to determine if it is a left or right value.
     * If the boolean is true, the left value is written using the specified leftType, otherwise the right value is written using the specified rightType.
     * while automatically determining the server and client versions based on the current server manager.
     * @param either the Either to write to the buffer
     * @param leftType the type to write the left value to the buffer
     * @param rightType the type to write the right value to the buffer
     * @param <L> the type of the left value, this is the generic of the {@link BinaryBufferType} specified for left values.
     * @param <R> the type of the right value, this is the generic of the {@link BinaryBufferType} specified for right values.
     */
    public <L, R> void writeEither(Either<L, R> either, BinaryBufferType<L> leftType, BinaryBufferType<R> rightType) {
        writeEither(either, leftType, rightType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }


    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification.
     * @param maxSize the maximum size of the collection to read, if the size exceeds this value an {@link IllegalArgumentException} will be thrown.
     * @param type the type to read from the buffer
     * @param serverVersion the server version to read the type for, these are important in specific cases These get passed into the type.
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        int size = read(VAR_INT, serverVersion, clientVersion);
        if (size > maxSize) {
            throw new IllegalArgumentException("Size of collection is too big: " + size);
        }
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(read(type, serverVersion, clientVersion));
        }
        return list;
    }


    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server version based on the current server manager.
     * @param maxSize the maximum size of the collection to read, if the size exceeds this value an {@link IllegalArgumentException} will be thrown.
     * @param type the type to read from the buffer
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type, ClientVersion clientVersion) {
        return readCollection(maxSize, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server and client versions based on the current server manager.
     * @param maxSize the maximum size of the collection to read, if the size exceeds this value an {@link IllegalArgumentException} will be thrown.
     * @param type the type to read from the buffer
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type) {
        return readCollection(maxSize, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification with a maximum size of {@link Short#MAX_VALUE}.
     * @param type the type to read from the buffer
     * @param serverVersion the server version to read the type for, these are important in specific cases These get passed into the type.
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> Collection<T> readCollection(BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        return readCollection(Short.MAX_VALUE, type, serverVersion, clientVersion);
    }

    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification with a maximum size of {@link Short#MAX_VALUE} while automatically determining the server version based on the current server manager.
     * @param type the type to read from the buffer
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> Collection<T> readCollection(BinaryBufferType<T> type, ClientVersion clientVersion) {
        return readCollection(Short.MAX_VALUE, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /**
     * Reads a collection using {@link BinaryBuffer#VAR_INT} for the size specification with a maximum size of {@link Short#MAX_VALUE} while automatically determining the server and client versions based on the current server manager.
     * @param type the type to read from the buffer
     * @return a collection of the specified type read from the buffer, or an empty collection if the read is unsuccessful.
     * @param <T> the type to read, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> Collection<T> readCollection(BinaryBufferType<T> type) {
        return readCollection(Short.MAX_VALUE, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /**
     * Writes a collection using {@link BinaryBuffer#VAR_INT} for the size specification.
     * @param collection the collection to write to the buffer
     * @param type the type to write to the buffer
     * @param serverVersion the server version to write the type for, these are important in specific cases These get passed into the type.
     * @param clientVersion the client version to write the type for, these are important in specific cases These get passed into the type.
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        write(VAR_INT, collection.size(), serverVersion, clientVersion);
        for (T t : collection) {
            write(type, t, serverVersion, clientVersion);
        }
    }

    /**
     * Writes a collection using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server version based on the current server manager.
     * @param collection the collection to write to the buffer
     * @param type the type to write to the buffer
     * @param clientVersion the client version to write the type for, these are important in specific cases These get passed into the type.
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type, ClientVersion clientVersion) {
        writeCollection(collection, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /**
     * Writes a collection using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server and client versions based on the current server manager.
     * @param collection the collection to write to the buffer
     * @param type the type to write to the buffer
     * @param <T> the type to write, this is the generic of the {@link BinaryBufferType} specified.
     */
    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type) {
        writeCollection(collection, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }


    /**
     * Writes a map to the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification. Every iteration it reads the keyType, and then the valueType
     * @param keyType the type to read the keys from the buffer
     * @param valueType the type to read the values from the buffer
     * @param serverVersion the server version to read the type for, these are important in specific cases These get passed into the type.
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a map of the specified types read from the buffer, or an empty map if the read is unsuccessful. {@link Map}
     * @param <K> the type to read the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to read the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ServerVersion serverVersion, ClientVersion clientVersion) {
        int size = read(VAR_INT, serverVersion, clientVersion);
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = read(keyType, serverVersion, clientVersion);
            V value = read(valueType, serverVersion, clientVersion);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Reads a map from the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server version based on the current server manager.
     * @param keyType the type to read the keys from the buffer
     * @param valueType the type to read the values from the buffer
     * @param clientVersion the client version to read the type for, these are important in specific cases These get passed into the type.
     * @return a map of the specified types read from the buffer, or an empty map if the read is unsuccessful. {@link Map}
     * @param <K> the type to read the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to read the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ClientVersion clientVersion) {
        return readMap(keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /**
     * Reads a map from the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server and client versions based on the current server manager.
     * @param keyType the type to read the keys from the buffer
     * @param valueType the type to read the values from the buffer
     * @return a map of the specified types read from the buffer, or an empty map if the read is unsuccessful. {@link Map}
     * @param <K> the type to read the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to read the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType) {
        return readMap(keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /**
     * Writes a map to the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification. Every iteration it writes the keyType, and then the valueType
     * @param map the map to write to the buffer
     * @param keyType the type to write the keys to the buffer
     * @param valueType the type to write the values to the buffer
     * @param serverVersion the server version to write the type for, these are important in specific cases These get passed into the type.
     * @param clientVersion the client version to write the type for, these are important in specific cases These get passed into the type.
     * @param <K> the type to write the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to write the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ServerVersion serverVersion, ClientVersion clientVersion) {
        write(VAR_INT, map.size(), serverVersion, clientVersion);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            write(keyType, entry.getKey(), serverVersion, clientVersion);
            write(valueType, entry.getValue(), serverVersion, clientVersion);
        }
    }

    /**
     * Writes a map to the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server version based on the current server manager.
     * @param map the map to write to the buffer
     * @param keyType the type to write the keys to the buffer
     * @param valueType the type to write the values to the buffer
     * @param clientVersion the client version to write the type for, these are important in specific cases These get passed into the type.
     * @param <K> the type to write the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to write the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ClientVersion clientVersion) {
        writeMap(map, keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    /**
     * Writes a map to the underlying buffer using {@link BinaryBuffer#VAR_INT} for the size specification while automatically determining the server and client versions based on the current server manager.
     * @param map the map to write to the buffer
     * @param keyType the type to write the keys to the buffer
     * @param valueType the type to write the values to the buffer
     * @param <K> the type to write the keys, this is the generic of the {@link BinaryBufferType} specified for keys.
     * @param <V> the type to write the values, this is the generic of the {@link BinaryBufferType} specified for values.
     */
    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType) {
        writeMap(map, keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }


    /**
     * Reads an enum value from the underlying buffer using its ordinal. The ordinal is read using {@link BinaryBuffer#VAR_INT}.
     * @param enumClass the class of the enum to read
     * @return the enum value read from the buffer, or throws an {@link IllegalArgumentException} if the ordinal is invalid.
     * @param <T> the type of the enum to read, this is the generic of the enum class specified.
     */
    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        int ordinal = read(VAR_INT);
        if (ordinal < 0 || ordinal >= constants.length) {
            throw new IllegalArgumentException("Invalid ordinal for enum " + enumClass.getName() + ": " + ordinal);
        }
        return constants[ordinal];
    }

    /**
     * Writes an enum value to the underlying buffer using its ordinal. The ordinal is written using {@link BinaryBuffer#VAR_INT}.
     * @param value the enum value to write to the buffer
     * @param <T> the type of the enum to write, this is the generic of the enum class specified.
     */
    public <T extends Enum<T>> void writeEnum(T value) {
        write(VAR_INT, value.ordinal());
    }

    /**
     * Returns the underlying buffer object. These should rarely be used outside packetevents internals.
     * @return the underlying buffer object, typically a ByteBuf or similar.
     */
    public Object getBuffer() {
        return buffer;
    }
}
