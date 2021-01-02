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

package io.github.retrooper.packetevents.injector.earlyinjector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.ChannelInjector;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Early channel injector.
 * This is an injector that injects the player a bit earlier than the {@link io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector}.
 * Injecting earlier can allow us to listen to the LOGIN and STATUS packets.
 * PacketEvents internally need the LOGIN HANDSHAKING packet to resolve client version independently.
 * If some supported dependencies are available, PacketEvents will always prioritize them
 * and use their API.
 *
 * @author retrooper
 * @see io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector
 * @since 1.8
 */
public class EarlyChannelInjector implements ChannelInjector {
    /**
     * 1.7.10 channel injector as the netty package is different on 1.7.10.
     */
    private EarlyChannelInjector7 injector7;
    /**
     * 1.8 and above channel injector.
     */
    private EarlyChannelInjector8 injector8;

    /**
     * A boolean storing which one to use.
     * If true, we are using the 1.7.10 injector,
     * if false, we are using the 1.8 (and above) injector.
     */
    private boolean outdatedInjectorMode = false;

    /**
     * Constructor, we require the Bukkit Plugin instance.
     *
     * @param plugin Plugin
     */
    public EarlyChannelInjector(final Plugin plugin) {
        if (PacketEvents.get().getServerUtils().getVersion() == ServerVersion.v_1_7_10) {
            injector7 = new EarlyChannelInjector7(plugin);
            this.outdatedInjectorMode = true;
        } else {
            injector8 = new EarlyChannelInjector8(plugin);
        }
    }

    /**
     * Add our channel handler into minecraft's channel handler list.
     * Access the minecraft network managers to synchronize against them.
     */
    public void startup() {
        if (outdatedInjectorMode) {
            injector7.startup();
        } else {
            injector8.startup();
        }
    }

    /**
     * Remove our channel handler from minecraft's channel handler list.
     */
    public void close() {
        if (outdatedInjectorMode) {
            injector7.close();
        } else {
            injector8.close();
        }
    }

    /**
     * Asynchronously remove our channel handler from minecraft's channel handler list.
     */
    public void closeAsync() {
        PacketEvents.get().injectAndEjectExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });
    }

    /**
     * Inject a player synchronously.
     *
     * @param player Target player.
     * @see io.github.retrooper.packetevents.handler.PacketHandlerInternal#injectPlayerSync(Player)
     */
    @Override
    public void injectPlayerSync(Player player) {
        if (outdatedInjectorMode) {
            injector7.injectPlayerSync(player);
        } else {
            injector8.injectPlayerSync(player);
        }
    }

    /**
     * Eject a player synchronously.
     *
     * @param player Target player.
     * @see io.github.retrooper.packetevents.handler.PacketHandlerInternal#ejectPlayerSync(Player)
     */
    @Override
    public void ejectPlayerSync(Player player) {
        if (outdatedInjectorMode) {
            injector7.ejectPlayerSync(player);
        } else {
            injector8.ejectPlayerSync(player);
        }
    }

    /**
     * Inject a player asynchronously.
     *
     * @param player Target player.
     * @see io.github.retrooper.packetevents.handler.PacketHandlerInternal#injectPlayerAsync(Player)
     */
    @Override
    public void injectPlayerAsync(Player player) {
        if (outdatedInjectorMode) {
            injector7.injectPlayerAsync(player);
        } else {
            injector8.injectPlayerAsync(player);
        }
    }

    /**
     * Eject a player asynchronously.
     *
     * @param player Target player.
     * @see io.github.retrooper.packetevents.handler.PacketHandlerInternal#ejectPlayerAsync(Player)
     */
    @Override
    public void ejectPlayerAsync(Player player) {
        if (outdatedInjectorMode) {
            injector7.ejectPlayerAsync(player);
        } else {
            injector8.ejectPlayerAsync(player);
        }
    }

    /**
     * Send an NMS Packet to a netty channel.
     *
     * @param channel Netty channel as Object as netty import is not the same on 1.7.10.
     * @param packet  NMS Packet.
     */
    @Override
    public void sendPacket(Object channel, Object packet) {
        if (outdatedInjectorMode) {
            injector7.sendPacket(channel, packet);
        } else {
            injector8.sendPacket(channel, packet);
        }
    }
}
