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

package com.github.retrooper.packetevents.protocol.packettype;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12_1;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_13;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14_4;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_17;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_18;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_1;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_3;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_4;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_5;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_7_10;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_8;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9_3;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_21;
import com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12_1;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_13;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_14;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_15_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_17;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_1;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_3;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_4;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_4;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_5;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_7_10;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_8;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_9;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.VersionMapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientConfigurationEndAck;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientCookieResponse;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPong;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientSelectKnownPacks;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientSettings;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerConfigurationEnd;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerCookieRequest;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerCustomReportDetails;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerDisconnect;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerPluginMessage;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResetChat;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResourcePackRemove;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResourcePackSend;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerSelectKnownPacks;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerServerLinks;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerStoreCookie;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerTransfer;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerUpdateEnabledFeatures;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientCookieResponse;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientEncryptionResponse;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginSuccessAck;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientPluginResponse;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerCookieRequest;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerDisconnect;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerEncryptionRequest;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerPluginRequest;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerSetCompression;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAdvancementTab;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatAck;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatPreview;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatSessionUpdate;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChunkBatchAck;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindowButton;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientTickEnd;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientConfigurationAck;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCookieResponse;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCraftRecipeRequest;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientDebugPing;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientDebugSampleSubscription;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientGenerateStructure;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientLockDifficulty;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientNameItem;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItem;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerLoaded;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryBlockNBT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryEntityNBT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectBundleItem;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectTrade;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetBeaconEffect;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetDifficulty;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetDisplayedRecipe;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetRecipeBookState;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetStructureBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetTestBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSlotStateChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSpectate;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerBoat;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTeleportConfirm;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTestInstanceBlockAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateCommandBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateCommandBlockMinecart;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateJigsawBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.github.retrooper.packetevents.wrapper.status.client.WrapperStatusClientPing;
import com.github.retrooper.packetevents.wrapper.status.client.WrapperStatusClientRequest;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerPong;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class PacketType {

    private static boolean PREPARED = false;

    //TODO UPDATE Update packet type mappings (clientbound pt. 1)
    private static final VersionMapper CLIENTBOUND_PLAY_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_7_10,
            ClientVersion.V_1_8,
            ClientVersion.V_1_9,
            ClientVersion.V_1_10,
            ClientVersion.V_1_12,
            ClientVersion.V_1_12_1,
            ClientVersion.V_1_13,
            ClientVersion.V_1_14,
            ClientVersion.V_1_14_4,
            ClientVersion.V_1_15,
            ClientVersion.V_1_15_2,
            ClientVersion.V_1_16,
            ClientVersion.V_1_16_2,
            ClientVersion.V_1_17,
            ClientVersion.V_1_18,
            ClientVersion.V_1_19,
            ClientVersion.V_1_19_1,
            ClientVersion.V_1_19_3,
            ClientVersion.V_1_19_4,
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21,
            ClientVersion.V_1_21_2,
            ClientVersion.V_1_21_5);

    //TODO UPDATE Update packet type mappings (serverbound pt. 1)
    private static final VersionMapper SERVERBOUND_PLAY_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_7_10,
            ClientVersion.V_1_8,
            ClientVersion.V_1_9,
            ClientVersion.V_1_12,
            ClientVersion.V_1_12_1,
            ClientVersion.V_1_13,
            ClientVersion.V_1_14,
            ClientVersion.V_1_15_2,
            ClientVersion.V_1_16,
            ClientVersion.V_1_16_2,
            ClientVersion.V_1_17,
            ClientVersion.V_1_19,
            ClientVersion.V_1_19_1,
            ClientVersion.V_1_19_3,
            ClientVersion.V_1_19_4,
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21_2,
            ClientVersion.V_1_21_4,
            ClientVersion.V_1_21_5);

    // TODO UPDATE Update packet type mappings (config clientbound pt. 1)
    private static final VersionMapper CLIENTBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21);
    // TODO UPDATE Update packet type mappings (config serverbound pt. 1)
    private static final VersionMapper SERVERBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_5);

    public static void prepare() {
        PacketType.Play.Client.load();
        PacketType.Play.Server.load();
        PacketType.Configuration.Client.load();
        PacketType.Configuration.Server.load();
        PREPARED = true;
    }

    public static boolean isPrepared() {
        return PREPARED;
    }

    public static PacketTypeCommon getById(PacketSide side, ConnectionState state, ClientVersion version, int packetID) {
        switch (state) {
            case HANDSHAKING:
                if (side == PacketSide.CLIENT) {
                    return PacketType.Handshaking.Client.getById(packetID);
                } else {
                    return PacketType.Handshaking.Server.getById(packetID);
                }
            case STATUS:
                if (side == PacketSide.CLIENT) {
                    return Status.Client.getById(packetID);
                } else {
                    return Status.Server.getById(packetID);
                }
            case LOGIN:
                if (side == PacketSide.CLIENT) {
                    return Login.Client.getById(packetID);
                } else {
                    return Login.Server.getById(packetID);
                }
            case PLAY:
                if (side == PacketSide.CLIENT) {
                    return Play.Client.getById(version, packetID);
                } else {
                    return Play.Server.getById(version, packetID);
                }
            case CONFIGURATION:
                if (side == PacketSide.CLIENT) {
                    return Configuration.Client.getById(version, packetID);
                } else {
                    return Configuration.Server.getById(version, packetID);
                }
            default:
                return null;
        }
    }

    public static class Handshaking {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            HANDSHAKE(0, WrapperHandshakingClientHandshake.class),
            /**
             * Technically not part of the current protocol, but clients older than 1.7 will send this to initiate Server List Ping.
             * 1.8 and newer servers will handle it correctly though.
             */
            LEGACY_SERVER_LIST_PING(0xFE, null),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return HANDSHAKE;
                } else if (packetID == 0xFE) {
                    return LEGACY_SERVER_LIST_PING;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            LEGACY_SERVER_LIST_RESPONSE(0xFE, null),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                return packetID == 0xFE ? LEGACY_SERVER_LIST_RESPONSE : null;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Status {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            REQUEST(0x00, WrapperStatusClientRequest.class),
            PING(0x01, WrapperStatusClientPing.class),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetId) {
                if (packetId == 0) {
                    return REQUEST;
                } else if (packetId == 1) {
                    return PING;
                } else {
                    return null;
                }
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            RESPONSE(0x00, WrapperStatusServerResponse.class),
            PONG(0x01, WrapperStatusServerPong.class),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return RESPONSE;
                } else if (packetID == 1) {
                    return PONG;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Login {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            LOGIN_START(0x00, WrapperLoginClientLoginStart.class),
            ENCRYPTION_RESPONSE(0x01, WrapperLoginClientEncryptionResponse.class),

            // Added in 1.13
            LOGIN_PLUGIN_RESPONSE(0x02, WrapperLoginClientPluginResponse.class),

            // Added in 1.20.2
            LOGIN_SUCCESS_ACK(0x03, WrapperLoginClientLoginSuccessAck.class),

            // Added in 1.20.5
            COOKIE_RESPONSE(0x04, WrapperLoginClientCookieResponse.class),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                switch (packetID) {
                    case 0x00:
                        return LOGIN_START;
                    case 0x01:
                        return ENCRYPTION_RESPONSE;
                    case 0x02:
                        return LOGIN_PLUGIN_RESPONSE;
                    case 0x03:
                        return LOGIN_SUCCESS_ACK;
                    case 0x04:
                        return COOKIE_RESPONSE;
                    default:
                        return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            DISCONNECT(0x00, WrapperLoginServerDisconnect.class),
            ENCRYPTION_REQUEST(0x01, WrapperLoginServerEncryptionRequest.class),
            LOGIN_SUCCESS(0x02, WrapperLoginServerLoginSuccess.class),

            // Added in 1.8
            SET_COMPRESSION(0x03, WrapperLoginServerSetCompression.class),

            // Added in 1.13
            LOGIN_PLUGIN_REQUEST(0x04, WrapperLoginServerPluginRequest.class),

            // Added in 1.20.5
            COOKIE_REQUEST(0x05, WrapperLoginServerCookieRequest.class),
            ;

            private final int id;
            private final Class<? extends PacketWrapper<?>> wrapperClass;

            Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
                this.id = id;
                this.wrapperClass = wrapperClass;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapperClass;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                switch (packetID) {
                    case 0x00:
                        return DISCONNECT;
                    case 0x01:
                        return ENCRYPTION_REQUEST;
                    case 0x02:
                        return LOGIN_SUCCESS;
                    case 0x03:
                        return SET_COMPRESSION;
                    case 0x04:
                        return LOGIN_PLUGIN_REQUEST;
                    case 0x05:
                        return COOKIE_REQUEST;
                    default:
                        return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    // Added in 1.20.2
    public static class Configuration {

        public enum Client implements PacketTypeCommon, ServerBoundPacket {

            CLIENT_SETTINGS(WrapperConfigClientSettings.class),
            PLUGIN_MESSAGE(WrapperConfigClientPluginMessage.class),
            CONFIGURATION_END_ACK(WrapperConfigClientConfigurationEndAck.class),
            KEEP_ALIVE(WrapperConfigClientKeepAlive.class),
            PONG(WrapperConfigClientPong.class),
            RESOURCE_PACK_STATUS(WrapperConfigClientResourcePackStatus.class),

            // Added in 1.20.5
            COOKIE_RESPONSE(WrapperConfigClientCookieResponse.class),
            SELECT_KNOWN_PACKS(WrapperConfigClientSelectKnownPacks.class),
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;
            private final Class<? extends PacketWrapper<?>> wrapper;

            Client(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
                this.ids = new int[SERVERBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
                Arrays.fill(this.ids, -1);
                this.wrapper = wrapper;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapper;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ServerboundConfigPacketType_1_20_2.values());
                loadPacketIds(ServerboundConfigPacketType_1_20_5.values());
                // TODO UPDATE Update packet type mappings (config serverbound pt. 2)
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Configuration.Client value = Configuration.Client.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static @Nullable PacketTypeCommon getById(int packetId) {
                return getById(ClientVersion.getLatest(), packetId);
            }

            public static @Nullable PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Deprecated
            public int getId() {
                return this.getId(ClientVersion.getLatest());
            }

            @Override
            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                return this.ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeCommon, ClientBoundPacket {

            PLUGIN_MESSAGE(WrapperConfigServerPluginMessage.class),
            DISCONNECT(WrapperConfigServerDisconnect.class),
            CONFIGURATION_END(WrapperConfigServerConfigurationEnd.class),
            KEEP_ALIVE(WrapperConfigServerKeepAlive.class),
            PING(null),
            REGISTRY_DATA(WrapperConfigServerRegistryData.class),
            RESOURCE_PACK_SEND(WrapperConfigServerResourcePackSend.class),
            UPDATE_ENABLED_FEATURES(WrapperConfigServerUpdateEnabledFeatures.class),
            UPDATE_TAGS(null),

            // Added in 1.20.3
            RESOURCE_PACK_REMOVE(WrapperConfigServerResourcePackRemove.class),

            // Added in 1.20.5
            COOKIE_REQUEST(WrapperConfigServerCookieRequest.class),
            RESET_CHAT(WrapperConfigServerResetChat.class),
            STORE_COOKIE(WrapperConfigServerStoreCookie.class),
            TRANSFER(WrapperConfigServerTransfer.class),
            SELECT_KNOWN_PACKS(WrapperConfigServerSelectKnownPacks.class),

            // added in 1.21
            CUSTOM_REPORT_DETAILS(WrapperConfigServerCustomReportDetails.class),
            SERVER_LINKS(WrapperConfigServerServerLinks.class),
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;
            private final Class<? extends PacketWrapper<?>> wrapper;

            Server(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
                this.ids = new int[CLIENTBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
                Arrays.fill(this.ids, -1);
                this.wrapper = wrapper;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapper;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ClientboundConfigPacketType_1_20_2.values());
                loadPacketIds(ClientboundConfigPacketType_1_20_3.values());
                loadPacketIds(ClientboundConfigPacketType_1_20_5.values());
                loadPacketIds(ClientboundConfigPacketType_1_21.values());
                // TODO UPDATE Update packet type mappings (config clientbound pt. 2)
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Configuration.Server value = Configuration.Server.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static @Nullable PacketTypeCommon getById(int packetId) {
                return getById(ClientVersion.getLatest(), packetId);
            }

            public static @Nullable PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Deprecated
            public int getId() {
                return this.getId(ClientVersion.getLatest());
            }

            @Override
            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                return this.ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Play {

        public enum Client implements PacketTypeCommon, ServerBoundPacket {

            // Packets which no longer exist on the latest version
            CHAT_PREVIEW(WrapperPlayClientChatPreview.class),

            TELEPORT_CONFIRM(WrapperPlayClientTeleportConfirm.class),
            QUERY_BLOCK_NBT(WrapperPlayClientQueryBlockNBT.class),
            SET_DIFFICULTY(WrapperPlayClientSetDifficulty.class),
            CHAT_MESSAGE(WrapperPlayClientChatMessage.class),
            CLIENT_STATUS(WrapperPlayClientClientStatus.class),
            CLIENT_SETTINGS(WrapperPlayClientSettings.class),
            TAB_COMPLETE(WrapperPlayClientTabComplete.class),
            WINDOW_CONFIRMATION(WrapperPlayClientWindowConfirmation.class),
            CLICK_WINDOW_BUTTON(WrapperPlayClientClickWindowButton.class),
            CLICK_WINDOW(WrapperPlayClientClickWindow.class),
            CLOSE_WINDOW(WrapperPlayClientCloseWindow.class),
            PLUGIN_MESSAGE(WrapperPlayClientPluginMessage.class),
            EDIT_BOOK(WrapperPlayClientEditBook.class),
            QUERY_ENTITY_NBT(WrapperPlayClientQueryEntityNBT.class),
            INTERACT_ENTITY(WrapperPlayClientInteractEntity.class),
            GENERATE_STRUCTURE(WrapperPlayClientGenerateStructure.class),
            KEEP_ALIVE(WrapperPlayClientKeepAlive.class),
            LOCK_DIFFICULTY(WrapperPlayClientLockDifficulty.class),
            PLAYER_POSITION(WrapperPlayClientPlayerPosition.class),
            PLAYER_POSITION_AND_ROTATION(WrapperPlayClientPlayerPositionAndRotation.class),
            PLAYER_ROTATION(WrapperPlayClientPlayerRotation.class),
            PLAYER_FLYING(WrapperPlayClientPlayerFlying.class),
            VEHICLE_MOVE(WrapperPlayClientVehicleMove.class),
            STEER_BOAT(WrapperPlayClientSteerBoat.class),
            /**
             * Removed with 1.21.4
             */
            @ApiStatus.Obsolete
            PICK_ITEM(WrapperPlayClientPickItem.class),
            CRAFT_RECIPE_REQUEST(WrapperPlayClientCraftRecipeRequest.class),
            PLAYER_ABILITIES(WrapperPlayClientPlayerAbilities.class),
            PLAYER_DIGGING(WrapperPlayClientPlayerDigging.class),
            ENTITY_ACTION(WrapperPlayClientEntityAction.class),
            /**
             * Removed with 1.21.2
             */
            @ApiStatus.Obsolete
            STEER_VEHICLE(WrapperPlayClientSteerVehicle.class),
            PONG(WrapperPlayClientPong.class),
            RECIPE_BOOK_DATA(null),
            SET_DISPLAYED_RECIPE(WrapperPlayClientSetDisplayedRecipe.class),
            SET_RECIPE_BOOK_STATE(WrapperPlayClientSetRecipeBookState.class),
            NAME_ITEM(WrapperPlayClientNameItem.class),
            RESOURCE_PACK_STATUS(WrapperPlayClientResourcePackStatus.class),
            ADVANCEMENT_TAB(WrapperPlayClientAdvancementTab.class),
            SELECT_TRADE(WrapperPlayClientSelectTrade.class),
            SET_BEACON_EFFECT(WrapperPlayClientSetBeaconEffect.class),
            HELD_ITEM_CHANGE(WrapperPlayClientHeldItemChange.class),
            UPDATE_COMMAND_BLOCK(WrapperPlayClientUpdateCommandBlock.class),
            UPDATE_COMMAND_BLOCK_MINECART(WrapperPlayClientUpdateCommandBlockMinecart.class),
            CREATIVE_INVENTORY_ACTION(WrapperPlayClientCreativeInventoryAction.class),
            UPDATE_JIGSAW_BLOCK(WrapperPlayClientUpdateJigsawBlock.class),
            UPDATE_STRUCTURE_BLOCK(WrapperPlayClientSetStructureBlock.class),
            UPDATE_SIGN(WrapperPlayClientUpdateSign.class),
            ANIMATION(WrapperPlayClientAnimation.class),
            SPECTATE(WrapperPlayClientSpectate.class),
            PLAYER_BLOCK_PLACEMENT(WrapperPlayClientPlayerBlockPlacement.class),
            USE_ITEM(WrapperPlayClientUseItem.class),

            /**
             * Added with 1.19
             */
            CHAT_COMMAND(WrapperPlayClientChatCommand.class),

            /**
             * Added with 1.19.1
             */
            CHAT_ACK(WrapperPlayClientChatAck.class),

            /**
             * Added with 1.19.3
             */
            CHAT_SESSION_UPDATE(WrapperPlayClientChatSessionUpdate.class),

            /**
             * Added with 1.20.2
             */
            CHUNK_BATCH_ACK(WrapperPlayClientChunkBatchAck.class),
            /**
             * Added with 1.20.2
             */
            CONFIGURATION_ACK(WrapperPlayClientConfigurationAck.class),
            /**
             * Added with 1.20.2
             */
            DEBUG_PING(WrapperPlayClientDebugPing.class),

            /**
             * Added with 1.20.3
             */
            SLOT_STATE_CHANGE(WrapperPlayClientSlotStateChange.class),

            /**
             * Added with 1.20.5
             */
            CHAT_COMMAND_UNSIGNED(WrapperPlayClientChatCommandUnsigned.class),
            /**
             * Added with 1.20.5
             */
            COOKIE_RESPONSE(WrapperPlayClientCookieResponse.class),
            /**
             * Added with 1.20.5
             */
            DEBUG_SAMPLE_SUBSCRIPTION(WrapperPlayClientDebugSampleSubscription.class),

            /**
             * Added with 1.21.2
             */
            CLIENT_TICK_END(WrapperPlayClientClientTickEnd.class),
            /**
             * Added with 1.21.2
             */
            SELECT_BUNDLE_ITEM(WrapperPlayClientSelectBundleItem.class),
            /**
             * Added with 1.21.2, based on {@link #STEER_VEHICLE}
             */
            PLAYER_INPUT(WrapperPlayClientPlayerInput.class),

            /**
             * Added with 1.21.4
             */
            PICK_ITEM_FROM_BLOCK(WrapperPlayClientPickItemFromBlock.class),
            /**
             * Added with 1.21.4
             */
            PICK_ITEM_FROM_ENTITY(WrapperPlayClientPickItemFromEntity.class),
            /**
             * Added with 1.21.4
             */
            PLAYER_LOADED(WrapperPlayClientPlayerLoaded.class),

            /**
             * Added with 1.21.5
             */
            SET_TEST_BLOCK(WrapperPlayClientSetTestBlock.class),
            /**
             * Added with 1.21.5
             */
            TEST_INSTANCE_BLOCK_ACTION(WrapperPlayClientTestInstanceBlockAction.class),
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;
            private final Class<? extends PacketWrapper<?>> wrapper;

            Client(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
                ids = new int[SERVERBOUND_PLAY_VERSION_MAPPER.getVersions().length];
                Arrays.fill(ids, -1);
                this.wrapper = wrapper;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapper;
            }

            @Nullable
            public static PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                return packetIdMap.get(packetId);
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Client value = Client.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index,
                            k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ServerboundPacketType_1_7_10.values());
                loadPacketIds(ServerboundPacketType_1_8.values());
                loadPacketIds(ServerboundPacketType_1_9.values());
                loadPacketIds(ServerboundPacketType_1_12.values());
                loadPacketIds(ServerboundPacketType_1_12_1.values());
                loadPacketIds(ServerboundPacketType_1_13.values());
                loadPacketIds(ServerboundPacketType_1_14.values());
                loadPacketIds(ServerboundPacketType_1_15_2.values());
                loadPacketIds(ServerboundPacketType_1_16.values());
                loadPacketIds(ServerboundPacketType_1_16_2.values());
                loadPacketIds(ServerboundPacketType_1_17.values());
                loadPacketIds(ServerboundPacketType_1_19.values());
                loadPacketIds(ServerboundPacketType_1_19_1.values());
                loadPacketIds(ServerboundPacketType_1_19_3.values());
                loadPacketIds(ServerboundPacketType_1_19_4.values());
                loadPacketIds(ServerboundPacketType_1_20_2.values());
                loadPacketIds(ServerboundPacketType_1_20_3.values());
                loadPacketIds(ServerboundPacketType_1_20_5.values());
                loadPacketIds(ServerboundPacketType_1_21_2.values());
                loadPacketIds(ServerboundPacketType_1_21_4.values());
                loadPacketIds(ServerboundPacketType_1_21_5.values());
                //TODO UPDATE Update packet type mappings (serverbound pt. 2)
            }

            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                return ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeCommon, ClientBoundPacket {

            // Packets which are no longer exist on the latest version
            SET_COMPRESSION(WrapperPlayServerSetCompression.class),
            MAP_CHUNK_BULK(WrapperPlayServerChunkDataBulk.class),
            UPDATE_ENTITY_NBT(WrapperPlayServerUpdateEntityNBT.class),
            UPDATE_SIGN(null),
            USE_BED(WrapperPlayServerUseBed.class),
            SPAWN_WEATHER_ENTITY(WrapperPlayServerSpawnWeatherEntity.class),
            TITLE(WrapperPlayServerSetTitleSubtitle.class),
            WORLD_BORDER(WrapperPlayServerInitializeWorldBorder.class),
            COMBAT_EVENT(WrapperPlayServerCombatEvent.class),
            ENTITY_MOVEMENT(WrapperPlayServerEntityMovement.class),
            SPAWN_LIVING_ENTITY(WrapperPlayServerSpawnLivingEntity.class),
            SPAWN_PAINTING(WrapperPlayServerSpawnPainting.class),
            SCULK_VIBRATION_SIGNAL(null),
            ACKNOWLEDGE_PLAYER_DIGGING(WrapperPlayServerAcknowledgePlayerDigging.class),
            CHAT_PREVIEW_PACKET(WrapperPlayServerChatPreview.class),
            NAMED_SOUND_EFFECT(null),
            PLAYER_CHAT_HEADER(WrapperPlayServerPlayerChatHeader.class),
            PLAYER_INFO(WrapperPlayServerPlayerInfo.class),
            DISPLAY_CHAT_PREVIEW(WrapperPlayServerSetDisplayChatPreview.class),
            UPDATE_ENABLED_FEATURES(WrapperPlayServerUpdateEnabledFeatures.class),
            SPAWN_PLAYER(WrapperPlayServerSpawnPlayer.class),

            // Still existing packets
            WINDOW_CONFIRMATION(WrapperPlayServerWindowConfirmation.class),
            SPAWN_ENTITY(WrapperPlayServerSpawnEntity.class),
            /**
             * Removed with 1.21.5
             */
            @ApiStatus.Obsolete
            SPAWN_EXPERIENCE_ORB(WrapperPlayServerSpawnExperienceOrb.class),
            ENTITY_ANIMATION(WrapperPlayServerEntityAnimation.class),
            STATISTICS(null),
            BLOCK_BREAK_ANIMATION(WrapperPlayServerBlockBreakAnimation.class),
            BLOCK_ENTITY_DATA(WrapperPlayServerBlockEntityData.class),
            BLOCK_ACTION(WrapperPlayServerBlockAction.class),
            BLOCK_CHANGE(WrapperPlayServerBlockChange.class),
            BOSS_BAR(WrapperPlayServerBossBar.class),
            SERVER_DIFFICULTY(WrapperPlayServerDifficulty.class),
            CLEAR_TITLES(WrapperPlayServerClearTitles.class),
            TAB_COMPLETE(WrapperPlayServerTabComplete.class),
            MULTI_BLOCK_CHANGE(WrapperPlayServerMultiBlockChange.class),
            DECLARE_COMMANDS(WrapperPlayServerDeclareCommands.class),
            CLOSE_WINDOW(WrapperPlayServerCloseWindow.class),
            WINDOW_ITEMS(WrapperPlayServerWindowItems.class),
            WINDOW_PROPERTY(WrapperPlayServerWindowProperty.class),
            SET_SLOT(WrapperPlayServerSetSlot.class),
            SET_COOLDOWN(WrapperPlayServerSetCooldown.class),
            PLUGIN_MESSAGE(WrapperPlayServerPluginMessage.class),
            DISCONNECT(WrapperPlayServerDisconnect.class),
            ENTITY_STATUS(WrapperPlayServerEntityStatus.class),
            EXPLOSION(WrapperPlayServerExplosion.class),
            UNLOAD_CHUNK(WrapperPlayServerUnloadChunk.class),
            CHANGE_GAME_STATE(WrapperPlayServerChangeGameState.class),
            OPEN_HORSE_WINDOW(WrapperPlayServerOpenHorseWindow.class),
            INITIALIZE_WORLD_BORDER(WrapperPlayServerInitializeWorldBorder.class),
            KEEP_ALIVE(WrapperPlayServerKeepAlive.class),
            CHUNK_DATA(WrapperPlayServerChunkData.class),
            EFFECT(null),
            PARTICLE(WrapperPlayServerParticle.class),
            UPDATE_LIGHT(WrapperPlayServerUpdateLight.class),
            JOIN_GAME(WrapperPlayServerJoinGame.class),
            MAP_DATA(WrapperPlayServerMapData.class),
            MERCHANT_OFFERS(WrapperPlayServerMerchantOffers.class),
            ENTITY_RELATIVE_MOVE(WrapperPlayServerEntityRelativeMove.class),
            ENTITY_RELATIVE_MOVE_AND_ROTATION(WrapperPlayServerEntityRelativeMoveAndRotation.class),
            ENTITY_ROTATION(WrapperPlayServerEntityRotation.class),
            VEHICLE_MOVE(WrapperPlayServerVehicleMove.class),
            OPEN_BOOK(WrapperPlayServerOpenBook.class),
            OPEN_WINDOW(WrapperPlayServerOpenWindow.class),
            OPEN_SIGN_EDITOR(WrapperPlayServerOpenSignEditor.class),
            PING(WrapperPlayServerPing.class),
            CRAFT_RECIPE_RESPONSE(WrapperPlayServerCraftRecipeResponse.class),
            PLAYER_ABILITIES(WrapperPlayServerPlayerAbilities.class),
            END_COMBAT_EVENT(WrapperPlayServerEndCombatEvent.class),
            ENTER_COMBAT_EVENT(WrapperPlayServerEnterCombatEvent.class),
            DEATH_COMBAT_EVENT(WrapperPlayServerDeathCombatEvent.class),
            FACE_PLAYER(WrapperPlayServerFacePlayer.class),
            PLAYER_POSITION_AND_LOOK(WrapperPlayServerPlayerPositionAndLook.class),
            /**
             * Removed with 1.21.2
             */
            @ApiStatus.Obsolete
            UNLOCK_RECIPES(null),
            DESTROY_ENTITIES(WrapperPlayServerDestroyEntities.class),
            REMOVE_ENTITY_EFFECT(WrapperPlayServerRemoveEntityEffect.class),
            RESOURCE_PACK_SEND(WrapperPlayServerResourcePackSend.class),
            RESPAWN(WrapperPlayServerRespawn.class),
            ENTITY_HEAD_LOOK(WrapperPlayServerEntityHeadLook.class),
            SELECT_ADVANCEMENTS_TAB(WrapperPlayServerSelectAdvancementsTab.class),
            ACTION_BAR(WrapperPlayServerActionBar.class),
            WORLD_BORDER_CENTER(WrapperPlayServerWorldBorderCenter.class),
            WORLD_BORDER_LERP_SIZE(WrapperPlayWorldBorderLerpSize.class),
            WORLD_BORDER_SIZE(WrapperPlayServerWorldBorderSize.class),
            WORLD_BORDER_WARNING_DELAY(WrapperPlayWorldBorderWarningDelay.class),
            WORLD_BORDER_WARNING_REACH(WrapperPlayServerWorldBorderWarningReach.class),
            CAMERA(WrapperPlayServerCamera.class),
            HELD_ITEM_CHANGE(WrapperPlayServerHeldItemChange.class),
            UPDATE_VIEW_POSITION(WrapperPlayServerUpdateViewPosition.class),
            UPDATE_VIEW_DISTANCE(WrapperPlayServerUpdateViewDistance.class),
            SPAWN_POSITION(WrapperPlayServerSpawnPosition.class),
            DISPLAY_SCOREBOARD(WrapperPlayServerDisplayScoreboard.class),
            ENTITY_METADATA(WrapperPlayServerEntityMetadata.class),
            ATTACH_ENTITY(WrapperPlayServerAttachEntity.class),
            ENTITY_VELOCITY(WrapperPlayServerEntityVelocity.class),
            ENTITY_EQUIPMENT(WrapperPlayServerEntityEquipment.class),
            SET_EXPERIENCE(WrapperPlayServerSetExperience.class),
            UPDATE_HEALTH(WrapperPlayServerUpdateHealth.class),
            SCOREBOARD_OBJECTIVE(WrapperPlayServerScoreboardObjective.class),
            SET_PASSENGERS(WrapperPlayServerSetPassengers.class),
            TEAMS(WrapperPlayServerTeams.class),
            UPDATE_SCORE(WrapperPlayServerUpdateScore.class),
            UPDATE_SIMULATION_DISTANCE(WrapperPlayServerUpdateSimulationDistance.class),
            SET_TITLE_SUBTITLE(WrapperPlayServerSetTitleSubtitle.class),
            TIME_UPDATE(WrapperPlayServerTimeUpdate.class),
            SET_TITLE_TEXT(WrapperPlayServerSetTitleText.class),
            SET_TITLE_TIMES(WrapperPlayServerSetTitleTimes.class),
            ENTITY_SOUND_EFFECT(WrapperPlayServerEntitySoundEffect.class),
            SOUND_EFFECT(WrapperPlayServerSoundEffect.class),
            STOP_SOUND(null),
            PLAYER_LIST_HEADER_AND_FOOTER(WrapperPlayServerPlayerListHeaderAndFooter.class),
            NBT_QUERY_RESPONSE(WrapperPlayServerNBTQueryResponse.class),
            COLLECT_ITEM(WrapperPlayServerCollectItem.class),
            ENTITY_TELEPORT(WrapperPlayServerEntityTeleport.class),
            UPDATE_ADVANCEMENTS(null),
            UPDATE_ATTRIBUTES(WrapperPlayServerUpdateAttributes.class),
            ENTITY_EFFECT(WrapperPlayServerEntityEffect.class),
            DECLARE_RECIPES(WrapperPlayServerDeclareRecipes.class),
            TAGS(WrapperPlayServerTags.class),
            CHAT_MESSAGE(WrapperPlayServerChatMessage.class),

            // Added in 1.19
            ACKNOWLEDGE_BLOCK_CHANGES(WrapperPlayServerAcknowledgeBlockChanges.class),
            SERVER_DATA(WrapperPlayServerServerData.class),
            SYSTEM_CHAT_MESSAGE(WrapperPlayServerSystemChatMessage.class),

            // Added in 1.19.1
            DELETE_CHAT(WrapperPlayServerDeleteChat.class),
            CUSTOM_CHAT_COMPLETIONS(WrapperPlayServerCustomChatCompletions.class),

            // Added in 1.19.3
            DISGUISED_CHAT(WrapperPlayServerDisguisedChat.class),
            PLAYER_INFO_REMOVE(WrapperPlayServerPlayerInfoRemove.class),
            PLAYER_INFO_UPDATE(WrapperPlayServerPlayerInfoUpdate.class),

            // Added in 1.19.4
            DAMAGE_EVENT(WrapperPlayServerDamageEvent.class),
            HURT_ANIMATION(WrapperPlayServerHurtAnimation.class),
            BUNDLE(WrapperPlayServerBundle.class),
            CHUNK_BIOMES(null),

            // Added in 1.20.2
            CHUNK_BATCH_END(WrapperPlayServerChunkBatchEnd.class),
            CHUNK_BATCH_BEGIN(WrapperPlayServerChunkBatchBegin.class),
            DEBUG_PONG(WrapperPlayServerDebugPong.class),
            CONFIGURATION_START(WrapperPlayServerConfigurationStart.class),

            // Added in 1.20.3
            RESET_SCORE(WrapperPlayServerResetScore.class),
            RESOURCE_PACK_REMOVE(WrapperPlayServerResourcePackRemove.class),
            TICKING_STATE(WrapperPlayServerTickingState.class),
            TICKING_STEP(WrapperPlayServerTickingStep.class),

            // Added in 1.20.5
            COOKIE_REQUEST(WrapperPlayServerCookieRequest.class),
            DEBUG_SAMPLE(WrapperPlayServerDebugSample.class),
            STORE_COOKIE(WrapperPlayServerStoreCookie.class),
            TRANSFER(WrapperPlayServerTransfer.class),
            PROJECTILE_POWER(WrapperPlayServerProjectilePower.class),

            // added in 1.21
            CUSTOM_REPORT_DETAILS(WrapperPlayServerCustomReportDetails.class),
            SERVER_LINKS(WrapperPlayServerServerLinks.class),

            // added in 1.21.2
            MOVE_MINECART(WrapperPlayServerMoveMinecart.class),
            SET_CURSOR_ITEM(WrapperPlayServerSetCursorItem.class),
            SET_PLAYER_INVENTORY(WrapperPlayServerSetPlayerInventory.class),
            ENTITY_POSITION_SYNC(WrapperPlayServerEntityPositionSync.class),
            PLAYER_ROTATION(WrapperPlayServerPlayerRotation.class),
            RECIPE_BOOK_ADD(WrapperPlayServerRecipeBookAdd.class),
            RECIPE_BOOK_REMOVE(WrapperPlayServerRecipeBookRemove.class),
            RECIPE_BOOK_SETTINGS(WrapperPlayServerRecipeBookSettings.class),

            /**
             * Added with 1.21.5
             */
            TEST_INSTANCE_BLOCK_STATUS(WrapperPlayServerTestInstanceBlockStatus.class),
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;
            private final Class<? extends PacketWrapper<?>> wrapper;

            Server(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
                ids = new int[CLIENTBOUND_PLAY_VERSION_MAPPER.getVersions().length];
                Arrays.fill(ids, -1);
                this.wrapper = wrapper;
            }

            @Override
            public Class<? extends PacketWrapper<?>> getWrapperClass() {
                return wrapper;
            }

            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                return ids[index];
            }

            @Nullable
            public static PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Server value = Server.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ClientboundPacketType_1_7_10.values());
                loadPacketIds(ClientboundPacketType_1_8.values());
                loadPacketIds(ClientboundPacketType_1_9.values());
                loadPacketIds(ClientboundPacketType_1_9_3.values());
                loadPacketIds(ClientboundPacketType_1_12.values());
                loadPacketIds(ClientboundPacketType_1_12_1.values());
                loadPacketIds(ClientboundPacketType_1_13.values());
                loadPacketIds(ClientboundPacketType_1_14.values());
                loadPacketIds(ClientboundPacketType_1_14_4.values());
                loadPacketIds(ClientboundPacketType_1_15.values());
                loadPacketIds(ClientboundPacketType_1_15_2.values());
                loadPacketIds(ClientboundPacketType_1_16.values());
                loadPacketIds(ClientboundPacketType_1_16_2.values());
                loadPacketIds(ClientboundPacketType_1_17.values());
                loadPacketIds(ClientboundPacketType_1_18.values());
                loadPacketIds(ClientboundPacketType_1_19.values());
                loadPacketIds(ClientboundPacketType_1_19_1.values());
                loadPacketIds(ClientboundPacketType_1_19_3.values());
                loadPacketIds(ClientboundPacketType_1_19_4.values());
                loadPacketIds(ClientboundPacketType_1_20_2.values());
                loadPacketIds(ClientboundPacketType_1_20_3.values());
                loadPacketIds(ClientboundPacketType_1_20_5.values());
                loadPacketIds(ClientboundPacketType_1_21.values());
                loadPacketIds(ClientboundPacketType_1_21_2.values());
                loadPacketIds(ClientboundPacketType_1_21_5.values());
                //TODO UPDATE Update packet type mappings (clientbound pt. 2)
            }
        }
    }
}
