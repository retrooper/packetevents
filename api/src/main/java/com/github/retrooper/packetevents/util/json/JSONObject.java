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

package com.github.retrooper.packetevents.util.json;


public class JSONObject extends org.json.simple.JSONObject {

    public JSONObject(org.json.simple.JSONObject object) {
        super(object);
    }

    public JSONObject() {
    }

    public JSONObject getJSONObject(String name) {
        return (JSONObject) getOrDefault(name, new JSONObject());
    }

    public void setJSONObject(String name, JSONObject object) {
        put(name, object);
    }

    public JSONArray getJSONArray(String name) {
        return (JSONArray) getOrDefault(name, new JSONArray());
    }

    public void setJSONArray(String name, JSONArray array) {
        put(name, array);
    }

    public String getString(String name) {
        return (String) getOrDefault(name, "");
    }

    public void setString(String name, String value) {
        put(name, value);
    }

    public int getInt(String name) {
        return (int) get(name);
    }

    public int getIntOrDefault(String name, int def) {
        return (int) getOrDefault(name, def);
    }

    public void setInt(String name, int value) {
        put(name, value);
    }

    public long getLong(String name) {
        return (long) get(name);
    }

    public long getLongOrDefault(String name, long def) {
        return (long) getOrDefault(name, def);
    }

    public void setLong(String name, long value) {
        put(name, value);
    }

    public double getDouble(String name) {
        return (double) get(name);
    }

    public double getDoubleOrDefault(String name, double def) {
        return (double) getOrDefault(name, def);
    }

    public void setDouble(String name, double value) {
        put(name, value);
    }

    public boolean getBoolean(String name) {
        return (boolean) getOrDefault(name, false);
    }

    public boolean getBooleanOrDefault(String name, boolean def) {
        return (boolean) getOrDefault(name, def);
    }

    public void setBoolean(String name, boolean value) {
        put(name, value);
    }

    public boolean isPresent(String name) {
        return containsKey(name);
    }
}
