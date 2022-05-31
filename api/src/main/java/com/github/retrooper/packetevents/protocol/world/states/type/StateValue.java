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

package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.states.enums.*;

import java.util.HashMap;
import java.util.function.Function;

public enum StateValue {
    AGE("age", Integer::parseInt),
    ATTACHED("attached", Boolean::parseBoolean),
    ATTACHMENT("attachment", Attachment::valueOf),
    AXIS("axis", Axis::valueOf),
    BERRIES("berries", Boolean::parseBoolean),
    BITES("bites", Integer::parseInt),
    BOTTOM("bottom", Boolean::parseBoolean),
    CANDLES("candles", Integer::parseInt),
    CHARGES("charges", Integer::parseInt),
    CONDITIONAL("conditional", Boolean::parseBoolean),
    DELAY("delay", Integer::parseInt),
    DISARMED("disarmed", Boolean::parseBoolean),
    DISTANCE("distance", Integer::parseInt),
    DOWN("down", Boolean::parseBoolean),
    DRAG("drag", Boolean::parseBoolean),
    EAST("east", East::valueOf),
    EGGS("eggs", Integer::parseInt),
    ENABLED("enabled", Boolean::parseBoolean),
    EXTENDED("extended", Boolean::parseBoolean),
    EYE("eye", Boolean::parseBoolean),
    FACE("face", Face::valueOf),
    FACING("facing", BlockFace::valueOf),
    HALF("half", Half::valueOf),
    HANGING("hanging", Boolean::parseBoolean),
    HAS_BOOK("has_book", Boolean::parseBoolean),
    HAS_BOTTLE_0("has_bottle_0", Boolean::parseBoolean),
    HAS_BOTTLE_1("has_bottle_1", Boolean::parseBoolean),
    HAS_BOTTLE_2("has_bottle_2", Boolean::parseBoolean),
    HAS_RECORD("has_record", Boolean::parseBoolean),
    HATCH("hatch", Integer::parseInt),
    HINGE("hinge", Hinge::valueOf),
    HONEY_LEVEL("honey_level", Integer::parseInt),
    IN_WALL("in_wall", Boolean::parseBoolean),
    INSTRUMENT("instrument", Instrument::valueOf),
    INVERTED("inverted", Boolean::parseBoolean),
    LAYERS("layers", Integer::parseInt),
    LEAVES("leaves", Leaves::valueOf),
    LEVEL("level", Integer::parseInt),
    LIT("lit", Boolean::parseBoolean),
    LOCKED("locked", Boolean::parseBoolean),
    MODE("mode", Mode::valueOf),
    MOISTURE("moisture", Integer::parseInt),
    NORTH("north", North::valueOf),
    NOTE("note", Integer::parseInt),
    OCCUPIED("occupied", Boolean::parseBoolean),
    OPEN("open", Boolean::parseBoolean),
    ORIENTATION("orientation", Orientation::valueOf),
    PART("part", Part::valueOf),
    PERSISTENT("persistent", Boolean::parseBoolean),
    PICKLES("pickles", Integer::parseInt),
    POWER("power", Integer::parseInt),
    POWERED("powered", Boolean::parseBoolean),
    ROTATION("rotation", Integer::parseInt),
    SCULK_SENSOR_PHASE("sculk_sensor_phase", SculkSensorPhase::valueOf),
    SHAPE("shape", Shape::valueOf),
    SHORT("short", Boolean::parseBoolean),
    SIGNAL_FIRE("signal_fire", Boolean::parseBoolean),
    SNOWY("snowy", Boolean::parseBoolean),
    STAGE("stage", Integer::parseInt),
    SOUTH("south", South::valueOf),
    THICKNESS("thickness", Thickness::valueOf),
    TILT("tilt", Tilt::valueOf),
    TRIGGERED("triggered", Boolean::parseBoolean),
    TYPE("type", Type::valueOf),
    UNSTABLE("unstable", Boolean::parseBoolean),
    UP("up", Boolean::parseBoolean),
    VERTICAL_DIRECTION("vertical_direction", VerticalDirection::valueOf),
    WATERLOGGED("waterlogged", Boolean::parseBoolean),
    WEST("west", West::valueOf);

    private final String name;
    private final Function<String, Object> parser;

    private static final HashMap<String, StateValue> values = new HashMap<>();

    StateValue(final String name, Function<String, Object> parser) {
        this.name = name;
        this.parser = parser;
    }

    public static StateValue byName(String name) {
        return values.get(name);
    }

    static {
        for (StateValue value : values()) {
            values.put(value.name, value);
        }
    }

    public String getName() {
        return name;
    }

    public Function<String, Object> getParser() {
        return parser;
    }
}
