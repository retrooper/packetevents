/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.utils.attributesnapshot;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.Supplier;

public class AttributeModifierWrapper extends WrappedPacket {
    private static boolean supplierPresent;
    private static Class<? extends Enum<?>> operationEnumClass;
    private static Class<?> attributeModifierClass;
    private static Constructor<?> attributeModifierConstructor;

    public AttributeModifierWrapper(NMSPacket packet) {
        super(packet);
    }

    public AttributeModifierWrapper(UUID id, String name, double amount, Operation operation) {
        super(create(id, name, amount, operation).packet);
    }

    public static AttributeModifierWrapper create(UUID id, String name, double amount, Operation operation) {
        if (attributeModifierConstructor == null) {
            try {
                attributeModifierConstructor = attributeModifierClass.getConstructor(UUID.class, String.class, double.class, operationEnumClass == null ? int.class : operationEnumClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        int operationIndex = operation.ordinal();
        Object attributeModifierObj = null;
        try {
            if (operationEnumClass == null) {
                attributeModifierObj = attributeModifierConstructor.newInstance(id, name, amount, operationIndex);
            } else {
                Enum<?> enumConst = EnumUtil.valueByIndex(operationEnumClass, operationIndex);
                attributeModifierObj = attributeModifierConstructor.newInstance(id, name, amount, enumConst);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new AttributeModifierWrapper(new NMSPacket(attributeModifierObj));
    }

    @Override
    protected void load() {
        supplierPresent = Reflection.getField(packet.getRawNMSPacket().getClass(), Supplier.class, 0) != null;
        operationEnumClass = SubclassUtil.getEnumSubClass(packet.getRawNMSPacket().getClass(), "Operation");
        attributeModifierClass = NMSUtils.getNMSClassWithoutException("AttributeModifier");
    }

    public double getAmount() {
        return readDouble(0);
    }

    public void setAmount(double amount) {
        writeDouble(0, amount);
    }

    public Operation getOperation() {
        if (operationEnumClass == null) {
            int operation = readInt(0);
            return Operation.values()[operation];
        } else {
            Enum<?> enumConst = readEnumConstant(0, operationEnumClass);
            return Operation.values()[enumConst.ordinal()];
        }
    }

    public void setOperation(Operation operation) {
        if (operationEnumClass == null) {
            writeInt(0, operation.ordinal());
        } else {
            Enum<?> enumConst = EnumUtil.valueByIndex(operationEnumClass, operation.ordinal());
            writeEnumConstant(0, enumConst);
        }
    }

    //TODO Might cause issues since now a resourcelocation
    public String getName() {
        if (supplierPresent) {
            //About 1.13 and above
            Supplier<String> supplier = readObject(0, Supplier.class);
            return supplier.get();
        } else {
            return readString(0);
        }
    }

    public void setName(String name) {
        if (supplierPresent) {
            Supplier<String> supplier = () -> name;

            writeObject(0, supplier);
        } else {
            writeString(0, name);
        }
    }

    public UUID getUUID() {
        return readObject(0, UUID.class);
    }

    public void setUUID(UUID uuid) {
        writeObject(0, uuid);
    }

    public NMSPacket getNMSPacket() {
        return packet;
    }

    public enum Operation {
        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL
    }
}
