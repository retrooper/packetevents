package io.github.retrooper.packetevents.utils.chat;

import com.google.common.base.Function;
import com.google.common.collect.*;

import javax.annotation.Nullable;
import java.util.*;

public abstract class WChatBaseComponent implements WIChatBaseComponent {

    protected List<WIChatBaseComponent> a = Lists.newArrayList();
    private WChatModifier b;

    public WChatBaseComponent() {
    }

    public WIChatBaseComponent addSibling(WIChatBaseComponent ichatbasecomponent) {
        ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        this.a.add(ichatbasecomponent);
        return this;
    }

    public List<WIChatBaseComponent> a() {
        return this.a;
    }

    public WIChatBaseComponent a(String s) {
        return this.addSibling(new WChatComponentText(s));
    }

    public WIChatBaseComponent setChatModifier(WChatModifier chatmodifier) {
        this.b = chatmodifier;
        Iterator iterator = this.a.iterator();

        while(iterator.hasNext()) {
            WIChatBaseComponent ichatbasecomponent = (WIChatBaseComponent)iterator.next();
            ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        }

        return this;
    }

    public WChatModifier getChatModifier() {
        if (this.b == null) {
            this.b = new WChatModifier();
            Iterator iterator = this.a.iterator();

            while(iterator.hasNext()) {
                WIChatBaseComponent ichatbasecomponent = (WIChatBaseComponent)iterator.next();
                ichatbasecomponent.getChatModifier().setChatModifier(this.b);
            }
        }

        return this.b;
    }

    public Iterator<WIChatBaseComponent> iterator() {
        return Iterators.concat(Iterators.forArray(new WChatBaseComponent[]{this}), a((Iterable)this.a));
    }

    public final String toPlainText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        while(iterator.hasNext()) {
            WIChatBaseComponent ichatbasecomponent = (WIChatBaseComponent)iterator.next();
            stringbuilder.append(ichatbasecomponent.getText());
        }

        return stringbuilder.toString();
    }

    public static Iterator<WIChatBaseComponent> a(Iterable<WIChatBaseComponent> iterable) {
        Iterator iterator = Iterators.concat(Iterators.transform(iterable.iterator(), new Function() {
            public Iterator<WIChatBaseComponent> a(@Nullable WIChatBaseComponent ichatbasecomponent) {
                return ichatbasecomponent.iterator();
            }

            public Object apply(@Nullable Object object) {
                return this.a((WIChatBaseComponent)object);
            }
        }));
        iterator = Iterators.transform(iterator, new Function() {
            public WIChatBaseComponent a(@Nullable WIChatBaseComponent ichatbasecomponent) {
                WIChatBaseComponent ichatbasecomponent1 = ichatbasecomponent.f();
                ichatbasecomponent1.setChatModifier(ichatbasecomponent1.getChatModifier().n());
                return ichatbasecomponent1;
            }

            public Object apply(@Nullable Object object) {
                return this.a((WIChatBaseComponent)object);
            }
        });
        return iterator;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof WChatBaseComponent)) {
            return false;
        } else {
            WChatBaseComponent chatbasecomponent = (WChatBaseComponent)object;
            return this.a.equals(chatbasecomponent.a) && this.getChatModifier().equals(chatbasecomponent.getChatModifier());
        }
    }

    public int hashCode() {
        return 31 * this.getChatModifier().hashCode() + this.a.hashCode();
    }

    public String toString() {
        return "WBaseComponent{style=" + this.b + ", siblings=" + this.a + '}';
    }
}
