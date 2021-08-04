package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.List;

public class PacketDecoderLegacy extends ByteToMessageDecoder {
    private static Method DECODE_METHOD;
    public volatile Player player;
    public volatile PacketState packetState;
    public final ByteToMessageDecoder minecraftDecoder;

    public PacketDecoderLegacy(ByteToMessageDecoder minecraftDecoder) {
        this.minecraftDecoder = minecraftDecoder;
        this.updateBuffer();
    }

    public void updateBuffer(){
        if (DECODE_METHOD == null) {
            try {
                DECODE_METHOD = io.netty.handler.codec.ByteToMessageDecoder.class.getDeclaredMethod("decode", io.netty.channel.ChannelHandlerContext.class, io.netty.buffer.ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void decode(net.minecraft.util.io.netty.channel.ChannelHandlerContext channelHandlerContext, net.minecraft.util.io.netty.buffer.ByteBuf byteBuf, List<Object> list) throws Exception {
        ByteBuf buf = byteBuf.copy();

        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(channelHandlerContext.channel(), player, buf);
        PacketEvents.get().getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        try {
            DECODE_METHOD.invoke(minecraftDecoder, channelHandlerContext, byteBuf, list);
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }
}
