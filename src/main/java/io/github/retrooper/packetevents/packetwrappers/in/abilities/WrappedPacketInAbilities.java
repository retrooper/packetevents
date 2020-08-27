/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.in.abilities;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static Class<?> abilitiesClass;
    private static boolean isMoreThanOneBoolPresent;
    private boolean isVulnerable;
    private boolean isFlying;
    private boolean allowFly;
    private boolean instantBuild;
    private float flySpeed;
    private float walkSpeed;
    public WrappedPacketInAbilities(Object packet) {
        super(packet);
    }

    public static void load() {
        abilitiesClass = PacketTypeClasses.Client.ABILITIES;
        isMoreThanOneBoolPresent = Reflection.getField(abilitiesClass, boolean.class, 1) != null;
    }

    @Override
    protected void setup() {
        try {
            if (isMoreThanOneBoolPresent) {
                this.isVulnerable = Reflection.getField(abilitiesClass, boolean.class, 0).getBoolean(packet);
                this.isFlying = Reflection.getField(abilitiesClass, boolean.class, 1).getBoolean(packet);
                this.allowFly = Reflection.getField(abilitiesClass, boolean.class, 2).getBoolean(packet);
                this.instantBuild = Reflection.getField(abilitiesClass, boolean.class, 3).getBoolean(packet);
                this.flySpeed = Reflection.getField(abilitiesClass, float.class, 0).getFloat(packet);
                this.walkSpeed = Reflection.getField(abilitiesClass, float.class, 1).getFloat(packet);
            } else {
                this.isFlying = Reflection.getField(abilitiesClass, boolean.class, 0).getBoolean(packet);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     */
    @Deprecated
    @Nullable
    public Boolean isVulnerable() {
        return isVulnerable;
    }

    public boolean isFlying() {
        return isFlying;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     */
    @Deprecated
    @Nullable
    public Boolean isFlightAllowed() {
        return allowFly;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     */
    @Deprecated
    @Nullable
    public Boolean canInstantlyBuild() {
        return instantBuild;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     */
    @Deprecated
    @Nullable
    public Float getFlySpeed() {
        return flySpeed;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     */
    @Deprecated
    @Nullable
    public Float getWalkSpeed() {
        return walkSpeed;
    }
}
