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

package io.github.retrooper.packetevents.packetwrappers.play.out.tabcomplete;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.ArrayList;
import java.util.List;
//TODO finish, and find out why nothing triggers on 1.16????????
class WrappedPacketOutTabComplete extends WrappedPacket {
    private static Class<?> suggestionsClass;
    private int transactionID;
    private String[] matches;
    public WrappedPacketOutTabComplete(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutTabComplete(String[] matches) {
        this.transactionID = -1;
        this.matches = matches;
    }

    public WrappedPacketOutTabComplete(int transactionID, String[] matches) {
        this.transactionID = transactionID;
        this.matches = matches;
    }

    @Override
    protected void load() {
        try {
            suggestionsClass = Class.forName("com.mojang.brigadier.suggestion.Suggestions");
        } catch (ClassNotFoundException e) {
            suggestionsClass = null;
        }
       /* net.minecraft.server.v1_7_R4.PacketPlayOutTabComplete a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutTabComplete a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutTabComplete a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutTabComplete a5; //TODO, its cancer on 1.13
        net.minecraft.server.v1_13_R2.PacketPlayOutTabComplete a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutTabComplete a7;*/
    }

    public int getTransactionId() {
        if (packet != null) {
            return readInt(0);
        }
        else {
            return transactionID;
        }
    }

    public void setTransactionId(int transactionID) {
        if (packet != null) {
            writeInt(0, transactionID);
        }
        else {
            this.transactionID = transactionID;
        }
    }

    public String[] getMatches() {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_13)) {
                Object suggestions = readObject(0, suggestionsClass);
                WrappedPacket suggestionsWrapper = new WrappedPacket(new NMSPacket(suggestions));
                List<Object> suggestionList = suggestionsWrapper.readObject(0, List.class);
                String[] matches = new String[suggestionList.size()];
                for (int i = 0; i < matches.length; i++) {
                    Object suggestion = suggestionList.get(i);
                    WrappedPacket suggestionWrapper = new WrappedPacket(new NMSPacket(suggestion));
                    matches[i] = suggestionWrapper.readString(0);
                }
                return matches;

            } else {
                return readStringArray(0);
            }
        }
        else {
            return matches;
        }
    }

    /*public void setMatches(String[] matches) {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_13)) {
                //TODO
                Object suggestions = readObject(0, suggestionsClass);
                WrappedPacket suggestionsWrapper = new WrappedPacket(new NMSPacket(suggestions));
                List<?> suggestionsList = new ArrayList<>();
                for (String match : matches) {
                    Object suggestion =
                }
                List<Object> suggestionList = suggestionsWrapper.readObject(0, List.class);
                String[] matches = new String[suggestionList.size()];
                for (int i = 0; i < matches.length; i++) {
                    Object suggestion = suggestionList.get(i);
                    WrappedPacket suggestionWrapper = new WrappedPacket(new NMSPacket(suggestion));
                    matches[i] = suggestionWrapper.readString(0);
                }
                return matches;
            } else {
                writeStringArray(0, matches);
            }
        }
        else {
            this.matches = matches;
        }
    }*/
}
