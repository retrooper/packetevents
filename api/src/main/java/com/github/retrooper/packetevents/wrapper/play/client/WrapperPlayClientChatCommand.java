package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class WrapperPlayClientChatCommand extends PacketWrapper<WrapperPlayClientChatCommand> {
    private String command;
    private MessageSignData messageSignData;
    private @Nullable LastSeenMessages.Update lastSeenMessages;
    private @Nullable LastSeenMessages.LegacyUpdate legacyLastSeenMessages;

    public WrapperPlayClientChatCommand(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, @Nullable LastSeenMessages.LegacyUpdate lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_COMMAND);
        this.command = command;
        this.messageSignData = messageSignData;
        this.legacyLastSeenMessages = lastSeenMessages;
    }

    public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, @Nullable LastSeenMessages.Update lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_MESSAGE);
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

            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
                this.lastSeenMessages = readLastSeenMessagesUpdate();
            } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
                this.legacyLastSeenMessages = readLegacyLastSeenMessagesUpdate();
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
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3) && lastSeenMessages != null) {
                writeLastSeenMessagesUpdate(lastSeenMessages);
            } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1) && serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_2) && legacyLastSeenMessages != null) {
                writeLegacyLastSeenMessagesUpdate(legacyLastSeenMessages);
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientChatCommand wrapper) {
        this.command = wrapper.command;
        this.messageSignData = wrapper.messageSignData;
        this.lastSeenMessages = wrapper.lastSeenMessages;
        this.legacyLastSeenMessages = wrapper.legacyLastSeenMessages;
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

    public LastSeenMessages.@Nullable Update getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.@Nullable Update lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }

    public LastSeenMessages.@Nullable LegacyUpdate getLegacyLastSeenMessages() {
        return legacyLastSeenMessages;
    }

    public void setLegacyLastSeenMessages(LastSeenMessages.@Nullable LegacyUpdate legacyLastSeenMessages) {
        this.legacyLastSeenMessages = legacyLastSeenMessages;
    }
}