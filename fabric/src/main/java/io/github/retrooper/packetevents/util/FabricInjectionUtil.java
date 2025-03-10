package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.SharedConstants;
import net.minecraft.network.protocol.PacketFlow;

public class FabricInjectionUtil {
    private static final String VIA_DECODER_NAME = "via-decoder";
    private static final String VIA_ENCODER_NAME = "via-encoder";

    private static final ClientVersion CLIENT_VERSION =
        ClientVersion.getById(SharedConstants.getProtocolVersion());

    public static void injectAtPipelineBuilder(ChannelPipeline pipeline, PacketFlow flow) {
        PacketSide pipelineSide = switch (flow) {
            case CLIENTBOUND -> PacketSide.CLIENT;
            case SERVERBOUND -> PacketSide.SERVER;
        };
        PacketSide apiSide = PacketEvents.getAPI().getInjector().getPacketSide();
        if (pipelineSide != apiSide) {
            // if pipeline side doesn't match api side, don't inject into
            // this pipeline - it probably means this is the pipeline from
            // integrated server to minecraft client, which is currently unsupported
            PacketEvents.getAPI().getLogManager().debug("Skipped pipeline injection on " + pipelineSide);
            return;
        }

        PacketEvents.getAPI().getLogManager().debug("Game connected!");

        Channel channel = pipeline.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING,
            null, new UserProfile(null, null));
        ProtocolManager.USERS.put(channel.pipeline(), user);

        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }

        String decoderName = channel.pipeline().names().contains("inbound_config") ? "inbound_config" : "decoder";
        channel.pipeline().addBefore(decoderName, PacketEvents.DECODER_NAME, new PacketDecoder(apiSide, user));
        String encoderName = channel.pipeline().names().contains("outbound_config") ? "outbound_config" : "encoder";
        channel.pipeline().addBefore(encoderName, PacketEvents.ENCODER_NAME, new PacketEncoder(apiSide, user));
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
    public static void reorderHandlers(ChannelHandlerContext ctx) {
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

        // Log the pipeline for debugging
        PacketEvents.getAPI().getLogManager().debug("Pipeline after reorder: " + pipeline.names());
    }
}
