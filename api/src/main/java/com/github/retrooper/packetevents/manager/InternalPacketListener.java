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
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPatterns;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
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
import com.github.retrooper.packetevents.util.mappings.SimpleTypesBuilderData;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InternalPacketListener extends PacketListenerAbstract {

    private static final Map<ResourceLocation, RegistryEntry<?>> REGISTRY_KEYS = Stream.of(
            // TODO: biome
            new RegistryEntry<>(ChatTypes.getRegistry(), ChatType::decode),
            new RegistryEntry<>(TrimPatterns.getRegistry(), TrimPattern::decode),
            // TODO: trim_material
            // TODO: wolf_variant
            // TODO: painting_variant
            new RegistryEntry<>(DimensionTypes.getRegistry(), DimensionType::decode),
            // TODO: damage_type
            new RegistryEntry<>(BannerPatterns.getRegistry(), BannerPattern::decode)
            // TODO: enchantment
            // TODO: jukebox_song
    ).collect(Collectors.toMap(RegistryEntry::getRegistryKey, Function.identity()));

    public InternalPacketListener() {
        this(PacketListenerPriority.LOWEST);
    }

    public InternalPacketListener(PacketListenerPriority priority) {
        super(priority);
    }

    private void handleRegistry(
            User user, ClientVersion version,
            ResourceLocation registryName,
            List<RegistryElement> elements
    ) {
        RegistryEntry<?> registry = REGISTRY_KEYS.get(registryName);
        if (registry != null) {
            IRegistry<?> syncedRegistry = registry.createFromElements(elements, version);
            user.putUserRegistry(syncedRegistry);
        }
    }

    private void handleLegacyRegistries(
            User user, ClientVersion version,
            NBTCompound registryData
    ) {
        for (NBT tag : registryData.getTags().values()) {
            NBTCompound compound = (NBTCompound) tag;
            // extract registry name
            ResourceLocation registryName = new ResourceLocation(
                    compound.getStringTagValueOrThrow("type"));
            // extract registry entries
            NBTList<NBTCompound> nbtElements =
                    compound.getCompoundListTagOrThrow("value");
            // store registry elements
            this.handleRegistry(user, version, registryName,
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
                this.handleRegistry(user, packet.getServerVersion().toClientVersion(),
                        packet.getRegistryKey(), packet.getElements());
            }
            if (packet.getRegistryData() != null) { // since 1.20.5
                this.handleLegacyRegistries(user, packet.getServerVersion()
                        .toClientVersion(), packet.getRegistryData());
            }
        }

        // The server sends registry info in login packet for 1.16 to 1.20.1
        else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
            user.setEntityId(joinGame.getEntityId());

            if (joinGame.getDimensionCodec() != null) { // 1.16 to 1.20.1
                this.handleLegacyRegistries(user, joinGame.getServerVersion().toClientVersion(),
                        joinGame.getDimensionCodec());
            }

            // FIXME registries need to be used WHILE decoding this packet
            user.setDimensionType(joinGame.getDimensionType());
        }

        // Respawn is used to switch dimensions
        else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn packet = new WrapperPlayServerRespawn(event);
            // FIXME registries need to be used WHILE decoding this packet
            user.setDimensionType(packet.getDimensionType());
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

    @FunctionalInterface
    private interface NbtEntryDecoder<T> {

        T decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data);
    }

    private static final class RegistryEntry<T extends MappedEntity & CopyableEntity<T>> {

        private final IRegistry<T> baseRegistry;
        private final NbtEntryDecoder<T> decoder;

        public RegistryEntry(
                IRegistry<T> baseRegistry,
                NbtEntryDecoder<T> decoder
        ) {
            this.baseRegistry = baseRegistry;
            this.decoder = decoder;
        }

        private void handleElement(
                SimpleRegistry<T> registry,
                RegistryElement element,
                int id, ClientVersion version
        ) {
            TypesBuilderData data = new SimpleTypesBuilderData(element.getId(), id);
            if (element.getData() != null) {
                // data was provided, use registry element sent over network
                T value = this.decoder.decode(element.getData(), version, data);
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
                registry.define(elementName, id, baseEntry.copy(data));
                return;
            }

            // can't find this element anywhere
            // TODO dummy values to make at least simple stuff work?
            PacketEvents.getAPI().getLogger().warning("Unknown registry entry "
                    + elementName + " for " + this.getRegistryKey());
        }

        public IRegistry<T> createFromElements(List<RegistryElement> elements, ClientVersion version) {
            SimpleRegistry<T> registry = new SimpleRegistry<>(this.getRegistryKey());
            for (int id = 0; id < elements.size(); id++) {
                RegistryElement element = elements.get(id);
                this.handleElement(registry, element, id, version);
            }
            return registry;
        }

        public ResourceLocation getRegistryKey() {
            return this.baseRegistry.getRegistryKey();
        }
    }
}
