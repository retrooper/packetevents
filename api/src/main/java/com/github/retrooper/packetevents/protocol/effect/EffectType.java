package com.github.retrooper.packetevents.protocol.effect;

import com.github.retrooper.packetevents.protocol.effect.type.Effects;
import org.jetbrains.annotations.NotNull;

public class EffectType {
    private Effects effect;

    public EffectType(@NotNull final Effects effect) {
        this.effect = effect;
    }

    public Effects getEffect() {
        return effect;
    }

    public void setEffect(Effects effect) {
        this.effect = effect;
    }

    public Type getType() {
        return Type.getType(effect.getName().getKey());
    }

    public enum Type {
        SOUND,
        VISUAL;

        public static Type getType(@NotNull final String key) {
            if (key.equals("sound")) {
                return SOUND;
            } else if (key.equals("visual")) {
                return VISUAL;
            }
            return null;
        }
    }
}
