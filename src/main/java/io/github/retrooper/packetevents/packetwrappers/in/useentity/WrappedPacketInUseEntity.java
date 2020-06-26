package io.github.retrooper.packetevents.packetwrappers.in.useentity;

import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public final class WrappedPacketInUseEntity extends WrappedPacket {
    private static Class<?> useEntityClass, entityClass;
    private static final Field[] fields = new Field[2];

    static {

        try {
            useEntityClass = NMSUtils.getNMSClass("PacketPlayInUseEntity");
            entityClass = NMSUtils.getNMSClass("Entity");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            fields[0] = useEntityClass.getDeclaredField("a");
            fields[1] = useEntityClass.getDeclaredField("action");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for (Field f : fields) {
            if (f != null) {
                f.setAccessible(true);
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
            this.entityId = fields[0].getInt(packet);
            this.entity = NMSUtils.getEntityById(this.entityId);
            final Object useActionEnum = fields[1].get(packet);
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
