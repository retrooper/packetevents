package io.github.retrooper.packetevents.enums.minecraft;

public enum EntityAnimationType {
    SWING_MAIN_ARM, DAMAGE, LEAVE_BED, SWING_OFFHAND, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT;

    public static final EntityAnimationType get(final int i) {
        return values()[i];
    }

    public final int getIndex() {
        for (int i = 0; i < values().length; i++) {
            final EntityAnimationType curType = values()[0];
            if (curType == this) {
                return i;
            }
        }
        return -1;
    }
}
