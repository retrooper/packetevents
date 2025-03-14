package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.Nullable;

public interface EventManager {
    void callEvent(PacketEvent event);

    void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction);

    PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority);

    PacketListenerCommon registerListener(PacketListenerCommon listener);

    PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners);

    void unregisterListener(PacketListenerCommon listener);

    void unregisterListeners(PacketListenerCommon... listeners);

    void unregisterAllListeners();
}
