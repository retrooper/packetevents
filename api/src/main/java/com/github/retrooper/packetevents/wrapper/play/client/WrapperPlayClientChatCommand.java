package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.time.Instant;

// TODO: Class needs to be finished due to pre-5 changes
public class WrapperPlayClientChatCommand extends PacketWrapper<WrapperPlayClientChatCommand> {
    private String command;
    private MessageSignData messageSignData;
    private LastSeenMessages.Update lastSeenMessages;

    public WrapperPlayClientChatCommand(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, LastSeenMessages.Update lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_COMMAND);
        this.command = command;
        this.messageSignData = messageSignData;
        this.lastSeenMessages = lastSeenMessages;
    }

    @Override
    public void read() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11) ? 256 : 100;
        this.command = readString(maxMessageLength);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            Instant timestamp = readTimestamp();
            SaltSignature saltSignature = readSaltSignature();
            boolean signedPreview = readBoolean();
            this.messageSignData = new MessageSignData(saltSignature, timestamp, signedPreview);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
                this.lastSeenMessages = new LastSeenMessages.Update(this);
            }
        }
    }

    @Override
    public void write() {
        writeString(command);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeTimestamp(messageSignData.getTimestamp());
            writeSaltSignature(messageSignData.getSaltSignature());
            writeBoolean(messageSignData.isSignedPreview());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
                this.lastSeenMessages.write(this);
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientChatCommand wrapper) {
        this.command = wrapper.command;
        this.messageSignData = wrapper.messageSignData;
        this.lastSeenMessages = wrapper.lastSeenMessages;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MessageSignData getMessageSignData() {
        return messageSignData;
    }

    public void setMessageSignData(MessageSignData messageSignData) {
        this.messageSignData = messageSignData;
    }

    public LastSeenMessages.Update getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.Update lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}