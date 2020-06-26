package io.github.retrooper.packetevents.reflectionutils.methoddata;

public class MethodWithIndexAndParams {
    private final Class<?> cls;
    private final int index;
    private final Class<?>[] params;

    public MethodWithIndexAndParams(Class<?> cls, int index, Class<?>[] params) {
        this.cls = cls;
        this.index = index;
        this.params = params;
    }

    public Class<?> getCls() {
        return cls;
    }

    public int getIndex() {
        return index;
    }

    public Class<?>[] getParams() {
        return params;
    }
}
