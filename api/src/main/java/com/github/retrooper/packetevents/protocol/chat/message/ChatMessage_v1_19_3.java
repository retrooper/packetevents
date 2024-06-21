package com.github.retrooper.packetevents.protocol.chat.message;

import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
//We'll extend ChatMessage_v1_16 for now, hopefully no breaking changes in the future
public class ChatMessage_v1_19_3 extends ChatMessage_v1_16 {
    int index;
    byte[] signature;
    String plainContent;
    Instant timestamp;
    long salt;
    LastSeenMessages.Packed lastSeenMessagesPacked;
    @Nullable Component unsignedChatContent;
    FilterMask filterMask;
    ChatType.Bound chatFormatting;

    public ChatMessage_v1_19_3(UUID senderUUID, int index, byte[] signature, String plainContent, Instant timestamp, long salt, LastSeenMessages.Packed lastSeenMessagesPacked, @Nullable Component unsignedChatContent, FilterMask filterMask, ChatType.Bound chatFormatting) {
        super(Component.text(plainContent), chatFormatting.getType(), senderUUID);
        this.index = index;
        this.signature = signature;
        this.plainContent = plainContent;
        this.timestamp = timestamp;
        this.salt = salt;
        this.lastSeenMessagesPacked = lastSeenMessagesPacked;
        this.unsignedChatContent = unsignedChatContent;
        this.filterMask = filterMask;
        this.chatFormatting = chatFormatting;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public Component getChatContent() {
        return Component.text(plainContent);
    }

    @Deprecated
    @Override
    public void setChatContent(Component chatContent) {
        throw new UnsupportedOperationException("PacketEvents is not able to serialize components to plain-text. Please use the #setPlainContent instead to update the content.");
    }

    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public long getSalt() {
        return salt;
    }

    public void setSalt(long salt) {
        this.salt = salt;
    }

    public LastSeenMessages.Packed getLastSeenMessagesPacked() {
        return lastSeenMessagesPacked;
    }

    public void setLastSeenMessagesPacked(LastSeenMessages.Packed lastSeenMessagesPacked) {
        this.lastSeenMessagesPacked = lastSeenMessagesPacked;
    }

    public Optional<Component> getUnsignedChatContent() {
        return Optional.ofNullable(unsignedChatContent);
    }

    public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
        this.unsignedChatContent = unsignedChatContent;
    }

    public FilterMask getFilterMask() {
        return filterMask;
    }

    public void setFilterMask(FilterMask filterMask) {
        this.filterMask = filterMask;
    }


    public ChatType.Bound getChatFormatting() {
        return chatFormatting;
    }

    public void setChatFormatting(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }

    /**
     * Retrieves the chat formatting. Please refer to {@link ChatMessage_v1_19_3#getChatFormatting}
     * @return chat formatting which contains the actual chat type.
     */
    @Deprecated
    public ChatType.Bound getChatType() {
        return chatFormatting;
    }

    /**
     * Sets the chat formatting. Please refer to {@link ChatMessage_v1_19_3#setChatFormatting}
     * @param chatFormatting formatting which contains the actual chat type.
     */
    @Deprecated
    public void setChatType(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }
}
