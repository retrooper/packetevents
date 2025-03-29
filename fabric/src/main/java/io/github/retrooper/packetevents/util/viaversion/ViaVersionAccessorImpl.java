package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.Channel;

import java.lang.reflect.Field;
import net.minecraft.server.level.ServerPlayer;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {

    private static Field CONNECTION_FIELD;

    @Override
    public int getProtocolVersion(User user) {
        try {
            Object viaEncoder = ((Channel) user.getChannel()).pipeline().get("via-encoder");
            if (CONNECTION_FIELD == null) {
                CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), "info");
            }
            UserConnection connection = (UserConnection) CONNECTION_FIELD.get(viaEncoder);
            return connection.getProtocolInfo().getProtocolVersion();
        } catch (IllegalAccessException e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }
}