package me.purplex.packetevents.packetwrappers.in.flying;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in.flying.impl.*;

public class WrappedPacketInFlying extends WrappedPacket {

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;

    public WrappedPacketInFlying(Object packet) {
        super(packet);
    }

    @Override
    protected void setup(){
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketInFlying_1_7_10 p = new WrappedPacketInFlying_1_7_10(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_8) {
            WrappedPacketInFlying_1_8 p = new WrappedPacketInFlying_1_8(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_8_3) {
            WrappedPacketInFlying_1_8_3 p = new WrappedPacketInFlying_1_8_3(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_8_8) {
            WrappedPacketInFlying_1_8_8 p = new WrappedPacketInFlying_1_8_8(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_9) {
            WrappedPacketInFlying_1_9 p = new WrappedPacketInFlying_1_9(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_9_4) {
            WrappedPacketInFlying_1_9_4 p = new WrappedPacketInFlying_1_9_4(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_10) {
            WrappedPacketInFlying_1_10 p = new WrappedPacketInFlying_1_10(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version== ServerVersion.v_1_11) {
            WrappedPacketInFlying_1_11 p = new WrappedPacketInFlying_1_11(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_12) {
            WrappedPacketInFlying_1_12 p = new WrappedPacketInFlying_1_12(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version== ServerVersion.v_1_13) {
            WrappedPacketInFlying_1_13 p = new WrappedPacketInFlying_1_13(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_13_2) {
            WrappedPacketInFlying_1_13_2 p = new WrappedPacketInFlying_1_13_2(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_14) {
            WrappedPacketInFlying_1_14 p = new WrappedPacketInFlying_1_14(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else if(version == ServerVersion.v_1_15) {
            WrappedPacketInFlying_1_15 p = new WrappedPacketInFlying_1_15(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
        else {
            throw throwUnsupportedVersion();
        }
    }

    public static class WrappedPacketInPosition extends WrappedPacketInFlying {

        public WrappedPacketInPosition(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            if(version == ServerVersion.v_1_7_10) {
                WrappedPacketInFlying_1_7_10.WrappedPacketInPosition_1_7_10 p
                        = new WrappedPacketInFlying_1_7_10.WrappedPacketInPosition_1_7_10(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8) {
                WrappedPacketInFlying_1_8.WrappedPacketInPosition_1_8 p
                        = new WrappedPacketInFlying_1_8.WrappedPacketInPosition_1_8(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_3) {
                WrappedPacketInFlying_1_8_3.WrappedPacketInPosition_1_8_3 p
                        = new WrappedPacketInFlying_1_8_3.WrappedPacketInPosition_1_8_3(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_8) {
                WrappedPacketInFlying_1_8_8.WrappedPacketInPosition_1_8_8 p
                        = new WrappedPacketInFlying_1_8_8.WrappedPacketInPosition_1_8_8(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9) {
                WrappedPacketInFlying_1_9.WrappedPacketInPosition_1_9 p
                        = new WrappedPacketInFlying_1_9.WrappedPacketInPosition_1_9(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9_4) {
                WrappedPacketInFlying_1_9_4.WrappedPacketInPosition_1_9_4 p
                        = new WrappedPacketInFlying_1_9_4.WrappedPacketInPosition_1_9_4(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_10) {
                WrappedPacketInFlying_1_10.WrappedPacketInPosition_1_10 p
                        = new WrappedPacketInFlying_1_10.WrappedPacketInPosition_1_10(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_11) {
                WrappedPacketInFlying_1_11.WrappedPacketInPosition_1_11 p
                        = new WrappedPacketInFlying_1_11.WrappedPacketInPosition_1_11(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_12) {
                WrappedPacketInFlying_1_12.WrappedPacketInPosition_1_12 p
                        = new WrappedPacketInFlying_1_12.WrappedPacketInPosition_1_12(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_13) {
                WrappedPacketInFlying_1_13.WrappedPacketInPosition_1_13 p
                        = new WrappedPacketInFlying_1_13.WrappedPacketInPosition_1_13(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_13_2) {
                WrappedPacketInFlying_1_13_2.WrappedPacketInPosition_1_13_2 p
                        = new WrappedPacketInFlying_1_13_2.WrappedPacketInPosition_1_13_2(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_14) {
                WrappedPacketInFlying_1_14.WrappedPacketInPosition_1_14 p
                        = new WrappedPacketInFlying_1_14.WrappedPacketInPosition_1_14(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_15) {
                WrappedPacketInFlying_1_15.WrappedPacketInPosition_1_15 p
                        = new WrappedPacketInFlying_1_15.WrappedPacketInPosition_1_15(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else {
                throw throwUnsupportedVersion();
            }
        }


    }

    public static class WrappedPacketInPosition_Look extends WrappedPacketInFlying {

        public WrappedPacketInPosition_Look(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            if(version == ServerVersion.v_1_7_10) {
                WrappedPacketInFlying_1_7_10.WrappedPacketInPosition_Look_1_7_10 p
                        = new WrappedPacketInFlying_1_7_10.WrappedPacketInPosition_Look_1_7_10(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8) {
                WrappedPacketInFlying_1_8.WrappedPacketInPosition_Look_1_8 p
                        = new WrappedPacketInFlying_1_8.WrappedPacketInPosition_Look_1_8(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_3) {
                WrappedPacketInFlying_1_8_3.WrappedPacketInPosition_Look_1_8_3 p
                        = new WrappedPacketInFlying_1_8_3.WrappedPacketInPosition_Look_1_8_3(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_8) {
                WrappedPacketInFlying_1_8_8.WrappedPacketInPosition_Look_1_8_8 p
                        = new WrappedPacketInFlying_1_8_8.WrappedPacketInPosition_Look_1_8_8(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9) {
                WrappedPacketInFlying_1_9.WrappedPacketInPosition_Look_1_9 p
                        = new WrappedPacketInFlying_1_9.WrappedPacketInPosition_Look_1_9(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9_4) {
                WrappedPacketInFlying_1_9_4.WrappedPacketInPosition_Look_1_9_4 p
                        = new WrappedPacketInFlying_1_9_4.WrappedPacketInPosition_Look_1_9_4(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_10) {
                WrappedPacketInFlying_1_10.WrappedPacketInPosition_Look_1_10 p
                        = new WrappedPacketInFlying_1_10.WrappedPacketInPosition_Look_1_10(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_11) {
                WrappedPacketInFlying_1_11.WrappedPacketInPosition_Look_1_11 p
                        = new WrappedPacketInFlying_1_11.WrappedPacketInPosition_Look_1_11(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_12) {
                WrappedPacketInFlying_1_12.WrappedPacketInPosition_Look_1_12 p
                        = new WrappedPacketInFlying_1_12.WrappedPacketInPosition_Look_1_12(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_13) {
                WrappedPacketInFlying_1_13.WrappedPacketInPosition_Look_1_13 p
                        = new WrappedPacketInFlying_1_13.WrappedPacketInPosition_Look_1_13(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_13_2) {
                WrappedPacketInFlying_1_13_2.WrappedPacketInPosition_Look_1_13_2 p
                        = new WrappedPacketInFlying_1_13_2.WrappedPacketInPosition_Look_1_13_2(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_14) {
                WrappedPacketInFlying_1_14.WrappedPacketInPosition_Look_1_14 p
                        = new WrappedPacketInFlying_1_14.WrappedPacketInPosition_Look_1_14(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_15) {
                WrappedPacketInFlying_1_15.WrappedPacketInPosition_Look_1_15 p
                        = new WrappedPacketInFlying_1_15.WrappedPacketInPosition_Look_1_15(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else {
                throw throwUnsupportedVersion();
            }
        }
    }

}
