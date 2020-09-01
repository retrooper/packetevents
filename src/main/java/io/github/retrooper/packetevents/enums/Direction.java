/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.enums;

public enum Direction {
    DOWN, UP, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
    NORTH_WEST, SOUTH_EAST,
    SOUTH_WEST, WEST_NORTH_WEST,
    NORTH_NORTH_WEST, NORTH_NORTH_EAST,
    EAST_NORTH_EAST, EAST_SOUTH_EAST,
    SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST,
    WEST_SOUTH_WEST, SELF, NULL;

    public static Direction get(final int i) {
        return values()[i];
    }
}
