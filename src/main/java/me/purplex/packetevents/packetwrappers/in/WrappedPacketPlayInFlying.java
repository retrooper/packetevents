package me.purplex.packetevents.packetwrappers.in;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in._1_10.WrappedPacketPlayInFlying_1_10;
import me.purplex.packetevents.packetwrappers.in._1_11.WrappedPacketPlayInFlying_1_11;
import me.purplex.packetevents.packetwrappers.in._1_12.WrappedPacketPlayInFlying_1_12;
import me.purplex.packetevents.packetwrappers.in._1_13.WrappedPacketPlayInFlying_1_13;
import me.purplex.packetevents.packetwrappers.in._1_13_2.WrappedPacketPlayInFlying_1_13_2;
import me.purplex.packetevents.packetwrappers.in._1_14.WrappedPacketPlayInFlying_1_14;
import me.purplex.packetevents.packetwrappers.in._1_15.WrappedPacketPlayInFlying_1_15;
import me.purplex.packetevents.packetwrappers.in._1_7_10.WrappedPacketPlayInFlying_1_7_10;
import me.purplex.packetevents.packetwrappers.in._1_8.WrappedPacketPlayInFlying_1_8;
import me.purplex.packetevents.packetwrappers.in._1_8_3.WrappedPacketPlayInFlying_1_8_3;
import me.purplex.packetevents.packetwrappers.in._1_8_8.WrappedPacketPlayInFlying_1_8_8;
import me.purplex.packetevents.packetwrappers.in._1_9.WrappedPacketPlayInFlying_1_9;
import me.purplex.packetevents.packetwrappers.in._1_9_4.WrappedPacketPlayInFlying_1_9_4;

public class WrappedPacketPlayInFlying extends WrappedPacket {

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;

    public WrappedPacketPlayInFlying(Object packet) {
        super(packet);
    }

