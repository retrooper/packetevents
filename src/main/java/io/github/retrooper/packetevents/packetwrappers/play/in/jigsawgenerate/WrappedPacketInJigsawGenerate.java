package io.github.retrooper.packetevents.packetwrappers.play.in.jigsawgenerate;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

/**
 * Sent when Generate is pressed on the Jigsaw Block interface.
 *
 * @author Tecnio
 * @see <a href="https://wiki.vg/Protocol#Generate_Structure"</a>
 */
public class WrappedPacketInJigsawGenerate extends WrappedPacket {

    public WrappedPacketInJigsawGenerate(final NMSPacket packet) {
        super(packet);
    }

    public Vector3i getBlockPosition() {
        return readBlockPosition(0);
    }

    public void setBlockPosition(final Vector3i blockPosition) {
        writeBlockPosition(0, blockPosition);
    }

    public int getLevels() {
        return readInt(0);
    }

    public void setLevels(final int levels) {
        writeInt(0, levels);
    }

    public boolean isKeepingJigsaws() {
        return readBoolean(0);
    }

    public void setKeepingJigsaws(final boolean keepingJigsaws) {
        writeBoolean(0, keepingJigsaws);
    }
}
