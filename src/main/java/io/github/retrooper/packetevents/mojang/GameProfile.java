package io.github.retrooper.packetevents.mojang;

import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class GameProfile {
    private final UUID id;
    private final String name;
    private final PropertyMap properties = new PropertyMap();
    private boolean legacy;

    public GameProfile(UUID id, String name) {
        if (id == null && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        } else {
            this.id = id;
            this.name = name;
        }
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }

    public boolean isComplete() {
        return this.id != null && StringUtils.isNotBlank(this.getName());
    }

    public boolean equalsMojangGameProfile(com.mojang.authlib.GameProfile profile) {
        if (profile != null) {
            return profile.getId() != null && getId() != null && profile.getId() == getId()
                    && profile.getName() != null && getName() != null && profile.getName().equals(getName());
        }
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GameProfile that = (GameProfile) o;
            if (this.id != null) {
                if (!this.id.equals(that.id)) {
                    return false;
                }
            } else if (that.id != null) {
                return false;
            }

            if (this.name != null) {
                return this.name.equals(that.name);
            } else return that.name == null;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
    }

    public boolean isLegacy() {
        return this.legacy;
    }
}
