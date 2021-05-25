package io.github.retrooper.packetevents.packetwrappers.play.out.bedit;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

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

    public void setItemStack(final ItemStack itemStack) {
        writeItemStack(0, itemStack);
    }

    public boolean isSigning() {
        return readBoolean(0);
    }

    public void setSigning(final boolean signing) {
        writeBoolean(0, signing);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_13, ServerVersion.v_1_16_5})
    public Optional<Hand> getHand() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
            final Enum<?> enumConst = readEnumConstant(0, NMSUtils.enumHandClass);
            return Optional.of(Hand.valueOf(enumConst.name()));
        }

        return Optional.empty();
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_13, ServerVersion.v_1_16_5})
    public void setHand(final Hand hand) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
            final Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumHandClass, hand.name());
            writeEnumConstant(0, enumConst);
        }
    }
}
