package io.github.retrooper.packetevents.packetwrappers.in.clientcommand;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

//TODO: Test on 1.9, 1.10, 1.11, 1.13, 1.14
public final class WrappedPacketInClientCommand extends WrappedPacket {
    private static Class<?> packetClass;
    @Nullable
    private static Class<?> enumClientCommandClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayInClientCommand");
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                enumClientCommandClass = Reflection.getSubClass(packetClass, "EnumClientCommand");
            } else {
                enumClientCommandClass = NMSUtils.getNMSClass("EnumClientCommand");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClientCommand clientCommand;

    public WrappedPacketInClientCommand(Object packet) {
        super(packet);
    }

    @Override
    public void setup() {
        try {
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                clientCommand = ClientCommand.get(Reflection.getField(enumClientCommandClass, int.class, 1).getInt(packet));
            } else {
                final Object enumObj = Reflection.getField(packetClass, enumClientCommandClass, 0).get(packet);
                this.clientCommand = ClientCommand.valueOf(enumObj.toString());
            }
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
        OPEN_INVENTORY_ACHIEVEMENT;

        ClientCommand() {
        }

        public static ClientCommand get(final int i) {
            return values()[i];
        }
    }

}
