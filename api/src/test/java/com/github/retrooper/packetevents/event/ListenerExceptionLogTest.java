package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.netty.channel.embedded.EmbeddedChannel;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListenerExceptionLogTest {

    private static final String TRACE = "PacketEvents caught an unhandled exception while calling your listener.";
    private static final long SECOND = TimeUnit.SECONDS.toNanos(1);

    private final TestAPI api = new TestAPI();
    private final EmbeddedChannel channel = new EmbeddedChannel();
    // System.nanoTime() can return anything, so this starts right before it overflows
    private long now = Long.MAX_VALUE - 10;
    private RuntimeException exception = new IllegalStateException("test");
    private final EventContainer container = new EventContainer() {
        @Override
        void logException(Exception t, long now) {
            super.logException(t, ListenerExceptionLogTest.this.now);
        }
    };

    @BeforeEach
    public void setup() {
        PacketEvents.setAPI(this.api);
        this.container.registerListener(new ThrowingListener());
    }

    @AfterEach
    public void teardown() {
        PacketEvents.setAPI(null);
        this.channel.close();
    }

    @Test
    public void testFewInARow() {
        // logged with their stack traces, like they always were
        for (int i = 0; i < 10; i++) {
            this.call();
        }

        assertEquals(Collections.nCopies(10, TRACE), this.api.messages);
        assertEquals(Collections.nCopies(10, this.exception), this.api.errors);
    }

    @Test
    public void testFlood() {
        // 500 a second for three seconds
        for (int i = 0; i < 1500; i++) {
            this.call();
            this.now += SECOND / 500;
        }
        List<String> expected = new ArrayList<>(Collections.nCopies(10, TRACE));
        // then one a second, which says how many were left out before it
        expected.add(skipped(490));
        expected.add(skipped(499));
        assertEquals(expected, this.api.messages);

        // two and a half seconds later three can be logged, the first one reports the rest of the flood
        this.now += 5 * SECOND / 2;
        for (int i = 0; i < 5; i++) {
            this.call();
        }
        expected.add(skipped(499));
        expected.add(TRACE);
        expected.add(TRACE);
        assertEquals(expected, this.api.messages);

        // no more than 10 in a row again, however long it was quiet
        this.now += 60 * SECOND;
        for (int i = 0; i < 12; i++) {
            this.call();
        }
        expected.add(skipped(2));
        expected.addAll(Collections.nCopies(9, TRACE));
        assertEquals(expected, this.api.messages);
        assertEquals(Collections.nCopies(expected.size(), this.exception), this.api.errors);
    }

    @Test
    public void testDebug() {
        this.api.getSettings().debug(true);
        for (int i = 0; i < 50; i++) {
            this.call();
        }

        assertEquals(Collections.nCopies(50, TRACE), this.api.messages);
        assertEquals(Collections.nCopies(50, this.exception), this.api.errors);
    }

    @Test
    public void testInvalidHandshake() {
        this.exception = new InvalidHandshakeException();
        this.call();
        this.exception = new RuntimeException(new InvalidHandshakeException());
        this.call();

        assertEquals(Collections.emptyList(), this.api.messages);
    }

    private static String skipped(int count) {
        return TRACE + " (" + count + " more were not logged)";
    }

    private void call() {
        ServerVersion version = ServerVersion.getLatest();
        User user = new User(this.channel, ConnectionState.PLAY, version.toClientVersion(),
                new UserProfile(UUID.randomUUID(), "tester"));
        this.container.callEvent(new PacketReceiveEvent(0, PacketType.Play.Client.PLAYER_POSITION, version,
                this.channel, user, null, null));
    }

    private final class ThrowingListener extends PacketListenerAbstract {

        @Override
        public void onPacketReceive(PacketReceiveEvent event) {
            throw ListenerExceptionLogTest.this.exception;
        }
    }

    // the api of the other tests doesn't let us read what it logs
    private static final class TestAPI extends PacketEventsAPI<Object> {

        private final List<String> messages = new ArrayList<>();
        private final List<Throwable> errors = new ArrayList<>();
        private final NettyManager nettyManager = new NettyManagerImpl();
        private final LogManager logManager = new LogManager(this) {
            @Override
            protected void log(Level level, NamedTextColor color, String message, Throwable error) {
                TestAPI.this.messages.add(message);
                TestAPI.this.errors.add(error);
            }

            @Override
            public void log(Level level, ComponentLike component, Throwable error) {
            }
        };

        @Override public LogManager getLogManager() { return this.logManager; }
        @Override public NettyManager getNettyManager() { return this.nettyManager; }
        // none of these are used
        @Override public ServerManager getServerManager() { return null; }
        @Override public ChannelInjector getInjector() { return null; }
        @Override public ProtocolManager getProtocolManager() { return null; }
        @Override public PlayerManager getPlayerManager() { return null; }
        @Override public Object getPlugin() { return this; }
        @Override public boolean isLoaded() { return true; }
        @Override public void init() { }
        @Override public boolean isInitialized() { return true; }
        @Override public boolean isTerminated() { return false; }
    }
}
