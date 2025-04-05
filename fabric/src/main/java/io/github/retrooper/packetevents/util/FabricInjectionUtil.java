package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricInjectionUtil {
    private static final String VIA_DECODER_NAME = "via-decoder";
    private static final String VIA_ENCODER_NAME = "via-encoder";

    public static void injectAtPipelineBuilder(ChannelPipeline pipeline, NetworkSide flow) {
        PacketSide pipelineSide = switch (flow) {
            case CLIENTBOUND -> PacketSide.CLIENT;
            case SERVERBOUND -> PacketSide.SERVER;
        };

        FabricPacketEventsAPI fabricPacketEventsAPI = FabricPacketEventsAPI.getAPI(pipelineSide);
        fabricPacketEventsAPI.getLogManager().debug("Game connected!");

        Channel channel = pipeline.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING,
            null, new UserProfile(null, null));

        fabricPacketEventsAPI.getProtocolManager().setUser(channel, user);

        UserConnectEvent connectEvent = new UserConnectEvent(user);
        fabricPacketEventsAPI.getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }

        String decoderName = channel.pipeline().names().contains("inbound_config") ? "inbound_config" : "decoder";
        channel.pipeline().addBefore(decoderName, PacketEvents.DECODER_NAME, new PacketDecoder(pipelineSide, user));
        String encoderName = channel.pipeline().names().contains("outbound_config") ? "outbound_config" : "encoder";
        channel.pipeline().addBefore(encoderName, PacketEvents.ENCODER_NAME, new PacketEncoder(pipelineSide, user));
        channel.closeFuture().addListener((ChannelFutureListener) future ->
            PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
    }

    // Shared method to remove handlers if they exist
    public static void removeIfExists(ChannelPipeline pipeline, String handlerName) {
        if (pipeline.get(handlerName) != null) {
            pipeline.remove(handlerName);
        }
    }

    // Shared method to reorder handlers after ViaVersion
    public static void reorderHandlers(ChannelHandlerContext ctx, PacketSide side) {
        ChannelPipeline pipeline = ctx.pipeline();

        // Re-inject decoder handler
        ChannelHandler decoder = pipeline.get(PacketEvents.DECODER_NAME);
        removeIfExists(pipeline, PacketEvents.DECODER_NAME);
        if (decoder != null) {
            if (pipeline.get(VIA_DECODER_NAME) != null) {
                // If ViaVersion is present, add our decoder after via-decoder
                pipeline.addAfter(VIA_DECODER_NAME, PacketEvents.DECODER_NAME, decoder);
            } else {
                // If ViaVersion is not present, add our decoder after decompress
                String decoderName = pipeline.names().contains("inbound_config") ? "inbound_config" : "decoder";
                pipeline.addBefore(decoderName, PacketEvents.DECODER_NAME, decoder);
            }
        }

        // Re-inject encoder handler
        ChannelHandler encoder = pipeline.get(PacketEvents.ENCODER_NAME);
        removeIfExists(pipeline, PacketEvents.ENCODER_NAME);
        if (encoder != null) {
            if (pipeline.get(VIA_ENCODER_NAME) != null) {
                // If ViaVersion is present, add our encoder after via-encoder
                pipeline.addAfter(VIA_ENCODER_NAME, PacketEvents.ENCODER_NAME, encoder);
            } else {
                // If ViaVersion is not present, add our encoder after compress
                String encoderName = pipeline.names().contains("outbound_config") ? "outbound_config" : "encoder";
                pipeline.addBefore(encoderName, PacketEvents.ENCODER_NAME, encoder);
            }
        }

        FabricPacketEventsAPI.getAPI(side).getLogManager().debug("Pipeline after reorder: " + pipeline.names());
    }

    public static void fireUserLoginEvent(ServerPlayerEntity player) {
        FabricPacketEventsAPI api = FabricPacketEventsAPI.getServerAPI();

        User user = api.getPlayerManager().getUser(player);
        if (user == null) {
            Object channelObj = api.getPlayerManager().getChannel(player);

            // Check if it's a fake connection
            if (!FakeChannelUtil.isFakeChannel(channelObj) &&
                    (!api.isTerminated() || api.getSettings().isKickIfTerminated())) {
                // Kick the player if they're not a fake player
                // player.connection.disconnect(Component.literal("PacketEvents 2.0 failed to inject"));
                player.networkHandler.disconnect(new TextComponent("PacketEvents 2.0 failed to inject"));
            }
            return;
        }

        api.getEventManager().callEvent(new UserLoginEvent(user, player));
    }
}
