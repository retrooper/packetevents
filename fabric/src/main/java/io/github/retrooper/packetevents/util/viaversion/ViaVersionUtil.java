package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.protocol.player.User;
import io.netty.channel.Channel;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class ViaVersionUtil {
    private static ViaState available = ViaState.UNKNOWN;
    private static ViaVersionAccessor viaVersionAccessor;
    private static final FabricLoader loader = FabricLoader.getInstance();
    private static final EnvType envType = loader.getEnvironmentType();

    private ViaVersionUtil() {
    }

    private static void load(User user) {
        if (viaVersionAccessor == null) {
            try {
                Class.forName("com.viaversion.viaversion.api.Via");
                // Possible for ViaFabricPlus to be loaded but not be activated
                if (envType == EnvType.CLIENT && ((Channel) user.getChannel()).pipeline().get("via-encoder") == null) return;
                viaVersionAccessor = new ViaVersionAccessorImpl();
                available = ViaState.ENABLED;
            } catch (Exception e) {
                viaVersionAccessor = null;
            }
        }
    }

    public static void checkIfViaIsPresent() {
        boolean present = loader.getModContainer("viaversion").isPresent();
        if (!present) {
            available = ViaState.DISABLED;
        } else if (envType == EnvType.SERVER) {
            available = ViaState.ENABLED;
        } else if (envType == EnvType.CLIENT) {
            // ViaFabricPlus can be unloaded client side!
            available = ViaState.UNKNOWN;
        }
    }

    public static boolean isAvailable(User user) {
        if (available == ViaState.UNKNOWN) {
            return getViaVersionAccessor(user) != null;
        }
        return available == ViaState.ENABLED;
    }

    public static ViaVersionAccessor getViaVersionAccessor(User user) {
        load(user);
        return viaVersionAccessor;
    }

    public static int getProtocolVersion(User user) {
        return getViaVersionAccessor(user).getProtocolVersion(user);
    }
}

enum ViaState {
    UNKNOWN,
    DISABLED,
    ENABLED
}