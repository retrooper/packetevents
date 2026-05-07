package com.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
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

    public static void ensurePreViaInternalListenerRegistered() {
        if (!hasPreViaInternalListener()) {
            PacketEvents.getAPI().getEventManager().registerListener(new PreViaInternalListener());
        }
    }

    public static boolean isPreViaInternalListener(PacketListenerCommon listener) {
        return listener instanceof PreViaInternalListener;
    }

    public static boolean isExternalPreViaListener(PacketListenerCommon listener) {
        return listener.isPreVia() && !isPreViaInternalListener(listener);
    }
}
