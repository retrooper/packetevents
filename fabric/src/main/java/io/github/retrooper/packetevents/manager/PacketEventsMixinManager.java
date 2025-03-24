package io.github.retrooper.packetevents.manager;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;

import java.util.List;
import java.util.Set;

public class PacketEventsMixinManager extends RestrictiveMixinConfigPlugin {
    @Override public String getRefMapperConfig() {
        return "";
    }

    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override public List<String> getMixins() {
        return List.of();
    }
}
