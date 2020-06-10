package io.github.retrooper.packetevents.packetwrappers.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class WrappedPacketInBlockPlace_1_7_10 extends WrappedPacket {
    private Vector3i blockPosition;
    private ItemStack itemStack;
    private int blockFace;
    WrappedPacketInBlockPlace_1_7_10(final Player player, final Object packet) {
        super(player, packet);
    }


    @Override
    protected void setup() {
        final PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace)packet;

        this.blockPosition = new Vector3i(blockPlace.c(), blockPlace.d(), blockPlace.e());

        this.blockFace = blockPlace.d();

        net.minecraft.server.v1_7_R4.ItemStack stack = blockPlace.getItemStack();
       this.itemStack = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asBukkitCopy(stack);
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getBlockFace() {
        return blockFace;
    }
}
