package me.purplex.packetevents.packetwrappers.in.useentity;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.Hand;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUseEntity extends WrappedPacket {
    private int entityId;
    private Entity entity;
    private EntityUseAction action;
    private Hand hand;

    public WrappedPacketInUseEntity(final Player player, final Object packet) {
        super(player, packet);
    }


    @Override
    protected void setup() throws IllegalAccessException, InvocationTargetException {
        this.entityId = fields[0].getInt(packet);

        this.entity = NMSUtils.getNearByEntityById(getPlayer().getWorld(), entityId);


        final Object useActionEnum = fields[1].get(packet);
        this.action = EntityUseAction.valueOf(useActionEnum.toString());

        final Object handObj;
        if (fields[2] == null) {
            handObj = Hand.MAIN_HAND.name();
        } else {
            handObj = fields[2].get(packet);
        }

        this.hand = Hand.valueOf(handObj.toString());
    }


    @Nullable
    /**
     * It is possible for the entity to be null, if the player attacks from way too far,
     * if it is null, please find the entity yourself with the entity ID
     */
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

        try {
            useEntityClass = NMSUtils.getNMSClass("PacketPlayInUseEntity");
            entityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


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
                f.setAccessible(true);
            }
        }

    }
}
