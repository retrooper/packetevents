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
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.enchantitem.WrappedPacketInEnchantItem;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo.WrappedPacketOutPlayerInfo;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.player.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PacketEventsPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings.compatInjector(false);
        settings.checkForUpdates(false);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        PacketEvents.get().init();
       /* PacketEvents.get().registerListener(new PacketListenerDynamic() {
            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                byte packetID = event.getPacketId();
                if (packetID == PacketType.Play.Client.CHAT) {
                    WrappedPacketInChat chat = new WrappedPacketInChat(event.getNMSPacket());
                    String message = chat.getMessage();
                    if (message.startsWith("/") && message.equalsIgnoreCase("/gamemode creative")) {
                        event.getPlayer().sendMessage("detected gamemode switch message");
                        event.setCancelled(true);
                        final Player player = event.getPlayer();
                        final WrappedGameProfile gameProfile = PacketEvents.get().getPlayerUtils().getGameProfile(player);
                        try {
                            CompletableFuture.runAsync(new Runnable() {
                                @Override
                                public void run() {
                                    List<WrappedPacketOutPlayerInfo.PlayerInfo> playerInfoList = new ArrayList<>();
                                    playerInfoList.add(new WrappedPacketOutPlayerInfo.PlayerInfo("retrooper", gameProfile, GameMode.SPECTATOR, 0));
                                    WrappedPacketOutPlayerInfo wrappedPacketOutPlayerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.UPDATE_GAME_MODE, playerInfoList);
                                    PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutPlayerInfo);
                                }
                            }).thenRunAsync(new Runnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("sent packet");
                                }
                            }).thenRunAsync(new Runnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("gay creating...");
                                    List<WrappedPacketOutPlayerInfo.PlayerInfo> playerInfoList = new ArrayList<>();
                                    playerInfoList.add(new WrappedPacketOutPlayerInfo.PlayerInfo("gay", new WrappedGameProfile(UUID.randomUUID(), "gay"), GameMode.SURVIVAL, 0));
                                    WrappedPacketOutPlayerInfo wrappedPacketOutPlayerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.ADD_PLAYER, playerInfoList);
                                    PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutPlayerInfo);
                                }
                            }).thenRunAsync(new Runnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("gay created!");
                                }
                            })
                                    .get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        event.getPlayer().sendMessage("scheduled!");

                    }
                }
            }
        });*/
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}