package io.github.retrooper.packetevents.loader;

import com.github.retrooper.packetevents.manager.server.ServerVersion;

public interface ChainLoadEntryPoint {
    void initialize(ChainLoadData chainLoadData);
    ServerVersion getNativeVersion();
}
