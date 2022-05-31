package com.github.retrooper.packetevents.protocol.item.book;

public enum BookType {
    CRAFTING,
    FURNACE,
    BLAST_FURNACE,
    SMOKER;

    public int getId() {
        return ordinal();
    }

    public static BookType getById(int id) {
        return VALUES[id];
    }
    
    private static final BookType[] VALUES = values();
}
