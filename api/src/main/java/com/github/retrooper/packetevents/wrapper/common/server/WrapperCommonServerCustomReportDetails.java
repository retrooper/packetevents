/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Map;

public abstract class WrapperCommonServerCustomReportDetails<T extends WrapperCommonServerCustomReportDetails<T>> extends PacketWrapper<T> {

    private static final int MAX_KEY_LENGTH = 128;
    private static final int MAX_VALUE_LENGTH = 4096;

    private Map<String, String> details;

    public WrapperCommonServerCustomReportDetails(PacketSendEvent event) {
        super(event);
    }

    public WrapperCommonServerCustomReportDetails(PacketTypeCommon packetType, Map<String, String> details) {
        super(packetType);
        this.details = details;
    }

    @Override
    public void read() {
        this.details = this.readMap(
                ew -> ew.readString(MAX_KEY_LENGTH),
                ew -> ew.readString(MAX_VALUE_LENGTH)
        );
    }

    @Override
    public void write() {
        this.writeMap(this.details,
                (ew, key) -> ew.writeString(key, MAX_KEY_LENGTH),
                (ew, val) -> ew.writeString(val, MAX_VALUE_LENGTH)
        );
    }

    @Override
    public void copy(T wrapper) {
        this.details = wrapper.getDetails();
    }

    public Map<String, String> getDetails() {
        return this.details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
