package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.ChannelUtilities;
import io.github.retrooper.packetevents.PacketEventsMod;
import io.github.retrooper.packetevents.handler.ServerPacketDecoder;
import io.github.retrooper.packetevents.handler.ServerPacketEncoder;
import io.github.retrooper.packetevents.handler.ClientPacketDecoder;
import io.github.retrooper.packetevents.handler.ClientPacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.network.Connection.class)
public class PacketEventsInjectorMixin {

    @Shadow @Final
    private PacketFlow receiving;

    @Inject(method = "channelActive", at = @At("HEAD"))
    private void channelActive(ChannelHandlerContext ctx, CallbackInfo info) {

        Channel channel = ctx.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING, ClientVersion.getLatest(),
                new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);
        ServerPacketDecoder decoder;
        ServerPacketEncoder encoder;
        if(receiving == PacketFlow.CLIENTBOUND) {
            PacketEventsMod.LOGGER.info("Connected as Client!");
            decoder = new ClientPacketDecoder(user);
            encoder = new ClientPacketEncoder(user);
        }
        else
        {

            PacketEventsMod.LOGGER.info("Connected as Server!");
            decoder = new ServerPacketDecoder(user);
            encoder = new ServerPacketEncoder(user);
        }

        if(ChannelUtilities.pipelineContainsSplitterAndPrepender(channel.pipeline()))
        {
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, decoder);
            channel.pipeline().addAfter("prepender", PacketEvents.ENCODER_NAME, encoder);
        }
        PacketEventsMod.LOGGER.info("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(channel));
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void channelInactive(ChannelHandlerContext ctx, CallbackInfo info) {
        PacketEventsMod.LOGGER.debug("Disconnected!");
    }
}
