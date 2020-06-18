package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    static {
        if (version.isHigherThan(ServerVersion.v_1_8_8)) {
            WrappedPacketInBlockPlace_1_9.initStatic();
        } else if (version.isLowerThan(ServerVersion.v_1_9) && version.isHigherThan(ServerVersion.v_1_7_10)) {
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
                final WrappedPacketInBlockPlace_1_9 updatedBlockPlace = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
                final Block block = updatedBlockPlace.getBlock();
                position = new Vector3i(block.getX(), block.getY(), block.getZ());
                itemStack = new ItemStack(block.getType());
            } else {
                final WrappedPacketInBlockPlace_1_8 legacyBlockPlace = new WrappedPacketInBlockPlace_1_8(packet);
                position = legacyBlockPlace.getBlockPosition();
                itemStack = legacyBlockPlace.getItemStack();
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
