package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ItemStackCodec {

    public static final NbtCodec<ItemStack> INLINE_CODEC = ItemTypes.getCodec()
            .apply(type -> ItemStack.builder().type(type).build(), ItemStack::getType);

    // TODO components nbt
    public static final NbtMapCodec<ItemStack> TEMPLATE_MAP_CODEC = new NbtMapCodec<ItemStack>() {
        @Override
        public ItemStack decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            return ItemStack.builder()
                    .type(tag.getOrThrow("id", ItemTypes.getCodec(), wrapper))
                    .amount(tag.getOr("count", NbtCodecs.INT, 1, wrapper))
                    .build();
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ItemStack value) throws NbtCodecException {
            tag.setTag("id", new NBTString(value.getType().getName().toString()));
            if (value.getAmount() != 1) {
                tag.setTag("count", new NBTInt(value.getAmount()));
            }
        }
    };

    public static final NbtCodec<ItemStack> TEMPLATE_CODEC = TEMPLATE_MAP_CODEC.codec().withAlternative(INLINE_CODEC);

    public static final NbtMapCodec<ItemStack> MAP_CODEC = new NbtMapCodec<ItemStack>() {
        @Override
        public ItemStack decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            boolean v1205 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5);
            return ItemStack.builder()
                    .type(tag.getOrThrow("id", ItemTypes.getCodec(), wrapper))
                    .amount(tag.getOr(v1205 ? "count" : "Count", NbtCodecs.INT, 1, wrapper))
                    .nbt(tag.getCompoundTagOrNull("tag"))
                    .build();
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, ItemStack value) throws NbtCodecException {
            boolean v1205 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5);
            tag.setTag("id", new NBTString(value.getType().getName().toString()));
            if (!v1205 || value.getAmount() != 1) {
                tag.setTag(v1205 ? "count" : "Count", new NBTInt(value.getAmount()));
            }
            if (!v1205 && value.getNBT() != null) {
                tag.setTag("tag", value.getNBT());
            }
        }
    };
    public static final NbtCodec<ItemStack> CODEC = MAP_CODEC.codec();

    private ItemStackCodec() {
    }
}
