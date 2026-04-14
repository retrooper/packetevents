package com.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PreViaSupport {

    private PreViaSupport() {
    }

    public static boolean hasPreViaListeners() {
        return PacketEvents.getAPI().getEventManager().hasPreViaListeners();
    }

    public static boolean hasPreViaInternalListener() {
        return PacketEvents.getAPI().getEventManager().hasPreViaInternalListener();
    }

    public static boolean shouldRegisterPreViaInternalListener() {
        return PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20_2);
    }

    public static boolean shouldUseLegacyLoginTracking(User user) {
        return shouldRegisterPreViaInternalListener()
                && hasPreViaInternalListener()
                && user.getClientVersion() != null
                && user.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_2);
    }
}
