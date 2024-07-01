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

package com.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.SimpleRegistry;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InternalPacketListener extends PacketListenerAbstract {

    private static final Map<ResourceLocation, RegistryEntry<?>> REGISTRY_KEYS = Stream.of(
            new RegistryEntry<>(BannerPatterns.getRegistry(),
                    BannerPattern::decode, BannerPattern::encode),
            new RegistryEntry<>(DimensionTypes.getRegistry(),
                    DimensionType::decode, DimensionType::encode)
    ).collect(Collectors.toMap(
            entry -> entry.baseRegistry.getRegistryKey(),
            Function.identity()));

    public InternalPacketListener() {
        this(PacketListenerPriority.LOWEST);
    }

    public InternalPacketListener(PacketListenerPriority priority) {
        super(priority);
    }

    private void handleRegistry(User user, ResourceLocation registryName, List<RegistryElement> elements) {
        RegistryEntry<?> registry = REGISTRY_KEYS.get(registryName);
        if (registry != null) {
            IRegistry<?> syncedRegistry = registry.createFromElements(elements);
            user.putSynchronizedRegistry(syncedRegistry);
        }
    }

    private void handleLegacyRegistries(User user, NBTCompound registryData) {
        for (NBT tag : registryData.getTags().values()) {
            NBTCompound compound = (NBTCompound) tag;
            // extract registry name
            ResourceLocation registryName = new ResourceLocation(
                    compound.getStringTagValueOrThrow("type"));
            // extract registry entries
            NBTList<NBTCompound> nbtElements =
                    compound.getCompoundListTagOrThrow("value");
            // store registry elements
            this.handleRegistry(user, registryName,
                    RegistryElement.convertNbt(nbtElements));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            Object channel = event.getChannel();
            //Process outgoing login success packet
            WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
            UserProfile profile = loginSuccess.getUserProfile();

            //Update user profile
            user.getProfile().setUUID(profile.getUUID());
            user.getProfile().setName(profile.getName());
            //Texture properties are passed in login success on 1.19
            user.getProfile().setTextureProperties(profile.getTextureProperties());

            //Map username with channel
            synchronized (channel) {
                ProtocolManager.CHANNELS.put(profile.getUUID(), channel);
            }

            PacketEvents.getAPI().getLogManager().debug("Mapped player UUID with their channel.");

            // Switch the user's connection state to new state, but the variable event.getConnectionState() remains LOGIN
            // We switch user state immediately to remain in sync with vanilla, allowing you to encode packets immediately
            boolean proxy = PacketEvents.getAPI().getInjector().isProxy();
            if (proxy ? event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_2)
                    : event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                user.setEncoderState(ConnectionState.CONFIGURATION);
            } else {
                user.setConnectionState(ConnectionState.PLAY);
            }
        }

        // The server sends dimension information in configuration phase, since 1.20.2
        else if (event.getPacketType() == PacketType.Configuration.Server.REGISTRY_DATA) {
            WrapperConfigServerRegistryData packet = new WrapperConfigServerRegistryData(event);

            if (packet.getElements() != null) { // 1.20.2 to 1.20.5
                this.handleRegistry(user, packet.getRegistryKey(), packet.getElements());
            }
            if (packet.getRegistryData() != null) { // since 1.20.5
                this.handleLegacyRegistries(user, packet.getRegistryData());
            }
        }

        // The server sends registry info in login packet for 1.17 to 1.20.1
        else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
            user.setEntityId(joinGame.getEntityId());
            user.setDimension(joinGame.getDimension());

            if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            if (joinGame.getDimensionCodec() != null) { // 1.16 to 1.20.1
                this.handleLegacyRegistries(user, joinGame.getDimensionCodec());
            }

            // Update world height
            user.switchDimensionType(joinGame.getServerVersion(), joinGame.getDimension());
        }

        // Respawn is used to switch dimensions
        else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
            user.setDimension(respawn.getDimension());
            if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            user.switchDimensionType(respawn.getServerVersion(), respawn.getDimension());
        } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
            user.setEncoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
            user.setEncoderState(ConnectionState.PLAY);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            Object channel = event.getChannel();
            InetSocketAddress address = event.getSocketAddress();
            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
            ConnectionState nextState = handshake.getNextConnectionState();
            ClientVersion clientVersion = handshake.getClientVersion();
            //Update client version for this event call(and user)
            user.setClientVersion(clientVersion);
            PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
            //Transition into LOGIN or STATUS connection state immediately, to remain in sync with vanilla
            user.setConnectionState(nextState);
        } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
            user.setDecoderState(ConnectionState.PLAY);
        }
    }

    private static final class RegistryEntry<T extends MappedEntity> {

        private final IRegistry<T> baseRegistry;
        private final BiFunction<NBT, ClientVersion, T> decoder;
        private final BiFunction<T, ClientVersion, NBT> encoder;

        public RegistryEntry(
                IRegistry<T> baseRegistry,
                BiFunction<NBT, ClientVersion, T> decoder,
                BiFunction<T, ClientVersion, NBT> encoder
        ) {
            this.baseRegistry = baseRegistry;
            this.decoder = decoder;
            this.encoder = encoder;
        }

        private void handleElement(
                SimpleRegistry<T> registry,
                RegistryElement element,
                int id, ClientVersion version
        ) {
            if (element.getData() != null) {
                // data was provided, use registry element sent over network
                T value = this.decoder.apply(element.getData(), version);
                registry.define(element.getId(), id, value);
                return;
            }

            // fallback to looking up in vanilla registry
            // this isn't a 100% valid solution, but a full solution to this
            // is not possible with Mojang's concept of known packs
            //
            // if packetevents isn't running on a proxy and two backend servers
            // share the same custom datapack, the entries wouldn't be sent as
            // the player would tell the server it already knows about them
            //
            // this will cause issues, especially when some datapack overrides world height
            // of a vanilla dimension - and this can't be fixed (or I missed something)

            ResourceLocation elementName = element.getId();
            T baseEntry = this.baseRegistry.getByName(elementName);
            if (baseEntry != null) {
                registry.define(elementName, id, baseEntry);
                return;
            }

            // can't find this element anywhere
            // TODO dummy values make at least simple stuff work?
            PacketEvents.getAPI().getLogger().warning("Unknown registry entry "
                    + elementName + " for " + this.baseRegistry.getRegistryKey());
        }

        public IRegistry<T> createFromElements(List<RegistryElement> elements, ClientVersion version) {
            SimpleRegistry<T> registry = new SimpleRegistry<>(this.baseRegistry.getRegistryKey());
            for (int id = 0; id < elements.size(); id++) {
                RegistryElement element = elements.get(id);
                this.handleElement(registry, element, id, version);
            }
            return registry;
        }
    }
}
