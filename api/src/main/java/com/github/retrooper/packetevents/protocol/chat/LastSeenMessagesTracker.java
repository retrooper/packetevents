package com.github.retrooper.packetevents.protocol.chat;

import java.util.Arrays;

public class LastSeenMessagesTracker {
    private final LastSeenMessages.Entry[] status;
    private int size;
    private LastSeenMessages result = LastSeenMessages.EMPTY;

    public LastSeenMessagesTracker(int index) {
        this.status = new LastSeenMessages.Entry[index];
    }

    public void push(LastSeenMessages.Entry entry) {
        LastSeenMessages.Entry entryCopy = entry;
        for (int i = 0; i < this.size; ++i) {
            LastSeenMessages.Entry newEntry = this.status[i];
            this.status[i] = entryCopy;
            entryCopy = newEntry;
            if (!newEntry.getUuid().equals(entry.getUuid())) {
                continue;
            }
            entryCopy = null;
            break;
        }
        if (entryCopy != null && this.size < this.status.length) {
            this.status[this.size++] = entryCopy;
        }
        this.result = new LastSeenMessages(Arrays.asList(Arrays.copyOf(this.status, this.size)));
    }

    public LastSeenMessages get() {
        return this.result;
    }
}
