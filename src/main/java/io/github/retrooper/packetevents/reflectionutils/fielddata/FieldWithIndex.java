package io.github.retrooper.packetevents.reflectionutils.fielddata;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldWithIndex that = (FieldWithIndex) o;
        return index == that.index &&
                Objects.equals(cls, that.cls);
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(cls, index);
	}
    
}
