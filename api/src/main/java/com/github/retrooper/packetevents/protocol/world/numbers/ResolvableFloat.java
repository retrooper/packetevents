package com.github.retrooper.packetevents.protocol.world.numbers;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ResolvableFloat {

    static Constant constantFloat(float value) {
        return new Constant(value);
    }

    static Reference referenceFloat(ResourceLocation location) {
        return new Reference(location);
    }

    static ResolvableFloat read(PacketWrapper<?> wrapper) {
        return wrapper.readBoolean() ? Constant.read(wrapper) : Reference.read(wrapper);
    }

    static void write(PacketWrapper<?> wrapper, ResolvableFloat resolvableInt) {
        if (resolvableInt instanceof Constant) {
            wrapper.writeBoolean(true);
            Constant.write(wrapper, (Constant) resolvableInt);
        } else if (resolvableInt instanceof Reference) {
            wrapper.writeBoolean(false);
            Reference.write(wrapper, (Reference) resolvableInt);
        } else {
            throw new UnsupportedOperationException("Unsupported ResolvableFloat implementation: " + resolvableInt);
        }
    }

    final class Constant implements ResolvableFloat {

        private final float value;

        public Constant(float value) {
            this.value = value;
        }

        public static Constant read(PacketWrapper<?> wrapper) {
            float v = wrapper.readFloat();
            return new Constant(v);
        }

        public static void write(PacketWrapper<?> wrapper, Constant constant) {
            wrapper.writeFloat(constant.value);
        }

        public float getValue() {
            return this.value;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) return false;
            Constant constant = (Constant) obj;
            return Float.compare(constant.value, this.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }
    }

    final class Reference implements ResolvableFloat {

        private final ResourceLocation location;

        public Reference(ResourceLocation location) {
            this.location = location;
        }

        public static Reference read(PacketWrapper<?> wrapper) {
            ResourceLocation location = ResourceLocation.read(wrapper);
            return new Reference(location);
        }

        public static void write(PacketWrapper<?> wrapper, Reference reference) {
            ResourceLocation.write(wrapper, reference.location);
        }

        public ResourceLocation getLocation() {
            return this.location;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) return false;
            Reference reference = (Reference) obj;
            return this.location.equals(reference.location);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.location);
        }
    }
}
