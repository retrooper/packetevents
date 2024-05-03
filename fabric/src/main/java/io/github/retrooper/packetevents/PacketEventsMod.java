package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEventsMod implements PreLaunchEntrypoint, ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("packetevents");

    @Override
    public void onPreLaunch() {
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("packetevents"));
        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
                    WrapperPlayClientChatMessage message = new WrapperPlayClientChatMessage(event);
                    message.setMessage(message.getMessage() + " - modified!");
                }
            }

            @Override
            public void onUserConnect(UserConnectEvent event) {
                new Thread(() -> {
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    event.getUser().sendMessage("You've connected!");
                }).start();
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                System.out.println("DISCONNECTED PACKETEVENTS");
            }
        });
        //TODO Test if userconnectevent and userdisconnectevent work.
        //especially disconnect
    }

    @Override
    public void onInitialize() {
        PacketEvents.getAPI().init();
    }
}
