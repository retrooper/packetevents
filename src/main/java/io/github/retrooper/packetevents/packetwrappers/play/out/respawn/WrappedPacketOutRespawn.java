package io.github.retrooper.packetevents.packetwrappers.play.out.respawn;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.world.Difficulty;
import io.github.retrooper.packetevents.utils.world.Dimension;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.world.LevelType;
import org.jetbrains.annotations.Nullable;
//TODO finish and test
class WrappedPacketOutRespawn extends WrappedPacket implements SendableWrapper {
    private static Class<?> worldTypeClass;
    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private LevelType levelType;
    public WrappedPacketOutRespawn(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        worldTypeClass = NMSUtils.getNMSClassWithoutException("WorldType");
        /*net.minecraft.server.v1_7_R4.PacketPlayOutRespawn a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutRespawn a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutRespawn a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutRespawn a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutRespawn a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutRespawn a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutRespawn a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutRespawn a7;*/

    }

    public Dimension getDimension() {
        if (packet != null) {
            return readDimension(0, 0);
        } else {
            return dimension;
        }
    }

    public void setDimension(Dimension dimension) {
        if (packet != null) {
            writeDimension(0, 0, dimension);
        }
        else {
            this.dimension = dimension;
        }
    }

    public GameMode getGameMode() {
        if (packet != null) {
           return readGameMode(0);
        } else {
            return gameMode;
        }
    }

    public void setGameMode(GameMode gameMode) {
        if (packet != null) {
            writeGameMode(0, gameMode);
        }
        else {
            this.gameMode = gameMode;
        }
    }

    public LevelType getLevelType() {
        if (packet != null) {
            //TODO
            Object worldTypeObject = readObject(0, worldTypeClass);
            WrappedPacket worldTypeWrapper = new WrappedPacket(new NMSPacket(worldTypeObject));
            String worldTypeName = worldTypeWrapper.readString(0);
            return LevelType.getByName(worldTypeName);
        }
        else {
            return levelType;
        }
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }
}
