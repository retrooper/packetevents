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

package com.github.retrooper.packetevents.manager.server;

/**
 * This enum contains all possible comparison types for server versions.
 */
public enum VersionComparison {
    /*
    The server version equals the compared server version.
     */
    EQUALS,
    /*
    The server version is newer than the compared server version.
     */
    NEWER_THAN,
    /*
    The server version is newer than or equal to the compared server version.
     */
    NEWER_THAN_OR_EQUALS,
    /*
    The server version is older than the compared server version.
     */
    OLDER_THAN,
    /*
    The server version is older than or equal to the compared server version.
     */
    OLDER_THAN_OR_EQUALS;
}