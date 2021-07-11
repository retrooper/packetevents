package io.github.retrooper.packetevents.packetwrappers.play.in.useitem;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUseItem extends WrappedPacket {

    private static boolean v_1_14;

    public WrappedPacketInUseItem(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_14 = version.isNewerThanOrEquals(ServerVersion.v_1_14);
    }

    public Hand getHand() {
        return Hand.values()[readEnumConstant(0, NMSUtils.enumHandClass).ordinal()];
    }

    public Vector3i getBlockPosition() {
        try {
            if (v_1_14) {
                Object movingBlockPosition = readObject(0, NMSUtils.movingObjectPositionBlock);
                Field position = Reflection.getField(movingBlockPosition.getClass(), NMSUtils.blockPosClass, 0);
                return readBlockPosition(position.get(movingBlockPosition));
            }
        } catch (IllegalAccessException | NullPointerException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return readBlockPosition(0);
    }

    private Vector3i readBlockPosition(Object blockPosObj) throws InvocationTargetException, IllegalAccessException {
        int x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
        int y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
        int z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
        return new Vector3i(x, y, z);
    }
}
