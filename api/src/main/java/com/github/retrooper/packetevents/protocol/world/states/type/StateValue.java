package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.protocol.world.states.enums.*;

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
    FACING("facing", Facing::valueOf),
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
    LEAVES("leaves", Integer::parseInt),
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
    SOUTH("south", South::valueOf),
    THICKNESS("thickness", Thickness::valueOf),
    TILT("tilt", Tilt::valueOf),
    TRIGGERED("triggered", Boolean::parseBoolean),
    TYPE("type", Type::valueOf),
    UNSTABLE("unstable", Boolean::parseBoolean),
    UP("up", Boolean::parseBoolean),
    VERTICAL_DIRECTION("vertical_direction", Boolean::parseBoolean),
    WATERLOGGED("waterlogged", Boolean::parseBoolean),
    WEST("west", West::valueOf);

    private final String name;
    private final Function<String, Object> parser;
    private final Class<?> returnType;

    StateValue(final String name, Function<String, Object> parser) {
        Class<?> tempReturnType;
        this.name = name;
        this.parser = parser;

        try {
            tempReturnType = parser.getClass().getDeclaredMethod("apply", String.class).getReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            tempReturnType = Object.class;
        }

        returnType = tempReturnType;
    }

    public String getName() {
        return name;
    }

    public Function<String, Object> getParser() {
        return parser;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
