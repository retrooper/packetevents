package io.github.explored.packetevents.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class BlockFinder {
    public static Block getBlocksInDirection(final Player player, final int blockDistance) {
        final BlockIterator it = new BlockIterator(player, blockDistance);
        Block cur = null;
        while(it.hasNext()) {
            cur = it.next();
        }
        return cur;
    }
}
