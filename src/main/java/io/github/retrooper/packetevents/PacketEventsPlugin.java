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
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    //TODO document some internal utils in the utils package, the packet type in the package type package and all packet wrappers....
    @Override
    public void onLoad() {
        /*
         * We use default settings. You should always specify the settings before loading.
         * PacketEvents won't load again if it has already loaded or is already loading.
         * If that is the case, the method will return false.
         */
        boolean successful = PacketEvents.create(this).load();
    }

    @Override
    public void onEnable() {
        /*
         * We created the instance in the onLoad method.
         * If another PacketEvents user has already initialized, we won't initialize again.
         * If we end up not initializing or fail to initialize, the method will return false.
         */
        boolean successful = PacketEvents.get().init(this);

        PacketEvents.get().registerListener(new PacketListenerDynamic() {
            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                if(event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
                    WrappedPacketInUseEntity ue = new WrappedPacketInUseEntity(event.getNMSPacket());
                    Entity entity = ue.getEntity();
                    event.getPlayer().sendMessage("you attacked " + entity.getName());
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.get().stop();
    }
}