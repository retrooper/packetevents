package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Player;

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

    void ejectPlayerSync(Player player);

    void injectPlayerAsync(Player player);

    void ejectPlayerAsync(Player player);

    void sendPacket(Object channel, Object rawNMSPacket);
}
