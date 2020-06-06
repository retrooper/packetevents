package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;


public class PacketSendEvent extends PacketEvent {
    private final Player player;
    private final String name;
    private final Object packet;
    private boolean cancelled;

    public PacketSendEvent(final Player player, final String packetName, final Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.cancelled = false;
    }


    public Player getPlayer() {
        return this.player;
    }

    public String getPacketName() {
        return this.name;
    }

    /**
     * Get the raw packet object
     *
     * @return nmsPacket
     */
    public Object getPacket() {
        return this.packet;
    }


    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }


/*
    /**
     * Get the player's ping in the EntityPlayer's class
     * @return ping
     * @throws VersionNotFoundException

    public int getPing() throws VersionNotFoundException {
        int ping;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else {
            throw new VersionNotFoundException();
        }
        return ping;
    }


    /**
     * Get the motX variable in the EntityPlayer class
     *
     * @return motX

    public double getMotX() throws VersionNotFoundException {
        double motX;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.getMot().getX();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.getMot().getX();
        } else {
            throw new VersionNotFoundException();
        }
        return motX;
    }



    /**
     * Get the motY variable in the EntityPlayer class
     *
     * @return motY

    public double getMotY() throws VersionNotFoundException {
        double motY;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.getMot().getY();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.getMot().getY();
        } else {
            throw new VersionNotFoundException();
        }
        return motY;
    }

    /**
     * Get the motZ variable in the EntityPlayer class
     *
     * @return motZ

    public double getMotZ() throws VersionNotFoundException {
        double motZ;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.getMot().getZ();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.getMot().getZ();
        } else {
            throw new VersionNotFoundException();
        }
        return motZ;
    }
    */


}

