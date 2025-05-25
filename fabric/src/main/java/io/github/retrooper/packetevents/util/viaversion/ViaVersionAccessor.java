package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;

public interface ViaVersionAccessor {

    int getProtocolVersion(User user);
}