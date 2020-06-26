package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    static {
        if (version.isHigherThan(ServerVersion.v_1_7_10)) {
            WrappedPacketInBlockPlace_1_8.initStatic();
        }
    }

    private Vector3i blockPosition;
    private ItemStack itemStack;

    public WrappedPacketInBlockPlace(final Player player, final Object packet) {
        super(player, packet);
    }

    @Override
    protected void setup() {
        //1.7.10
        Vector3i position = null;
        ItemStack itemStack = null;
        try {
            if (version.isHigherThan(ServerVersion.v_1_8_8)) {
                final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                final Block block = blockPlace_1_9.getBlock();
                position = new Vector3i(block.getX(), block.getY(), block.getZ());
                itemStack = new ItemStack(block.getType());
            } else if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                position = blockPlace_1_8.getBlockPosition();
                itemStack = blockPlace_1_8.getItemStack();
            } else {
                final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(getPlayer(), packet);
                position = blockPlace_1_7_10.getBlockPosition();
                this.itemStack = blockPlace_1_7_10.getItemStack();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.blockPosition = position;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
