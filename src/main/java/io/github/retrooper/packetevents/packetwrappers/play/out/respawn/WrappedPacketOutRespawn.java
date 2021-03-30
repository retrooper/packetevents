package io.github.retrooper.packetevents.packetwrappers.play.out.respawn;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.world.Difficulty;
import io.github.retrooper.packetevents.utils.world.Dimension;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.world.LevelType;
import org.jetbrains.annotations.Nullable;
//TODO finish
class WrappedPacketOutRespawn extends WrappedPacket implements SendableWrapper {
    private static Class<?> dimensionManagerClass;
    private static Class<?> worldTypeClass;
    private static Class<? extends Enum<?>> enumDifficultyClass;
    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private LevelType levelType;
    public WrappedPacketOutRespawn(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        dimensionManagerClass = NMSUtils.getNMSClassWithoutException("DimensionManager");
        worldTypeClass = NMSUtils.getNMSClassWithoutException("WorldType");
        enumDifficultyClass = NMSUtils.getNMSEnumClassWithoutException("EnumDifficulty");
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
            int dimensionID;
            if (version.isOlderThan(ServerVersion.v_1_13_2)) {
                dimensionID = readInt(0);
            } else {
                Object dimensionManagerObject = readObject(0, dimensionManagerClass);
                WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
                dimensionID = dimensionManagerWrapper.readInt(0) - 1;
            }
            return Dimension.getById(dimensionID);
        } else {
            return dimension;
        }
    }
/*
    public void setDimension(Dimension dimension) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_13_2)) {
                writeInt(0, dimension.getId());
            } else {
                Object dimensionManagerObject = readObject(0, dimensionManagerClass);
                WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
                dimensionID = dimensionManagerWrapper.readInt(0) - 1;
            }
        }
    }*/

    @Nullable
    public GameMode getGameMode() {
        if (packet != null) {
            Enum<?> enumConst = readEnumConstant(0, NMSUtils.enumGameModeClass);
            try {
                return GameMode.valueOf(enumConst.name());
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        } else {
            return gameMode;
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
