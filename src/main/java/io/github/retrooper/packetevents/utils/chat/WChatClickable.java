package io.github.retrooper.packetevents.utils.chat;

import com.google.common.collect.Maps;

import java.util.Map;

public class WChatClickable {
    private final WChatClickable.EnumClickAction a;
    private final String b;

    public WChatClickable(WChatClickable.EnumClickAction var1, String var2) {
        this.a = var1;
        this.b = var2;
    }

    public WChatClickable.EnumClickAction a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            WChatClickable var2 = (WChatClickable)var1;
            if (this.a != var2.a) {
                return false;
            } else {
                if (this.b != null) {
                    return this.b.equals(var2.b);
                } else return var2.b == null;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "ClickEvent{action=" + this.a + ", value='" + this.b + '\'' + '}';
    }

    public int hashCode() {
        int var1 = this.a.hashCode();
        var1 = 31 * var1 + (this.b != null ? this.b.hashCode() : 0);
        return var1;
    }

    public enum EnumClickAction {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, WChatClickable.EnumClickAction> f = Maps.newHashMap();
        private final boolean g;
        private final String h;

        EnumClickAction(String var3, boolean var4) {
            this.h = var3;
            this.g = var4;
        }

        public boolean a() {
            return this.g;
        }

        public String b() {
            return this.h;
        }

        public static WChatClickable.EnumClickAction a(String var0) {
            return f.get(var0);
        }

        static {
            WChatClickable.EnumClickAction[] var0 = values();

            for (EnumClickAction var3 : var0) {
                f.put(var3.b(), var3);
            }

        }
    }
}
