package com.github.retrooper.packetevents.protocol.world.states;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Map;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

@NullMarked
public final class BlockStateCodec {

    public static final NbtCodec<WrappedBlockState> FULL_CODEC = new NbtMapCodec<WrappedBlockState>() {
        @Override
        public WrappedBlockState decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            boolean v263 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3);
            StateType type = tag.getOrThrow(v263 ? "id" : "Name", StateType.CODEC, wrapper);
            WrappedBlockState state = WrappedBlockState.getDefaultState(wrapper.getServerVersion().toClientVersion(), type);
            if (!state.data.isEmpty()) {
                NBTCompound propsTag = tag.getCompoundTagOrNull(v263 ? "properties" : "Properties");
                if (propsTag != null) {
                    decodeProperties(state, propsTag);
                }
            }
            return state;
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WrappedBlockState value) throws NbtCodecException {
            boolean v263 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3);
            tag.set(v263 ? "id" : "Name", value.type, StateType.CODEC, wrapper);
            if (!value.data.isEmpty()) {
                tag.setTag(v263 ? "properties" : "Properties", encodeProperties(value));
            }
        }
    }.codec();

    public static final NbtCodec<WrappedBlockState> CODEC = new NbtCodec<WrappedBlockState>() {
        @Override
        public WrappedBlockState decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            // lenient decode, this was only added with 26.3
            if (tag instanceof NBTString) {
                StateType type = StateType.CODEC.decode(tag, wrapper);
                return WrappedBlockState.getDefaultState(wrapper.getServerVersion().toClientVersion(), type);
            }
            return FULL_CODEC.decode(tag, wrapper);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, WrappedBlockState value) throws NbtCodecException {
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_3)) {
                WrappedBlockState defaultState = WrappedBlockState.getDefaultState(wrapper.getServerVersion().toClientVersion(), value.type, false);
                if (value.data.equals(defaultState.data)) {
                    return StateType.CODEC.encode(wrapper, value.type);
                }
            }
            return FULL_CODEC.encode(wrapper, value);
        }
    };

    private BlockStateCodec() {
    }

    private static NBTCompound encodeProperties(WrappedBlockState state) {
        NBTCompound tag = new NBTCompound();
        for (Map.Entry<StateValue, Object> dataEntry : state.getInternalData().entrySet()) {
            StateValue stateValue = dataEntry.getKey();
            // vanilla encodes all property values as strings
            String value = dataEntry.getValue().toString().toLowerCase(Locale.ROOT);
            tag.setTag(stateValue.getName(), new NBTString(value));
        }
        return tag;
    }

    private static void decodeProperties(WrappedBlockState state, NBTCompound tag) {
        boolean modified = false;
        for (Map.Entry<String, NBT> entry : tag.getTags().entrySet()) {
            StateValue stateValue = indexValueOrThrow(StateValue.NAME_INDEX, entry.getKey());
            if (!state.hasProperty(stateValue)) {
                continue; // ignore property not applicable to this block
            }
            Object value;
            if (stateValue.getDataClass() == boolean.class && entry.getValue() instanceof NBTNumber) {
                // special parsing
                value = ((NBTNumber) entry.getValue()).getAsByte() != 0;
            } else if (entry.getValue() instanceof NBTNumber) {
                Number num = ((NBTNumber) entry.getValue()).getAsNumber();
                value = stateValue.parse(num.toString());
            } else {
                NBTString stringTag = entry.getValue().castOrThrow(NBTString.class);
                value = stateValue.parse(stringTag.getValue().toUpperCase(Locale.ROOT));
            }
            if (!modified) {
                state.checkIfCloneNeeded();
                modified = true;
            }
            state.data.put(stateValue, value);
        }
        if (modified) {
            state.checkIsStillValid();
        }
    }
}
