package io.github.retrooper.packetevents.utils.chat;

import com.google.gson.*;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class WChatModifier {

    private WChatModifier a;
    private WEnumChatFormat colr;
    private Boolean bold;
    private Boolean ital;
    private Boolean undl;
    private Boolean skth;
    private Boolean obfd;
    private WChatClickable clickable;
    private WChatHoverable hoverable;
    private String insr;
    private static final WChatModifier def = new WChatModifier() {
        @Nullable
        public WEnumChatFormat getColor() {
            return null;
        }

        public boolean isBold() {
            return false;
        }

        public boolean isItalic() {
            return false;
        }

        public boolean isStrikethrough() {
            return false;
        }

        public boolean isUnderlined() {
            return false;
        }

        public boolean isRandom() {
            return false;
        }

        @Nullable
        public WChatClickable h() {
            return null;
        }

        @Nullable
        public WChatHoverable i() {
            return null;
        }

        @Nullable
        public String j() {
            return null;
        }

        public WChatModifier setColor(WEnumChatFormat enumchatformat) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setBold(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setItalic(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setStrikethrough(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setUnderline(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setRandom(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setChatClickable(WChatClickable chatclickable) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setChatHoverable(WChatHoverable chathoverable) {
            throw new UnsupportedOperationException();
        }

        public WChatModifier setWChatModifier(WChatModifier chatmodifier) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "Style.ROOT";
        }

        public WChatModifier clone() {
            return this;
        }

        public WChatModifier n() {
            return this;
        }
    };

    public WChatModifier() {
    }

    @Nullable
    public WEnumChatFormat getColor() {
        return this.colr == null ? this.o().getColor() : this.colr;
    }

    public boolean isBold() {
        return this.bold == null ? this.o().isBold() : this.bold;
    }

    public boolean isItalic() {
        return this.ital == null ? this.o().isItalic() : this.ital;
    }

    public boolean isStrikethrough() {
        return this.skth == null ? this.o().isStrikethrough() : this.skth;
    }

    public boolean isUnderlined() {
        return this.undl == null ? this.o().isUnderlined() : this.undl;
    }

    public boolean isRandom() {
        return this.obfd == null ? this.o().isRandom() : this.obfd;
    }

    public boolean g() {
        return this.bold == null && this.ital == null && this.skth == null && this.undl == null && this.obfd == null && this.colr == null && this.clickable == null && this.hoverable == null && this.insr == null;
    }

    @Nullable
    public WChatClickable h() {
        return this.clickable == null ? this.o().h() : this.clickable;
    }

    @Nullable
    public WChatHoverable i() {
        return this.hoverable == null ? this.o().i() : this.hoverable;
    }

    @Nullable
    public String j() {
        return this.insr == null ? this.o().j() : this.insr;
    }

    public WChatModifier setColor(WEnumChatFormat enumchatformat) {
        this.colr = enumchatformat;
        return this;
    }

    public WChatModifier setBold(Boolean obool) {
        this.bold = obool;
        return this;
    }

    public WChatModifier setItalic(Boolean obool) {
        this.ital = obool;
        return this;
    }

    public WChatModifier setStrikethrough(Boolean obool) {
        this.skth = obool;
        return this;
    }

    public WChatModifier setUnderline(Boolean obool) {
        this.undl = obool;
        return this;
    }

    public WChatModifier setRandom(Boolean obool) {
        this.obfd = obool;
        return this;
    }

    public WChatModifier setChatClickable(WChatClickable chatclickable) {
        this.clickable = chatclickable;
        return this;
    }

    public WChatModifier setChatHoverable(WChatHoverable chathoverable) {
        this.hoverable = chathoverable;
        return this;
    }

    public WChatModifier setInsertion(String s) {
        this.insr = s;
        return this;
    }

    public WChatModifier setChatModifier(WChatModifier chatmodifier) {
        this.a = chatmodifier;
        return this;
    }

    private WChatModifier o() {
        return this.a == null ? def : this.a;
    }

    public String toString() {
        return "Style{hasParent=" + (this.a != null) + ", color=" + this.colr + ", bold=" + this.bold + ", italic=" + this.ital + ", underlined=" + this.undl + ", obfuscated=" + this.obfd + ", clickEvent=" + this.h() + ", hoverEvent=" + this.i() + ", insertion=" + this.j() + '}';
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof WChatModifier)) {
            return false;
        } else {
            boolean flag;
            label73: {
                WChatModifier chatmodifier = (WChatModifier)object;
                if (this.isBold() == chatmodifier.isBold() && this.getColor() == chatmodifier.getColor() && this.isItalic() == chatmodifier.isItalic() && this.isRandom() == chatmodifier.isRandom() && this.isStrikethrough() == chatmodifier.isStrikethrough() && this.isUnderlined() == chatmodifier.isUnderlined()) {
                    label72: {
                        if (this.h() != null) {
                            if (!this.h().equals(chatmodifier.h())) {
                                break label72;
                            }
                        } else if (chatmodifier.h() != null) {
                            break label72;
                        }

                        if (this.i() != null) {
                            if (!this.i().equals(chatmodifier.i())) {
                                break label72;
                            }
                        } else if (chatmodifier.i() != null) {
                            break label72;
                        }

                        if (this.j() != null) {
                            if (this.j().equals(chatmodifier.j())) {
                                break label73;
                            }
                        } else if (chatmodifier.j() == null) {
                            break label73;
                        }
                    }
                }

                flag = false;
                return flag;
            }

            flag = true;
            return flag;
        }
    }

    public int hashCode() {
        int i = this.colr == null ? 0 : this.colr.hashCode();
        i = 31 * i + (this.bold == null ? 0 : this.bold.hashCode());
        i = 31 * i + (this.ital == null ? 0 : this.ital.hashCode());
        i = 31 * i + (this.undl == null ? 0 : this.undl.hashCode());
        i = 31 * i + (this.skth == null ? 0 : this.skth.hashCode());
        i = 31 * i + (this.obfd == null ? 0 : this.obfd.hashCode());
        i = 31 * i + (this.clickable == null ? 0 : this.clickable.hashCode());
        i = 31 * i + (this.hoverable == null ? 0 : this.hoverable.hashCode());
        i = 31 * i + (this.insr == null ? 0 : this.insr.hashCode());
        return i;
    }

    public WChatModifier clone() {
        WChatModifier chatmodifier = new WChatModifier();
        chatmodifier.bold = this.bold;
        chatmodifier.ital = this.ital;
        chatmodifier.skth = this.skth;
        chatmodifier.undl = this.undl;
        chatmodifier.obfd = this.obfd;
        chatmodifier.colr = this.colr;
        chatmodifier.clickable = this.clickable;
        chatmodifier.hoverable = this.hoverable;
        chatmodifier.a = this.a;
        chatmodifier.insr = this.insr;
        return chatmodifier;
    }

    public WChatModifier n() {
        WChatModifier chatmodifier = new WChatModifier();
        chatmodifier.setBold(this.isBold());
        chatmodifier.setItalic(this.isItalic());
        chatmodifier.setStrikethrough(this.isStrikethrough());
        chatmodifier.setUnderline(this.isUnderlined());
        chatmodifier.setRandom(this.isRandom());
        chatmodifier.setColor(this.getColor());
        chatmodifier.setChatClickable(this.h());
        chatmodifier.setChatHoverable(this.i());
        chatmodifier.setInsertion(this.j());
        return chatmodifier;
    }

    public static class WChatModifierSerializer implements JsonDeserializer<WChatModifier>, JsonSerializer<WChatModifier> {
        public WChatModifierSerializer() {
        }

        @Nullable
        public WChatModifier a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                WChatModifier chatmodifier = new WChatModifier();
                JsonObject jsonobject = jsonelement.getAsJsonObject();
                if (jsonobject == null) {
                    return null;
                } else {
                    if (jsonobject.has("bold")) {
                        chatmodifier.bold = jsonobject.get("bold").getAsBoolean();
                    }

                    if (jsonobject.has("italic")) {
                        chatmodifier.ital = jsonobject.get("italic").getAsBoolean();
                    }

                    if (jsonobject.has("underlined")) {
                        chatmodifier.undl = jsonobject.get("underlined").getAsBoolean();
                    }

                    if (jsonobject.has("strikethrough")) {
                        chatmodifier.skth = jsonobject.get("strikethrough").getAsBoolean();
                    }

                    if (jsonobject.has("obfuscated")) {
                        chatmodifier.obfd = jsonobject.get("obfuscated").getAsBoolean();
                    }

                    if (jsonobject.has("color")) {
                        chatmodifier.colr = (WEnumChatFormat)jsondeserializationcontext.deserialize(jsonobject.get("color"), WEnumChatFormat.class);
                    }

                    if (jsonobject.has("insertion")) {
                        chatmodifier.insr = jsonobject.get("insertion").getAsString();
                    }

                    JsonObject jsonobject1;
                    JsonPrimitive jsonprimitive;
                    if (jsonobject.has("clickEvent")) {
                        jsonobject1 = jsonobject.getAsJsonObject("clickEvent");
                        if (jsonobject1 != null) {
                            jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                            WChatClickable.EnumClickAction chatclickable_enumclickaction = jsonprimitive == null ? null : WChatClickable.EnumClickAction.a(jsonprimitive.getAsString());
                            JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                            String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();
                            if (chatclickable_enumclickaction != null && s != null && chatclickable_enumclickaction.a()) {
                                chatmodifier.clickable = new WChatClickable(chatclickable_enumclickaction, s);
                            }
                        }
                    }

                    if (jsonobject.has("hoverEvent")) {
                        jsonobject1 = jsonobject.getAsJsonObject("hoverEvent");
                        if (jsonobject1 != null) {
                            jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                            WChatHoverable.EnumHoverAction chathoverable_enumhoveraction = jsonprimitive == null ? null : WChatHoverable.EnumHoverAction.a(jsonprimitive.getAsString());
                            WIChatBaseComponent ichatbasecomponent = (WIChatBaseComponent)jsondeserializationcontext.deserialize(jsonobject1.get("value"), WIChatBaseComponent.class);
                            if (chathoverable_enumhoveraction != null && ichatbasecomponent != null && chathoverable_enumhoveraction.a()) {
                                chatmodifier.hoverable = new WChatHoverable(chathoverable_enumhoveraction, ichatbasecomponent);
                            }
                        }
                    }

                    return chatmodifier;
                }
            } else {
                return null;
            }
        }

        @Nullable
        public JsonElement a(WChatModifier chatmodifier, Type type, JsonSerializationContext jsonserializationcontext) {
            if (chatmodifier.g()) {
                return null;
            } else {
                JsonObject jsonobject = new JsonObject();
                if (chatmodifier.bold != null) {
                    jsonobject.addProperty("bold", chatmodifier.bold);
                }

                if (chatmodifier.ital != null) {
                    jsonobject.addProperty("italic", chatmodifier.ital);
                }

                if (chatmodifier.undl != null) {
                    jsonobject.addProperty("underlined", chatmodifier.undl);
                }

                if (chatmodifier.skth != null) {
                    jsonobject.addProperty("strikethrough", chatmodifier.skth);
                }

                if (chatmodifier.obfd != null) {
                    jsonobject.addProperty("obfuscated", chatmodifier.obfd);
                }

                if (chatmodifier.colr != null) {
                    jsonobject.add("color", jsonserializationcontext.serialize(chatmodifier.colr));
                }

                if (chatmodifier.insr != null) {
                    jsonobject.add("insertion", jsonserializationcontext.serialize(chatmodifier.insr));
                }

                JsonObject jsonobject1;
                if (chatmodifier.clickable != null) {
                    jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("action", chatmodifier.clickable.a().b());
                    jsonobject1.addProperty("value", chatmodifier.clickable.b());
                    jsonobject.add("clickEvent", jsonobject1);
                }

                if (chatmodifier.hoverable != null) {
                    jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("action", chatmodifier.hoverable.a().b());
                    jsonobject1.add("value", jsonserializationcontext.serialize(chatmodifier.hoverable.b()));
                    jsonobject.add("hoverEvent", jsonobject1);
                }

                return jsonobject;
            }
        }

        @Nullable
        public JsonElement serialize(WChatModifier object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Nullable
        public WChatModifier deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
