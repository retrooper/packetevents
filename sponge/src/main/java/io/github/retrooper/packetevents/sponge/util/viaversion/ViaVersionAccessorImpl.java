package io.github.retrooper.packetevents.sponge.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.lang.reflect.Field;

@NullMarked
public class ViaVersionAccessorImpl implements ViaVersionAccessor {

    // effectively checks for https://github.com/ViaVersion/ViaVersion/commit/22bd350e35880b345203ce4e93078a5ee43e5e24#diff-7f601b0b63bd577a3833b606b5004d9ef134efd5a0ae36f4bb44995b72b0121cR112
    private static final boolean HAS_INFO_PROTOCOL_VERSION = Reflection.getClassByNameWithoutException("com.viaversion.viaversion.api.protocol.version.VersionType") != null;

    private static final Class<?> ENCODE_HANDLER;
    private static final Class<?> DECODE_HANDLER;
    private static @MonotonicNonNull Field CONNECTION_FIELD;

    static {
        try {
            ENCODE_HANDLER = Class.forName("com.viaversion.sponge.handlers.SpongeEncodeHandler");
            DECODE_HANDLER = Class.forName("com.viaversion.sponge.handlers.SpongeDecodeHandler");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getProtocolVersion(ServerPlayer player) {
        @SuppressWarnings("unchecked")
        ViaAPI<ServerPlayer> api = Via.getAPI();
        return api.getPlayerVersion(player);
    }

    @Override
    public int getProtocolVersion(User user) {
        ViaInjector viaInjector = Via.getManager().getInjector();
        Channel channel = (Channel) user.getChannel();
        Object viaEncoder = channel.pipeline().get(viaInjector.getEncoderName());

        // read user connection object using reflection from netty handler
        if (CONNECTION_FIELD == null) {
            CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), UserConnection.class, 0);
        }
        UserConnection connection = null;
        if (CONNECTION_FIELD != null) {
            try {
                connection = (UserConnection) CONNECTION_FIELD.get(viaEncoder);
            } catch (IllegalAccessException ignored) {
            }
        }

        return connection != null ? this.getVersion(connection) : UNKNOWN_PROTOCOL_VERSION;
    }

    private int getVersion(UserConnection connection) {
        if (HAS_INFO_PROTOCOL_VERSION) {
            ProtocolVersion version = connection.getProtocolInfo().protocolVersion();
            if (version.isKnown()) {
                return version.getVersion();
            }
            return UNKNOWN_PROTOCOL_VERSION;
        } else {
            @SuppressWarnings("deprecation")
            int proto = connection.getProtocolInfo().getProtocolVersion();
            return proto;
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
