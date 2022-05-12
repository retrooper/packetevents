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