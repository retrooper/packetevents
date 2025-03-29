package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;
import net.minecraft.server.level.ServerPlayer;

public interface ViaVersionAccessor {

    int getProtocolVersion(User user);
}