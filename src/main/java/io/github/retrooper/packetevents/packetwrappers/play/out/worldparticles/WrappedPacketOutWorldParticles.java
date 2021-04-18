package io.github.retrooper.packetevents.packetwrappers.play.out.worldparticles;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

//TODO finish this wrapper and test
class WrappedPacketOutWorldParticles extends WrappedPacket implements SendableWrapper {
    private static Class<?> particleEnumClass;
    private static Method particleEnumGetNameMethod;

    private Particle particle;
    private boolean longDistance;
    private float x, y, z;
    private float offsetX, offsetY, offsetZ;
    private float particleData;
    private int particleCount;
    private int[] data;

    public WrappedPacketOutWorldParticles(NMSPacket packet) {
        super(packet);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_7_10})
    @Deprecated
    public WrappedPacketOutWorldParticles(Particle particle, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float particleData, int particleCount) {
        this.particle = particle;
        this.longDistance = false;//REDUNDANT, NOT USED ON 1.7.10
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.particleData = particleData;
        this.particleCount = particleCount;
        this.data = new int[particleCount];//REDUNDANT, NOT USED ON 1.7.10
    }

    public WrappedPacketOutWorldParticles(Particle particle, boolean longDistance, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float particleData, int particleCount, int[] data) {
        this.particle = particle;
        this.longDistance = longDistance;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.particleData = particleData;
        this.particleCount = particleCount;
        this.data = data;
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutWorldParticles a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutWorldParticles a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutWorldParticles a7;

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
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_13)) { //TODO civ
                Object particleParamObj = readObject(0, particleEnumClass);
                String particleName = null;
                try {
                    particleName = particleEnumGetNameMethod.invoke(particleParamObj).toString();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return Particle.getParticleByName(particleName);
            } else {
                Enum<?> enumConst = readEnumConstant(0, (Class<? extends Enum<?>>) particleEnumClass);
                return Particle.getParticleById(enumConst.ordinal());
            }
        } else {
            return particle;
        }
    }

    //TODO finish setter add 1.13+ support
    void setParticle(Particle particle) {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_13)) { //TODO civ

            } else {
                Enum<?> enumConst = EnumUtil.valueByIndex((Class<? extends Enum<?>>) particleEnumClass, particle.ordinal());
                writeEnumConstant(0, enumConst);
            }
        } else {
            this.particle = particle;
        }
    }

    public float getX() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return x;
        }
    }

    public void setX(float x) {
        if (packet != null) {
            writeFloat(0, x);
        } else {
            this.x = x;
        }
    }

    public float getY() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return y;
        }
    }

    public float getZ() {
        if (packet != null) {
            return readFloat(2);
        } else {
            return z;
        }
    }

    public float getOffsetX() {
        if (packet != null) {
            return readFloat(3);
        } else {
            return offsetX;
        }
    }

    public float getOffsetY() {
        if (packet != null) {
            return readFloat(4);
        } else {
            return offsetY;
        }
    }

    public float getOffsetZ() {
        if (packet != null) {
            return readFloat(5);
        } else {
            return offsetZ;
        }
    }

    public float getParticleData() {
        if (packet != null) {
            return readFloat(6);
        } else {
            return particleData;
        }
    }

    public int getParticleCount() {
        if (packet != null) {
            return readInt(0);
        } else {
            return particleCount;
        }
    }

    public Optional<int[]> getData() {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readIntArray(0));
        } else {
            return Optional.of(data);
        }
    }

    public void setData(int[] data) {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return;
        }
        if (packet != null) {
            writeIntArray(0, data);
        } else {
            this.data = data;
        }
    }

    public Optional<Boolean> isLongDistance() {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readBoolean(0));
        } else {
            return Optional.of(longDistance);
        }
    }

    public void setLongDistance(boolean longDistance) {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return;
        }
        if (packet != null) {
            writeBoolean(0, longDistance);
        } else {
            this.longDistance = longDistance;
        }
    }

    public enum Particle {
        EXPLOSION_NORMAL("explode"),
        EXPLOSION_LARGE("largeexplode"),
        EXPLOSION_HUGE("hugeexplosion"),
        FIREWORKS_SPARK("fireworksSpark"),
        WATER_BUBBLE("bubble"),
        WATER_SPLASH("splash"),
        WATER_WAKE("wake"),
        SUSPENDED("suspended"),
        SUSPENDED_DEPTH("depthsuspend"),
        CRIT("crit"),
        CRIT_MAGIC("magicCrit"),
        SMOKE_NORMAL("smoke"),
        SMOKE_LARGE("largesmoke"),
        SPELL("spell"),
        SPELL_INSTANT("instantSpell"),
        SPELL_MOB("mobSpell"),
        SPELL_MOB_AMBIENT("mobSpellAmbient"),
        SPELL_WITCH("witchMagic"),
        DRIP_WATER("dripWater"),
        DRIP_LAVA("dripLava"),
        VILLAGER_ANGRY("angryVillager"),
        VILLAGER_HAPPY("happyVillager"),
        TOWN_AURA("townaura"),
        NOTE("note"),
        PORTAL("portal"),
        ENCHANTMENT_TABLE("enchantmenttable"),
        FLAME("flame"),
        LAVA("lava"),
        FOOTSTEP("footstep"),
        CLOUD("cloud"),
        REDSTONE("reddust"),
        SNOWBALL("snowballpoof"),
        SNOW_SHOVEL("snowshovel"),
        SLIME("slime"),
        HEART("heart"),
        BARRIER("barrier"),
        ITEM_CRACK("iconcrack"),
        BLOCK_CRACK("blockcrack"),
        BLOCK_DUST("blockdust"),
        WATER_DROP("droplet"),
        ITEM_TAKE("take"),
        MOB_APPEARANCE("mobappearance"),
        DRAGON_BREATH("dragonbreath"),
        END_ROD("endRod"),
        DAMAGE_INDICATOR("damageIndicator"),
        SWEEP_ATTACK("sweepAttack");
        private final String name;

        Particle(String name) {
            this.name = name;
        }

        public static Particle getParticleById(int id) {
            return values()[id];
        }

        @Nullable
        public static Particle getParticleByName(String name) {
            if (name == null) {
                return null;
            }
            for (Particle particle : values()) {
                if (particle.name.equals(name) || particle.name().equals(name)) {
                    return particle;
                }
            }
            return null;
        }
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }
}
