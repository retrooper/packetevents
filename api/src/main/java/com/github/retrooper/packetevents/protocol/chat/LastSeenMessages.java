package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.util.crypto.MessageVerifier;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Finish class
public class LastSeenMessages {
    public static final LastSeenMessages EMPTY = new LastSeenMessages(new ArrayList<>());

    private final List<Entry> entries;

    public LastSeenMessages(List<Entry> entries) {
        this.entries = entries;
    }

    public LastSeenMessages(PacketWrapper<?> wrapper) {
        this.entries = wrapper.readCollection(PacketWrapper.limitValue(ArrayList::new, 5), Entry::new);
    }

    public void write(PacketWrapper<?> wrapper) {
        wrapper.writeList(this.entries, (wrapper1, o) -> o.write(wrapper1));
    }

    public void updateHash(DataOutput output) throws IOException {
        for (Entry entry : this.entries) {
            UUID uuid = entry.getUuid();
            MessageVerifier lastVerifier = entry.getLastVerifier();
            output.writeByte(70);
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
            // FIXME: Fix this later
            // output.write(lastVerifier.bytes());
        }
    }

    public static class Entry {
        private final UUID uuid;
        private final MessageVerifier lastVerifier; // Should be MessageSignature

        public Entry(UUID uuid, MessageVerifier lastVerifier) {
            this.uuid = uuid;
            this.lastVerifier = lastVerifier;
        }

        public Entry(PacketWrapper<?> wrapper) {
            // FIXME: Fix MessageVerifier class to be compatible with this class
            this(wrapper.readUUID(), new MessageVerifier());
        }

        public void write(PacketWrapper<?> wrapper) {
            wrapper.writeUUID(uuid);
            // Write through the lastVerifier class (?) [byteArray]
        }

        public UUID getUuid() {
            return uuid;
        }

        public MessageVerifier getLastVerifier() {
            return lastVerifier;
        }
    }

    public static class Update {
        private final LastSeenMessages lastSeenMessages;
        private final Entry lastReceived;

        public Update(LastSeenMessages lastSeenMessages, Entry lastReceived) {
            this.lastSeenMessages = lastSeenMessages;
            this.lastReceived = lastReceived;
        }

        public Update(PacketWrapper<?> wrapper) {
            this(new LastSeenMessages(wrapper), wrapper.readOptional(Entry::new));
        }

        public void write(PacketWrapper<?> wrapper) {
            this.lastSeenMessages.write(wrapper);
            wrapper.writeOptional(lastSeenMessages, (wrapper1, o) -> o.write(wrapper1));
        }

        public LastSeenMessages getLastSeenMessages() {
            return lastSeenMessages;
        }

        public Entry getLastReceived() {
            return lastReceived;
        }
    }
}
