package io.github.retrooper.packetevents.packetwrappers.play.out.blockaction;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Material;

/**
 * This packet is used for a number of actions and animations performed by blocks, usually non-persistent.
 *
 * @author Tecnio
 * @see <a href="https://wiki.vg/Protocol#Block_Action"</a>
 */
public class WrappedPacketOutBlockAction extends WrappedPacket implements SendableWrapper {

    public Vector3i getBlockPosition() {
        final Vector3i blockPos = new Vector3i();

        final Object blockPosObj = readObject(1, NMSUtils.blockPosClass);

        try {
            blockPos.x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
            blockPos.y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
            blockPos.z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return blockPos;
    }

    public int getActionId() {
        return readInt(0);
    }

    public int getActionParam() {
        return readInt(1);
    }

    public Material getBlockMaterial() {
        return NMSUtils.getMaterialFromNMSBlock(readObject(0, NMSUtils.blockClass));
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return null;
    }
}
