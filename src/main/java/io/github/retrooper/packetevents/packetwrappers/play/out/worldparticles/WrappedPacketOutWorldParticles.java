package io.github.retrooper.packetevents.packetwrappers.play.out.worldparticles;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedPacketOutWorldParticles extends WrappedPacket {
    private static Class<?> particleEnumClass;
    private static Method particleEnumGetNameMethod;
    public WrappedPacketOutWorldParticles(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            particleEnumClass = NMSUtils.getNMSClass("EnumParticle");
        } catch (ClassNotFoundException e) {
            try {
                particleEnumClass = NMSUtils.getNMSClass("ParticleParam");
                particleEnumGetNameMethod = Reflection.getMethod(particleEnumClass, String.class, 0);
            } catch (ClassNotFoundException e2) {
                throw new IllegalArgumentException("Failed to find the particle enum while loading the wrapper!");
            }
        }
    }

    public Particle getParticle() {
        String particleName;
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            particleName = readString(0);
        } else if (version.isLowerThan(ServerVersion.v_1_13)) {//TODO civ
            Object particleEnumObj = readObject(0, particleEnumClass);
            particleName = particleEnumObj.toString();
        } else {
            Object particleParamObj = readObject(0, particleEnumClass);
            try {
                particleName = particleEnumGetNameMethod.invoke(particleParamObj).toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                particleName = "lol"; //why? nice spigot
            }
        }
        return Particle.getParticleByName(particleName);
    }

    public float getX() {
        return readFloat(0);
    }

    public float getY() {
        return readFloat(1);
    }

    public float getZ() {
        return readFloat(2);
    }

    public float getOffsetX() {
        return readFloat(3);
    }

    public float getOffsetY() {
        return readFloat(4);
    }

    public float getOffsetZ() {
        return readFloat(5);
    }

    public float getParticleData() {
        return readFloat(6);
    }

    public int getParticleCount() {
        return readInt(0);
    }

    public boolean isLongDistance() {
        return readBoolean(0);
    }

    public enum Particle {
        EXPLOSION_NORMAL("explode", 0),
        EXPLOSION_LARGE("largeexplode", 1),
        EXPLOSION_HUGE("hugeexplosion", 2),
        FIREWORKS_SPARK("fireworksSpark", 3),
        WATER_BUBBLE("bubble", 4),
        WATER_SPLASH("splash", 5),
        WATER_WAKE("wake", 6),
        SUSPENDED("suspended", 7),
        SUSPENDED_DEPTH("depthsuspend", 8),
        CRIT("crit", 9),
        CRIT_MAGIC("magicCrit", 10),
        SMOKE_NORMAL("smoke", 11),
        SMOKE_LARGE("largesmoke", 12),
        SPELL("spell", 13),
        SPELL_INSTANT("instantSpell", 14),
        SPELL_MOB("mobSpell", 15),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16),
        SPELL_WITCH("witchMagic", 17),
        DRIP_WATER("dripWater", 18),
        DRIP_LAVA("dripLava", 19),
        VILLAGER_ANGRY("angryVillager", 20),
        VILLAGER_HAPPY("happyVillager", 21),
        TOWN_AURA("townaura", 22),
        NOTE("note", 23),
        PORTAL("portal", 24),
        ENCHANTMENT_TABLE("enchantmenttable", 25),
        FLAME("flame", 26),
        LAVA("lava", 27),
        FOOTSTEP("footstep", 28),
        CLOUD("cloud", 29),
        REDSTONE("reddust", 30),
        SNOWBALL("snowballpoof", 31),
        SNOW_SHOVEL("snowshovel", 32),
        SLIME("slime", 33),
        HEART("heart", 34),
        BARRIER("barrier", 35),
        ITEM_CRACK("iconcrack", 36),
        BLOCK_CRACK("blockcrack", 37),
        BLOCK_DUST("blockdust", 38),
        WATER_DROP("droplet", 39),
        ITEM_TAKE("take", 40),
        MOB_APPEARANCE("mobappearance", 41),
        DRAGON_BREATH("dragonbreath", 42),
        END_ROD("endRod", 43),
        DAMAGE_INDICATOR("damageIndicator", 44),
        SWEEP_ATTACK("sweepAttack", 45);
        private final String name;
        private final byte id;

        Particle(String name, int id) {
            this.name = name;
            this.id = (byte) id;
        }

        public byte getParticleId() {
            return id;
        }

        @Nullable
        public static Particle getParticleById(byte id) {
            for (Particle particle : values()) {
                if (particle.id == id) {
                    return particle;
                }
            }
            return null;
        }

        @Nullable
        public static Particle getParticleByName(String name) {
            for (Particle particle : values()) {
                if (particle.name.equals(name)) {
                    return particle;
                }
            }
            return null;
        }
    }
}
