package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class SignTextComponent {

    private final List<Component> messages;
    private final @Nullable List<Component> filteredMessages;
    private final DyeColor color;
    private final boolean glowing;

    public SignTextComponent(
            List<Component> messages, @Nullable List<Component> filteredMessages,
            DyeColor color, boolean glowing
    ) {
        this.messages = Collections.unmodifiableList(messages);
        this.filteredMessages = filteredMessages != null ? Collections.unmodifiableList(filteredMessages) : null;
        this.color = color;
        this.glowing = glowing;
    }

    public static SignTextComponent read(PacketWrapper<?> wrapper) {
        List<Component> messages = wrapper.readFixedList(PacketWrapper::readComponent, 4);
        List<Component> filteredMessages = wrapper.readOptional(ew ->
                ew.readFixedList(PacketWrapper::readComponent, 4));
        DyeColor color = DyeColor.read(wrapper);
        boolean glowing = wrapper.readBoolean();
        return new SignTextComponent(messages, filteredMessages, color, glowing);
    }

    public static void write(PacketWrapper<?> wrapper, SignTextComponent component) {
        wrapper.writeFixedList(component.messages, PacketWrapper::writeComponent);
        wrapper.writeOptional(component.filteredMessages, (ew, v) ->
                ew.writeFixedList(v, PacketWrapper::writeComponent));
        DyeColor.write(wrapper, component.color);
        wrapper.writeBoolean(component.glowing);
    }

    public List<Component> getMessages() {
        return this.messages;
    }

    public @Nullable List<Component> getFilteredMessages() {
        return this.filteredMessages;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SignTextComponent)) return false;
        SignTextComponent that = (SignTextComponent) obj;
        if (this.glowing != that.glowing) return false;
        if (!this.color.equals(that.color)) return false;
        if (!this.messages.equals(that.messages)) return false;
        return Objects.equals(this.filteredMessages, that.filteredMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.messages, this.filteredMessages, this.color, this.glowing);
    }
}
