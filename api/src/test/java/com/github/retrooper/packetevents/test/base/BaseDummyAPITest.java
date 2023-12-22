package com.github.retrooper.packetevents.test.base;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.retrooper.packetevents.PacketEvents;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;

public abstract class BaseDummyAPITest {

    public static final Logger LOGGER = TestPacketEventsBuilder.LOGGER;

    private ServerMock server;
    private MockPlugin plugin;

    @BeforeEach
    public void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin("packetevents");
        PacketEvents.setAPI(TestPacketEventsBuilder.build(plugin));
    }

    @AfterEach
    public void teardown() {
        MockBukkit.unmock();
        PacketEvents.setAPI(null);
    }
}
