package io.github.retrooper.packetevents.reflectionutils.methoddata;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodWithIndexAndParams that = (MethodWithIndexAndParams) o;
        return index == that.index &&
                Objects.equals(cls, that.cls) &&
                Objects.equals(params, that.params);
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(cls, params, index);
	}
    
}
