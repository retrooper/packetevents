package io.github.retrooper.packetevents.sponge.util.viaversion;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.Channel;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.lang.reflect.Field;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {

    private static final Class<?> ENCODE_HANDLER;
    private static final Class<?> DECODE_HANDLER;

    static {
        try {
            ENCODE_HANDLER = Class.forName("com.viaversion.sponge.handlers.SpongeEncodeHandler");
            DECODE_HANDLER = Class.forName("com.viaversion.sponge.handlers.SpongeDecodeHandler");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field CONNECTION_FIELD;

    @Override
    public int getProtocolVersion(ServerPlayer player) {
        return Via.getAPI().getPlayerVersion(player);
    }

    @Override
    public int getProtocolVersion(User user) {
        try {
            Object viaEncoder = ((Channel) user.getChannel()).pipeline().get("via-encoder");
            if (CONNECTION_FIELD == null) {
                CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), "info");
            }
            UserConnection connection = (UserConnection) CONNECTION_FIELD.get(viaEncoder);
            return connection.getProtocolInfo().getProtocolVersion();
        }
        catch (IllegalAccessException e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    @Override
    public Class<?> getUserConnectionClass() {
        return UserConnection.class;
    }

    @Override
    public Class<?> getSpongeDecodeHandlerClass() {
        return DECODE_HANDLER;
    }

    @Override
    public Class<?> getSpongeEncodeHandlerClass() {
        return ENCODE_HANDLER;
    }
}