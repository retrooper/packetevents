package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

public class ViaVersionUtil {
    private static ViaState available = ViaState.UNKNOWN;
    private static ViaVersionAccessor viaVersionAccessor;

    private ViaVersionUtil() {
    }

    private static void load() {
        if (viaVersionAccessor == null) {
            try {
                Class.forName("com.viaversion.viaversion.api.Via");
                viaVersionAccessor = new ViaVersionAccessorImpl();
            } catch (Exception e) {
                viaVersionAccessor = null;
            }
        }
    }

    public static void checkIfViaIsPresent() {
        boolean present = FabricLoader.getInstance().getModContainer("viaversion").isPresent();
        available = present ? ViaState.ENABLED : ViaState.DISABLED;
    }

    public static boolean isAvailable() {
        if (available == ViaState.UNKNOWN) { // Plugins haven't loaded... let's refer to whether we have a class
            return getViaVersionAccessor() != null;
        }
        return available == ViaState.ENABLED;
    }

    public static ViaVersionAccessor getViaVersionAccessor() {
        load();
        return viaVersionAccessor;
    }

    public static int getProtocolVersion(User user) {
        return getViaVersionAccessor().getProtocolVersion(user);
    }

    public static int getProtocolVersion(ServerPlayer player) {
        return getViaVersionAccessor().getProtocolVersion(player);
    }

    public static Class<?> getUserConnectionClass() {
        return getViaVersionAccessor().getUserConnectionClass();
    }

    public static Class<?> getSpongeDecodeHandlerClass() {
        return getViaVersionAccessor().getSpongeDecodeHandlerClass();
    }

    public static Class<?> getSpongeEncodeHandlerClass() {
        return getViaVersionAccessor().getSpongeEncodeHandlerClass();
    }
}

enum ViaState {
    UNKNOWN,
    DISABLED,
    ENABLED
}