package io.github.retrooper.packetevents.packetwrappers.play.out.jigsawgenerate;

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

    public int getLevels() {
        return readInt(0);
    }

    public boolean isKeepingJigsaws() {
        return readBoolean(0);
    }
}
