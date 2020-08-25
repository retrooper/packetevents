package io.github.retrooper.packetevents.packetwrappers.in.blockdig;

import io.github.retrooper.packetevents.enums.Direction;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

public final class WrappedPacketInBlockDig extends WrappedPacket {
    private static Class<?> blockDigClass, blockPositionClass, enumDirectionClass, digTypeClass;
    private static boolean isVersionLowerThan_v_1_8;
    public static void load() {
        blockDigClass = PacketTypeClasses.Client.BLOCK_DIG;
        try {
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                blockPositionClass = NMSUtils.getNMSClass("BlockPosition");
                enumDirectionClass = NMSUtils.getNMSClass("EnumDirection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isVersionLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);

        if (version != ServerVersion.v_1_7_10) {
            try {
                digTypeClass = NMSUtils.getNMSClass("EnumPlayerDigType");
            } catch (ClassNotFoundException e) {
                //It is probably a subclass
                digTypeClass = Reflection.getSubClass(blockDigClass, "EnumPlayerDigType");
            }
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
        int x = 0, y = 0, z = 0;
        //1.7.10
        try {
            if (isVersionLowerThan_v_1_8) {
                enumDigType = PlayerDigType.get(Reflection.getField(blockDigClass, int.class, 4).getInt(packet));
                x = Reflection.getField(blockDigClass, int.class, 0).getInt(packet);
                y = Reflection.getField(blockDigClass, int.class, 1).getInt(packet);
                z = Reflection.getField(blockDigClass, int.class, 2).getInt(packet);
                enumDirection = null;
            } else {
                //1.8+
                final Object blockPosObj = Reflection.getField(blockDigClass, blockPositionClass, 0).get(packet);
                final Object enumDirectionObj = Reflection.getField(blockDigClass, enumDirectionClass, 0).get(packet);
                final Object digTypeObj = Reflection.getField(blockDigClass, digTypeClass, 0).get(packet);

                Class<?> blockPosSuper = blockPositionClass;
                x = Reflection.getField(blockPosSuper, int.class, 0).getInt(blockPosObj);
                y = Reflection.getField(blockPosSuper, int.class, 1).getInt(blockPosObj);
                z = Reflection.getField(blockPosSuper, int.class, 2).getInt(blockPosObj);

                enumDirection = Direction.valueOf(((Enum) enumDirectionObj).name());
                enumDigType = PlayerDigType.valueOf(((Enum) digTypeObj).name());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.blockPosition = new Vector3i(x, y, z);
        if (enumDirection == null) {
            this.direction = Direction.NULL;
        } else {
            this.direction = enumDirection;
        }
        this.digType = enumDigType;
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
