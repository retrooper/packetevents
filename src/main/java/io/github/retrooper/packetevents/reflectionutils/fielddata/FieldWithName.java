package io.github.retrooper.packetevents.reflectionutils.fielddata;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldWithName that = (FieldWithName) o;
        return name.equals(that.name) &&
                Objects.equals(cls, that.cls);
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(cls, name);
	}
    
}
