/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.adventure.*;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.gson.BackwardCompatUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomClickEventTest extends BaseDummyAPITest {

    @Test
    @DisplayName("Test custom click event payload preservation during serialization")
    public void testCustomClickEventPayloadSerialization() {
        // Only test if Adventure 4.22.0 is available
        if (!BackwardCompatUtil.IS_4_22_0_OR_NEWER) {
            LOGGER.info("Skipping custom click event test - Adventure 4.22.0 required");
            return;
        }

        // Create a component with custom click event with payload
        NBTCompound payloadData = new NBTCompound();
        payloadData.setTag("test_key", new NBTString("test_value"));
        payloadData.setTag("number", new NBTInt(42));

        Component originalComponent = Component.text("Click me")
                .clickEvent(ClickEvent.custom(
                        Key.key("minecraft", "test_event"),
                        new NbtTagHolder(payloadData)
                ));

        // Serialize to NBT
        AdventureNBTSerializer serializer = AdventureSerializer.serializer(ClientVersion.V_1_21_6).nbt();
        PacketWrapper < ? > wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_6);
        NBT serialized = serializer.serialize(originalComponent, wrapper);

        // Verify the serialized NBT contains the payload
        assertInstanceOf(NBTCompound.class, serialized);
        NBTCompound compound = (NBTCompound) serialized;

        // Check click_event exists
        NBT clickEventTag = compound.getTagOrNull("click_event");
        assertNotNull(clickEventTag, "click_event should be present");
        assertInstanceOf(NBTCompound.class, clickEventTag);

        NBTCompound clickEventCompound = (NBTCompound) clickEventTag;

        // Check action is "custom"
        NBT actionTag = clickEventCompound.getTagOrNull("action");
        assertNotNull(actionTag, "action should be present");
        assertInstanceOf(NBTString.class, actionTag);
        assertEquals("custom", ((NBTString) actionTag).getValue());

        // Check id is present
        NBT idTag = clickEventCompound.getTagOrNull("id");
        assertNotNull(idTag, "id should be present");

        // Check payload is present and contains our data
        NBT payloadTag = clickEventCompound.getTagOrNull("payload");
        assertNotNull(payloadTag, "payload should be present and not removed");
        assertInstanceOf(NBTCompound.class, payloadTag);

        NBTCompound payloadCompound = (NBTCompound) payloadTag;
        NBT testKeyTag = payloadCompound.getTagOrNull("test_key");
        assertNotNull(testKeyTag, "payload should contain test_key");
        assertInstanceOf(NBTString.class, testKeyTag);
        assertEquals("test_value", ((NBTString) testKeyTag).getValue());

        // Deserialize back to Component
        Component deserialized = serializer.deserialize(serialized, wrapper);

        // Verify the deserialized component has the click event
        assertNotNull(deserialized.clickEvent(), "Click event should be present after deserialization");
        assertEquals(ClickEvent.Action.CUSTOM, deserialized.clickEvent().action());

        // Verify the payload is preserved
        ClickEvent.Payload payload = deserialized.clickEvent().payload();
        assertInstanceOf(ClickEvent.Payload.Custom.class, payload);
        ClickEvent.Payload.Custom customPayload = (ClickEvent.Payload.Custom) payload;
        assertEquals(Key.key("minecraft", "test_event"), customPayload.key());

        // Verify the NBT data in payload
        assertInstanceOf(NbtTagHolder.class, customPayload.nbt());
        NbtTagHolder nbtHolder = (NbtTagHolder) customPayload.nbt();
        NBT deserializedPayload = nbtHolder.getTag();
        assertInstanceOf(NBTCompound.class, deserializedPayload);

        NBTCompound deserializedPayloadCompound = (NBTCompound) deserializedPayload;
        NBT deserializedTestKey = deserializedPayloadCompound.getTagOrNull("test_key");
        assertNotNull(deserializedTestKey, "Deserialized payload should contain test_key");
        assertInstanceOf(NBTString.class, deserializedTestKey);
        assertEquals("test_value", ((NBTString) deserializedTestKey).getValue());

        NBT deserializedNumber = deserializedPayloadCompound.getTagOrNull("number");
        assertNotNull(deserializedNumber, "Deserialized payload should contain number");
        assertInstanceOf(NBTInt.class, deserializedNumber);
        assertEquals(42, ((NBTInt) deserializedNumber).getAsInt());
    }

    @Test
    @DisplayName("Test custom click event without payload")
    public void testCustomClickEventWithoutPayload() {
        // Only test if Adventure 4.22.0 is available
        if (!BackwardCompatUtil.IS_4_22_0_OR_NEWER) {
            LOGGER.info("Skipping custom click event test - Adventure 4.22.0 required");
            return;
        }

        // Create a component with custom click event without payload (empty NBT)
        Component originalComponent = Component.text("Click me")
                .clickEvent(ClickEvent.custom(
                        Key.key("minecraft", "test_event"),
                        new NbtTagHolder(
                                NBTEnd.INSTANCE
                        )
                ));

        // Serialize to NBT
        AdventureNBTSerializer serializer = AdventureSerializer.serializer(ClientVersion.V_1_21_6).nbt();
        PacketWrapper < ? > wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_6);
        NBT serialized = serializer.serialize(originalComponent, wrapper);

        // Verify the serialized NBT
        assertInstanceOf(NBTCompound.class, serialized);
        NBTCompound compound = (NBTCompound) serialized;

        // Check click_event exists
        NBT clickEventTag = compound.getTagOrNull("click_event");
        assertNotNull(clickEventTag, "click_event should be present");

        NBTCompound clickEventCompound = (NBTCompound) clickEventTag;

        // Check payload is NOT present (since it's empty)
        NBT payloadTag = clickEventCompound.getTagOrNull("payload");
        assertNull(payloadTag, "payload should not be present when empty");

        // Deserialize back to Component
        Component deserialized = serializer.deserialize(serialized, wrapper);

        // Verify the deserialized component has the click event
        assertNotNull(deserialized.clickEvent(), "Click event should be present after deserialization");
        assertEquals(ClickEvent.Action.CUSTOM, deserialized.clickEvent().action());
    }
}