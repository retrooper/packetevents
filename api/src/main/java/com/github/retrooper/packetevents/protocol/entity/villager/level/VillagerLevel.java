package com.github.retrooper.packetevents.protocol.entity.villager.level;


public enum VillagerLevel  {

    NOVICE,
    APPRENTICE,
    JOURNEYMAN,
    EXPERT,
    MASTER;

    private static final VillagerLevel[] VALUES = values();

    public static VillagerLevel getById(int id) {
        return VALUES[id];
    }

    public int getId() {
        return ordinal();
    }

}


