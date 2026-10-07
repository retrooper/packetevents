package io.github.retrooper.packetevents.sponge.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerSetCompression;
import io.github.retrooper.packetevents.sponge.injector.SpongeChannelInjector;
import io.github.retrooper.packetevents.sponge.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.sponge.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import org.jspecify.annotations.Nullable;

public class InternalSpongePacketListener extends InternalPacketListener {

    private @Nullable PacketEventsEncoder getEncoder(ProtocolPacketEvent event) {
        SpongeChannelInjector injector = (SpongeChannelInjector) PacketEvents.getAPI().getInjector();
        return injector.getEncoder((Channel) event.getUser().getChannel());
    }

    @Override
    protected void handlePlayEnter(ProtocolPacketEvent event) {
        PacketEventsEncoder encoder = this.getEncoder(event);
        if (encoder != null) {
            encoder.handledCompression = true; // stop handling compression
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        super.onPacketSend(event);

        if (event.getPacketType() == PacketType.Login.Server.SET_COMPRESSION) {
            WrapperLoginServerSetCompression packet = new WrapperLoginServerSetCompression(event);
            if (packet.getThreshold() > 0) {
                PacketEventsEncoder encoder = this.getEncoder(event);
                if (encoder != null) {
                    encoder.handleCompression = true; // start handling compression relocation
                }
            }
        }
    }

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
