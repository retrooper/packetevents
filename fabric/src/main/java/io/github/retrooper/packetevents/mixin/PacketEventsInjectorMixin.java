package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.PacketEventsMod;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.BandwidthDebugMonitor;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;


@Mixin(net.minecraft.network.Connection.class)
public class PacketEventsInjectorMixin {
    @Inject(method = "configureSerialization", at = @At("TAIL"))
    private static void configureSerialization(ChannelPipeline pipeline, PacketFlow flow, boolean memoryOnly, BandwidthDebugMonitor bandwithDebugMonitor, CallbackInfo ci) throws Exception {
        PacketEvents.getAPI().getLogManager().debug("Game connected!");
        Channel channel = pipeline.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING, ClientVersion.getLatest(),
                new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);

        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }
        PacketDecoder decoder = new PacketDecoder(user);
        PacketEncoder encoder = new PacketEncoder(user);
        channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, decoder);
        channel.pipeline().addAfter("prepender", PacketEvents.ENCODER_NAME, encoder);
        channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
    }
}
