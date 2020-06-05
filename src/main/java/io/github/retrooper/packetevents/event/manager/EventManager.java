package io.github.retrooper.packetevents.event.manager;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.PacketHandler;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

    private static HashMap<PacketListener, List<Method>> registeredMethods = new HashMap<PacketListener, List<Method>>();

    public static void callEvent(final PacketEvent e) {
        for (final PacketListener listener : registeredMethods.keySet()) {
            //Annotated methods
            final List<Method> methods = registeredMethods.get(listener);
            for (final Method method : methods) {
                if (method.getParameterTypes()[0].equals(PacketEvent.class)
                        || method.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
                    try {
                        method.invoke(listener, e);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }


    public static void registerListener(final PacketListener e) {
        final List<Method> methods = new ArrayList<Method>();
        for (final Method m : e.getClass().getMethods()) {
            if (m.isAnnotationPresent(PacketHandler.class)) {
                methods.add(m);
            }
        }
        registeredMethods.put(e, methods);
    }

    public static void unregisterListener(final PacketListener e) {
        registeredMethods.remove(e);
    }

    public static void unregisterAllListeners() {
        registeredMethods.clear();
    }

}
