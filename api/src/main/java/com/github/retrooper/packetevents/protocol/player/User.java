/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.sql.Wrapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class User implements Audience, Identified {
    private final Object channel;
    private ConnectionState decoderState;
    private ConnectionState encoderState;
    private ClientVersion clientVersion;
    private final UserProfile profile;
    private final Identity identity;
    private int entityId = -1;
    private int minWorldHeight = 0;
    private int totalWorldHeight = 256;
    private List<NBTCompound> worldNBT;
    private Dimension dimension = new Dimension(0);

    public User(Object channel,
                ConnectionState connectionState, ClientVersion clientVersion,
                UserProfile profile) {
        this.channel = channel;
        this.decoderState = connectionState;
        this.encoderState = connectionState;
        this.clientVersion = clientVersion;
        this.profile = profile;
        this.identity = Identity.identity(profile.getUUID());
    }

    public Object getChannel() {
        return channel;
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) ChannelHelper.remoteAddress(channel);
    }

    public ConnectionState getConnectionState() {
        ConnectionState decoderState = this.decoderState;
        ConnectionState encoderState = this.encoderState;
        if (decoderState != encoderState) {
            throw new IllegalArgumentException("Can't get common connection state: " + decoderState + " != " + encoderState);
        }
        return decoderState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.setDecoderState(connectionState);
        this.setEncoderState(connectionState);
    }

    @Override
    public Identity identity() {
        return identity;
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        Component title = null, subtitle = null;
        Title.Times times = null;
        if (part == TitlePart.TITLE) {
            title = (Component) value;
        }
        else if (part == TitlePart.SUBTITLE) {
            subtitle = (Component) value;
        }
        else if (part == TitlePart.TIMES) {
            times = (Title.Times) value;
        }
        int stay, out, in;
        if (times != null) {
            stay = (int) (times.stay().toMillis() / 20);
            out = (int) (times.fadeOut().toMillis() / 20);
            in = (int) (times.fadeIn().toMillis() / 20);
        }
        else {
            stay = 20;
            out = 20;
            in = 20;
        }

        ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion().toServerVersion() :
                PacketEvents.getAPI().getServerManager().getVersion();
        boolean modern = version.isNewerThanOrEquals(ServerVersion.V_1_17);
        PacketWrapper<?> animation;
        PacketWrapper<?> setTitle = null;
        PacketWrapper<?> setSubtitle = null;
        if (modern) {
            animation = new WrapperPlayServerSetTitleTimes(in, stay, out);
            if (title != null) {
                setTitle = new WrapperPlayServerSetTitleText(title);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
            }
        } else {
            animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                    TitleAction.SET_TIMES_AND_DISPLAY, (Component) null, null, null,
                    in, stay, out);
            if (title != null) {
                setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_TITLE, title, null, null,
                        0, 0, 0);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_SUBTITLE, null, subtitle, null,
                        0, 0, 0);
            }
        }
        sendPacket(animation);
        if (setTitle != null) {
            sendPacket(setTitle);
        }
        if (setSubtitle != null) {
            sendPacket(setSubtitle);
        }
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        WrapperPlayServerSystemChatMessage wrapper = new WrapperPlayServerSystemChatMessage(true, message);
        sendPacket(wrapper);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        for (ResourcePackInfo info : request.packs()) {
            WrapperPlayServerResourcePackSend wrapper = new WrapperPlayServerResourcePackSend(
                    info.id(), info.uri().toString(), info.hash(), true, null
            );
            sendPacket(wrapper);
        }
    }

    @Override
    public void clearTitle() {
        sendPacket(new WrapperPlayServerClearTitles(false));
    }

    @Override
    public void resetTitle() {
        sendPacket(new WrapperPlayServerClearTitles(true));
    }

    /**
     * PacketEvents is not responsible for keeping track of the boss bars you show and hide.
     */
    @Override
    public void showBossBar(@NotNull BossBar bar) {
        boolean darkenScreen = false, playBossMusic = false, createWorldFog = false;
        Set<BossBar.Flag> barFlags = bar.flags();
        if (barFlags.contains(BossBar.Flag.DARKEN_SCREEN)) {
            darkenScreen = true;
        }
        if (barFlags.contains(BossBar.Flag.PLAY_BOSS_MUSIC)) {
            playBossMusic = true;
        }
        if (barFlags.contains(BossBar.Flag.CREATE_WORLD_FOG)) {
            createWorldFog = true;
        }
        WrapperPlayServerBossBar wrapper = new WrapperPlayServerBossBar(
                UUID.randomUUID(),
                WrapperPlayServerBossBar.Action.add(
                        bar.name(),
                        bar.progress(),
                        WrapperPlayServerBossBar.Color.values()[bar.color().ordinal()],
                        WrapperPlayServerBossBar.Division.VALUES[bar.overlay().ordinal()],
                        WrapperPlayServerBossBar.createFlags(darkenScreen, playBossMusic, createWorldFog)
                )
        );
        sendPacket(wrapper);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        hideBossBar((UUID) null);
    }

    public void hideBossBar(UUID bossBarId) {
        if (bossBarId == null) {
            throw new IllegalArgumentException("Boss bar id cannot be null! You need to keep track of boss bars you create.");
        }
        sendPacket(new WrapperPlayServerBossBar(bossBarId, WrapperPlayServerBossBar.Action.remove()));
    }

    @Override
    public void playSound(@NotNull Sound sound) {
    }

    @Override
    public void playSound(@NotNull Sound sound, Sound.Emitter emitter) {
        Audience.super.playSound(sound, emitter);
    }

    @Override
    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        Audience.super.playSound(sound, x, y, z);
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        Audience.super.stopSound(sound);
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        Audience.super.stopSound(stop);
    }

    @Override
    public void showTitle(@NotNull Title title) {
        int fadeIn = 20, stay = 20, fadeOut = 20;
        if (title.times() != null) {
            fadeIn = (int) (title.times().fadeIn().toMillis() / 20);
            stay = (int) (title.times().stay().toMillis() / 20);
            fadeOut = (int) (title.times().fadeOut().toMillis() / 20);
        }
        sendTitle(title.title(), title.subtitle(), fadeIn, stay, fadeOut);
    }

    @Override
    public void clearResourcePacks() {
        sendPacket(new WrapperPlayServerResourcePackRemove((UUID) null));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        WrapperPlayServerPlayerListHeaderAndFooter wrapper = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        sendPacket(wrapper);
    }

    public ConnectionState getDecoderState() {
        return this.decoderState;
    }

    public void setDecoderState(ConnectionState decoderState) {
        this.decoderState = decoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s decoder into " + decoderState + " state!");
    }

    public ConnectionState getEncoderState() {
        return this.encoderState;
    }

    public void setEncoderState(ConnectionState encoderState) {
        this.encoderState = encoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s encoder into " + encoderState + " state!");
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public String getName() {
        return profile.getName();
    }

    public UUID getUUID() {
        return profile.getUUID();
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void sendPacket(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, buffer);
    }

    public void sendPacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, wrapper);
    }

    public void sendPacketSilently(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, wrapper);
    }

    public void writePacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(channel, wrapper);
    }

    public void flushPackets() {
        ChannelHelper.flush(channel);
    }

    public void closeConnection() {
        ChannelHelper.close(channel);
    }

    //Might be tough with the message signing
   /* public void chat(String message) {
        //Fake an incoming chat packet
        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(message);
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, chatMessage);
    }*/

    public void closeInventory() {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, closeWindow);
    }

    public void sendMessage(String legacyMessage) {
        Component component = AdventureSerializer.fromLegacyFormat(legacyMessage);
        sendMessage(component);
    }

    @Override
    public void sendMessage(Component component) {
        sendMessage(component, ChatTypes.CHAT);
    }


    public void sendMessage(Component component, ChatType type) {
        ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion().toServerVersion() :
                PacketEvents.getAPI().getServerManager().getVersion();
        PacketWrapper<?> chatPacket;
        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            chatPacket = new WrapperPlayServerSystemChatMessage(false, component);
        } else {
            ChatMessage message;
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                message = new ChatMessage_v1_16(component, type, new UUID(0L, 0L));
            } else {
                message = new ChatMessageLegacy(component, type);
            }
            chatPacket = new WrapperPlayServerChatMessage(message);
        }
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, chatPacket);
    }

    public void sendTitle(String legacyTitle, String legacySubtitle,
                          int fadeInTicks, int stayTicks, int fadeOutTicks) {
        Component title = AdventureSerializer.fromLegacyFormat(legacyTitle);
        Component subtitle = AdventureSerializer.fromLegacyFormat(legacySubtitle);
        sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion().toServerVersion() :
                PacketEvents.getAPI().getServerManager().getVersion();
        boolean modern = version.isNewerThanOrEquals(ServerVersion.V_1_17);
        PacketWrapper<?> animation;
        PacketWrapper<?> setTitle = null;
        PacketWrapper<?> setSubtitle = null;
        if (modern) {
            animation = new WrapperPlayServerSetTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerSetTitleText(title);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
            }
        } else {
            animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                    TitleAction.SET_TIMES_AND_DISPLAY, (Component) null, null, null,
                    fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_TITLE, title, null, null,
                        0, 0, 0);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_SUBTITLE, null, subtitle, null,
                        0, 0, 0);
            }
        }
        sendPacket(animation);
        if (setTitle != null) {
            sendPacket(setTitle);
        }
        if (setSubtitle != null) {
            sendPacket(setSubtitle);
        }
    }

    //TODO sendTitle that is cross-version

    public int getMinWorldHeight() {
        return minWorldHeight;
    }

    public void setMinWorldHeight(int minWorldHeight) {
        this.minWorldHeight = minWorldHeight;
    }

    public int getTotalWorldHeight() {
        return totalWorldHeight;
    }

    public void setTotalWorldHeight(int totalWorldHeight) {
        this.totalWorldHeight = totalWorldHeight;
    }

    public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
        this.worldNBT = worldNBT.getTags();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    @Nullable
    public NBTCompound getWorldNBT(String worldName) {
        if (worldNBT == null) {
            return null;
        }
        for (NBTCompound compound : worldNBT) {
            if (compound.getStringTagOrNull("name").getValue().equals(worldName)) {
                return compound;
            }
        }
        return null;
    }
}
