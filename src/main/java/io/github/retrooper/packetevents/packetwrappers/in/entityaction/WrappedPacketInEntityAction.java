package io.github.retrooper.packetevents.packetwrappers.in.entityaction;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static Class<?> entityActionClass;
    @Nullable
    private static Class<?> enumPlayerActionClass;
    @Nullable
    private static Object[] enumPlayerActionConstants;

    static {
        try {
            entityActionClass = NMSUtils.getNMSClass("PacketPlayInEntityAction");
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                for (final Class<?> sub : entityActionClass.getDeclaredClasses()) {
                    if (sub.getSimpleName().equals("EnumPlayerAction")) {
                        enumPlayerActionClass = sub;
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            final int entityId = Reflection.getField(entityActionClass, int.class, 0).getInt(packet);
            final int jumpBoost = Reflection.getField(entityActionClass, int.class, 2).getInt(packet);

            int animationIndex = -1;

            //1.7.10
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                animationIndex = Reflection.getField(entityActionClass, int.class, 1).getInt(packet);
            } else {
                final Object enumObj = Reflection.getField(entityActionClass, enumPlayerActionClass, 0);

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
            this.jumpBoost = jumpBoost;
            this.action = PlayerAction.get(animationIndex);

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
