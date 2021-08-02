/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.packetwrappers;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.exceptions.WrapperFieldNotFoundException;
import io.github.retrooper.packetevents.exceptions.WrapperUnsupportedUsageException;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrapperPacketReader;
import io.github.retrooper.packetevents.packetwrappers.api.WrapperPacketWriter;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.utils.world.Difficulty;
import io.github.retrooper.packetevents.utils.world.Dimension;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class WrappedPacket implements WrapperPacketReader, WrapperPacketWriter {

    private static final Map<Class<? extends WrappedPacket>, Boolean> LOADED_WRAPPERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    private static byte isVersion_1_17 = -1;
    public static ServerVersion version;
    protected final NMSPacket packet;
    private final Class<?> packetClass;

    public WrappedPacket() {
        packet = null;
        packetClass = null;
        load0();
    }

    public WrappedPacket(final NMSPacket packet) {
        this(packet, packet.getRawNMSPacket().getClass());
    }

    public WrappedPacket(final NMSPacket packet, Class<?> packetClass) {
        if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Client.FLYING)) {
            packetClass = PacketTypeClasses.Play.Client.FLYING;
        } else if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Play.Server.ENTITY;
        }
        this.packetClass = packetClass;
        this.packet = packet;
        load0();
    }

    private void load0() {
        final Class<? extends WrappedPacket> clazz = getClass();
        if (!LOADED_WRAPPERS.containsKey(clazz)) {
            if (!isSupported()) {
                throw new WrapperUnsupportedUsageException(getClass());
            }
            try {
                load();
            } catch (Exception ex) {
                String wrapperName = ClassUtil.getClassSimpleName(clazz);
                PacketEvents.get().getPlugin().getLogger()
                        .log(Level.SEVERE, "PacketEvents found an exception while loading the " + wrapperName + " packet wrapper. Please report this bug! Tell us about your server version, spigot and code(of you using the wrapper)", ex);
                LOADED_WRAPPERS.put(clazz, false);
            }
            LOADED_WRAPPERS.put(clazz, true);
        }
    }

    protected void load() {
    }

    protected boolean hasLoaded() {
        return LOADED_WRAPPERS.getOrDefault(getClass(), false);
    }

    protected void throwUnsupportedOperation(Enum<?> enumConst) throws UnsupportedOperationException {
        Class<?> enumConstClass = enumConst.getClass();
        Field field = null;
        try {
            field = enumConstClass.getField(enumConst.name());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field.isAnnotationPresent(SupportedVersions.class)) {
            SupportedVersions supportedVersions = field.getAnnotation(SupportedVersions.class);
            List<ServerVersion> versionList = parseSupportedVersionsAnnotation(supportedVersions);
            String supportedVersionsMsg = Arrays.toString(versionList.toArray(new ServerVersion[0]));
            throw new UnsupportedOperationException("PacketEvents failed to use the " + enumConst.name() + " enum constant in the " + enumConstClass.getSimpleName() + " enum. This enum constant is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n This enum constant is only supported on these server versions: " + supportedVersionsMsg);
        } else {
            throw new UnsupportedOperationException("PacketEvents failed to use the " + enumConst.name() + " enum constant in the " + enumConstClass.getSimpleName() + " enum. This enum constant is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n Failed to find out what server versions this enum constant is supported on.");
        }
    }

    protected void throwUnsupportedOperation() throws UnsupportedOperationException {
        final String currentMethodName = "throwUnsupportedOperation";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int stackTraceElementIndex = 2;
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement element = stackTraceElements[i];
            if (element.getMethodName().equals(currentMethodName)) {
                stackTraceElementIndex = i + 1; //It is the next method
                break;
            }
        }
        StackTraceElement stackTraceElement = stackTraceElements[stackTraceElementIndex];
        String methodName = stackTraceElement.getMethodName();
        List<Method> possibleMethods = Reflection.getMethods(getClass(), methodName, (Class<?>) null);
        Method method = null;
        for (Method m : possibleMethods) {
            if (m.isAnnotationPresent(SupportedVersions.class)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new UnsupportedOperationException("PacketEvents failed to access your requested field. This field is not supported on your server version. Failed to lookup the server versions this field supports...");
        } else {
            SupportedVersions supportedVersions = method.getAnnotation(SupportedVersions.class);
            List<ServerVersion> versionList = parseSupportedVersionsAnnotation(supportedVersions);

            String supportedVersionsMsg = Arrays.toString(versionList.toArray(new ServerVersion[0]));
            throw new UnsupportedOperationException("PacketEvents failed to access your requested field. This field is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n This field is only supported on these server versions: " + supportedVersionsMsg);
        }
    }

    private List<ServerVersion> parseSupportedVersionsAnnotation(SupportedVersions supportedVersions) {
        List<ServerVersion> versionList = new ArrayList<>();
        for (int i = 0; i < supportedVersions.ranges().length; i += 2) {
            ServerVersion first = supportedVersions.ranges()[i];
            ServerVersion last = supportedVersions.ranges()[i + 1];
            if (first == last) {
                versionList.add(first);
                continue;
            } else if (first == ServerVersion.ERROR) {
                first = ServerVersion.getOldest();
            } else if (last == ServerVersion.ERROR) {
                last = ServerVersion.getLatest();
            }
            versionList.addAll(Arrays.asList(ServerVersion.values()).subList(first.ordinal(), last.ordinal() + 1));
        }
        versionList.remove(ServerVersion.ERROR);
        return versionList;
    }


    @Override
    public boolean readBoolean(int index) {
        return read(index, boolean.class);
    }

    @Override
    public byte readByte(int index) {
        return read(index, byte.class);
    }

    @Override
    public short readShort(int index) {
        return read(index, short.class);
    }

    @Override
    public int readInt(int index) {
        return read(index, int.class);
    }

    @Override
    public long readLong(int index) {
        return read(index, long.class);
    }

    @Override
    public float readFloat(int index) {
        return read(index, float.class);
    }

    @Override
    public double readDouble(int index) {
        return read(index, double.class);
    }

    @Override
    public boolean[] readBooleanArray(int index) {
        return read(index, boolean[].class);
    }

    @Override
    public byte[] readByteArray(int index) {
        return read(index, byte[].class);
    }

    @Override
    public short[] readShortArray(int index) {
        return read(index, short[].class);
    }

    @Override
    public int[] readIntArray(int index) {
        return read(index, int[].class);
    }

    @Override
    public long[] readLongArray(int index) {
        return read(index, long[].class);
    }

    @Override
    public float[] readFloatArray(int index) {
        return read(index, float[].class);
    }

    @Override
    public double[] readDoubleArray(int index) {
        return read(index, double[].class);
    }

    @Override
    public String[] readStringArray(int index) {
        return read(index, String[].class); // JavaImpact: Can we be sure that returning the original array is okay? retrooper: Yes
    }

    @Override
    public String readString(int index) {
        return read(index, String.class);
    }

    @Override
    public Object readAnyObject(int index) {
        try {
            Field f = packetClass.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            try {
                return f.get(packet.getRawNMSPacket());
            } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(packetClass) + " class!");
        }
        return null;
    }

    @Override
    public <T> T readObject(int index, Class<? extends T> type) {
        return read(index, type);
    }

    @Override
    public Enum<?> readEnumConstant(int index, Class<? extends Enum<?>> type) {
        return read(index, type);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(int index, Class<? extends T> type) {
        try {
            Field field = getField(type, index);
            return (T) field.get(packet.getRawNMSPacket());
        } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
    }


    @Override
    public void writeBoolean(int index, boolean value) {
        write(boolean.class, index, value);
    }

    @Override
    public void writeByte(int index, byte value) {
        write(byte.class, index, value);
    }

    @Override
    public void writeShort(int index, short value) {
        write(short.class, index, value);
    }

    @Override
    public void writeInt(int index, int value) {
        write(int.class, index, value);
    }

    @Override
    public void writeLong(int index, long value) {
        write(long.class, index, value);
    }

    @Override
    public void writeFloat(int index, float value) {
        write(float.class, index, value);
    }

    @Override
    public void writeDouble(int index, double value) {
        write(double.class, index, value);
    }

    @Override
    public void writeString(int index, String value) {
        write(String.class, index, value);
    }

    @Override
    public void writeObject(int index, Object value) {
        write(value.getClass(), index, value);
    }

    @Override
    public void writeBooleanArray(int index, boolean[] array) {
        write(boolean[].class, index, array);
    }

    @Override
    public void writeByteArray(int index, byte[] value) {
        write(byte[].class, index, value);
    }

    @Override
    public void writeShortArray(int index, short[] value) {
        write(short[].class, index, value);
    }

    @Override
    public void writeIntArray(int index, int[] value) {
        write(int[].class, index, value);
    }

    @Override
    public void writeLongArray(int index, long[] value) {
        write(long[].class, index, value);
    }

    @Override
    public void writeFloatArray(int index, float[] value) {
        write(float[].class, index, value);
    }

    @Override
    public void writeDoubleArray(int index, double[] value) {
        write(double[].class, index, value);
    }

    @Override
    public void writeStringArray(int index, String[] value) {
        write(String[].class, index, value);
    }

    @Override
    public void writeAnyObject(int index, Object value) {
        try {
            Field f = packetClass.getDeclaredFields()[index];
            f.set(packet.getRawNMSPacket(), value);
        } catch (Exception e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(packetClass) + " class!");
        }
    }

    @Override
    public void writeEnumConstant(int index, Enum<?> enumConstant) {
        try {
            write(enumConstant.getClass(), index, enumConstant);
        }
        catch (WrapperFieldNotFoundException ex) {
            write(enumConstant.getDeclaringClass(), index, enumConstant);
        }
    }

    public void write(Class<?> type, int index, Object value) throws WrapperFieldNotFoundException {
        Field field = getField(type, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
        try {
            field.set(packet.getRawNMSPacket(), value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Vector3i readBlockPosition(int index) {
        Object blockPosObj = readObject(index, NMSUtils.blockPosClass);
        try {
            int x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
            int y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
            int z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
            return new Vector3i(x, y, z);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeBlockPosition(int index, Vector3i blockPosition) {
        Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPosition);
        write(NMSUtils.blockPosClass, index, blockPosObj);
    }

    public ItemStack readItemStack(int index) {
        Object nmsItemStack = readObject(index, NMSUtils.nmsItemStackClass);
        return NMSUtils.toBukkitItemStack(nmsItemStack);
    }

    public void writeItemStack(int index, ItemStack stack) {
        Object nmsItemStack = NMSUtils.toNMSItemStack(stack);
        write(NMSUtils.nmsItemStackClass, index, nmsItemStack);
    }

    public GameMode readGameMode(int index) {
        Enum<?> enumConst = readEnumConstant(index, NMSUtils.enumGameModeClass);
        return GameMode.values()[enumConst.ordinal() - 1];
    }

    public void writeGameMode(int index, GameMode gameMode) {
        Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumGameModeClass, gameMode.ordinal() + 1);
        writeEnumConstant(index, enumConst);
    }

    public Dimension readDimension(int index, int dimensionIDLegacyIndex) {
        int dimensionID;
        if (version.isOlderThan(ServerVersion.v_1_13_2)) {
            dimensionID = readInt(dimensionIDLegacyIndex);
        } else {
            Object dimensionManagerObject = readObject(index, NMSUtils.dimensionManagerClass);
            WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
            dimensionID = dimensionManagerWrapper.readInt(0) - 1;
        }
        return Dimension.getById(dimensionID);
    }

    public void writeDimension(int index, int dimensionIDLegacyIndex, Dimension dimension) {
        if (version.isOlderThan(ServerVersion.v_1_13_2)) {
            writeInt(dimensionIDLegacyIndex, dimension.getId());
        } else {
            Object dimensionManagerObject = readObject(index, NMSUtils.dimensionManagerClass);
            WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
            dimensionManagerWrapper.writeInt(0, dimension.getId() + 1);
        }
    }


    public Difficulty readDifficulty(int index) {
        Enum<?> enumConstant = readEnumConstant(index, NMSUtils.enumDifficultyClass);
        return Difficulty.values()[enumConstant.ordinal()];
    }

    public void writeDifficulty(int index, Difficulty difficulty) {
        Enum<?> enumConstant = EnumUtil.valueByIndex(NMSUtils.enumDifficultyClass, difficulty.ordinal());
        writeEnumConstant(index, enumConstant);
    }

    public String readIChatBaseComponent(int index) {
        Object iChatBaseComponent = readObject(index, NMSUtils.iChatBaseComponentClass);
        return NMSUtils.readIChatBaseComponent(iChatBaseComponent);
    }

    public void writeIChatBaseComponent(int index, String content) {
        Object iChatBaseComponent = NMSUtils.generateIChatBaseComponent(content);
        write(NMSUtils.iChatBaseComponentClass, index, iChatBaseComponent);
    }

    public String readMinecraftKey(int index) {
        if (isVersion_1_17 == -1) {
            isVersion_1_17 = (byte) (version.isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }
        int namespaceIndex = isVersion_1_17 == 1 ? 2 : 0;
        int keyIndex = isVersion_1_17 == 1 ? 3 : 1;
        Object minecraftKey = readObject(index, NMSUtils.minecraftKeyClass);
        WrappedPacket minecraftKeyWrapper = new WrappedPacket(new NMSPacket(minecraftKey));
        return minecraftKeyWrapper.readString(namespaceIndex) + ":" + minecraftKeyWrapper.readString(keyIndex);
    }

    public void writeMinecraftKey(int index, String content) {
        Object minecraftKey = null;
        try {
            minecraftKey = NMSUtils.minecraftKeyConstructor.newInstance(content);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        write(NMSUtils.minecraftKeyClass, index, minecraftKey);
    }

    public List<Object> readList(int index) {
        return read(index, List.class);
    }

    public void writeList(int index, List<Object> list) {
        write(List.class, index, list);
    }

    private Field getField(Class<?> type, int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.computeIfAbsent(packetClass, k -> new ConcurrentHashMap<>());
        Field[] fields = cached.computeIfAbsent(type, typeClass -> getFields(typeClass, packetClass.getDeclaredFields()));
        if (fields.length >= index + 1) {
            return fields[index];
        } else {
            throw new WrapperFieldNotFoundException(packetClass, type, index);
        }
    }

    private Field[] getFields(Class<?> type, Field[] fields) {
        List<Field> ret = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(type)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                ret.add(field);
            }
        }
        return ret.toArray(EMPTY_FIELD_ARRAY);
    }


    /**
     * Does the local server version support reading at-least one field with this packet wrapper?
     * If it does, we can label this wrapper to be supported on the local server version.
     * One example where it would not be supported would be if the packet the wrapper is wrapping doesn't even exist on the local server version.
     *
     * @return Is the wrapper supported on the local server version?
     */
    public boolean isSupported() {
        return true;
    }

    /**
     * If a method in a wrapper is annotated with this, it means it isn't supported on all server versions.
     * It not being supported by all server versions can lead to exceptions. Make sure you always decompile a wrapper getter before using it.
     * This annotation will specify what versions are supported.
     *
     * @author retrooper
     * @since 1.8
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SupportedVersions {
        ServerVersion[] ranges() default {};
    }
}
