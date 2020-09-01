/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.packetwrappers.in.clientcommand;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

//TODO: Test on 1.9, 1.10, 1.11, 1.13, 1.14
public final class WrappedPacketInClientCommand extends WrappedPacket {
    private static Class<?> packetClass;
    @Nullable
    private static Class<?> enumClientCommandClass;

    private static boolean isLowerThan_v_1_8;
    private ClientCommand clientCommand;

    public WrappedPacketInClientCommand(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.CLIENT_COMMAND;

        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);
        try {
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                enumClientCommandClass = Reflection.getSubClass(packetClass, "EnumClientCommand");
            } else {
                enumClientCommandClass = NMSUtils.getNMSClass("EnumClientCommand");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() {
        try {
            Object enumObj = Reflection.getField(packetClass, enumClientCommandClass, 0).get(packet);
            this.clientCommand = ClientCommand.valueOf(enumObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClientCommand getClientCommand() {
        return clientCommand;
    }

    public enum ClientCommand {
        PERFORM_RESPAWN,
        REQUEST_STATS,
        OPEN_INVENTORY_ACHIEVEMENT
    }

}
