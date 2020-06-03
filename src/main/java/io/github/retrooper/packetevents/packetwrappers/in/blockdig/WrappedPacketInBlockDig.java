package io.github.retrooper.packetevents.packetwrappers.in.blockdig;

import io.github.retrooper.packetevents.enums.PlayerDigType;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.BaseBlockUtils;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.enums.Direction;

import java.lang.reflect.Field;

public class WrappedPacketInBlockDig extends WrappedPacket {
    private Vector3i blockPosition;
    private Direction direction;
    private PlayerDigType digType;

    public WrappedPacketInBlockDig(Object packet) {
        super(packet);
    }


    @Override
    protected void setup() throws Exception {
        final Direction enumDirection;
        final PlayerDigType enumDigType;
        //1.7.10
        if (version == ServerVersion.v_1_7_10) {
            final int x = fields[0].getInt(packet);
            final int y = fields[1].getInt(packet);
            final int z = fields[2].getInt(packet);

            this.blockPosition = new Vector3i(x, y, z);

            enumDirection = Direction.get((byte) face.getInt(packet));
            enumDigType = PlayerDigType.get((byte)e.getInt(packet));
        } else {
            //1.8+
            final Object blockPosObj = fields[0].get(packet);
            final Object enumDirectionObj = fields[1].get(packet);
            final Object digTypeObj = fields[2].get(packet);

            final int x = blockPosXYZ[0].getInt(blockPosObj);
            final int y = blockPosXYZ[1].getInt(blockPosObj);
            final int z = blockPosXYZ[2].getInt(blockPosObj);

            this.blockPosition = new Vector3i(x, y, z);

            enumDirection = Direction.valueOf(((Enum)enumDirectionObj).name());
            enumDigType =  PlayerDigType.valueOf(((Enum)digTypeObj).name());
        }

        this.direction =  enumDirection;
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

    //ALL
    private static Class<?> blockDigClass;


    //1.8
    private static Field[] fields = new Field[3];

    private static Class<?> blockPositionClass;

    private static Field[] blockPosXYZ = new Field[3];

    //1.7
    private static Field face;

    private static Field e;

    static {

        try {
            blockDigClass = NMSUtils.getNMSClass("PacketPlayInBlockDig");
            if (version != ServerVersion.v_1_7_10) {
                blockPositionClass = NMSUtils.getNMSClass("BlockPosition");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            //blockpos
            fields[0] = blockDigClass.getDeclaredField("a");
            //direction
            fields[1] = blockDigClass.getDeclaredField("b");
            //digtype
            fields[2] = blockDigClass.getDeclaredField("c");

            if (version != ServerVersion.v_1_7_10) {
                blockPosXYZ[0] = blockPositionClass.getSuperclass().getDeclaredField(BaseBlockUtils.getPosXFieldName());
                blockPosXYZ[1] = blockPositionClass.getSuperclass().getDeclaredField(BaseBlockUtils.getPosXFieldName());
                blockPosXYZ[2] = blockPositionClass.getSuperclass().getDeclaredField(BaseBlockUtils.getPosZFieldName());
            } else {
                face = blockDigClass.getDeclaredField("face");
                e = blockDigClass.getDeclaredField("e");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        for(Field f : fields) {
            if(f != null) {
                f.setAccessible(true);
            }
        }

        for(Field f : blockPosXYZ) {
            if(f != null) {
                f.setAccessible(true);
            }
        }

        if(e != null) {
            e.setAccessible(true);
        }

        if(face != null) {
            face.setAccessible(true);
        }
    }


}
