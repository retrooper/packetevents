package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.mojang.GameProfile;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketLoginStart extends WrappedPacket {
    private static final Reflection.FieldAccessor<com.mojang.authlib.GameProfile> gameProfileAccessor;
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketLoginInStart");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        gameProfileAccessor = Reflection.getField(packetClass, com.mojang.authlib.GameProfile.class, 0);
    }

    private GameProfile gameProfile;

    public WrappedPacketLoginStart(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        com.mojang.authlib.GameProfile gp = gameProfileAccessor.get(packet);
        this.gameProfile = new GameProfile(gp.getId(), gp.getName());
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }
}
