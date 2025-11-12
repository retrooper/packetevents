package io.github.retrooper.packetevents.sponge.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.sponge.util.viaversion.ViaVersionUtil;

public class InternalSpongePacketListener extends InternalPacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            User user = event.getUser();
            WrapperHandshakingClientHandshake packet = new WrapperHandshakingClientHandshake(event);
            ClientVersion clientVersion = packet.getClientVersion();
            ConnectionState state = packet.getNextConnectionState();

            String feature;
            if (ViaVersionUtil.isAvailable()) {
                clientVersion = ClientVersion.getById(ViaVersionUtil.getProtocolVersion(user));
                feature = "ViaVersion";
            } else {
                feature = null;
            }

            LogManager logger = PacketEvents.getAPI().getLogManager();
            if (logger.isDebug()) {
                logger.debug("Processed handshake for " + event.getAddress() + ": "
                        + state.name() + " / " + packet.getClientVersion().getReleaseName()
                        + (feature != null ? " (using " + feature + ")" : ""));
            }

            user.setClientVersion(clientVersion);
            user.setConnectionState(state);
        } else {
            super.onPacketReceive(event);
        }
    }
}
