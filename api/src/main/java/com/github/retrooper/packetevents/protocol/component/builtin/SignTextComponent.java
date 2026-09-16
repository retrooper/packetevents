package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

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
}
