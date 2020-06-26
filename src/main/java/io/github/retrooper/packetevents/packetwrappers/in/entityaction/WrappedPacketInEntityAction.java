package io.github.retrooper.packetevents.packetwrappers.in.entityaction;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static Class<?> entityActionClass;
    @Nullable
    private static Class<?> enumPlayerActionClass;
    private static final Field[] fields = new Field[3];
    @Nullable
    private static Object[] enumPlayerActionConstants;

    static {
        try {
            entityActionClass = NMSUtils.getNMSClass("PacketPlayInEntityAction");
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                enumPlayerActionClass = NMSUtils.getNMSClass("PacketPlayInEntityAction$EnumPlayerAction");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            //ints in 1.7.10, EnumPlayerAction in 1.8+
            fields[0] = entityActionClass.getDeclaredField("a");
            fields[1] = entityActionClass.getDeclaredField("animation");
            fields[2] = entityActionClass.getDeclaredField("c");
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                enumPlayerActionConstants = enumPlayerActionClass.getEnumConstants();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for (final Field f : fields) {
            f.setAccessible(true);
        }
    }

    private Entity entity;
    private int entityId;
    private PlayerAction action;
    private int jumpBoost;

    public WrappedPacketInEntityAction(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            final int entityId = fields[0].getInt(packet);
            final int jumpBoost = fields[2].getInt(packet);

            int animationIndex = -1;

            //1.7.10
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                animationIndex = fields[1].getInt(packet);
            } else {
                final Object enumObj = fields[1].get(packet);

                final Object[] enumValues = enumPlayerActionConstants;
                final int len = enumValues.length;
                for (int i = 0; i < len; i++) {
                    final Object val = enumValues[i];
                    if (val.toString().equals(enumObj.toString())) {
                        animationIndex = i;
                        break;
                    }
                }
            }


            this.entityId = entityId;
            this.action = action;
            this.jumpBoost = jumpBoost;
            this.action = PlayerAction.get((byte) animationIndex);

            this.entity = NMSUtils.getEntityById(this.entityId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public PlayerAction getAction() {
        return action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }
}
