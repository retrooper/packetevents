/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.injector.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector.PreViaState;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NoSuchElementException;

@NullMarked
@ApiStatus.Internal
public final class PacketHandlerRelocation {

    public static final String PRE_VIA_PREFIX = "pre-";
    public static final String SPLITTER_NAME = "splitter";

    private static final String DECOMPRESS_NAME = "decompress";
    private static final String COMPRESS_NAME = "compress";
    private static final String DECODER_NAME = "decoder";
    private static final String ENCODER_NAME = "encoder";
    private static final String INBOUND_CONFIG_NAME = "inbound_config";
    private static final String OUTBOUND_CONFIG_NAME = "outbound_config";

    public static final @Nullable Object COMPRESSION_ENABLED_EVENT = findCompressionEnabledEvent();

    private PacketHandlerRelocation() {
    }

    public static Slot slotOf(PreViaState state) {
        return state == PreViaState.PRE_VIA ? Slot.PRE_VIA : Slot.NORMAL;
    }

    public static void inject(Channel channel, User user, Slot slot) {
        inject(channel, user, slot, stateFor(slot));
    }

    public static void inject(Channel channel, User user, Slot slot, PreViaState state) {
        ChannelPipeline pipeline = channel.pipeline();
        String decoderName = slot.decoderName();
        String encoderName = slot.encoderName();

        PacketEventsDecoder decoder = (PacketEventsDecoder) pipeline.get(decoderName);
        PacketEventsEncoder encoder = (PacketEventsEncoder) pipeline.get(encoderName);
        if (decoder != null && encoder != null && isCorrectlyPlaced(pipeline, slot)) {
            // keep the existing instances, but make sure their dispatch mode is up to date
            decoder.preVia = state;
            encoder.preVia = state;
            return;
        }

        List<String> handlers = pipeline.names();
        String decoderTarget = targetDecoderName(handlers, slot);
        String encoderTarget = targetEncoderName(handlers, slot);
        if (!handlers.contains(decoderTarget) || !handlers.contains(encoderTarget)) {
            throw new IllegalStateException("PacketEvents failed to add the " + slot
                    + " handlers, target handlers not found. Pipeline handlers: "
                    + ChannelHelper.pipelineHandlerNamesAsString(channel));
        }

        if (decoder != null && encoder != null) {
            // recreate the handlers at the correct position, retaining their runtime state
            encoder = new PacketEventsEncoder((PacketEventsEncoder) pipeline.remove(encoderName));
            decoder = new PacketEventsDecoder((PacketEventsDecoder) pipeline.remove(decoderName));
        } else {
            // partially injected or not injected yet - start over with a clean pair
            if (encoder != null) {
                pipeline.remove(encoderName);
            }
            if (decoder != null) {
                pipeline.remove(decoderName);
            }
            encoder = new PacketEventsEncoder(user, state);
            decoder = new PacketEventsDecoder(user, state);
        }

        encoder.preVia = state;
        decoder.preVia = state;

        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            PacketEvents.getAPI().getLogManager().debug(
                    "Relocating " + slot + " handlers, pipeline: " + pipeline.names());
        }
        try {
            pipeline
                    .addBefore(decoderTarget, decoderName, decoder)
                    .addBefore(encoderTarget, encoderName, encoder);
        } catch (NoSuchElementException exception) {
            throw new IllegalStateException("PacketEvents failed to add the " + slot
                    + " handlers to the netty pipeline. Pipeline handlers: "
                    + ChannelHelper.pipelineHandlerNamesAsString(channel), exception);
        }
        if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            PacketEvents.getAPI().getLogManager().debug(
                    "Relocated " + slot + " handlers, pipeline: " + pipeline.names());
        }
    }

    public static boolean remove(Channel channel, Slot slot) {
        ChannelPipeline pipeline = channel.pipeline();
        boolean removed = false;
        if (pipeline.get(slot.decoderName()) != null) {
            pipeline.remove(slot.decoderName());
            removed = true;
        }
        if (pipeline.get(slot.encoderName()) != null) {
            pipeline.remove(slot.encoderName());
            removed = true;
        }
        return removed;
    }

    public static boolean isCompressionEnabled(ChannelPipeline pipeline) {
        return pipeline.get(COMPRESS_NAME) != null;
    }

    public static void handleCompressionEnabledEvent(Channel channel, User user, Slot slot) {
        inject(channel, user, slot);
        if (slot == Slot.NORMAL && isPreViaActivated()) {
            // ViaVersion repositions its own handlers while this event is being dispatched.
            // Move our client-versioned handlers afterward, once Via's handlers are final.
            scheduleInjection(channel, user, Slot.PRE_VIA);
        }
    }

    public static boolean relocateForCompression(
            ChannelHandlerContext ctx, User user, Slot slot, ByteBuf buffer
    ) throws InvocationTargetException {
        List<String> handlers = ctx.pipeline().names();
        int compressIndex = handlers.indexOf(COMPRESS_NAME);
        int encoderIndex = handlers.indexOf(slot.encoderName());
        if (compressIndex == -1 || encoderIndex == -1 || compressIndex <= encoderIndex) {
            // compression has not been enabled yet, or we are already behind the compress handler
            return false;
        }

        boolean needsRecompression = false;
        ClientVersion clientVersion = user.getClientVersion();
        if (slot != Slot.PRE_VIA
                || clientVersion == null
                || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
            decompressBuffer(ctx, buffer);
            needsRecompression = true;
        }

        inject(ctx.channel(), user, slot);
        return needsRecompression;
    }

    public static void compressBuffer(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get(COMPRESS_NAME);
        ByteBuf compressed = ctx.alloc().buffer();
        try {
            if (compressor != null) {
                CustomPipelineUtil.callEncode(compressor, ctx, buffer, compressed);
            }
        } finally {
            buffer.clear().writeBytes(compressed);
            compressed.release();
        }
    }

    public static void decompressBuffer(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get(DECOMPRESS_NAME);
        if (decompressor == null) {
            return;
        }
        ByteBuf decompressed = (ByteBuf) CustomPipelineUtil.callDecode(decompressor, ctx, buffer).get(0);
        try {
            buffer.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
    }

    private static PreViaState stateFor(Slot slot) {
        if (slot == Slot.PRE_VIA) {
            return PreViaState.PRE_VIA;
        }
        return PacketEvents.getAPI().getEventManager().hasClientVersionedListeners()
                && !ViaVersionUtil.isAvailable()
                ? PreViaState.FALLBACK
                : PreViaState.NORMAL;
    }

    private static boolean isCorrectlyPlaced(ChannelPipeline pipeline, Slot slot) {
        List<String> handlers = pipeline.names();
        String decoderName = slot.decoderName();
        String encoderName = slot.encoderName();
        if (!handlers.contains(decoderName) || !handlers.contains(encoderName)) {
            return false;
        }

        if (!isBefore(handlers, decoderName, targetDecoderName(handlers, slot))
                || !isBefore(handlers, encoderName, targetEncoderName(handlers, slot))) {
            return false;
        }

        return (!handlers.contains(DECOMPRESS_NAME) || isBefore(handlers, DECOMPRESS_NAME, decoderName))
                && (!handlers.contains(COMPRESS_NAME) || isBefore(handlers, COMPRESS_NAME, encoderName));
    }

    private static String targetDecoderName(List<String> handlers, Slot slot) {
        if (slot == Slot.PRE_VIA) {
            String viaDecoder = ViaVersionUtil.getDecoderName();
            if (viaDecoder != null && handlers.contains(viaDecoder)) {
                return viaDecoder;
            }
            return DECODER_NAME;
        }
        return handlers.contains(INBOUND_CONFIG_NAME) ? INBOUND_CONFIG_NAME : DECODER_NAME;
    }

    private static String targetEncoderName(List<String> handlers, Slot slot) {
        if (slot == Slot.PRE_VIA) {
            String viaEncoder = ViaVersionUtil.getEncoderName();
            if (viaEncoder != null && handlers.contains(viaEncoder)) {
                return viaEncoder;
            }
            return ENCODER_NAME;
        }
        return handlers.contains(OUTBOUND_CONFIG_NAME) ? OUTBOUND_CONFIG_NAME : ENCODER_NAME;
    }

    private static boolean isBefore(List<String> handlers, String handler, String target) {
        boolean found = false;
        for (String current : handlers) {
            if (current.equals(handler)) {
                found = true;
            } else if (current.equals(target)) {
                return found;
            }
        }
        return found;
    }

    private static void scheduleInjection(Channel channel, User user, Slot slot) {
        channel.eventLoop().execute(() -> {
            if (!channel.isActive()) {
                return;
            }
            try {
                inject(channel, user, slot);
            } catch (Exception exception) {
                PacketEvents.getAPI().getLogManager().severe(
                        "PacketEvents failed to relocate the " + slot + " handlers after compression was enabled.",
                        exception);
            }
        });
    }

    private static boolean isPreViaActivated() {
        ChannelInjector injector = PacketEvents.getAPI().getInjector();
        return injector instanceof SpigotChannelInjector
                && ((SpigotChannelInjector) injector).isPreViaVersionActivated();
    }

    private static @Nullable Object findCompressionEnabledEvent() {
        try {
            Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    public enum Slot {

        NORMAL,
        PRE_VIA,
        ;

        public String decoderName() {
            return this == PRE_VIA ? PRE_VIA_PREFIX + PacketEvents.DECODER_NAME : PacketEvents.DECODER_NAME;
        }

        public String encoderName() {
            return this == PRE_VIA ? PRE_VIA_PREFIX + PacketEvents.ENCODER_NAME : PacketEvents.ENCODER_NAME;
        }
    }
}
