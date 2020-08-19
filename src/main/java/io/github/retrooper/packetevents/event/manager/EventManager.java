package io.github.retrooper.packetevents.event.manager;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EventManager {
    private final HashMap<PacketListener, List<Method>> registeredMethods = new HashMap<PacketListener, List<Method>>();

    public void callEvent(final PacketEvent e) {
        for (final PacketListener listener : registeredMethods.keySet()) {
            //Annotated methods
            final List<Method> methods = registeredMethods.get(listener);
            for (final Method method : methods) {
                final Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PacketEvent.class)
                        || parameterType.isInstance(e)) {
                    final PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                    final Runnable invokeMethod = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(listener, e);
                            } catch (IllegalAccessException | InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    };

                    switch (annotation.synchronization()) {
                        case FORCE_ASYNC:
                            Bukkit.getScheduler().runTaskAsynchronously(PacketEvents.getPlugin(), invokeMethod);
                            break;
                        case FORCE_SYNC:
                            Bukkit.getScheduler().runTask(PacketEvents.getPlugin(), invokeMethod);
                            break;
                        default:
                            invokeMethod.run();
                            break;
                    }
                }
            }
        }
    }

    public void registerListener(final PacketListener e) {
        final List<Method> methods = new ArrayList<Method>();
        for (final Method m : e.getClass().getMethods()) {
            if (m.isAnnotationPresent(PacketHandler.class)
                    && m.getParameterTypes().length == 1) {
                methods.add(m);
            }
        }
        if (!methods.isEmpty()) {
            registeredMethods.put(e, methods);
        }
    }

    public void registerListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            registerListener(listener);
        }
    }

    public void unregisterListener(final PacketListener e) {
        registeredMethods.remove(e);
    }

    public void unregisterListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            unregisterListener(listener);
        }
    }

    public void unregisterAllListeners() {
        registeredMethods.clear();
    }

    public boolean isRegistered(final PacketListener listener) {
        return registeredMethods.containsKey(listener);
    }
}
