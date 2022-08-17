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

package com.github.retrooper.packetevents.protocol.nbt;

public abstract class NBTNumber extends NBT {

    //PacketEvents start - We added this to make the nbt -> json conversion easier.
    //We also implemented it in the classes extending this class.
    public abstract Number getAsNumber();
    //PacketEvents end

    public abstract byte getAsByte();

    public abstract short getAsShort();

    public abstract int getAsInt();

    public abstract long getAsLong();

    public abstract float getAsFloat();

    public abstract double getAsDouble();

}
