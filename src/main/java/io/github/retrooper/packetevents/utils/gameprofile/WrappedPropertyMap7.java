package io.github.retrooper.packetevents.utils.gameprofile;

import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap;

import java.util.ArrayList;
import java.util.Collection;

public class WrappedPropertyMap7 implements WrappedPropertyMapAbstract<String, WrappedProperty> {
    private PropertyMap propertyMap;
    public WrappedPropertyMap7(Object propertyMap){
        this.propertyMap = (PropertyMap) propertyMap;
    }
    @Override
    public Collection<WrappedProperty> get(String key) {
        Collection<Property> properties = propertyMap.get(key);
        Collection<WrappedProperty> wrappedProperties = new ArrayList<>();
        for (Property property : properties) {
            wrappedProperties.add(GameProfileUtil.getWrappedProperty(property));
        }
        return wrappedProperties;
    }

    @Override
    public void put(String key, WrappedProperty value) {
        propertyMap.put(key, (Property) GameProfileUtil.getProperty(value.getName(), value.getValue(), value.getSignature()));
    }
}
