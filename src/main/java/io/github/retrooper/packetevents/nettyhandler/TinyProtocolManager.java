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

package io.github.retrooper.packetevents.nettyhandler;

import io.github.retrooper.packetevents.nettyhandler.tinyprotocol.TinyProtocol_7;
import io.github.retrooper.packetevents.nettyhandler.tinyprotocol.TinyProtocol_8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TinyProtocolManager {
    public static Object tinyProtocol;

    public static void init(Plugin plugin) {
        tinyProtocol = NettyPacketManager.v1_7_nettyMode ? new TinyProtocol_7(plugin) {
            @Override
            public Object onPacketOutAsync(Player receiver, Object channel, Object packet) {
                return NettyPacketManager.write(receiver, packet);
            }

            @Override
            public Object onPacketInAsync(Player sender, Object channel, Object packet) {
                return NettyPacketManager.read(sender, packet);
            }
        } : new TinyProtocol_8(plugin) {
            @Override
            public Object onPacketOutAsync(Player receiver, Object channel, Object packet) {
                return NettyPacketManager.write(receiver, packet);
            }

            public Object onPacketInAsync(Player sender, Object channel, Object packet) {
                return NettyPacketManager.read(sender, packet);
            }
        };
    }

    public static Object getChannel(Player player) {
        if(tinyProtocol != null) {
            if(tinyProtocol instanceof TinyProtocol_7) {
                TinyProtocol_7 t = (TinyProtocol_7)tinyProtocol;
                return t.getChannel(player);
            }
            else if(tinyProtocol instanceof TinyProtocol_8) {
                TinyProtocol_8 t= (TinyProtocol_8)tinyProtocol;
                return t.getChannel(player);
            }
        }
        else {
            return NMSUtils.getChannel(player);
        }
        return null;
    }

}
