package io.github.retrooper.packetevents.reflectionutils.fielddata;

public class FieldWithIndex {
    private final Class<?> cls;
    private final int index;

    public FieldWithIndex(Class<?> cls, int index) {
        this.cls = cls;
        this.index = index;
    }

    public Class<?> getCls() {
        return cls;
    }

    public int getIndex() {
        return index;
    }
}
