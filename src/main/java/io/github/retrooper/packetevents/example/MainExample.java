/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        //ClientVersion cannot be resolved, it is the same as the server's.
        //The clientversion can only not be resolved if
        // ViaVersion, ProtocolSuppport, or ProtocolLib isn't present.
        //ClientVersion might be resolvable if the server version is 1.7.10.
        PacketEvents.getSettings().setDoAutoResolveClientProtocolVersion(true);

        //Deprecated, as it is no longer needed
        //PacketEvents.getSettings().setIdentifier("packetevents_api");

        PacketEvents.init(this);
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }


}