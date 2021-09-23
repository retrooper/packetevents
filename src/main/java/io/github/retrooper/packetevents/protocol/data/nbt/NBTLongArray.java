/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.protocol.data.nbt;

import java.util.Arrays;

public class NBTLongArray extends NBT {

	@Override
	public NBTType<NBTLongArray> getType() {
		return NBTType.LONG_ARRAY;
	}

	protected final long[] array;

	public NBTLongArray(long[] array) {
		this.array = array;
	}

	public long[] getValue() {
		return array;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NBTLongArray other = (NBTLongArray) obj;
		return Arrays.equals(array, other.array);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}

}
