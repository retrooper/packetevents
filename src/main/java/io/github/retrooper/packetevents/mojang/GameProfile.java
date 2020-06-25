package io.github.retrooper.packetevents.mojang;

import com.mojang.authlib.properties.PropertyMap;

import java.util.UUID;

public final class GameProfile {
    private final UUID id;
    private final String name;
    private final PropertyMap properties = new PropertyMap();
    private boolean legacy;

    public GameProfile(UUID id, String name) {
        if (id == null && name.isEmpty()) {
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
        return this.id != null && !getName().isEmpty();
    }


    public boolean isLegacy() {
        return this.legacy;
    }

    public boolean equals(com.mojang.authlib.GameProfile profile) {
        if (profile != null) {
            return profile.getId() != null && getId() != null && profile.getId() == getId()
                    && profile.getName() != null && getName() != null && profile.getName().equals(getName());
        }
        return false;
    }

    @Override
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

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", legacy: " + legacy;
    }
}
