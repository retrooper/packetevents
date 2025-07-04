package com.github.retrooper.packetevents.protocol;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;

public class PacketTransformer {
   protected final User user;

   public PacketTransformer(User user) {
      this.user = user;
   }

   public void onPreReceive(PacketReceiveEvent event) {}
   public void onPostReceive(PacketReceiveEvent event) {}
   public void onPreSend(PacketSendEvent event) {}
   public void onPostSend(PacketSendEvent event) {}
}
