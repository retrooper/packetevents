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

package io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketInClientCommand extends WrappedPacket {
    private static Class<? extends Enum<?>> enumClientCommandClass;

    public WrappedPacketInClientCommand(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.CLIENT_COMMAND;
        try {
            enumClientCommandClass = NMSUtils.getNMSEnumClass("EnumClientCommand");
        } catch (ClassNotFoundException e) {
            //Probably a subclass
            enumClientCommandClass = SubclassUtil.getEnumSubClass(packetClass, "EnumClientCommand");
        }
    }


    public ClientCommand getClientCommand() {
        Enum<?> emumConst = readEnumConstant(0, enumClientCommandClass);
        return ClientCommand.valueOf(emumConst.name());
    }

    public void setClientCommand(ClientCommand command) throws UnsupportedOperationException {
        if (command == ClientCommand.OPEN_INVENTORY_ACHIEVEMENT && version.isNewerThan(ServerVersion.v_1_16)) {
            throwUnsupportedOperation(command);
        }
        Enum<?> enumConst = EnumUtil.valueOf(enumClientCommandClass, command.name());
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
