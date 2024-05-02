package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.SignedCommandArgument;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

public class WrapperPlayClientChatCommand extends PacketWrapper<WrapperPlayClientChatCommand> {
    private String command;
    private MessageSignData messageSignData;
    private List<SignedCommandArgument> signedArguments;
    private @Nullable LastSeenMessages.Update lastSeenMessages;

    public WrapperPlayClientChatCommand(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, List<SignedCommandArgument> signedArguments) {
        super(PacketType.Play.Client.CHAT_COMMAND);
        this.command = command;
        this.messageSignData = messageSignData;
        this.signedArguments = signedArguments;
    }

    public WrapperPlayClientChatCommand(String command, MessageSignData messageSignData, List<SignedCommandArgument> signedArguments, @Nullable LastSeenMessages.Update lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_COMMAND);
        this.command = command;
        this.messageSignData = messageSignData;
        this.signedArguments = signedArguments;
        this.lastSeenMessages = lastSeenMessages;
    }

    @Override
    public void read() {
        this.command = readString(256);
        Instant timestamp = readTimestamp();
        long salt = readLong();
        this.messageSignData = new MessageSignData(new SaltSignature(salt, new byte[0]), timestamp);
        this.signedArguments = readSignedCommandArguments();
        this.lastSeenMessages = readLastSeenMessagesUpdate();
    }

    @Override
    public void write() {
        writeString(command, 256);
        writeTimestamp(messageSignData.getTimestamp());
        writeLong(messageSignData.getSaltSignature().getSalt());
        writeSignedCommandArguments(signedArguments);
        if (lastSeenMessages != null)
            writeLastSeenMessagesUpdate(lastSeenMessages);
    }

    @Override
    public void copy(WrapperPlayClientChatCommand wrapper) {
        this.command = wrapper.command;
        this.messageSignData = wrapper.messageSignData;
        this.signedArguments = wrapper.signedArguments;
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

    public List<SignedCommandArgument> getSignedArguments() {
        return signedArguments;
    }

    public void setSignedArguments(List<SignedCommandArgument> signedArguments) {
        this.signedArguments = signedArguments;
    }

    public LastSeenMessages.@Nullable Update getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.@Nullable Update lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}