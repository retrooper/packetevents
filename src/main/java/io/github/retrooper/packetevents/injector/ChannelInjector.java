package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Player;

import java.util.List;

public interface ChannelInjector {
    default void prepare() {

    }

    default void cleanup() {

    }

    default void cleanupAsync() {
        PacketEvents.get().injectAndEjectExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                cleanup();
            }
        });
    }

    void injectPlayerSync(Player player);

    void injectPlayersSync(List<Player> players);

    void ejectPlayerSync(Player player);

    void ejectPlayersSync(List<Player> players);

    void injectPlayerAsync(Player player);

    void injectPlayersAsync(List<Player> players);

    void ejectPlayerAsync(Player player);

    void ejectPlayersAsync(List<Player> players);

    void sendPacket(Object channel, Object rawNMSPacket);
}
