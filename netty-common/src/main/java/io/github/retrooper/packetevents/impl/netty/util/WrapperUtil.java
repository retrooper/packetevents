package io.github.retrooper.packetevents.impl.netty.util;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerDisconnect;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerDisconnect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import net.kyori.adventure.text.Component;

@NullMarked
@ApiStatus.Internal
public final class WrapperUtil {

    public static @Nullable PacketWrapper<?> createDisconnectWrapper(ConnectionState state, Component component) {
        switch (state) {
            case HANDSHAKING:
            case STATUS:
                return null;
            case LOGIN:
                return new WrapperLoginServerDisconnect(component);
            case PLAY:
                return new WrapperPlayServerDisconnect(component);
            case CONFIGURATION:
                return new WrapperConfigServerDisconnect(component);
            default:
                throw new AssertionError();
        }
    }
}