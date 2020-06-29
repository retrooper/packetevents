package io.github.retrooper.packetevents.enums;

public enum SystemOS {
    WINDOWS, MACOS, LINUX, OTHER;

    private static SystemOS value;

    private static SystemOS getOS() {
        final String os = System.getProperty("os.name");
        for (final String osName : getOperatingSystemNames()) {
            if(os.toLowerCase().contains(osName.toLowerCase())) {
                return SystemOS.valueOf(osName);
            }
        }
        return OTHER;
    }

    public static SystemOS getOperatingSystem() {
        if(value == null) {
            value = getOS();
        }
        return value;
    }

    public static String[] getOperatingSystemNames() {
        final String[] arr = new String[values().length - 1];
        for (int i = 0; i < values().length - 1; i++) {
            arr[i] = values()[i].name();
        }
        return arr;
    }
}
