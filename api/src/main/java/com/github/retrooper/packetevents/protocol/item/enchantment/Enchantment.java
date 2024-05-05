/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.item.enchantment;

import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;

public class Enchantment {
    private EnchantmentType type;
    private int level;

    public Enchantment(EnchantmentType type, int level) {
        this.type = type;
        this.level = level;
    }

    public EnchantmentType getType() {
        return type;
    }

    public void setType(EnchantmentType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private EnchantmentType type;
        private int level;

        public Builder type(EnchantmentType type) {
            this.type = type;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Enchantment build() {
            return new Enchantment(type, level);
        }
    }
}
