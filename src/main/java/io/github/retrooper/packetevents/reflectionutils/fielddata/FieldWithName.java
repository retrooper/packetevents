package io.github.retrooper.packetevents.reflectionutils.fielddata;

public class FieldWithName {

    private final Class<?> cls;
    private final String name;
    public FieldWithName(Class<?> cls, String name) {
        this.cls = cls;
        this.name = name;
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getName() {
        return name;
    }
}
