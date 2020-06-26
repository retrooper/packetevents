package io.github.retrooper.packetevents.reflectionutils.fielddata;

public class FieldWithTypeAndIndex {
    private final Class<?> cls, type;
    private final int index;

    public FieldWithTypeAndIndex(Class<?> cls, Class<?> type, int index) {
        this.cls = cls;
        this.type = type;
        this.index = index;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Class<?> getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
