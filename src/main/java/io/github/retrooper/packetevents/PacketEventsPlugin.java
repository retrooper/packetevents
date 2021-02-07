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

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
//TODO do what cleanups you did to Direction enum, to all others

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if (PacketEvents.get() == null) {
            PacketEvents.create(this);
            PacketEvents.get().load();
            PacketEvents.get().init(this);
            PacketListenerDynamic listener = new PacketListenerDynamic() {
                @Override
                public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                    if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
                        event.getPlayer().sendMessage("nice hit");
                    } else if (event.getPacketId() == PacketType.Play.Client.CHAT) {
                        event.getPlayer().sendMessage(PacketEvents.get().getPlayerUtils().getClientVersion(event.getPlayer()).toString());
                    }
                }
            };
            listener.filterAll();
            listener.addClientSidedPlayFilter(PacketType.Play.Client.USE_ENTITY);
            PacketEvents.get().getEventManager().registerListener(listener);
        }

    }

    @Override
    public void onDisable() {
        if (PacketEvents.get() != null) {
            PacketEvents.get().terminate();
        }
    }
}