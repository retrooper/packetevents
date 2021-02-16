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

import java.util.ArrayList;
import java.util.List;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

/**
 * A PacketPlayOutEntityMetadata.
 * Update metadata properties for an existing entity.
 * Any properties not included are left unchanged.
 *
 * @author SteelPhoenix
 */
public class WrappedPacketOutEntityMetadata extends WrappedPacket {

	public WrappedPacketOutEntityMetadata(NMSPacket packet) {
		super(packet);
	}

	// TODO: PacketPlayOutEntityMetadata constructors

	/**
	 * Get the entity ID.
	 *
	 * @return the current value.
	 */
	public int getEntityId() {
    	return readInt(0);
	}

	/**
	 * Set the entity ID.
	 *
	 * @param value New value.
	 */
	public void setEntityId(int value) {
		writeInt(0, value);
	}

	/**
	 * Get the entity metadata.
	 * Note that null values are preserved.
	 *
	 * @return the current value.
	 */
	public List<WrappedWatchableObject> getWatchableObjects() {
		// We assume this is the right type
		List<?> nms = (List<?>) readObject(0, List.class);

		// Wrap
		// We assume every element can be wrapped
		List<WrappedWatchableObject> watchableObjects = new ArrayList<>();
		for (Object nmsWatchable : nms) {
			watchableObjects.add(new WrappedWatchableObject(nmsWatchable));
		}

		return watchableObjects;
	}

	/**
	 * Set the entity metadata.
	 * Note that null values are preserved.
	 *
	 * @param value New value.
	 */
	@SuppressWarnings("unchecked")
	public void setWatchableObjects(List<WrappedWatchableObject> value) {
		// Preconditions
		if (value == null) {
			throw new NullPointerException("Object cannot be null");
		}

		// Try to maintain list type if possible
		List<Object> list;
		try {
			list = value.getClass().newInstance();
		} catch (ReflectiveOperationException | SecurityException exception) {
			list = new ArrayList<>();
		}

		for (WrappedWatchableObject wrappedWatchable : value) {
			// Handle nulls
			if (wrappedWatchable == null) {
				list.add(null);
			}

			// Only add if a raw object is set
			Object nms = wrappedWatchable.getRaw();
			if (nms != null) {
				list.add(nms);
			}
		}

		write(List.class, 0, list);
	}
}
