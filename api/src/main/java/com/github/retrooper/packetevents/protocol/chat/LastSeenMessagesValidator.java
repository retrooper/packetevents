package com.github.retrooper.packetevents.protocol.chat;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LastSeenMessagesValidator {
    private static final int NOT_FOUND = Integer.MIN_VALUE;
    private LastSeenMessages lastSeenMessages = LastSeenMessages.EMPTY;
    private final List<LastSeenMessages.Entry> pendingEntries = new ArrayList<>();

    public void addPending(LastSeenMessages.Entry entry) {
        this.pendingEntries.add(entry);
    }

    public int pendingMessagesCount() {
        return this.pendingEntries.size();
    }

    private boolean hasDuplicateProfiles(LastSeenMessages lastSeenMessages) {
        Set<UUID> set = new HashSet<>(lastSeenMessages.getEntries().size());
        for (LastSeenMessages.Entry entry : lastSeenMessages.getEntries()) {
            if (set.add(entry.getUuid())) {
                continue;
            }
            return true;
        }
        return false;
    }

    private int calculateIndices(List<LastSeenMessages.Entry> entries, int[] ints, @Nullable LastSeenMessages.Entry entry) {
        Arrays.fill(ints, Integer.MIN_VALUE);
        List<LastSeenMessages.Entry> entryCopyList = this.lastSeenMessages.getEntries();
        int size = entryCopyList.size();
        for (int i = size - 1; i >= 0; --i) {
            int index = entries.indexOf(entryCopyList.get(i));
            if (index == -1) {
                continue;
            }
            ints[index] = -i - 1;
        }

        int minValue = Integer.MIN_VALUE;
        int pendingSize = this.pendingEntries.size();
        for (int i = 0; i < pendingSize; ++i) {
            LastSeenMessages.Entry entry1 = this.pendingEntries.get(i);
            int index = entries.indexOf(entry1);
            if (index != -1) {
                ints[index] = i;
            }
            if (!entry1.equals(entry)) {
                continue;
            }
            minValue = i;
        }
        return minValue;
    }

    public Set<ErrorCondition> validateAndUpdate(LastSeenMessages.Update update) {
        Set<ErrorCondition> errorConditions = EnumSet.noneOf(ErrorCondition.class);
        LastSeenMessages lastSeen = update.getLastSeenMessages();
        LastSeenMessages.Entry entry = update.getLastReceived();
        List<LastSeenMessages.Entry> entries = lastSeen.getEntries();
        int size = this.lastSeenMessages.getEntries().size();
        int minValue = Integer.MIN_VALUE;
        int entriesSize = entries.size();
        if (entriesSize < size) {
            errorConditions.add(ErrorCondition.REMOVED_MESSAGES);
        }
        int[] ints = new int[entriesSize];
        int indices = this.calculateIndices(entries, ints, entry);
        for (int i = entriesSize - 1; i >= 0; --i) {
            int anInt = ints[i];
            if (anInt != Integer.MIN_VALUE) {
                if (anInt < minValue) {
                    errorConditions.add(ErrorCondition.OUT_OF_ORDER);
                    continue;
                }
                minValue = anInt;
                continue;
            }
            errorConditions.add(ErrorCondition.UNKNOWN_MESSAGES);
        }
        if (entry != null) {
            if (indices == Integer.MIN_VALUE || indices < minValue) {
                errorConditions.add(ErrorCondition.UNKNOWN_MESSAGES);
            } else {
                minValue = indices;
            }
        }
        if (minValue >= 0) {
            // FIXME: What is this method? Can't find anything about it in the docs.
            // this.pendingEntries.removeElements(0, minValue + 1);
        }
        if (this.hasDuplicateProfiles(lastSeen)) {
            errorConditions.add(ErrorCondition.DUPLICATED_PROFILES);
        }
        this.lastSeenMessages = lastSeen;
        return errorConditions;
    }

    public enum ErrorCondition {
        OUT_OF_ORDER("messages received out of order"),
        DUPLICATED_PROFILES("multiple entries for single profile"),
        UNKNOWN_MESSAGES("unknown message"),
        REMOVED_MESSAGES("previously present messages removed from context");

        private final String message;

        ErrorCondition(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }
    }
}