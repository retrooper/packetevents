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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * UNFINISHED, DO NOT USE
 */
public class WrappedPacketOutEntityMetadata extends WrappedPacket {
    private static Constructor<?> watchableObjectConstructor;
    public WrappedPacketOutEntityMetadata(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            watchableObjectConstructor = NMSUtils.watchableObjectClass.getConstructor(int.class, int.class, Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEntityId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return 0;
        }
    }

    public List<WrappedWatchableObject> getWatchableObjects() {
        List<Object> nmsWatchables = (List<Object>) readObject(0, List.class);
        List<WrappedWatchableObject> watchableObjects = new ArrayList<>();
        for (Object nmsWatchable : nmsWatchables) {
            watchableObjects.add(new WrappedWatchableObject(nmsWatchable));
        }
        return watchableObjects;
    }

    public static class WrappedWatchableObject {
        private final int type;
        private final int index;
        private Object value;

        public WrappedWatchableObject(int type, int index, Object value) {
            this.type = type;
            this.index = index;
            this.value = value;
        }

        public WrappedWatchableObject(Object nmsWatchableObject) {
            WrappedPacket watchableWrapper = new WrappedPacket(new NMSPacket(nmsWatchableObject));
            this.type = watchableWrapper.readInt(0);
            this.index = watchableWrapper.readInt(1);
            this.value = watchableWrapper.readObject(0, Object.class);
        }

        public int getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object asMojangWatchableObject() {
            try {
                return watchableObjectConstructor.newInstance(type, index, value);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
