package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsBuilder;
import io.github.retrooper.packetevents.factory.fabric.ServerInstanceManager;
import io.github.retrooper.packetevents.mixin.ClientPacketListenerAccessor;
import io.github.retrooper.packetevents.mixin.ConnectionAccessor;
import io.github.retrooper.packetevents.mixin.ServerGamePacketListenerImplAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEventsMod implements PreLaunchEntrypoint, ModInitializer, ClientModInitializer {
    //single player world are locked on loading, don't know why
    public static final Logger LOGGER = LoggerFactory.getLogger("packetevents");

    @Override
    public void onPreLaunch() {
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("packetevents"));
        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        //TODO Test if userconnectevent and userdisconnectevent work.
        //especially disconnect
    }

    @Override
    public void onInitialize() {
        ServerInstanceManager.init();
        PacketEvents.getAPI().init();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var connection = ((ServerGamePacketListenerImplAccessor)handler).getConnection();
            var channel = ((ConnectionAccessor)connection).getChannel();
            if(ChannelUtilities.pipelineContainsSplitterAndPrepender(channel.pipeline()))
                PacketEvents.getAPI().getInjector().setPlayer(channel, handler.player);
        });

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                System.out.println("Packet send: " + event.getPacketType());
                System.out.println("player: " + event.getPlayer());
            }
        });
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var connection = ((ClientPacketListenerAccessor)handler).getConnection();
            var channel = ((ConnectionAccessor)connection).getChannel();
            if(ChannelUtilities.pipelineContainsSplitterAndPrepender(channel.pipeline()))
                PacketEvents.getAPI().getInjector().setPlayer(channel, Minecraft.getInstance().player);
        });
    }
}
