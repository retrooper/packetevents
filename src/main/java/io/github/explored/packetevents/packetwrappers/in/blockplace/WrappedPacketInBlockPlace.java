package io.github.explored.packetevents.packetwrappers.in.blockplace;

import io.github.explored.packetevents.enums.*;
import io.github.explored.packetevents.packetwrappers.api.WrappedPacket;
import io.github.explored.packetevents.utils.vector.Vector3i;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class WrappedPacketInBlockPlace extends WrappedPacket {
    private Vector3i blockPosition;
    private Hand hand;
    private Direction direction;
    private ItemStack itemStack;

    public WrappedPacketInBlockPlace(final Player player, final Object packet) {
        super(player, packet);
    }

    @Override
    protected void setup() throws Exception {
        //1.7.10
        final Direction direction;
        final Vector3i position;
        final ItemStack itemStack;
        final Hand hand;
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
        }
        this.direction = Direction.NULL;
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

    @Deprecated
    public Direction getDirection() {
        return direction;
    }

    static {
        if (version.isHigherThan(ServerVersion.v_1_8_8)) {
            WrappedPacketInBlockPlace_1_9.setupStaticVars();
        } else {
            WrappedPacketInBlockPlace_Legacy.setupStaticVars();
        }
    }

}
