package io.github.retrooper.packetevents.packetwrappers.in.abilities;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Field;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static Class<?> abilitiesClass;
    private static final Field[] fields = new Field[6];

    static {

        try {
            abilitiesClass = NMSUtils.getNMSClass("PacketPlayInAbilities");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            fields[0] = abilitiesClass.getDeclaredField("a");
            fields[1] = abilitiesClass.getDeclaredField("b");
            fields[2] = abilitiesClass.getDeclaredField("c");
            fields[3] = abilitiesClass.getDeclaredField("d");
            fields[4] = abilitiesClass.getDeclaredField("e");
            fields[5] = abilitiesClass.getDeclaredField("f");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for (Field f : fields) {
            if (f != null) {
                f.setAccessible(true);
            }
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
            this.isVulnerable = fields[0].getBoolean(packet);
            this.isFlying = fields[1].getBoolean(packet);
            this.allowFly = fields[2].getBoolean(packet);
            this.instantBuild = fields[3].getBoolean(packet);
            this.flySpeed = fields[4].getFloat(packet);
            this.walkSpeed = fields[5].getFloat(packet);
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
