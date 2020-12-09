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
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {
    @Override
    public void onLoad() {
        //We use default settings
        PacketEvents.create().load();
    }

    @Override
    public void onEnable() {
        /*
         * We created the instance in the onLoad method
         */
        PacketEvents.get().init(this);
        /**
         *  PacketEvents.get().getEventManager().registerListener(new PacketListenerDynamic() {
         *             @Override
         *             public void onPacketLogin(PacketLoginEvent event) {
         *                 if (event.getPacketId() == PacketType.Login.Client.HANDSHAKE) {
         *                     WrappedPacketLoginInHandshake handshake = new WrappedPacketLoginInHandshake(event.getNMSPacket());
         *                     System.out.println("IP: " + event.getSocketAddress().getHostName() + " sent protocol: " + handshake.getProtocolVersion());
         *                 }
         *             }
         *
         *             @Override
         *             public void onPacketReceive(PacketReceiveEvent event) {
         *                 if (event.getPacketId() == PacketType.Play.Client.CHAT) {
         *                     WrappedPacketInChat chat = new WrappedPacketInChat(event.getNMSPacket());
         *                     if (chat.getMessage().equalsIgnoreCase("version")) {
         *                         if (event.getPlayer() != null) {
         *                             event.getPlayer().sendMessage("Your version is " +
         *                                     PacketEvents.get().getPlayerUtils().getClientVersion(event.getPlayer()));
         *                         }
         *                     }
         *                 }
         *             }
         *         });
         */
    }

    @Override
    public void onDisable() {

        PacketEvents.get().stop();
    }
}