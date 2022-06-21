package com.github.retrooper.packetevents.protocol.entity;

public enum EntityAction {
    START_SNEAKING,
    STOP_SNEAKING,
    LEAVE_BED,
    START_SPRINTING,
    STOP_SPRINTING,
    START_JUMPING_WITH_HORSE,
    STOP_JUMPING_WITH_HORSE,
    OPEN_HORSE_INVENTORY,
    START_FLYING_WITH_ELYTRA;

    private static final EntityAction[] VALUES = values();

    public EntityAction getById(int index) {
        return VALUES[index];
    }
}