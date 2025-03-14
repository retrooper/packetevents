package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.Nullable;

public abstract class EventManager {
    public abstract void callEvent(PacketEvent event);

    public abstract void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction);

    public abstract PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority);

    public abstract PacketListenerCommon registerListener(PacketListenerCommon listener);

    public abstract PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners);

    public abstract void unregisterListener(PacketListenerCommon listener);

    public abstract void unregisterListeners(PacketListenerCommon... listeners);

    public abstract void unregisterAllListeners();
}
