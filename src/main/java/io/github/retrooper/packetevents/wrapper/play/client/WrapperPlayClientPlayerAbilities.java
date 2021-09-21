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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientPlayerAbilities extends PacketWrapper<WrapperPlayClientPlayerAbilities> {
    private boolean flying;
    private Optional<Boolean> vulnerable;
    private Optional<Boolean> flightAllowed;
    private Optional<Boolean> buildingInstantlyAllowed;
    private Optional<Float> flySpeed;
    private Optional<Float> walkSpeed;

    public WrapperPlayClientPlayerAbilities(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerAbilities(boolean flying, Optional<Boolean> vulnerable, Optional<Boolean> flightAllowed,
                                            Optional<Boolean> buildingInstantlyAllowed,
                                            Optional<Float> flySpeed, Optional<Float> walkSpeed) {
        super(PacketType.Play.Client.PLAYER_ABILITIES.getID());
        this.flying = flying;
        this.vulnerable = vulnerable;
        this.flightAllowed = flightAllowed;
        this.buildingInstantlyAllowed = buildingInstantlyAllowed;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public WrapperPlayClientPlayerAbilities(boolean flying) {
        this(flying, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public void readData() {
        byte mask = readByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16)) {
            flying = (mask & 0x02) != 0;
            vulnerable = Optional.empty();
            flightAllowed = Optional.empty();
            buildingInstantlyAllowed = Optional.empty();
            flySpeed = Optional.empty();
            walkSpeed = Optional.empty();
        } else {
            vulnerable = Optional.of((mask & 0x01) != 0);
            flying = (mask & 0x02) != 0;
            flightAllowed = Optional.of((mask & 0x04) != 0);
            buildingInstantlyAllowed = Optional.of((mask & 0x08) != 0);
            flySpeed = Optional.of(readFloat());
            walkSpeed = Optional.of(readFloat());
        }

    }

    @Override
    public void readData(WrapperPlayClientPlayerAbilities wrapper) {
        vulnerable = wrapper.vulnerable;
        flying = wrapper.flying;
        flightAllowed = wrapper.flightAllowed;
        buildingInstantlyAllowed = wrapper.buildingInstantlyAllowed;
        flySpeed = wrapper.flySpeed;
        walkSpeed = wrapper.walkSpeed;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16)) {
            byte mask = (byte) (flying ? 0x02 : 0x00);
            writeByte(mask);
        }
        else {
            byte mask = 0x00;
            if (vulnerable.orElse(false)) {
                mask |= 0x01;
            }

            if (flying) {
                mask |= 0x02;
            }

            if (flightAllowed.orElse(false)) {
                mask |= 0x04;
            }

            if (buildingInstantlyAllowed.orElse(false)) {
                mask |= 0x08;
            }
            writeByte(mask);
            //These are my guesses of default values, don't cry, just pass in the fly/walk speed next time :0
            writeFloat(flySpeed.orElse(0.1f));
            writeFloat(walkSpeed.orElse(0.2f));
        }
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public Optional<Boolean> isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(Optional<Boolean> vulnerable) {
        this.vulnerable = vulnerable;
    }

    public Optional<Boolean> isFlightAllowed() {
        return flightAllowed;
    }

    public void setFlightAllowed(Optional<Boolean> flightAllowed) {
        this.flightAllowed = flightAllowed;
    }

    public Optional<Boolean> isBuildingInstantlyAllowed() {
        return buildingInstantlyAllowed;
    }

    public void setBuildingInstantlyAllowed(Optional<Boolean> buildingInstantlyAllowed) {
        this.buildingInstantlyAllowed = buildingInstantlyAllowed;
    }

    public Optional<Float> getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(Optional<Float> flySpeed) {
        this.flySpeed = flySpeed;
    }

    public Optional<Float> getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(Optional<Float> walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
}
