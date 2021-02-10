package io.github.retrooper.packetevents.utils.chat;

import java.util.Iterator;

public class WChatComponentText extends WChatBaseComponent {
    private final String b;

    public WChatComponentText(String var1) {
        this.b = var1;
    }

    public String g() {
        return this.b;
    }

    public String getText() {
        return this.b;
    }

    public WChatComponentText f() {
        WChatComponentText var1 = new WChatComponentText(this.b);
        var1.setChatModifier(this.getChatModifier().clone());
        Iterator var2 = this.a().iterator();

        while(var2.hasNext()) {
            WIChatBaseComponent var3 = (WIChatBaseComponent)var2.next();
            var1.addSibling(var3.f());
        }

        return var1;
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof WChatComponentText)) {
            return false;
        } else {
            WChatComponentText var2 = (WChatComponentText)var1;
            return this.b.equals(var2.g()) && super.equals(var1);
        }
    }

    public String toString() {
        return "TextComponent{text='" + this.b + '\'' + ", siblings=" + this.a + ", style=" + this.getChatModifier() + '}';
    }
}
