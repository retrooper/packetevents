package io.github.retrooper.packetevents.utils.chat;

import java.util.List;

// todo: WChatComponentKeybind WChatComponentScore WChatComponentSelector WChatMessage
// todo: Rename everything to make sense
// todo: ChatSerializer
// todo: google bad for some reason (don't import from com.google)
public interface WIChatBaseComponent extends Iterable<WIChatBaseComponent> {
    WIChatBaseComponent setChatModifier(WChatModifier var1);

    WChatModifier getChatModifier();

    WIChatBaseComponent a(String var1);

    WIChatBaseComponent addSibling(WIChatBaseComponent var1);

    String getText();

    String toPlainText();

    List<WIChatBaseComponent> a();

    WIChatBaseComponent f();
}
