package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;
import net.minecraft.server.level.ServerPlayer;

public interface ViaVersionAccessor {

    int getProtocolVersion(ServerPlayer player);

    int getProtocolVersion(User user);

    Class<?> getUserConnectionClass();

    Class<?> getSpongeDecodeHandlerClass();

    Class<?> getSpongeEncodeHandlerClass();
}