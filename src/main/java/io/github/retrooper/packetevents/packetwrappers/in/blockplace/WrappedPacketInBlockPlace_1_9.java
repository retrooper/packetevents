package io.github.retrooper.packetevents.packetwrappers.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.BlockIteratorUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

final class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private Block block;

    public WrappedPacketInBlockPlace_1_9(final Player player, final Object packet) {
        super(player, packet);
    }


    @Override
    protected void setup() {
        this.block = BlockIteratorUtils.getBlockLookingAt(getPlayer(), 3);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlock() {
        return block;
    }


}
