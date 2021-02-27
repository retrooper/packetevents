package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.exceptions.WrapperFieldNotFoundException;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A WatchableObject.
 *
 * @author SteelPhoenix
 */
public class WrappedWatchableObject extends WrappedPacket {

    private static final Map<Class<?>, Integer> ID_MAP = new HashMap<>();
    private static Class<?> TYPE;
    private static Field VALUE;
    private static Field DIRTY;
    private static Field INDEX;
    private static Field DWOBJECT;
    private static Constructor<?> CONSTRUCTOR;
    private static final String[] NMS_ALIASES = {"DataWatcher$Item", "DataWatcher$WatchableObject", "WatchableObject"};

    public WrappedWatchableObject(Object nms) {
        super(new NMSPacket(nms));
    }

    public WrappedWatchableObject(DataWatcherObject object, Object value) {
        this(newHandle(object, value));
    }

    /**
     * Create a watchable object instance.
     *
     * @param object Data watcher.
     * @param value  Data value.
     * @return the watchable object.
     */
    private static Object newHandle(DataWatcherObject object, Object value) {
        // Preconditions
        if (object == null) {
            throw new IllegalArgumentException("Data watcher object cannot be null");
        }

        // 1.9+
        if (DataWatcherObject.isPresent()) {
            try {
                return CONSTRUCTOR.newInstance(object.getRaw(), value);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {

                throw new RuntimeException("Could not invoke constructor", exception);
            }
        }

        try {
            return CONSTRUCTOR.newInstance(Optional.ofNullable(ID_MAP.get(value.getClass()))
                    .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + value.getClass())), object.getIndex(), value);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {

            throw new RuntimeException("Could not invoke constructor", exception);
        }
    }

    @Override
    protected void load() {
        // WHY ARE THERE NO REFLECTION HELPERS REEEEEEEEEEEEEEEEEEEEEEE

        // Data watcher item
        Class<?> clazz = null;
        for (String s : NMS_ALIASES) {
            try {
                clazz = NMSUtils.getNMSClass(s);
                break;
            } catch (ClassNotFoundException exception) {
                // Nothing
            }
        }

        // We did not find the correct class from the aliases
        if (clazz == null) {
            // TODO: Search DataWatcher class for required class directly
            throw new RuntimeException("No datawatcher class found");
        }

        TYPE = clazz;

        // Value field
        // Some NMS bs with the field index
        VALUE = TYPE.getDeclaredFields()[DataWatcherObject.isPresent() ? 1 : 2];

        // Dirty field
        Field dirty = null;
        for (Field f : TYPE.getDeclaredFields()) {
            if (f.getType() == Boolean.TYPE) {
                dirty = f;
                break;
            }
        }

        if (dirty == null) {
            throw new WrapperFieldNotFoundException(TYPE, Boolean.class, 0);
        }

        dirty.setAccessible(true);

        DIRTY = dirty;

        if (DataWatcherObject.isPresent()) {
            Field field = null;
            for (Field f : TYPE.getDeclaredFields()) {
                if (f.getType() == DataWatcherObject.TYPE) {
                    field = f;
                    break;
                }
            }

            if (field == null) {
                throw new WrapperFieldNotFoundException(TYPE, DataWatcherObject.TYPE, 0);
            }

            INDEX = null;
            DWOBJECT = field;
        } else {

            Field index = null;
            int i = 0;
            for (Field f : TYPE.getDeclaredFields()) {
                if (f.getType() == Integer.TYPE) {
                    i++;
                    if (i == 1) {
                        index = f;
                        break;
                    }
                }
            }

            if (index == null) {
                throw new WrapperFieldNotFoundException(TYPE, Integer.TYPE, 1);
            }

            index.setAccessible(true);

            INDEX = index;
            DWOBJECT = null;
        }


        // Only one constructor exists in known versions
        // Which one depends on DataWatcherObject#isPresent()
        CONSTRUCTOR = TYPE.getConstructors()[0];

        // Legacy ID map
        ID_MAP.put(Byte.class, 0);
        ID_MAP.put(Short.class, 1);
        ID_MAP.put(Integer.class, 2);
        ID_MAP.put(Float.class, 3);
        ID_MAP.put(String.class, 4);
        Class<?> item;
        try {
            item = NMSUtils.getNMSClass("ItemStack");
        } catch (ClassNotFoundException exception) {
            // TODO: Get type from CraftBukkit handle field
            throw new RuntimeException("No ItemStack class found");
        }
        ID_MAP.put(item, 5);
        Class<?> position;
        try {
            position = NMSUtils.getNMSClass("BlockPosition");
        } catch (ClassNotFoundException exception) {
            // TODO: Get type from CraftBukkit handle field
            throw new RuntimeException("No BlockPosition class found");
        }
        ID_MAP.put(position, 6);
        Class<?> vector3f;
        try {
            vector3f = NMSUtils.getNMSClass("Vector3f");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("No vector class found");
        }
        ID_MAP.put(vector3f, 7);
    }

