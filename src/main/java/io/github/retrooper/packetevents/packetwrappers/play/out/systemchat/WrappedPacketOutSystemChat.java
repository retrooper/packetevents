package io.github.retrooper.packetevents.packetwrappers.play.out.systemchat;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;

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
            packetConstructor = PacketTypeClasses.Play.Server.SYSTEM_CHAT.getConstructor(String.class, int.class);
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
            return WrappedPacketOutChat.ChatPosition.values()[readInt(0)];
        }
        else {
            return position;
        }
    }

    public void setPosition(WrappedPacketOutChat.ChatPosition position) {
        if (packet != null) {
            writeInt(0, position.ordinal());
        }
        else {
            this.position = position;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getMessageJSON(), getPosition().ordinal());
    }
}
