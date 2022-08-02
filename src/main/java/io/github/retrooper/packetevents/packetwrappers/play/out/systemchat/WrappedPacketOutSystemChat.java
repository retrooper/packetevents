package io.github.retrooper.packetevents.packetwrappers.play.out.systemchat;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

public class WrappedPacketOutSystemChat extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private String message;
    private WrappedPacketOutChat.ChatPosition position;

    public WrappedPacketOutSystemChat(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutSystemChat(String messageJSON, WrappedPacketOutChat.ChatPosition position) {
        this.message = messageJSON;
        this.position = position;
    }

    @Override
    protected void load() {
        try {
            if (version.isNewerThanOrEquals(ServerVersion.v_1_19_1)) {
                packetConstructor = PacketTypeClasses.Play.Server.SYSTEM_CHAT.getConstructor(String.class, boolean.class);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.SYSTEM_CHAT.getConstructor(String.class, int.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getMessageJSON() {
        if (packet != null) {
            return readString(0);
        } else {
            return message;
        }
    }

    public void setMessageJSON(String message) {
        if (packet != null) {
            writeString(0, message);
        } else {
            this.message = message;
        }
    }


    public WrappedPacketOutChat.ChatPosition getPosition() {
        if (packet != null) {
            if (version.isNewerThanOrEquals(ServerVersion.v_1_19_1)) {
                return readBoolean(1) ? WrappedPacketOutChat.ChatPosition.GAME_INFO : WrappedPacketOutChat.ChatPosition.SYSTEM;
            }
            return WrappedPacketOutChat.ChatPosition.getById(version, readInt(0));
        }
        else {
            return position;
        }
    }

    public void setPosition(WrappedPacketOutChat.ChatPosition position) {
        if (packet != null) {
            if (version.isNewerThanOrEquals(ServerVersion.v_1_19_1)) {
                writeBoolean(1, position == WrappedPacketOutChat.ChatPosition.GAME_INFO);
                return;
            }
            writeInt(0, position.ordinal());
        }
        else {
            this.position = position;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19_1)) {
            return packetConstructor.newInstance(getMessageJSON(), getPosition() == WrappedPacketOutChat.ChatPosition.GAME_INFO);

        }
        else {
            return packetConstructor.newInstance(getMessageJSON(), getPosition().ordinal());
        }
    }
}
