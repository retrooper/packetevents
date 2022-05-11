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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Combat;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.OptionalInt;

public class WrapperPlayServerCombatEvent extends PacketWrapper<WrapperPlayServerCombatEvent> {
  private Combat combat;
  private OptionalInt duration;
  private int entityId;
  private OptionalInt playerId;
  private Optional<String> deathMessage;

  public WrapperPlayServerCombatEvent(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerCombatEvent(Combat combat) {
    super(PacketType.Play.Server.COMBAT_EVENT);
    this.combat = combat;
  }

  public WrapperPlayServerCombatEvent(Combat combat, OptionalInt duration, int entityId) {
    super(PacketType.Play.Server.COMBAT_EVENT);
    this.combat = combat;
    this.duration = duration;
    this.entityId = entityId;
  }

  public WrapperPlayServerCombatEvent(Combat combat, OptionalInt playerId, int entityId, Optional<String> deathMessage) {
    super(PacketType.Play.Server.COMBAT_EVENT);
    this.combat = combat;
    this.playerId = playerId;
    this.entityId = entityId;
    this.deathMessage = deathMessage;
  }

  @Override
  public void read() {
    this.duration = OptionalInt.empty();
    this.playerId = OptionalInt.empty();
    this.deathMessage = Optional.empty();
    this.combat = Combat.VALUES[readVarInt()];
    switch (combat) {
      case END_COMBAT:
        this.duration = OptionalInt.of(readVarInt());
        this.entityId = readInt();
        break;
      case ENTITY_DEAD:
        this.playerId = OptionalInt.of(readVarInt());
        this.entityId = readInt();
        this.deathMessage = Optional.of(readString());
        break;
    }
  }

  @Override
  public void write() {
    writeVarInt(combat.ordinal());
    switch (combat) {
      case END_COMBAT:
        writeVarInt(duration.getAsInt());
        writeInt(entityId);
        break;
      case ENTITY_DEAD:
        writeVarInt(playerId.getAsInt());
        writeInt(entityId);
        writeString(deathMessage.orElse(""));
        break;
    }
  }

  @Override
  public void copy(WrapperPlayServerCombatEvent wrapper) {
    this.combat = wrapper.combat;
    this.duration = wrapper.duration;
    this.entityId = wrapper.entityId;
    this.playerId = wrapper.playerId;
    this.deathMessage = wrapper.deathMessage;
  }

  public Combat getCombat() {
    return combat;
  }

  public void setCombat(Combat combat) {
    this.combat = combat;
  }

  public OptionalInt getDuration() {
    return duration;
  }

  public void setDuration(OptionalInt duration) {
    this.duration = duration;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public OptionalInt getPlayerId() {
    return playerId;
  }

  public void setPlayerId(OptionalInt playerId) {
    this.playerId = playerId;
  }

  public Optional<String> getDeathMessage() {
    return deathMessage;
  }

  public void setDeathMessage(Optional<String> deathMessage) {
    this.deathMessage = deathMessage;
  }
}
