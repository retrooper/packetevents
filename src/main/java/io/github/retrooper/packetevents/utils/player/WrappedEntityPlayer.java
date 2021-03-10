package io.github.retrooper.packetevents.utils.player;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.Server;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class WrappedEntityPlayer extends WrappedPacket {
    private static Constructor<?> entityPlayerConstructor, playerInteractManagerConstructor;
    private static Method getEntityId, getUniqueId, setLocation;
    private Object entityPlayer;
    private final WrappedGameProfile gameProfile;
    private int id = -1;
    private UUID uuid;

    public WrappedEntityPlayer(Server server, World world, WrappedGameProfile gameProfile) {
        this.gameProfile = gameProfile;
        Object minecraftServer = NMSUtils.convertBukkitServerToNMSServer(server);
        Object worldServer = NMSUtils.convertBukkitWorldToNMSWorld(world);
        Object mojangGameProfile = GameProfileUtil.getGameProfile(gameProfile.id, gameProfile.name);

        try {
            Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldServer);
            this.entityPlayer = entityPlayerConstructor.newInstance(minecraftServer, worldServer, mojangGameProfile, playerInteractManager);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void load() {
        try {
            entityPlayerConstructor = NMSUtils.entityPlayerClass.getConstructor(NMSUtils.minecraftServerClass, NMSUtils.worldServerClass,
                    NMSUtils.gameProfileClass, NMSUtils.playerInteractManagerClass);
            getEntityId = NMSUtils.nmsEntityClass.getMethod("getId");
            getUniqueId = NMSUtils.nmsEntityClass.getMethod("getUniqueID");
            setLocation = NMSUtils.nmsEntityClass.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            playerInteractManagerConstructor = NMSUtils.playerInteractManagerClass.getConstructor(NMSUtils.nmsWorldClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public WrappedGameProfile getGameProfile() {
        return gameProfile;
    }

    public int getEntityId() {
        if (id == -1) {
            try {
                id = (int) getEntityId.invoke(entityPlayer);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public UUID getUUID() {
        if (uuid == null) {
            try {
                uuid = (UUID) getUniqueId.invoke(entityPlayer);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return uuid;
    }

    public void setLocation(double x, double y, double z, float yaw, float pitch) {
        try {
            setLocation.invoke(entityPlayer, x, y, z, yaw, pitch);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getRawEntityPlayer() {
        return entityPlayer;
    }
}
