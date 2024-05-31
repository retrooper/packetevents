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

package io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketInClientCommand extends WrappedPacket {
    private static boolean v_1_16;
    private static Class<? extends Enum<?>> enumClientCommandClass;

    public WrappedPacketInClientCommand(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_16 = version.isNewerThanOrEquals(ServerVersion.v_1_16);
        enumClientCommandClass = NMSUtils.getNMSEnumClassWithoutException("EnumClientCommand");
        if (enumClientCommandClass == null) {
            //Probably a subclass
            enumClientCommandClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.CLIENT_COMMAND, "EnumClientCommand");
            if (enumClientCommandClass == null) {
                enumClientCommandClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.CLIENT_COMMAND, 0);
            }
        }
    }


    public ClientCommand getClientCommand() {
        Enum<?> enumConst = readEnumConstant(0, enumClientCommandClass);
        return ClientCommand.values()[enumConst.ordinal()];
    }

    public void setClientCommand(ClientCommand command) throws UnsupportedOperationException {
        if (command == ClientCommand.OPEN_INVENTORY_ACHIEVEMENT && v_1_16) {
            throwUnsupportedOperation(command);
        }
        Enum<?> enumConst = EnumUtil.valueByIndex(enumClientCommandClass, command.ordinal());
        writeEnumConstant(0, enumConst);
    }

    public enum ClientCommand {
        PERFORM_RESPAWN,
        REQUEST_STATS,

        //This enum constant only exists on 1.7.10 -> 1.15.2
        @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
        @Deprecated
        OPEN_INVENTORY_ACHIEVEMENT
    }

}
