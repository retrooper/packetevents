package io.github.retrooper.packetevents.reflectionutils.fielddata;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldWithTypeAndIndexk that = (FieldWithTypeAndIndex) o;
        return index == that.index &&
                Objects.equals(cls, that.cls) &&
                Objects.equals(type, that.type);
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(cls, type, index);
	}
    
}
