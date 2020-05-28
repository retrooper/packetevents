package me.purplex.packetevents.packetwrappers.in.use_entity;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.Hand;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.utils.NMSUtils;
import me.purplex.packetevents.utils.math.Vector3d;
import org.bukkit.entity.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
public class WrappedPacketInUseEntity extends WrappedPacket {
    private int entityId;
    private Entity entity;
    private EntityUseAction action;
    private Hand hand;

    public WrappedPacketInUseEntity(Object packet) {
        super(packet);
    }


    @Override
    protected void setup() throws IllegalAccessException, InvocationTargetException {
        entityId = fields[0].getInt(packet);

       // Object nmsEntity = NMSUtils.getEntityById(entityId);

        //this.entity = (Entity) getBukkitEntityFromNMS.invoke(nmsEntity);
        this.entity = (Entity) NMSUtils.getEntityById(entityId);

        Object useActionEnum = fields[1].get(packet);
        action = EntityUseAction.valueOf(useActionEnum.toString());

        Object handObj;
        if (fields[2] == null) {
            handObj = Hand.MAIN_HAND.name();
        } else {
            handObj = fields[2].get(packet);
        }

        hand = Hand.valueOf(handObj.toString());
    }

    /**
     * Getting entity will be optimized
     * @return
     */
    @Deprecated
    public Entity getEntity() {
        return entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public EntityUseAction getAction() {
        return action;
    }


    public Hand getHand() {
        return hand;
    }


    private static Class<?> useEntityClass;

    private static Class<?> entityClass;

    private static Field[] fields = new Field[3];

    static {
        //CLASSES
        try {
            useEntityClass = NMSUtils.getNMSClass("PacketPlayInUseEntity");
            entityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //FIELDS
        try {
            fields[0] = useEntityClass.getDeclaredField("a");
            fields[1] = useEntityClass.getDeclaredField("action");
            if (version.isHigherThan(ServerVersion.v_1_8_8)) {
                fields[2] = useEntityClass.getDeclaredField("d");

            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for (Field f : fields) {
            if (f != null) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
            }
        }

    }
}
