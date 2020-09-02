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

package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.event.CancellableEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    //TODO
    //registering a packet listener now gets declared methods
    //packetevents has been fully documented
    //PacketEvents.getAPI().getPlayerUtils().ejectPlayer(Player) RENAME, DEPRECATION OF OLD METHOD
    //
    @Override
    public void onEnable() {
        //Deprecated, as it is no longer needed
        //PacketEvents.getSettings().setIdentifier("packetevents_api");

        PacketEvents.init(this);
        PacketEvents.getAPI().getEventManager().registerListener(this);

        System.out.println("Server Version: " + PacketEvents.getAPI().getServerUtils().getVersion());
        System.out.println("Server Platform: " + PacketEvents.getAPI().getServerUtils().getOperatingSystem());
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }
    @PacketHandler
    public void onInject(PlayerInjectEvent e) {
        if(e.isAsync()) {
            System.out.println("ASYNC injection");
        }
        else {
            System.out.println("SYNC injection");
            ClientVersion version = PacketEvents.getAPI().getPlayerUtils().getClientVersion(e.getPlayer());
            System.out.println("ClientVersion: " + version.name());
        }
    }


}