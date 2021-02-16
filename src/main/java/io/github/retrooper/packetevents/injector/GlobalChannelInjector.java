package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.early.channelinitializer.modern.EarlyChannelInjector8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.util.List;

public class GlobalChannelInjector implements ChannelInjector {
    //  private final ChannelInjector injector;
    private EarlyChannelInjector8 channelInjector;

    public GlobalChannelInjector() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        //Early injector
        // if (PacketEvents.get().getSettings().shouldInjectEarly()) {
        //   injector = legacy ? new EarlyChannelInjector7() : new EarlyChannelInjector8();
        //}
        //Late injector
        //else {
        //  injector = legacy ? new LateChannelInjector7() : new LateChannelInjector8();
        //}
        channelInjector = new EarlyChannelInjector8();
    }

    @Override
    public void prepare() {
        channelInjector.inject();
    }

    @Override
    public void cleanup() {
        channelInjector.eject();
    }

    @Override
    public void cleanupAsync() {
        //
    }

    @Override
    public void injectPlayerSync(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        channelInjector.updatePlayerObject(player, channel);
    }

    @Override
    public void injectPlayersSync(List<Player> players) {

    }

    @Override
    public void ejectPlayerSync(Player player) {

    }

    @Override
    public void ejectPlayersSync(List<Player> players) {

    }

    @Override
    public void injectPlayerAsync(Player player) {
        Object channel = PacketEvents.get().packetProcessorInternal.getChannel(player);
        channelInjector.updatePlayerObject(player, channel);
    }

    @Override
    public void injectPlayersAsync(List<Player> players) {
        //injector.injectPlayersAsync(players);
    }

    @Override
    public void ejectPlayerAsync(Player player) {

    }

    @Override
    public void ejectPlayersAsync(List<Player> players) {

    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.write(rawNMSPacket);
    }
}