    @Override
    protected void setup(){
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketPlayInFlying_1_7_10 p = new WrappedPacketPlayInFlying_1_7_10(packet);
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
            WrappedPacketPlayInFlying_1_8 p = new WrappedPacketPlayInFlying_1_8(packet);
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
            WrappedPacketPlayInFlying_1_8_3 p = new WrappedPacketPlayInFlying_1_8_3(packet);
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
            WrappedPacketPlayInFlying_1_8_8 p = new WrappedPacketPlayInFlying_1_8_8(packet);
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
            WrappedPacketPlayInFlying_1_9 p = new WrappedPacketPlayInFlying_1_9(packet);
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
            WrappedPacketPlayInFlying_1_9_4 p = new WrappedPacketPlayInFlying_1_9_4(packet);
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
            WrappedPacketPlayInFlying_1_10 p = new WrappedPacketPlayInFlying_1_10(packet);
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
            WrappedPacketPlayInFlying_1_11 p = new WrappedPacketPlayInFlying_1_11(packet);
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
            WrappedPacketPlayInFlying_1_12 p = new WrappedPacketPlayInFlying_1_12(packet);
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
            WrappedPacketPlayInFlying_1_13 p = new WrappedPacketPlayInFlying_1_13(packet);
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
            WrappedPacketPlayInFlying_1_13_2 p = new WrappedPacketPlayInFlying_1_13_2(packet);
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
            WrappedPacketPlayInFlying_1_14 p = new WrappedPacketPlayInFlying_1_14(packet);
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
            WrappedPacketPlayInFlying_1_15 p = new WrappedPacketPlayInFlying_1_15(packet);
            this.x = p.x;
            this.y = p.y;
            this.z = p.z;
            this.yaw = p.yaw;
            this.pitch = p.pitch;
            this.f = p.f;
            this.hasPos = p.hasPos;
            this.hasLook = p.hasLook;
        }
    }

    public static class WrappedPacketPlayInPosition extends WrappedPacketPlayInFlying {

        public WrappedPacketPlayInPosition(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            if(version == ServerVersion.v_1_7_10) {
                WrappedPacketPlayInFlying_1_7_10.WrappedPacketPlayInPosition_1_7_10 p
                        = new WrappedPacketPlayInFlying_1_7_10.WrappedPacketPlayInPosition_1_7_10(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8) {
                WrappedPacketPlayInFlying_1_8.WrappedPacketPlayInPosition_1_8 p
                        = new WrappedPacketPlayInFlying_1_8.WrappedPacketPlayInPosition_1_8(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_3) {
                WrappedPacketPlayInFlying_1_8_3.WrappedPacketPlayInPosition_1_8_3 p
                        = new WrappedPacketPlayInFlying_1_8_3.WrappedPacketPlayInPosition_1_8_3(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_8_8) {
                WrappedPacketPlayInFlying_1_8_8.WrappedPacketPlayInPosition_1_8_8 p
                        = new WrappedPacketPlayInFlying_1_8_8.WrappedPacketPlayInPosition_1_8_8(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9) {
                WrappedPacketPlayInFlying_1_9.WrappedPacketPlayInPosition_1_9 p
                        = new WrappedPacketPlayInFlying_1_9.WrappedPacketPlayInPosition_1_9(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_9_4) {
                WrappedPacketPlayInFlying_1_9_4.WrappedPacketPlayInPosition_1_9_4 p
                        = new WrappedPacketPlayInFlying_1_9_4.WrappedPacketPlayInPosition_1_9_4(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_10) {
                WrappedPacketPlayInFlying_1_10.WrappedPacketPlayInPosition_1_10 p
                        = new WrappedPacketPlayInFlying_1_10.WrappedPacketPlayInPosition_1_10(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_11) {
                WrappedPacketPlayInFlying_1_11.WrappedPacketPlayInPosition_1_11 p
                        = new WrappedPacketPlayInFlying_1_11.WrappedPacketPlayInPosition_1_11(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_12) {
                WrappedPacketPlayInFlying_1_12.WrappedPacketPlayInPosition_1_12 p
                        = new WrappedPacketPlayInFlying_1_12.WrappedPacketPlayInPosition_1_12(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version== ServerVersion.v_1_13) {
                WrappedPacketPlayInFlying_1_13.WrappedPacketPlayInPosition_1_13 p
                        = new WrappedPacketPlayInFlying_1_13.WrappedPacketPlayInPosition_1_13(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_13_2) {
                WrappedPacketPlayInFlying_1_13_2.WrappedPacketPlayInPosition_1_13_2 p
                        = new WrappedPacketPlayInFlying_1_13_2.WrappedPacketPlayInPosition_1_13_2(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_14) {
                WrappedPacketPlayInFlying_1_14.WrappedPacketPlayInPosition_1_14 p
                        = new WrappedPacketPlayInFlying_1_14.WrappedPacketPlayInPosition_1_14(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
            else if(version == ServerVersion.v_1_15) {
                WrappedPacketPlayInFlying_1_15.WrappedPacketPlayInPosition_1_15 p
                        = new WrappedPacketPlayInFlying_1_15.WrappedPacketPlayInPosition_1_15(packet);
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
        }


    }

    public static class WrappedPacketPlayInPosition_Look extends WrappedPacketPlayInFlying {

        public WrappedPacketPlayInPosition_Look(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            if(version == ServerVersion.v_1_7_10) {
                WrappedPacketPlayInFlying_1_7_10.WrappedPacketPlayInPosition_Look_1_7_10 p
                        = new WrappedPacketPlayInFlying_1_7_10.WrappedPacketPlayInPosition_Look_1_7_10(packet);
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
                WrappedPacketPlayInFlying_1_8.WrappedPacketPlayInPosition_Look_1_8 p
                        = new WrappedPacketPlayInFlying_1_8.WrappedPacketPlayInPosition_Look_1_8(packet);
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
                WrappedPacketPlayInFlying_1_8_3.WrappedPacketPlayInPosition_Look_1_8_3 p
                        = new WrappedPacketPlayInFlying_1_8_3.WrappedPacketPlayInPosition_Look_1_8_3(packet);
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
                WrappedPacketPlayInFlying_1_8_8.WrappedPacketPlayInPosition_Look_1_8_8 p
                        = new WrappedPacketPlayInFlying_1_8_8.WrappedPacketPlayInPosition_Look_1_8_8(packet);
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
                WrappedPacketPlayInFlying_1_9.WrappedPacketPlayInPosition_Look_1_9 p
                        = new WrappedPacketPlayInFlying_1_9.WrappedPacketPlayInPosition_Look_1_9(packet);
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
                WrappedPacketPlayInFlying_1_9_4.WrappedPacketPlayInPosition_Look_1_9_4 p
                        = new WrappedPacketPlayInFlying_1_9_4.WrappedPacketPlayInPosition_Look_1_9_4(packet);
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
                WrappedPacketPlayInFlying_1_10.WrappedPacketPlayInPosition_Look_1_10 p
                        = new WrappedPacketPlayInFlying_1_10.WrappedPacketPlayInPosition_Look_1_10(packet);
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
                WrappedPacketPlayInFlying_1_11.WrappedPacketPlayInPosition_Look_1_11 p
                        = new WrappedPacketPlayInFlying_1_11.WrappedPacketPlayInPosition_Look_1_11(packet);
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
                WrappedPacketPlayInFlying_1_12.WrappedPacketPlayInPosition_Look_1_12 p
                        = new WrappedPacketPlayInFlying_1_12.WrappedPacketPlayInPosition_Look_1_12(packet);
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
                WrappedPacketPlayInFlying_1_13.WrappedPacketPlayInPosition_Look_1_13 p
                        = new WrappedPacketPlayInFlying_1_13.WrappedPacketPlayInPosition_Look_1_13(packet);
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
                WrappedPacketPlayInFlying_1_13_2.WrappedPacketPlayInPosition_Look_1_13_2 p
                        = new WrappedPacketPlayInFlying_1_13_2.WrappedPacketPlayInPosition_Look_1_13_2(packet);
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
                WrappedPacketPlayInFlying_1_14.WrappedPacketPlayInPosition_Look_1_14 p
                        = new WrappedPacketPlayInFlying_1_14.WrappedPacketPlayInPosition_Look_1_14(packet);
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
                WrappedPacketPlayInFlying_1_15.WrappedPacketPlayInPosition_Look_1_15 p
                        = new WrappedPacketPlayInFlying_1_15.WrappedPacketPlayInPosition_Look_1_15(packet);
                this.x = p.x;
                this.y = p.y;
                this.z = p.z;
                this.yaw = p.yaw;
                this.pitch = p.pitch;
                this.f = p.f;
                this.hasPos = p.hasPos;
                this.hasLook = p.hasLook;
            }
        }
    }

}
