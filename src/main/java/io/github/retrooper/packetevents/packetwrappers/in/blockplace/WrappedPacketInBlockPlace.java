package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.enums.Hand;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class WrappedPacketInBlockPlace extends WrappedPacket {
    private Vector3i blockPosition;
    private Hand hand;
    private ItemStack itemStack;

    public WrappedPacketInBlockPlace(final Player player, final Object packet) {
        super(player, packet);
    }

    @Override
    protected void setup() {
        //1.7.10
        Vector3i position = null;
        ItemStack itemStack = null;
        Hand hand = null;
        try {
        if (version.isHigherThan(ServerVersion.v_1_8_8)) {
            final WrappedPacketInBlockPlace_1_9 updatedBlockPlace = new WrappedPacketInBlockPlace_1_9(getPlayer(), packet);
            final Block block = updatedBlockPlace.getBlock();
            position = new Vector3i(block.getX(), block.getY(), block.getZ());
            hand = updatedBlockPlace.getHand();
            itemStack = new ItemStack(block.getType());
            //  init direction
        } else {
            final WrappedPacketInBlockPlace_Legacy legacyBlockPlace = new WrappedPacketInBlockPlace_Legacy(packet);
            position = legacyBlockPlace.getBlockPosition();
            hand = legacyBlockPlace.getHand();
            itemStack = legacyBlockPlace.getItemStack();
            //init direction
        } }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.blockPosition = position;
        this.itemStack = itemStack;
        this.hand = hand;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public Hand getHand() {
        return hand;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    static {
        if (version.isHigherThan(ServerVersion.v_1_8_8)) {
            WrappedPacketInBlockPlace_1_9.setupStaticVars();
        } else {
            WrappedPacketInBlockPlace_Legacy.setupStaticVars();
        }
    }

}
