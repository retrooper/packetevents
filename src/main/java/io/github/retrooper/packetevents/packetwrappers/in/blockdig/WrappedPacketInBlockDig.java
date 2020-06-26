package io.github.retrooper.packetevents.packetwrappers.in.blockdig;

import io.github.retrooper.packetevents.enums.Direction;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.block.Block;

import java.lang.reflect.Field;

public final class WrappedPacketInBlockDig extends WrappedPacket {
    private static Class<?> blockDigClass, blockPositionClass, enumDirectionClass, digTypeClass;

    static {

        try {
            blockDigClass = NMSUtils.getNMSClass("PacketPlayInBlockDig");
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                blockPositionClass = NMSUtils.getNMSClass("BlockPosition");
                enumDirectionClass = NMSUtils.getNMSClass("EnumDirection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            digTypeClass = NMSUtils.getNMSClass("EnumPlayerDigType");
        }
        catch(ClassNotFoundException e) {
            //It is probably a subclass
            digTypeClass = Reflection.getSubClass(blockDigClass, "EnumPlayerDigType");
        }
    }

    private Vector3i blockPosition;
    private Direction direction;
    private PlayerDigType digType;

    public WrappedPacketInBlockDig(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        Direction enumDirection = null;
        PlayerDigType enumDigType = null;
        //1.7.10
        try {
            if (version.isLowerThan(ServerVersion.v_1_8)) {
                setupCoordinates(blockDigClass, packet);
                enumDirection = Direction.get(Reflection.getField(digTypeClass, int.class, 3).getInt(packet));
                enumDigType = PlayerDigType.get(Reflection.getField(digTypeClass, int.class, 4).getInt(packet));
            } else {
                //1.8+
                final Object blockPosObj = Reflection.getField(blockDigClass, blockPositionClass, 0).get(packet);
                final Object enumDirectionObj = Reflection.getField(blockDigClass, enumDirectionClass, 0).get(packet);
                final Object digTypeObj = Reflection.getField(blockDigClass, digTypeClass, 0).get(packet);

                Class<?> blockPosSuper = blockPositionClass;
                setupCoordinates(blockPosSuper, blockPosObj);

                enumDirection = Direction.valueOf(((Enum) enumDirectionObj).name());
                enumDigType = PlayerDigType.valueOf(((Enum) digTypeObj).name());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.direction = enumDirection;
        this.digType = enumDigType;
    }

    private void setupCoordinates(final Class<?> cls, final Object object) throws IllegalAccessException {
        final int x = Reflection.getField(cls, int.class, 0).getInt(object);
        final int y = Reflection.getField(cls, int.class, 1).getInt(object);
        final int z = Reflection.getField(cls, int.class, 2).getInt(object);

        this.blockPosition = new Vector3i(x, y, z);
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public Direction getDirection() {
        return direction;
    }

    public PlayerDigType getDigType() {
        return digType;
    }


}
