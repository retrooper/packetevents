package com.github.retrooper.packetevents.test.event;

import com.github.retrooper.packetevents.event.InheritableEventManager;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListenerStoreTest {
    static PacketListenerCommon mockListener() {
        return mockListener(PacketListenerPriority.NORMAL, () -> {});
    }

    static PacketListenerCommon mockListener(Runnable onAccept) {
        return mockListener(PacketListenerPriority.NORMAL, onAccept);
    }

    static PacketListenerCommon mockListener(PacketListenerPriority priority, Runnable onAccept) {
        return new PacketListenerCommon(priority) {
            @Override
            public void onUserConnect(UserConnectEvent event) {
                super.onUserConnect(event);
                onAccept.run();
            }
        };
    }

    @Test
    void testSuccessAdd() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        final PacketListenerCommon packetListenerCommon = mockListener(() -> {});
        listenerStore.add(packetListenerCommon);

        assertEquals(1, listenerStore.get().length);
        assertSame(listenerStore.get()[0], packetListenerCommon);
    }

    @Test
    void testSuccessRemove() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        final PacketListenerCommon packetListenerCommon = mockListener(() -> {});
        listenerStore.add(packetListenerCommon);
        listenerStore.remove(packetListenerCommon);

        assertEquals(0, listenerStore.get().length);
    }

    @Test
    void shouldNotAddDuplicateListener() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        listenerStore.add(mockListener());

        final PacketListenerCommon packetListenerCommon = mockListener(() -> {});
        listenerStore.add(packetListenerCommon);
        listenerStore.add(mockListener());
        listenerStore.add(packetListenerCommon);

        assertEquals(3, listenerStore.get().length);
    }

    @Test
    void shouldRunInTheRightOrderByPriority() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        final List<PacketListenerPriority> result = new ArrayList<>();
        for (final PacketListenerPriority priority : PacketListenerPriority.values()) {
            final PacketListenerCommon packetListenerCommon = mockListener(priority, () -> result.add(priority));
            listenerStore.add(packetListenerCommon);
        }

        listenerStore.call(new UserConnectEvent(null));
        assertEquals(Arrays.asList(PacketListenerPriority.values()), result);
    }

    @Test
    void shouldRunInTheRightOrderByRegistration() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        final List<Integer> executionOrder = new ArrayList<>();

        listenerStore.add(mockListener(PacketListenerPriority.NORMAL, () -> executionOrder.add(1)));
        listenerStore.add(mockListener(PacketListenerPriority.NORMAL, () -> executionOrder.add(2)));
        listenerStore.add(mockListener(PacketListenerPriority.NORMAL, () -> executionOrder.add(3)));

        listenerStore.call(new UserConnectEvent(null));

        assertEquals(Arrays.asList(1, 2, 3), executionOrder);
    }

    @Test
    void shouldClearSuccessfully() {
        final InheritableEventManager.ListenerStore listenerStore = new InheritableEventManager.ListenerStore();
        listenerStore.add(mockListener());
        listenerStore.add(mockListener());
        listenerStore.add(mockListener());

        listenerStore.clear();
        assertNull(listenerStore.get());
    }
}
