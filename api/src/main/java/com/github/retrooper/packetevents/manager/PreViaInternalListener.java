package com.github.retrooper.packetevents.manager;


import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import org.jetbrains.annotations.ApiStatus;

/**
 * Keeps the raw client-side state used by previa handlers separate from the normal server state.
 */
@ApiStatus.Internal
public class PreViaInternalListener extends PacketListenerAbstract {

    public PreViaInternalListener() {
        super(PacketListenerPriority.LOWEST);
    }

    @Override
    public boolean isPreVia() {
        return true;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            WrapperHandshakingClientHandshake packet = new WrapperHandshakingClientHandshake(event);
            ClientVersion clientVersion = packet.getClientVersion();

            user.setClientVersion(clientVersion);
            user.setPreViaDecoderState(packet.getNextConnectionState());
            user.setPreViaEncoderState(packet.getNextConnectionState());
        } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
            user.setPreViaDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
            user.setPreViaDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
            user.setPreViaDecoderState(ConnectionState.PLAY);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            if (event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                user.setPreViaEncoderState(ConnectionState.CONFIGURATION);
            } else {
                user.setPreViaEncoderState(ConnectionState.PLAY);
            }
        } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
            user.setPreViaEncoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
            user.setPreViaEncoderState(ConnectionState.PLAY);
        }
    }
}
