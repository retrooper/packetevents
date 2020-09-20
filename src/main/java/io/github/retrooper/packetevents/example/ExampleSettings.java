package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.settings.PacketEventsSettings;

public class ExampleSettings extends PacketEventsSettings {

    public ExampleSettings() {
        //Unique Packet Events Identifier
        super("MyPlugin");

        //Configure Settings
        setInjectAsync(true);
        setUseProtocolLibIfAvailable(false);

        //Easily Register Events
        registerEvent(new ListenerExample());
    }
}
