package io.github.retrooper.packetevents.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class BlockIteratorUtils {
    public static Block getBlockLookingAt(final Player player, final int distance) {
        final Location location = player.getEyeLocation();
        final BlockIterator blocksToAdd = new BlockIterator(location, 0.0D, distance);
        Block block = null;
        while(blocksToAdd.hasNext()) {
            block = blocksToAdd.next();
        }
        return block;
    }
}
