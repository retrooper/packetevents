package io.github.retrooper.packetevents.packetwrappers.in.useentity;

import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> useEntityClass, entityClass, enumEntityUseActionClass;

    static {

        try {
            useEntityClass = NMSUtils.getNMSClass("PacketPlayInUseEntity");
            entityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            enumEntityUseActionClass = NMSUtils.getNMSClass("EnumEntityUseAction");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            for (final Class<?> sub : useEntityClass.getDeclaredClasses()) {
                if (sub.getSimpleName().equals("EnumEntityUseAction")) {
                    enumEntityUseActionClass = sub;
                    break;
                }
            }
        }
    }

    private int entityId;
    private Entity entity;
    private EntityUseAction action;

    public WrappedPacketInUseEntity(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            this.entityId = Reflection.getField(useEntityClass, int.class, 0).getInt(packet);
            this.entity = NMSUtils.getEntityById(this.entityId);
            final Object useActionEnum = Reflection.getField(useEntityClass, enumEntityUseActionClass, 0).get(packet);
            this.action = EntityUseAction.valueOf(useActionEnum.toString());
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

    public EntityUseAction getAction() {
        return action;
    }
}
