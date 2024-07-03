/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import com.github.retrooper.packetevents.protocol.world.states.enums.East;
import com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import com.github.retrooper.packetevents.protocol.world.states.enums.Hinge;
import com.github.retrooper.packetevents.protocol.world.states.enums.Instrument;
import com.github.retrooper.packetevents.protocol.world.states.enums.Leaves;
import com.github.retrooper.packetevents.protocol.world.states.enums.Mode;
import com.github.retrooper.packetevents.protocol.world.states.enums.North;
import com.github.retrooper.packetevents.protocol.world.states.enums.Orientation;
import com.github.retrooper.packetevents.protocol.world.states.enums.Part;
import com.github.retrooper.packetevents.protocol.world.states.enums.SculkSensorPhase;
import com.github.retrooper.packetevents.protocol.world.states.enums.Shape;
import com.github.retrooper.packetevents.protocol.world.states.enums.South;
import com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import com.github.retrooper.packetevents.protocol.world.states.enums.TrialSpawnerState;
import com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import com.github.retrooper.packetevents.protocol.world.states.enums.VaultState;
import com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import com.github.retrooper.packetevents.protocol.world.states.enums.West;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum StateValue {

    AGE("age", int.class, Integer::parseInt),
    ATTACHED("attached", boolean.class, Boolean::parseBoolean),
    ATTACHMENT("attachment", Attachment.class, Attachment::valueOf),
    AXIS("axis", Axis.class, Axis::valueOf),
    BERRIES("berries", boolean.class, Boolean::parseBoolean),
    BITES("bites", int.class, Integer::parseInt),
    BLOOM("bloom", boolean.class, Boolean::parseBoolean),
    BOTTOM("bottom", boolean.class, Boolean::parseBoolean),
    CANDLES("candles", int.class, Integer::parseInt),
    CAN_SUMMON("can_summon", boolean.class, Boolean::parseBoolean),
    CHARGES("charges", int.class, Integer::parseInt),
    CONDITIONAL("conditional", boolean.class, Boolean::parseBoolean),
    CRACKED("cracked", boolean.class, Boolean::parseBoolean),
    CRAFTING("crafting", boolean.class, Boolean::parseBoolean),
    DELAY("delay", int.class, Integer::parseInt),
    DISARMED("disarmed", boolean.class, Boolean::parseBoolean),
    DISTANCE("distance", int.class, Integer::parseInt),
    DOWN("down", boolean.class, Boolean::parseBoolean),
    DRAG("drag", boolean.class, Boolean::parseBoolean),
    DUSTED("dusted", int.class, Integer::parseInt),
    EAST("east", East.class, East::valueOf),
    EGGS("eggs", int.class, Integer::parseInt),
    ENABLED("enabled", boolean.class, Boolean::parseBoolean),
    EXTENDED("extended", boolean.class, Boolean::parseBoolean),
    EYE("eye", boolean.class, Boolean::parseBoolean),
    FACE("face", Face.class, Face::valueOf),
    FACING("facing", BlockFace.class, BlockFace::valueOf),
    FLOWER_AMOUNT("flower_amount", int.class, Integer::parseInt),
    HALF("half", Half.class, Half::valueOf),
    HANGING("hanging", boolean.class, Boolean::parseBoolean),
    HAS_BOOK("has_book", boolean.class, Boolean::parseBoolean),
    HAS_BOTTLE_0("has_bottle_0", boolean.class, Boolean::parseBoolean),
    HAS_BOTTLE_1("has_bottle_1", boolean.class, Boolean::parseBoolean),
    HAS_BOTTLE_2("has_bottle_2", boolean.class, Boolean::parseBoolean),
    HAS_RECORD("has_record", boolean.class, Boolean::parseBoolean),
    HATCH("hatch", int.class, Integer::parseInt),
    HINGE("hinge", Hinge.class, Hinge::valueOf),
    HONEY_LEVEL("honey_level", int.class, Integer::parseInt),
    IN_WALL("in_wall", boolean.class, Boolean::parseBoolean),
    INSTRUMENT("instrument", Instrument.class, Instrument::valueOf),
    INVERTED("inverted", boolean.class, Boolean::parseBoolean),
    LAYERS("layers", int.class, Integer::parseInt),
    LEAVES("leaves", Leaves.class, Leaves::valueOf),
    LEVEL("level", int.class, Integer::parseInt),
    LIT("lit", boolean.class, Boolean::parseBoolean),
    LOCKED("locked", boolean.class, Boolean::parseBoolean),
    MODE("mode", Mode.class, Mode::valueOf),
    MOISTURE("moisture", int.class, Integer::parseInt),
    NORTH("north", North.class, North::valueOf),
    NOTE("note", int.class, Integer::parseInt),
    OCCUPIED("occupied", boolean.class, Boolean::parseBoolean),
    OMINOUS("ominous", boolean.class, Boolean::parseBoolean),
    OPEN("open", boolean.class, Boolean::parseBoolean),
    ORIENTATION("orientation", Orientation.class, Orientation::valueOf),
    PART("part", Part.class, Part::valueOf),
    PERSISTENT("persistent", boolean.class, Boolean::parseBoolean),
    PICKLES("pickles", int.class, Integer::parseInt),
    POWER("power", int.class, Integer::parseInt),
    POWERED("powered", boolean.class, Boolean::parseBoolean),
    ROTATION("rotation", int.class, Integer::parseInt),
    SCULK_SENSOR_PHASE("sculk_sensor_phase", SculkSensorPhase.class, SculkSensorPhase::valueOf),
    SHAPE("shape", Shape.class, Shape::valueOf),
    SHORT("short", boolean.class, Boolean::parseBoolean),
    SHRIEKING("shrieking", boolean.class, Boolean::parseBoolean),
    SIGNAL_FIRE("signal_fire", boolean.class, Boolean::parseBoolean),
    SLOT_0_OCCUPIED("slot_0_occupied", boolean.class, Boolean::parseBoolean),
    SLOT_1_OCCUPIED("slot_1_occupied", boolean.class, Boolean::parseBoolean),
    SLOT_2_OCCUPIED("slot_2_occupied", boolean.class, Boolean::parseBoolean),
    SLOT_3_OCCUPIED("slot_3_occupied", boolean.class, Boolean::parseBoolean),
    SLOT_4_OCCUPIED("slot_4_occupied", boolean.class, Boolean::parseBoolean),
    SLOT_5_OCCUPIED("slot_5_occupied", boolean.class, Boolean::parseBoolean),
    SNOWY("snowy", boolean.class, Boolean::parseBoolean),
    STAGE("stage", int.class, Integer::parseInt),
    SOUTH("south", South.class, South::valueOf),
    THICKNESS("thickness", Thickness.class, Thickness::valueOf),
    TILT("tilt", Tilt.class, Tilt::valueOf),
    TRIAL_SPAWNER_STATE("trial_spawner_state", TrialSpawnerState.class, TrialSpawnerState::valueOf),
    TRIGGERED("triggered", boolean.class, Boolean::parseBoolean),
    TYPE("type", Type.class, Type::valueOf),
    UNSTABLE("unstable", boolean.class, Boolean::parseBoolean),
    UP("up", boolean.class, Boolean::parseBoolean),
    VAULT_STATE("vault_state", VaultState.class, VaultState::valueOf),
    VERTICAL_DIRECTION("vertical_direction", VerticalDirection.class, VerticalDirection::valueOf),
    WATERLOGGED("waterlogged", boolean.class, Boolean::parseBoolean),
    WEST("west", West.class, West::valueOf);

    public static final Index<String, StateValue> NAME_INDEX = Index.create(
            StateValue.class, StateValue::getName);

    private final String name;
    private final Class<?> dataClass;
    private final Function<String, Object> parser;

    StateValue(String name, Class<?> dataClass, Function<String, Object> parser) {
        this.name = name;
        this.dataClass = dataClass;
        this.parser = parser;
    }

    public static @Nullable StateValue byName(String name) {
        return NAME_INDEX.value(name);
    }

    public Object parse(String input) {
        return this.parser.apply(input);
    }

    public String getName() {
        return name;
    }

    public Class<?> getDataClass() {
        return this.dataClass;
    }

    public Function<String, Object> getParser() {
        return parser;
    }
}
