package io.github.retrooper.packetevents.packetwrappers.in.abilities;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static Class<?> abilitiesClass;

    static {

        try {
            abilitiesClass = NMSUtils.getNMSClass("PacketPlayInAbilities");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isVulnerable;
    private boolean isFlying;
    private boolean allowFly;
    private boolean instantBuild;
    private float flySpeed;
    private float walkSpeed;

    public WrappedPacketInAbilities(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            this.isVulnerable = Reflection.getField(abilitiesClass, boolean.class, 0).getBoolean(packet);
            this.isFlying = Reflection.getField(abilitiesClass, boolean.class, 1).getBoolean(packet);
            this.allowFly = Reflection.getField(abilitiesClass, boolean.class, 2).getBoolean(packet);
            this.instantBuild = Reflection.getField(abilitiesClass, boolean.class, 3).getBoolean(packet);
            this.flySpeed = Reflection.getField(abilitiesClass, float.class, 0).getFloat(packet);
            this.walkSpeed = Reflection.getField(abilitiesClass, float.class, 1).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public boolean isFlightAllowed() {
        return allowFly;
    }

    public boolean canInstantlyBuild() {
        return instantBuild;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }
}
