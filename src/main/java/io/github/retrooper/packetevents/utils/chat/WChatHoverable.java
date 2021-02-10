package io.github.retrooper.packetevents.utils.chat;

import com.google.common.collect.Maps;

import java.util.Map;

public class WChatHoverable {

    private final WChatHoverable.EnumHoverAction a;
    private final WIChatBaseComponent b;

    public WChatHoverable(WChatHoverable.EnumHoverAction var1, WIChatBaseComponent var2) {
        this.a = var1;
        this.b = var2;
    }

    public WChatHoverable.EnumHoverAction a() {
        return this.a;
    }

    public WIChatBaseComponent b() {
        return this.b;
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            WChatHoverable var2 = (WChatHoverable)var1;
            if (this.a != var2.a) {
                return false;
            } else {
                if (this.b != null) {
                    if (!this.b.equals(var2.b)) {
                        return false;
                    }
                } else if (var2.b != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "HoverEvent{action=" + this.a + ", value='" + this.b + '\'' + '}';
    }

    public int hashCode() {
        int var1 = this.a.hashCode();
        var1 = 31 * var1 + (this.b != null ? this.b.hashCode() : 0);
        return var1;
    }

    public static enum EnumHoverAction {
        SHOW_TEXT("show_text", true),
        SHOW_ITEM("show_item", true),
        SHOW_ENTITY("show_entity", true);

        private static final Map<String, WChatHoverable.EnumHoverAction> d = Maps.newHashMap();
        private final boolean e;
        private final String f;

        private EnumHoverAction(String var3, boolean var4) {
            this.f = var3;
            this.e = var4;
        }

        public boolean a() {
            return this.e;
        }

        public String b() {
            return this.f;
        }

        public static WChatHoverable.EnumHoverAction a(String var0) {
            return (WChatHoverable.EnumHoverAction)d.get(var0);
        }

        static {
            WChatHoverable.EnumHoverAction[] var0 = values();
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                WChatHoverable.EnumHoverAction var3 = var0[var2];
                d.put(var3.b(), var3);
            }

        }
    }
}
