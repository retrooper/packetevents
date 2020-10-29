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

package io.github.retrooper.packetevents.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class BungeePluginMessageListener implements PluginMessageListener {
    public static String tagName = "packetevents";

    @Override
    public void onPluginMessageReceived(String tag, Player player, byte[] bytes) {
        if (tag.equals(tagName)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            if (in.readUTF().equals("version")) {
                String uuidString = in.readUTF();
                Object channel = NMSUtils.getChannel(player);
                short protocolVersion = in.readShort();
                ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
                PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(channel, version);
                //DEBUG
                System.out.println("ClientVersion received from Bungee: " + version.name());
            }
        }
    }


}
