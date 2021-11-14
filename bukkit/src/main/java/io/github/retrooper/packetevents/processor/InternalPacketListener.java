/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.player.attributes.TabCompleteAttribute;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.ComponentParser;
import com.github.retrooper.packetevents.protocol.chat.component.HoverEvent;
import com.github.retrooper.packetevents.protocol.chat.component.TextComponent;
import com.github.retrooper.packetevents.protocol.inventory.ItemStack;
import com.github.retrooper.packetevents.protocol.inventory.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InternalPacketListener implements PacketListener {
    //Make this specific event be at MONITOR priority
    @Override
    public void onPacketSend(PacketSendEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            //Transition into the PLAY connection state
            PacketEvents.getAPI().getPlayerManager().changeConnectionState(event.getChannel(), ConnectionState.PLAY);
        } else if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
            WrapperPlayServerChatMessage chatMessage = new WrapperPlayServerChatMessage(event);
            String og = chatMessage.getJSONMessageRaw();
            //System.out.println("OG JSON: " + og);
            List<TextComponent> components = chatMessage.getMessageComponents();
            for (TextComponent component : components) {
                //System.out.println("Component part: " + component.getText());
            }
            String jsonMessage = ComponentParser.buildJSONString(components);
            //System.out.println("JSON Message: " + jsonMessage);
        }
        /*
        else if (event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
            WrapperPlayServerChunkData chunkData = new WrapperPlayServerChunkData(event);
            Column column = chunkData.getColumn();
            int x = column.getX();
            int z = column.getZ();
            NBTCompound heightMaps = column.getHeightMaps();
            if (player != null) {
                //   player.sendMessage("X: " + x + ", Z: " + z);
                //  player.sendMessage("HEIGHT MAPS: " + heightMaps.getTagNames());
                //  player.sendMessage("CHUNKS:");
                //TODO Credit in all chunk related classes
                for (BaseChunk chunk : column.getChunks()) {
                    try {
                        BaseBlockState state = chunk.get(column.getX(), player.getLocation().getBlockY(), column.getZ());
                        if (state != null) {
                            //player.sendMessage("Jackpot!");
                            //player.sendMessage("Block type: " + state.getCombinedId());
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
            event.setLastUsedWrapper(null);
        }
        if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
            WrapperPlayServerChatMessage msg = new WrapperPlayServerChatMessage(event);
            for (TextComponent component : msg.getMessageComponents()) {
                System.out.println("Text: " + component.getText() + ", color: " + component.getColor().name() + ", bold: " + component.isBold());
            }
            //System.out.println("msg: " + msg.getJSONMessage());
        } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
            System.out.println("dimension world type: " + respawn.getDimension().getType());
            System.out.println("world name: " + respawn.getWorldName());

        } else if (event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            WrapperPlayServerTabComplete tabComplete = new WrapperPlayServerTabComplete(event);
            String lastInput = PacketEvents.getAPI().getPlayerManager().getAttribute(player.getUniqueId(), TabCompleteAttribute.class).getInput();
            System.out.println("Last input length: " + lastInput.length() + ", Last input: " + lastInput);
            for (WrapperPlayServerTabComplete.CommandMatch match : tabComplete.getCommandMatches()) {
                System.out.println("MATCH: " + match.getText());
            }
        }*/
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        switch (event.getConnectionState()) {
            case HANDSHAKING:
                if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
                    WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
                    ClientVersion clientVersion = handshake.getClientVersion();

                    //Update client version for this event call
                    event.setClientVersion(clientVersion);

                    //Map netty channel with the client version.
                    PacketEvents.getAPI().getPlayerManager().CLIENT_VERSIONS.put(event.getChannel(), clientVersion);

                    //Transition into the LOGIN OR STATUS connection state
                    PacketEvents.getAPI().getPlayerManager().changeConnectionState(event.getChannel(), handshake.getNextConnectionState());

                }
                break;
            case LOGIN:
                if (event.getPacketType() == PacketType.Login.Client.LOGIN_START) {
                    WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(event);
                    //Map the player usernames with their netty channels
                    PacketEvents.getAPI().getPlayerManager().CHANNELS.put(start.getUsername(), event.getChannel());
                }
                break;
            case PLAY:
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    WrapperPlayClientTabComplete tabComplete = new WrapperPlayClientTabComplete(event);
                    String text = tabComplete.getText();
                    TabCompleteAttribute tabCompleteAttribute =
                            PacketEvents.getAPI().getPlayerManager().getAttributeOrDefault(player.getUniqueId(),
                                    TabCompleteAttribute.class,
                                    new TabCompleteAttribute());
                    tabCompleteAttribute.setInput(text);
                    Optional<Integer> transactionID = tabComplete.getTransactionId();
                    transactionID.ifPresent(tabComplete::setTransactionID);
                } else if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
                    List<TextComponent> components = new ArrayList<>();
                    components.add(TextComponent.builder().text("id: " + interactEntity.getEntityId() + " ")
                            .color(Color.BRIGHT_GREEN)
                            .italic(true)
                            .build());

                    TextComponent hoverComponent = TextComponent.builder().text("Request to get banned!").color(Color.DARK_RED)
                            .bold(true)
                            .underlined(true)
                            .build();

                    String hoverText = hoverComponent.buildJSON().toJSONString();
                    components.add(TextComponent.builder().text("type: " + interactEntity.getType().name())
                            .color(Color.DARK_RED)
                            .bold(true)
                            .hoverEvent(new HoverEvent(HoverEvent.HoverType.SHOW_TEXT, new String[]{hoverText}))
                            .build());


                    UUID uuid = player != null ? player.getUniqueId() : new UUID(0L, 0L);
                    WrapperPlayServerChatMessage cm = new WrapperPlayServerChatMessage(components, WrapperPlayServerChatMessage.ChatPosition.CHAT, uuid);
                    //PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), cm);


                    int id = ItemTypes.DIAMOND_SWORD.getId();
                    //player.sendMessage("sword id: " + id);
                    ItemStack item = ItemStack.builder().type(ItemTypes.DIAMOND_BLOCK)
                            .amount(61).build();

                    WrapperPlayServerSetSlot setSlot =
                            new WrapperPlayServerSetSlot(0, 4, 3, item);

                    //PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), setSlot);
                    if (player != null) {
                        //player.sendMessage("Yes set slot!");
                    }
                }
                break;
        }
    }


}