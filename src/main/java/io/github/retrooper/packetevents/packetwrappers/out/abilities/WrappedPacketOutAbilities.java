package io.github.retrooper.packetevents.packetwrappers.out.abilities;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutAbilities extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutAbilities");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            packetConstructor = packetClass.getConstructor(PlayerAbilitiesUtils.playerAbilitiesClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private boolean vulnerable, flying, allowFlight, instantBuild;
    private float flySpeed, walkSpeed;

    public WrappedPacketOutAbilities(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                     final float flySpeed, final float walkSpeed) {
        super(null);
        this.vulnerable = isVulnerable;
        this.flying = isFlying;
        this.allowFlight = allowFlight;
        this.instantBuild = canBuildInstantly;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    @Override
    protected void setup() {
        try {
            this.vulnerable = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
            this.flying = Reflection.getField(packetClass, boolean.class, 1).getBoolean(packet);
            this.allowFlight = Reflection.getField(packetClass, boolean.class, 2).getBoolean(packet);
            this.instantBuild = Reflection.getField(packetClass, boolean.class, 3).getBoolean(packet);

            this.flySpeed = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.walkSpeed = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public boolean isFlying() {
        return flying;
    }

    public boolean isFlightAllowed() {
        return allowFlight;
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

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(PlayerAbilitiesUtils.getPlayerAbilities(vulnerable, flying, allowFlight, instantBuild, flySpeed, walkSpeed));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
