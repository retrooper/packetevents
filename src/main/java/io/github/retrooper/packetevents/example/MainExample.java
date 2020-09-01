/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import org.bukkit.plugin.java.JavaPlugin;
public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {

        PacketEvents.getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);

        //ClientVersion cannot be resolved, it is the same as the server's.
        //The clientversion can only not be resolved if
        // ViaVersion, ProtocolSuppport, or ProtocolLib isn't present.
        //ClientVersion might be resolvable if the server version is 1.7.10.
        PacketEvents.getSettings().setDoAutoResolveClientProtocolVersion(true);

        //Deprecated, as it is no longer needed
        //PacketEvents.getSettings().setIdentifier("packetevents_api");

        PacketEvents.start(this);
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }


}