package io.github.retrooper.packetevents.packetwrappers.play.out.joingame;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.player.GameMode;

/**
 * Make sendable and finish!
 */
public class WrappedPacketOutLogin extends WrappedPacketEntityAbstraction {
    public WrappedPacketOutLogin(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutLogin a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutLogin a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutLogin a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutLogin a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutLogin a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutLogin a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutLogin a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutLogin a7;
    }

    public boolean isHardcore() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            //TODO finish
            return false;
        }
    }

    public void setHardcore(boolean value) {
        if (packet != null) {
            writeBoolean(0, value);
        } else {
            //TODO finish
        }
    }

    public GameMode getGameMode() {
        if (packet != null) {
            return readGameMode(0);
        } else {
            //TODO finish
            return null;
        }
    }

    public void setGameMode(GameMode gameMode) {
        if (packet != null) {
            writeGameMode(0, gameMode);
        } else {
            //TODO finish
        }
    }

    public int getMaxPlayers() {
        if (packet != null) {
            //TODO index 2 on legacy, index 1 on modern
            int index = 2;
            return readInt(index);
        } else {
            return -1;
        }
    }
}
