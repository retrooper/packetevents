package io.github.retrooper.packetevents.utils.chat;

import com.google.common.collect.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public enum WEnumChatFormat {

    BLACK("BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", '1', 1),
    DARK_GREEN("DARK_GREEN", '2', 2),
    DARK_AQUA("DARK_AQUA", '3', 3),
    DARK_RED("DARK_RED", '4', 4),
    DARK_PURPLE("DARK_PURPLE", '5', 5),
    GOLD("GOLD", '6', 6),
    GRAY("GRAY", '7', 7),
    DARK_GRAY("DARK_GRAY", '8', 8),
    BLUE("BLUE", '9', 9),
    GREEN("GREEN", 'a', 10),
    AQUA("AQUA", 'b', 11),
    RED("RED", 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
    YELLOW("YELLOW", 'e', 14),
    WHITE("WHITE", 'f', 15),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1);

    private static final Map<String, WEnumChatFormat> w = Maps.newHashMap();
    private static final Pattern x = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private final String y;
    public final char character;
    private final boolean A;
    private final String B;
    private final int C;

    private static String c(String var0) {
        return var0.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    WEnumChatFormat(String var3, char var4, int var5) {
        this(var3, var4, false, var5);
    }

    WEnumChatFormat(String var3, char var4, boolean var5) {
        this(var3, var4, var5, -1);
    }

    WEnumChatFormat(String var3, char var4, boolean var5, int var6) {
        this.y = var3;
        this.character = var4;
        this.A = var5;
        this.C = var6;
        this.B = "§" + var4;
    }

    public int b() {
        return this.C;
    }

    public boolean isFormat() {
        return this.A;
    }

    public boolean d() {
        return !this.A && this != RESET;
    }

    public String e() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.B;
    }

    @Nullable
    public static String a(@Nullable String var0) {
        return var0 == null ? null : x.matcher(var0).replaceAll("");
    }

    @Nullable
    public static WEnumChatFormat b(@Nullable String var0) {
        return var0 == null ? null : w.get(c(var0));
    }

    @Nullable
    public static WEnumChatFormat a(int var0) {
        if (var0 < 0) {
            return RESET;
        } else {
            WEnumChatFormat[] var1 = values();

            for (WEnumChatFormat var4 : var1) {
                if (var4.b() == var0) {
                    return var4;
                }
            }

            return null;
        }
    }

    public static Collection<String> a(boolean var0, boolean var1) {
        ArrayList<String> var2 = Lists.newArrayList();
        WEnumChatFormat[] var3 = values();

        for (WEnumChatFormat var6 : var3) {
            if ((!var6.d() || var0) && (!var6.isFormat() || var1)) {
                var2.add(var6.e());
            }
        }

        return var2;
    }

    static {
        WEnumChatFormat[] var0 = values();
        int var1 = var0.length;

        for (WEnumChatFormat var3 : var0) {
            w.put(c(var3.y), var3);
        }
    }
}
