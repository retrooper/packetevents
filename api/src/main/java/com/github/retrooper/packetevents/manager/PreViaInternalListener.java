package com.github.retrooper.packetevents.manager;


import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.ApiStatus;

/**
 * Handles decoder state transitions for the pre-via decoder on servers older than 1.20.2
 * The pre-via decoder processes client packets before ViaVersion so the decoder state
 * must follow the client's protocol: LOGIN → CONFIGURATION → PLAY
 * On pre-1.20.2 servers ViaVersion cancels the transition packets (LOGIN_SUCCESS_ACK, CONFIGURATION_END_ACK)
 * before they reach the post-via decoder, so this listener handles them on the pre-via side instead.
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
        if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
            user.setDecoderState(ConnectionState.PLAY);
        }
    }
}
