package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PacketDecodeEvent extends PacketEvent implements PlayerEvent, CancellableEvent {
    private final Player player;
    private boolean cancel;
    private ByteBufAbstract byteBuf;

    public PacketDecodeEvent(Player player, ByteBufAbstract byteBuf){
        this.player = player;
        this.byteBuf = byteBuf;
    }

    public PacketDecodeEvent(Player player, Object rawByteBuf) {
        this.player = player;
        this.byteBuf = PacketEvents.get().getServerUtils().generateByteBufAbstract(rawByteBuf);
    }


    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketDecode(this);
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean val) {
        this.cancel = val;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return player;
    }

    public ByteBufAbstract getByteBuf(){
        return this.byteBuf;
    }

    public void setByteBuf(ByteBufAbstract byteBuf) {
        this.byteBuf = byteBuf;
    }
}
