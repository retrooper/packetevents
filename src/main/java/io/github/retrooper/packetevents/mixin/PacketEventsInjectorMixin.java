package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.PacketEventsMod;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.network.Connection.class)
public class PacketEventsInjectorMixin {
    @Inject(method = "channelActive", at = @At("HEAD"))
    private void channelActive(ChannelHandlerContext ctx, CallbackInfo info) throws Exception {
        PacketEventsMod.LOGGER.info("Connected!");
        Channel channel = ctx.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING, ClientVersion.getLatest(),
                new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);
        LocalPlayer player = Minecraft.getInstance().player;
        PacketDecoder decoder = new PacketDecoder(user, player);
        PacketEncoder encoder = new PacketEncoder(user, player);
        channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, decoder);
        channel.pipeline().addAfter("prepender", PacketEvents.ENCODER_NAME, encoder);
        //TODO Handle compression
        PacketEventsMod.LOGGER.info("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(channel));
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void channelInactive(ChannelHandlerContext ctx, CallbackInfo info) {
        PacketEventsMod.LOGGER.info("Disconnected!");
    }
}
