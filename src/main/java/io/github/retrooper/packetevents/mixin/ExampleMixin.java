package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.ExampleMod;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.network.ClientConnection.class)
public class ExampleMixin {
    @Inject(method = "channelActive", at = @At("HEAD"))
    private void channelActive(ChannelHandlerContext ctx, CallbackInfo info) throws Exception {
        ExampleMod.LOGGER.info("Connected!");
        Channel channel = ctx.channel();
        ChannelAbstract ch = PacketEvents.getAPI().getNettyManager().wrapChannel(channel);
        User user = new User(ch, ConnectionState.HANDSHAKING, ClientVersion.getLatest(),
                new UserProfile(null, null));
        System.out.println("state: " + user.getConnectionState());
        PacketEvents.getAPI().getPlayerManager().USERS.put(ch, user);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        PacketDecoder decoder = new PacketDecoder(user, player);
        PacketEncoder encoder = new PacketEncoder(user, player);
        channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, decoder);
        channel.pipeline().addAfter("prepender", PacketEvents.ENCODER_NAME, encoder);
        ExampleMod.LOGGER.info("Pipeline: " + ch.pipeline().namesToString());
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void channelInactive(ChannelHandlerContext ctx, CallbackInfo info) {
        ExampleMod.LOGGER.info("Disconnected!");
    }
}
