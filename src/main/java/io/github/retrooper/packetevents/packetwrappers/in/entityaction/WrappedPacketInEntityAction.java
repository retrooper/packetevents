package io.github.retrooper.packetevents.packetwrappers.in.entityaction;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static Class<?> entityActionClass;
    @Nullable
    private static Class<?> enumPlayerActionClass;

    private static boolean isLowerThan_v_1_8;

    public static void load() {
        entityActionClass = PacketTypeClasses.Client.ENTITY_ACTION;
        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);
        if (version.isHigherThan(ServerVersion.v_1_7_10)) {
            enumPlayerActionClass = Reflection.getSubClass(entityActionClass, "EnumPlayerAction");
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
            final int jumpBoost;
            if (isLowerThan_v_1_8) {
                jumpBoost = Reflection.getField(entityActionClass, int.class, 2).getInt(packet);
            } else {
                jumpBoost = Reflection.getField(entityActionClass, int.class, 1).getInt(packet);
            }

            int animationIndex = -1;

            //1.7.10
            if (isLowerThan_v_1_8) {
                animationIndex = Reflection.getField(entityActionClass, int.class, 1).getInt(packet); // TODO: += if packetdataserializer.version < 16
            } else {
                final Object enumObj = Reflection.getField(entityActionClass, enumPlayerActionClass, 0).get(packet);
                final String enumValue = enumObj.toString();
                try {
                    this.action = PlayerAction.valueOf(enumValue);
                } catch (IllegalArgumentException e) {
                    //They aren't on a legacy version and the enum has changed
                    final PlayerAction.UpdatedPlayerAction updatedPlayerAction = PlayerAction.UpdatedPlayerAction.valueOf(enumValue);
                    this.action = PlayerAction.get(updatedPlayerAction.getIndex());
                }
            }


            this.entityId = entityId;
            this.jumpBoost = jumpBoost;
            if (animationIndex != -1) {
                this.action = PlayerAction.get(animationIndex);
            }
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
