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

package com.github.retrooper.packetevents.protocol.world;

// From MCProtocolLib
public enum Direction {
  DOWN(-1),
  UP(-1),
  NORTH(0),
  SOUTH(1),
  WEST(2),
  EAST(3);

  private final int horizontalIndex;

  Direction(int horizontalIndex) {
    this.horizontalIndex = horizontalIndex;
  }

  public int getHorizontalIndex() {
    return horizontalIndex;
  }

  private static final Direction[] HORIZONTAL_VALUES = {NORTH, SOUTH, WEST, EAST};
  public static final Direction[] VALUES = values();

  public static Direction getByHorizontalIndex(int index) {
    return HORIZONTAL_VALUES[index % HORIZONTAL_VALUES.length];
  }
}