package com.github.retrooper.packetevents.test.event;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.event.inheritable.GlobalEventManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InheritableEventManagerTest {
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
    void shouldDestroyChildrenAfterGC() throws Throwable {
        final GlobalEventManager rootEventManager = new GlobalEventManager();
        Object children = new Object();

        rootEventManager.getChildren(children);
        rootEventManager.getChildren(children);
        assertEquals(1, rootEventManager.childrenCount());

        children = null;
        System.gc();

        TimeUnit.SECONDS.sleep(1);

        assertEquals(0, rootEventManager.childrenCount());
    }

    @Test
    void shouldCallBothListeners() {
        final GlobalEventManager rootEventManager = new GlobalEventManager();
        final EventManager children = rootEventManager.getChildren(new Object());
        final List<String> calls = new ArrayList<>();

        children.registerListener(mockListener(() -> calls.add("children")));
        rootEventManager.registerListener(mockListener(() -> calls.add("root")));

        children.callEvent(new UserConnectEvent(null));

        assertEquals(Arrays.asList("children", "root"), calls);
    }
}
