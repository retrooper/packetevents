package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class BungeeChannelInitializer extends ChannelInitializer<Channel> {
    private static final Method INIT_CHANNEL_METHOD;

    static {
        try {
            INIT_CHANNEL_METHOD = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            INIT_CHANNEL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    private final Object oldInitializer;
    public BungeeChannelInitializer(Object oldInitializer) {
        this.oldInitializer = oldInitializer;
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        if (!channel.isActive()) return;
        INIT_CHANNEL_METHOD.invoke(oldInitializer, channel);

        //No injection if no minecraft handlers are present
        if (channel.pipeline().get("packet-decoder") == null) return;
        if (channel.pipeline().get("packet-encoder") == null) return;

        ServerConnectionInitializer.initChannel(channel, ConnectionState.HANDSHAKING);
    }
}
