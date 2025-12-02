package com.github.retrooper.packetevents.test.base;

import com.github.retrooper.packetevents.PacketEvents;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.plugin.PluginMock;
import org.slf4j.Logger;

public abstract class BaseDummyAPITest {

    public static final Logger LOGGER = TestPacketEventsBuilder.LOGGER;

    private ServerMock server;
    private PluginMock plugin;

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
