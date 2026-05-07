package io.github.retrooper.packetevents.injector.connection;

import com.github.retrooper.packetevents.manager.PreViaSupport;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PreViaPipelineSupport {

    public enum DispatchMode {
        DISABLED,
        FALLBACK,
        PIPELINE
    }

    private PreViaPipelineSupport() {
    }

    public static DispatchMode getDispatchMode() {
        if (!PreViaSupport.hasPreViaListeners()) {
            return DispatchMode.DISABLED;
        }

        return ViaVersionUtil.isAvailable() ? DispatchMode.PIPELINE : DispatchMode.FALLBACK;
    }

    public static boolean shouldDispatchFallbackPreViaEvents(boolean pipelinePreVia) {
        return !pipelinePreVia && getDispatchMode() == DispatchMode.FALLBACK;
    }
}
