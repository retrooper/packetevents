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

package io.github.retrooper.packetevents.packetmanager.tinyprotocol;

import io.github.retrooper.packetevents.packetmanager.netty.NettyPacketManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TinyProtocol {
    private final Object tinyProt;

    public TinyProtocol(Plugin plugin) {
        if (NettyPacketManager.v1_7_nettyMode) {
            tinyProt = new TinyProtocol7(plugin);
        } else {
            tinyProt = new TinyProtocol8(plugin);
        }
    }

    public void injectPlayer(Player player) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.injectPlayer(player);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.injectPlayer(player);
        }
    }

    public void injectPlayerAsync(Player player) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.injectPlayerAsync(player);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.injectPlayerAsync(player);
        }
    }

    public void ejectPlayer(Player player) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.uninjectPlayer(player);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.uninjectPlayer(player);
        }
    }

    public void ejectPlayerAsync(Player player) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.uninjectPlayerAsync(player);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.uninjectPlayerAsync(player);
        }
    }

    public void ejectChannelSync(Object channel) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.ejectChannelSync(channel);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.ejectChannelSync(channel);
        }
    }

    public void ejectChannelAsync(Object channel) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.ejectChannelAsync(channel);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.ejectChannelAsync(channel);
        }
    }

    public void sendPacket(Object channel, Object packet) {
        if (!NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.sendPacket(channel, packet);
        } else {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.sendPacket(channel, packet);
        }
    }

    public Object getChannel(Player player) {
        if (NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            return tp7.getChannel(player);
        } else {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            return tp8.getChannel(player);
        }
    }

    public void unregisterChannelHandler() {

    }

    public void handleQueuedKicks() {
        if (NettyPacketManager.v1_7_nettyMode) {
            TinyProtocol7 tp7 = (TinyProtocol7) tinyProt;
            tp7.handleQueuedKicks();
        } else {
            TinyProtocol8 tp8 = (TinyProtocol8) tinyProt;
            tp8.handleQueuedKicks();
        }
    }
}
