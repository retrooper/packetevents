package io.github.retrooper.packetevents.utils.turbospigot;

public class TurboSpigotUtils {
    private static boolean turboSpigot;

    static {
        try {
            Class.forName("io.github.retrooper.turbospigot.command.KnockbackCommand");
            turboSpigot = true;
        } catch (ClassNotFoundException e) {
            turboSpigot = false;
        }
    }

    public static boolean isTurboSpigot() {
        return turboSpigot;
    }


}
