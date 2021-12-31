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

package com.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.player.attributes.TabCompleteAttribute;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.gameprofile.GameProfile;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import java.util.Optional;
import java.util.UUID;

public class InternalPacketListener implements PacketListener {
    //Make this specific event be at MONITOR priority
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            //Process outgoing login success packet
            WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
            //Store game profile
            GameProfile profile = loginSuccess.getGameProfile();
            PacketEvents.getAPI().getPlayerManager().setGameProfile(event.getChannel(), profile);
            PacketEvents.getAPI().getLogManager().debug("Set game profile for player " + profile.getName());
            //Transition into the PLAY connection state
            PacketEvents.getAPI().getPlayerManager().changeConnectionState(event.getChannel(), ConnectionState.PLAY);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
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
                    /*WrapperPlayClientTabComplete tabComplete = new WrapperPlayClientTabComplete(event);
                    String text = tabComplete.getText();
                    GameProfile profile = PacketEvents.getAPI().getPlayerManager().getGameProfile(event.getChannel());
                    UUID uuid = profile.getId();
                    TabCompleteAttribute tabCompleteAttribute =
                            PacketEvents.getAPI().getPlayerManager().getAttributeOrDefault(uuid,
                                    TabCompleteAttribute.class,
                                    new TabCompleteAttribute());
                    tabCompleteAttribute.setInput(text);
                    Optional<Integer> transactionID = tabComplete.getTransactionId();
                    transactionID.ifPresent(tabComplete::setTransactionId);
                    PacketEvents.getAPI().getLogManager().debug("Tab complete received: " + text);*/
                }
                break;
        }
    }
}