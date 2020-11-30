/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetmanager.netty;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyPacketManager {
    public static final boolean v1_7_nettyMode;

    static {
        boolean v1_7_nettyMode1;
        try {
            Class.forName("net.minecraft.util.io.netty.channel.Channel");
            v1_7_nettyMode1 = true;
        } catch (ClassNotFoundException e) {
            v1_7_nettyMode1 = false;
        }
        v1_7_nettyMode = v1_7_nettyMode1;
    }

    private final Object npm;

    public NettyPacketManager(Plugin plugin) {
        if (v1_7_nettyMode) {
            npm = new NettyPacketManager_7();
        } else {
            npm = new NettyPacketManager_8();
        }
    }

    /**
     * Synchronously inject a player
     *
     * @param player Target player to inject
     */
    public void injectPlayer(final Player player) {
        if (v1_7_nettyMode) {
            NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
            npm_7.injectPlayer(player);
        } else {
            NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
            npm_8.injectPlayer(player);
        }
    }

    /**
     * Asynchronously inject a player
     *
     * @param player
     */
    public void injectPlayerAsync(final Player player) {
        //Redundant channel variable created just so we can cache it synchronously.
        Object channel = NMSUtils.getChannel(player);
        PacketEvents.get().packetHandlingExecutorService.execute(() -> {
            if (v1_7_nettyMode) {
                NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
                npm_7.injectPlayer(player);
            } else {
                NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
                npm_8.injectPlayer(player);
            }
        });
    }

    /**
     * Synchronously eject a player.
     *
     * @param player
     */
    public void ejectPlayer(final Player player) {
        if (v1_7_nettyMode) {
            NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
            npm_7.ejectPlayer(player);
        } else {
            NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
            npm_8.ejectPlayer(player);
        }
    }

    /**
     * Asynchronously eject a player
     *
     * @param player
     */
    public void ejectPlayerAsync(final Player player) {
        PacketEvents.get().packetHandlingExecutorService.execute(() -> {
            if (v1_7_nettyMode) {
                NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
                npm_7.ejectPlayer(player);
            } else {
                NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
                npm_8.ejectPlayer(player);
            }
        });
    }

    public void ejectChannelSync(Object channel) {
        if (v1_7_nettyMode) {
            NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
            npm_7.ejectChannel(channel);
        } else {
            NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
            npm_8.ejectChannel(channel);
        }
    }

    public void ejectChannelAsync(Object channel) {
        PacketEvents.get().packetHandlingExecutorService.execute(() -> {
            if (v1_7_nettyMode) {
                NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
                npm_7.ejectChannel(channel);
            } else {
                NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
                npm_8.ejectChannel(channel);
            }
        });
    }

    public void sendPacket(Object channel, Object packet) {
        if (v1_7_nettyMode) {
            NettyPacketManager_7 npm_7 = (NettyPacketManager_7) npm;
            npm_7.sendPacket(channel, packet);
        } else {
            NettyPacketManager_8 npm_8 = (NettyPacketManager_8) npm;
            npm_8.sendPacket(channel, packet);
        }
    }
}