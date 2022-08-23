package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface LegacyConvertible {

    LegacyParticleData toLegacy(ClientVersion version);

}
