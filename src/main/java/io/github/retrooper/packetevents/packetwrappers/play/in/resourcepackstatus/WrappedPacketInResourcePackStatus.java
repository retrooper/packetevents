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

package io.github.retrooper.packetevents.packetwrappers.play.in.resourcepackstatus;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class WrappedPacketInResourcePackStatus extends WrappedPacket {
    private static Class<? extends Enum<?>> enumResourcePackStatusClass;

    public WrappedPacketInResourcePackStatus(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        enumResourcePackStatusClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, "EnumResourcePackStatus");
    }

    public ResourcePackStatus getStatus() {
        Enum<?> enumConst = readEnumConstant(0, enumResourcePackStatusClass);
        return ResourcePackStatus.valueOf(enumConst.name());
    }

    public void setStatus(ResourcePackStatus status) {
        Enum<?> enumConst = EnumUtil.valueOf(enumResourcePackStatusClass, status.name());
        writeEnumConstant(0, enumConst);
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10);
    }

    public enum ResourcePackStatus {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED
    }
}
