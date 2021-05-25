package io.github.retrooper.packetevents.packetwrappers.play.out.bedit;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

/**
 * @author Tecnio
 * @see <a href="https://wiki.vg/Protocol#Edit_Book"</a>
 */
public class WrappedPacketInBEdit extends WrappedPacket {

    public WrappedPacketInBEdit(final NMSPacket packet) {
        super(packet);
    }

    public ItemStack getItemStack() {
        return readItemStack(0);
    }

    public boolean isSigning() {
        return readBoolean(0);
    }

    @Deprecated
    @SupportedVersions(ranges = {ServerVersion.v_1_13, ServerVersion.v_1_16_5})
    public Hand getHand() {
        final Object hand = readObject(0, NMSUtils.enumHandClass);

        try {
            final String handName = ((String) hand.getClass().getMethod("name").invoke(hand)).toLowerCase();

            if (handName.contains("off")) {
                return Hand.OFF_HAND;
            } else {
                return Hand.MAIN_HAND;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
