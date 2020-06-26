package io.github.retrooper.packetevents.packetwrappers.in.flying;


import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public class WrappedPacketInFlying extends WrappedPacket {
    private static Class<?> flyingClass;

    static {

        try {
            flyingClass = NMSUtils.getNMSClass("PacketPlayInFlying");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean isPosition;
    private boolean isLook;

    public WrappedPacketInFlying(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            //x, y, z, yaw pitch, onGround, isPosition, isLook
            this.x = Reflection.getField(flyingClass, double.class, 0).getDouble(packet);
            this.y = Reflection.getField(flyingClass, double.class, 1).getDouble(packet);
            this.z = Reflection.getField(flyingClass, double.class, 2).getDouble(packet);

            this.yaw = Reflection.getField(flyingClass, float.class, 0).getFloat(packet);
            this.pitch = Reflection.getField(flyingClass, float.class, 1).getFloat(packet);

            this.onGround = Reflection.getField(flyingClass, boolean.class, 0).getBoolean(packet);

            this.isPosition = Reflection.getField(flyingClass, boolean.class, 1).getBoolean(packet);
            this.isLook = Reflection.getField(flyingClass, boolean.class, 2).getBoolean(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getZ() {
        return z;
    }

    public final float getYaw() {
        return yaw;
    }

    public final float getPitch() {
        return pitch;
    }

    public final boolean isOnGround() {
        return onGround;
    }

    public boolean isPosition() {
        return isPosition;
    }

    public boolean isLook() {
        return isLook;
    }

    public static class WrappedPacketInPosition extends WrappedPacketInFlying {
        public WrappedPacketInPosition(Object packet) {
            super(packet);
        }

        @Override
        public boolean isPosition() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 1).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public boolean isLook() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 2).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static class WrappedPacketInPosition_Look extends WrappedPacketInFlying {
        public WrappedPacketInPosition_Look(Object packet) {
            super(packet);
        }

        @Override
        public boolean isPosition() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 1).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public boolean isLook() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 2).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public static class WrappedPacketInLook extends WrappedPacketInFlying {

        public WrappedPacketInLook(Object packet) {
            super(packet);
        }
        @Override
        public boolean isPosition() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 1).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean isLook() {
            try {
                return Reflection.getField(flyingClass, boolean.class, 2).getBoolean(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
