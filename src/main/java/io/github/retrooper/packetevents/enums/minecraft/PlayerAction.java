package io.github.retrooper.packetevents.enums.minecraft;

public enum PlayerAction {
    START_SNEAKING,

    STOP_SNEAKING,

    STOP_SLEEPING,

    START_SPRINTING,

    STOP_SPRINTING,

    START_RIDING_JUMP,

    STOP_RIDING_JUMP,

    OPEN_INVENTORY,

    START_FALL_FLYING;

    public static PlayerAction get(final int i) {
        return values()[i];
    }

    public int getIndex() {
        for (int i = 0; i < values().length; i++) {
            final PlayerAction action = values()[i];
            if (action == this) {
                return i;
            }
        }
        return -1;
    }

    public enum UpdatedPlayerAction {
        PRESS_SHIFT_KEY, RELEASE_SHIFT_KEY, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, START_RIDING_JUMP, STOP_RIDING_JUMP, OPEN_INVENTORY, START_FALL_FLYING;

        public static UpdatedPlayerAction get(final int index) {
            for (int i = 0; i < index + 1; i++) {
                final UpdatedPlayerAction action = values()[i];
                if (i == index) {
                    return action;
                }
            }
            return null;
        }

        public int getIndex() {
            for (int i = 0; i < values().length; i++) {
                final UpdatedPlayerAction action = values()[i];
                if (action == this) {
                    return i;
                }
            }
            return -1;
        }
    }
}
