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

package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.DAMAGE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.ENCHANTABLE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.ENCHANTMENTS;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.MAX_DAMAGE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.MAX_STACK_SIZE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.STORED_ENCHANTMENTS;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.UNBREAKABLE_MODERN;

@NullMarked
public class ItemStack {

    public static final ItemStack EMPTY = ItemStack.builder().nbt(new NBTCompound()).build();

    private final ClientVersion version;
    private final IRegistryHolder registryHolder;

    private final ItemType type;
    private int amount;

    /**
     * Removed with 1.20.5
     */
    @ApiStatus.Obsolete
    @Nullable
    private NBTCompound nbt;
    /**
     * Added with 1.20.5
     */
    @Nullable // lazy loaded
    private PatchableComponentMap components;
    /**
     * Removed with 1.13
     */
    @ApiStatus.Obsolete
    private int legacyData;

    private ItemStack(
            ItemType type,
            int amount,
            @Nullable NBTCompound nbt,
            @Nullable PatchableComponentMap components,
            int legacyData,
            ClientVersion version,
            IRegistryHolder registryHolder
    ) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        this.components = components;
        this.legacyData = legacyData;
        this.version = version;
        this.registryHolder = registryHolder;
    }

    public static ItemStack decode(NBT nbt, ClientVersion version) {
        if (nbt instanceof NBTString) {
            ResourceLocation itemName = new ResourceLocation(((NBTString) nbt).getValue());
            return ItemStack.builder().type(ItemTypes.getByName(itemName.toString())).build();
        }
        NBTCompound compound = (NBTCompound) nbt;
        ItemStack.Builder builder = ItemStack.builder();

        ResourceLocation itemName = Optional.ofNullable(compound.getStringTagValueOrNull("id")).map(Optional::of)
                .orElseGet(() -> Optional.ofNullable(compound.getStringTagValueOrNull("item")))
                .map(ResourceLocation::new).orElseThrow(() -> new IllegalArgumentException(
                        "No item type specified: " + compound.getTags().keySet()));
        builder.type(ItemTypes.getByName(itemName.toString()));
        builder.nbt(compound.getCompoundTagOrNull("tag"));

        Optional.ofNullable(compound.getNumberTagOrNull("Count")).map(Optional::of)
                .orElseGet(() -> Optional.ofNullable(compound.getNumberTagOrNull("count")))
                .map(NBTNumber::getAsInt).ifPresent(builder::amount);

        // TODO components

        return builder.build();
    }

    public static NBT encodeForParticle(ItemStack itemStack, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            boolean simple = itemStack.isEmpty()
                    || itemStack.components == null
                    || itemStack.components.getPatches().isEmpty();
            if (simple) {
                return new NBTString(itemStack.type.getName().toString());
            }
        }

        NBTCompound compound = new NBTCompound();
        compound.setTag("id", new NBTString(itemStack.type.getName().toString()));
        if (version.isOlderThan(ClientVersion.V_1_20_5)) {
            compound.setTag("Count", new NBTInt(itemStack.getAmount()));
            if (itemStack.nbt != null) {
                compound.setTag("tag", itemStack.nbt);
            }
        }

        // TODO components

        return compound;
    }

    public int getMaxStackSize() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.getComponentOr(MAX_STACK_SIZE, 1);
        }
        return this.getType().getMaxAmount();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }

    public boolean isDamageableItem() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.hasComponent(MAX_DAMAGE)
                    && !this.hasComponent(UNBREAKABLE_MODERN)
                    && this.hasComponent(DAMAGE);
        }
        return !this.isEmpty() && this.getMaxDamage() > 0
                && (this.nbt == null || !this.nbt.getBoolean("Unbreakable"));
    }

    public boolean isDamaged() {
        return this.isDamageableItem() && this.getDamageValue() > 0;
    }

    public int getDamageValue() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            int value = this.getComponentOr(DAMAGE, 0);
            return MathUtil.clamp(value, 0, this.getMaxDamage());
        } else {
            NBTNumber damage = this.nbt != null ? this.nbt.getNumberTagOrNull("Damage") : null;
            return damage == null ? 0 : damage.getAsInt();
        }
    }

    public void setDamageValue(int damage) {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            this.setComponent(DAMAGE, MathUtil.clamp(damage, 0, this.getMaxDamage()));
        } else {
            this.getOrCreateTag().setTag("Damage", new NBTInt(Math.max(0, damage)));
        }
    }

    public int getMaxDamage() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.getComponentOr(MAX_DAMAGE, 0);
        } else {
            return this.getType().getMaxDurability();
        }
    }

    public NBTCompound getOrCreateTag() {
        if (this.nbt == null) {
            this.nbt = new NBTCompound();
        }
        return this.nbt;
    }

    public ItemType getType() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
            // vanilla prevents negative-stacked items starting with 1.11
            return this.isEmpty() ? ItemTypes.AIR : this.type;
        }
        return this.type;
    }

    public int getAmount() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
            // vanilla prevents negative-stacked items starting with 1.11
            return this.isEmpty() ? 0 : this.amount;
        }
        return this.amount;
    }

    public void shrink(int amount) {
        this.amount -= amount;
    }

    public void grow(int amount) {
        this.amount += amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack split(int toTake) {
        int i = Math.min(toTake, getAmount());
        ItemStack itemstack = this.copy();
        itemstack.setAmount(i);
        this.shrink(i);
        return itemstack;
    }

    public ItemStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        }
        return new ItemStack(
                this.type, this.amount,
                this.nbt == null ? null : this.nbt.copy(),
                this.components == null ? null : this.components.copy(),
                this.legacyData, this.version, this.registryHolder
        );
    }

    @Nullable
    public NBTCompound getNBT() {
        return nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

    public <T> T getComponentOr(ComponentType<T> type, T otherValue) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOr(type, otherValue);
        }
        return this.getType().getComponents().getOr(type, otherValue);
    }

    public <T> Optional<T> getComponent(ComponentType<T> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOptional(type);
        }
        return this.getType().getComponents().getOptional(type);
    }

    public <T> void setComponent(ComponentType<T> type, T value) {
        this.getComponents().set(type, value);
    }

    public <T> void unsetComponent(ComponentType<T> type) {
        this.getComponents().unset(type);
    }

    public <T> void setComponent(ComponentType<T> type, Optional<T> value) {
        this.getComponents().set(type, value);
    }

    public boolean hasComponent(ComponentType<?> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().has(type);
        }
        return this.getType().getComponents().has(type);
    }

    public boolean hasComponentPatches() {
        return this.components != null && !this.components.getPatches().isEmpty();
    }

    public PatchableComponentMap getComponents() {
        if (this.components == null) { // lazy load on access
            this.components = new PatchableComponentMap(
                    this.type.getComponents(), new HashMap<>(4));
        }
        return this.components;
    }

    /**
     * @param components if set null will reset to components of {@link ItemType}
     */
    public void setComponents(@Nullable PatchableComponentMap components) {
        this.components = components;
    }

    public int getLegacyData() {
        return legacyData;
    }

    public void setLegacyData(int legacyData) {
        this.legacyData = legacyData;
    }

    public boolean isEnchantable() {
        return this.isEnchantable(this.version);
    }

    /**
     * @deprecated use {@link #isEnchantable()} instead
     */
    @Deprecated
    public boolean isEnchantable(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // component logic
            return this.hasComponent(ENCHANTABLE) && !this.isEnchanted(version);
        }
        // legacy nbt logic
        if (this.type == ItemTypes.BOOK) {
            return this.getAmount() == 1;
        } else if (this.type == ItemTypes.ENCHANTED_BOOK) {
            return false;
        }
        return this.getMaxStackSize() == 1 && this.canBeDepleted() && !this.isEnchanted(version);
    }

    public boolean isEnchanted() {
        return this.isEnchanted(this.version);
    }

    /**
     * @deprecated use {@link #isEnchanted()} instead
     */
    @Deprecated
    public boolean isEnchanted(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // component logic
            return !this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()
                    || !this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        }
        // legacy nbt logic
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> enchantments = this.nbt.getCompoundListTagOrNull(tagName);
            return enchantments != null && !enchantments.getTags().isEmpty();
        }
        return false;
    }

    public List<Enchantment> getEnchantments() {
        return this.getEnchantments(this.version);
    }

    /**
     * @deprecated use {@link #getEnchantments()} instead
     */
    @Deprecated
    public List<Enchantment> getEnchantments(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // component logic
            ItemEnchantments enchantmentsComp = this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments storedEnchantmentsComp = this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            List<Enchantment> enchantmentsList = new ArrayList<>(
                    enchantmentsComp.getEnchantmentCount()
                            + storedEnchantmentsComp.getEnchantmentCount());
            for (Map.Entry<EnchantmentType, Integer> enchantment : enchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            for (Map.Entry<EnchantmentType, Integer> enchantment : storedEnchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            return enchantmentsList;
        }
        // legacy nbt logic
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
                List<NBTCompound> compounds = nbtList.getTags();
                List<Enchantment> enchantments = new ArrayList<>(compounds.size());

                for (NBTCompound compound : compounds) {
                    EnchantmentType type = getEnchantmentTypeFromTag(compound, version);

                    if (type != null) {
                        NBTNumber levelTag = compound.getNumberTagOrNull("lvl");
                        if (levelTag != null) {
                            int level = levelTag.getAsInt();
                            Enchantment enchantment = Enchantment.builder().type(type).level(level).build();
                            enchantments.add(enchantment);
                        }
                    }
                }
                return enchantments;
            }
        }

        return new ArrayList<>(0);
    }

    public int getEnchantmentLevel(EnchantmentType enchantment) {
        return this.getEnchantmentLevel(enchantment, this.version);
    }

    /**
     * @deprecated use {@link #getEnchantmentLevel(EnchantmentType)} instead
     */
    @Deprecated
    public int getEnchantmentLevel(EnchantmentType enchantment, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // component logic
            ItemEnchantments enchantmentsComp = this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY);
            if (!enchantmentsComp.isEmpty()) {
                int level = enchantmentsComp.getEnchantmentLevel(enchantment);
                if (level > 0) {
                    return level;
                }
            }
            ItemEnchantments storedEnchantmentsComp = this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            if (!storedEnchantmentsComp.isEmpty()) {
                return storedEnchantmentsComp.getEnchantmentLevel(enchantment);
            }
            return 0;
        }

        // legacy nbt logic
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
                for (NBTCompound base : nbtList.getTags()) {
                    EnchantmentType type = getEnchantmentTypeFromTag(base, version);
                    if (Objects.equals(type, enchantment)) {
                        NBTNumber nbtLevel = base.getNumberTagOrNull("lvl");
                        return nbtLevel != null ? nbtLevel.getAsInt() : 0;
                    }
                }
            }
        }

        return 0;
    }

    private static @Nullable EnchantmentType getEnchantmentTypeFromTag(NBTCompound tag, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            String id = tag.getStringTagValueOrNull("id");
            return EnchantmentTypes.getByName(id);
        } else {
            NBTShort idTag = tag.getTagOfTypeOrNull("id", NBTShort.class);
            return idTag != null ? EnchantmentTypes.getById(version, idTag.getAsInt()) : null;
        }
    }

    public void setEnchantments(List<Enchantment> enchantments) {
        this.setEnchantments(enchantments, this.version);
    }

    /**
     * @deprecated use {@link #setEnchantments(List)} instead
     */
    @Deprecated
    public void setEnchantments(List<Enchantment> enchantments, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // component logic
            Map<EnchantmentType, Integer> enchantmentsMap = new HashMap<>(enchantments.size());
            for (Enchantment enchantment : enchantments) {
                enchantmentsMap.put(enchantment.getType(), enchantment.getLevel());
            }
            ComponentType<ItemEnchantments> componentType =
                    this.hasComponent(STORED_ENCHANTMENTS) ? STORED_ENCHANTMENTS : ENCHANTMENTS;
            Optional<ItemEnchantments> prevEnchantments = this.getComponent(componentType);
            boolean showInTooltip = prevEnchantments.map(ItemEnchantments::isShowInTooltip).orElse(true);
            this.setComponent(componentType, new ItemEnchantments(enchantmentsMap, showInTooltip));
        } else {
            // legacy nbt logic
            String tagName = this.getEnchantmentsTagName(version);
            if (enchantments.isEmpty()) {
                // just remove enchantment tag
                if (this.nbt != null && this.nbt.getTagOrNull(tagName) != null) {
                    this.nbt.removeTag(tagName);
                }
            } else {
                List<NBTCompound> list = new ArrayList<>();
                for (Enchantment enchantment : enchantments) {
                    NBTCompound compound = new NBTCompound();
                    if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
                        compound.setTag("id", new NBTString(enchantment.getType().getName().toString()));
                    } else {
                        compound.setTag("id", new NBTShort((short) enchantment.getType().getId(version)));
                    }
                    compound.setTag("lvl", new NBTShort((short) enchantment.getLevel()));
                    list.add(compound);
                }
                this.getOrCreateTag().setTag(tagName, new NBTList<>(NBTType.COMPOUND, list));
            }
        }
    }

    @Deprecated
    public String getEnchantmentsTagName(ClientVersion version) {
        String tagName = version.isNewerThanOrEquals(ClientVersion.V_1_13) ? "Enchantments" : "ench";
        if (this.type == ItemTypes.ENCHANTED_BOOK) {
            tagName = "StoredEnchantments";
        }
        return tagName;
    }

    public boolean canBeDepleted() {
        return this.getMaxDamage() > 0;
    }

    public boolean is(ItemType type) {
        return this.getType() == type;
    }

    public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
        return isSameItemSameComponents(stack, otherStack);
    }

    public static boolean isSameItemSameComponents(ItemStack stack, ItemStack otherStack) {
        if (stack.version != otherStack.version) {
            throw new IllegalArgumentException("Can't compare two ItemStacks across versions: "
                    + stack.version + " != " + otherStack.version);
        } else if (stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            // compare components
            return stack.is(otherStack.getType())
                    && (stack.isEmpty() && otherStack.isEmpty()
                    || stack.getComponents().equals(otherStack.getComponents()));
        }
        // component nbt
        return stack.is(otherStack.getType())
                && (stack.isEmpty() && otherStack.isEmpty()
                || (Objects.equals(stack.nbt, otherStack.nbt)));
    }

    public static boolean tagMatches(@Nullable ItemStack stack, @Nullable ItemStack otherStack) {
        if (stack == otherStack) {
            return true;
        } else if (stack == null) {
            return otherStack.isEmpty();
        } else if (otherStack == null) {
            return stack.isEmpty();
        }
        if (stack.version != otherStack.version) {
            throw new IllegalArgumentException("Can't compare two ItemStacks across versions: "
                    + stack.version + " != " + otherStack.version);
        } else if (stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return stack.getComponents().equals(otherStack.getComponents());
        }
        return Objects.equals(stack.nbt, otherStack.nbt);
    }

    public boolean isEmpty() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            return this.type == ItemTypes.AIR || this.amount <= 0;
        } else if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
            return this.type == ItemTypes.AIR || this.amount <= 0
                    || this.legacyData < (short) (1 << 15) || this.legacyData > (1 << 16);
        }
        return this.amount <= 0;
    }

    public ClientVersion getVersion() {
        return this.version;
    }

    public IRegistryHolder getRegistryHolder() {
        return this.registryHolder;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            return this.type.equals(itemStack.type)
                    && this.amount == itemStack.amount
                    && Objects.equals(this.nbt, itemStack.nbt)
                    && Objects.equals(this.components, itemStack.components)
                    && this.legacyData == itemStack.legacyData;
        }
        return false;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "ItemStack[EMPTY]";
        }
        return "ItemStack["
                + this.getAmount() + "x/" + this.getMaxStackSize() + "x " + this.type.getName()
                + (this.nbt != null ? ", nbt tag names=" + this.nbt.getTagNames() : "")
                + (this.legacyData != -1 ? ", legacy data=" + this.legacyData : "")
                + (this.components != null ? ", components=" + this.components.getPatches() : "")
                + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        private IRegistryHolder registryHolder = GlobalRegistryHolder.INSTANCE;

        private ItemType type = ItemTypes.AIR;
        private int amount = 1;
        private @Nullable NBTCompound nbt = null;
        private @Nullable PatchableComponentMap components = null;
        private int legacyData = -1;

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder nbt(NBTCompound nbt) {
            this.nbt = nbt;
            return this;
        }

        public Builder nbt(String key, NBT tag) {
            if (this.nbt == null) {
                this.nbt = new NBTCompound();
            }
            this.nbt.setTag(key, tag);
            return this;
        }

        public Builder components(@Nullable PatchableComponentMap components) {
            this.components = components;
            return this;
        }

        public <T> Builder component(ComponentType<T> type, @Nullable T value) {
            if (this.components == null) {
                this.components = new PatchableComponentMap(this.type.getComponents(this.version));
            }
            this.components.set(type, value);
            return this;
        }

        public Builder legacyData(int legacyData) {
            this.legacyData = legacyData;
            return this;
        }

        public Builder user(User user) {
            return this.version(user.getPacketVersion()).registryHolder(user);
        }

        public Builder wrapper(PacketWrapper<?> wrapper) {
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            return this.version(version).registryHolder(wrapper.getRegistryHolder());
        }

        public Builder version(ClientVersion version) {
            this.version = version;
            return this;
        }

        public Builder registryHolder(IRegistryHolder registryHolder) {
            this.registryHolder = registryHolder;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(this.type, this.amount, this.nbt, this.components,
                    this.legacyData, this.version, this.registryHolder);
        }
    }
}