    /**
     * Get the item's index.
     *
     * @return the index.
     */
    public int getIndex() {
        if (DataWatcherObject.isPresent()) {
            Object value;
            try {
                value = DWOBJECT.get(packet.getRawNMSPacket());
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                throw new RuntimeException("Could not read field value", exception);
            }

            if (value == null) {
                throw new NullPointerException("No data watcher object set");
            }

            return DataWatcherObject.fromHandle(value).getIndex();
        }

        try {
            return (int) INDEX.get(packet.getRawNMSPacket());
        } catch (IllegalArgumentException | IllegalAccessException exception) {

            throw new RuntimeException("Could not read field value", exception);
        }
    }

    /**
     * Get the raw (nms) data value.
     *
     * @return the current value.
     */
    public Object getValueRaw() {
        try {
            return VALUE.get(packet.getRawNMSPacket());
        } catch (IllegalArgumentException | IllegalAccessException exception) {

            throw new RuntimeException("Could not read field value", exception);
        }
    }

    /**
     * Set the raw (nms) data value.
     *
     * @param value New value.
     */
    public void setValueRaw(Object value) {
        try {
            VALUE.set(packet.getRawNMSPacket(), value);
        } catch (IllegalArgumentException | IllegalAccessException exception) {
            throw new RuntimeException("Could not write field value", exception);
        }
    }

    /**
     * Get if this value is dirty.
     *
     * @return the current value.
     * @deprecated Although this fields exists, it is not part of the protocol spec.
     */
    @Deprecated
    public boolean isDirty() {
        try {
            return (boolean) DIRTY.get(packet.getRawNMSPacket());
        } catch (IllegalArgumentException | IllegalAccessException exception) {

            throw new RuntimeException("Could not read field value", exception);
        }
    }

    /**
     * Set if this data is dirty.
     *
     * @param value New value.
     * @deprecated Although this fields exists, it is not part of the protocol spec.
     */
    @Deprecated
    public void setDirty(boolean value) {
        try {
            DIRTY.set(packet.getRawNMSPacket(), value);
        } catch (IllegalArgumentException | IllegalAccessException exception) {

            throw new RuntimeException("Could not write field value", exception);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getIndex())
                .append(getValueRaw().hashCode())
                .append(isDirty())
                .build();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WrappedWatchableObject)) {
            return false;
        }
        WrappedWatchableObject other = (WrappedWatchableObject) object;
        return new EqualsBuilder()
                .append(getIndex(), other.getIndex())
                .append(getValueRaw(), other.getValueRaw())
                .append(isDirty(), other.isDirty())
                .build();
    }

    @Override
    public String toString() {
        return "WatchableObject[index=" + getIndex() + ", value=" + getValueRaw() + ", dirty=" + isDirty() + "]";
    }

    public Object getRaw() {
        return packet.getRawNMSPacket();
    }
}