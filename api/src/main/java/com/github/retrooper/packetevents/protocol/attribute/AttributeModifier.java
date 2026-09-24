package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@NullMarked
public final class AttributeModifier {

    public static final NbtMapCodec<AttributeModifier> MAP_CODEC = new NbtMapCodec<AttributeModifier>() {
        @Override
        public AttributeModifier decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
                throw new NbtCodecException(wrapper.getServerVersion() + " is not supported");
            }
            ResourceLocation id = tag.getOrThrow("id", ResourceLocation.CODEC, wrapper);
            double amount = tag.getNumberTagValueOrThrow("amount").doubleValue();
            AttributeOperation operation = tag.getOrThrow("operation", AttributeOperation.CODEC, wrapper);
            return new AttributeModifier(id, amount, operation);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, AttributeModifier value) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
                throw new NbtCodecException(wrapper.getServerVersion() + " is not supported");
            }
            tag.set("id", value.id, ResourceLocation.CODEC, wrapper);
            tag.setTag("amount", new NBTDouble(value.amount));
            tag.set("operation", value.operation, AttributeOperation.CODEC, wrapper);
        }
    };

    /**
     * @versions -1.20.6
     */
    @ApiStatus.Obsolete
    private final UUID uniqueId;
    /**
     * @versions 1.21+
     */
    private final ResourceLocation id;
    private final double amount;
    private final AttributeOperation operation;

    /**
     * @versions -1.20.6
     */
    @ApiStatus.Obsolete
    public AttributeModifier(UUID uniqueId, double amount, AttributeOperation operation) {
        this.uniqueId = uniqueId;
        this.id = new ResourceLocation(uniqueId.toString());
        this.amount = amount;
        this.operation = operation;
    }

    /**
     * @versions 1.21+
     */
    public AttributeModifier(ResourceLocation id, double amount, AttributeOperation operation) {
        this.uniqueId = WrapperPlayServerUpdateAttributes.PropertyModifier.generateSemiUniqueId(id);
        this.id = id;
        this.amount = amount;
        this.operation = operation;
    }

    public static AttributeModifier read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            UUID uniqueId = wrapper.readUUID();
            double amount = wrapper.readDouble();
            AttributeOperation operation = wrapper.readEnum(AttributeOperation.values());
            return new AttributeModifier(uniqueId, amount, operation);
        }
        ResourceLocation id = ResourceLocation.read(wrapper);
        double amount = wrapper.readDouble();
        AttributeOperation operation = wrapper.readEnum(AttributeOperation.values());
        return new AttributeModifier(id, amount, operation);
    }

    public static void write(PacketWrapper<?> wrapper, AttributeModifier modifier) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            wrapper.writeUUID(modifier.uniqueId);
        } else {
            ResourceLocation.write(wrapper, modifier.id);
        }
        wrapper.writeDouble(modifier.amount);
        wrapper.writeEnum(modifier.operation);
    }

    /**
     * @versions -1.20.6
     */
    @ApiStatus.Obsolete
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    /**
     * @versions 1.12+
     */
    public ResourceLocation getId() {
        return this.id;
    }

    public double getAmount() {
        return this.amount;
    }

    public AttributeOperation getOperation() {
        return this.operation;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        AttributeModifier that = (AttributeModifier) obj;
        if (Double.compare(that.amount, this.amount) != 0) return false;
        if (!this.id.equals(that.id)) return false;
        return this.operation == that.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.amount, this.operation);
    }
}
