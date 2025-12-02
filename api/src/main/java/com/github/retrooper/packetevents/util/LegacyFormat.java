package com.github.retrooper.packetevents.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class LegacyFormat {
    private LegacyFormat() {
    }

    public static String trimLegacyFormat(String text, int length) {
        if (text.length() <= length) {
            return text;
        }

        if (text.charAt(length - 1) == LegacyComponentSerializer.SECTION_CHAR) {
            return text.substring(0, length - 1);
        }

        return text.substring(0, length);
    }
}
