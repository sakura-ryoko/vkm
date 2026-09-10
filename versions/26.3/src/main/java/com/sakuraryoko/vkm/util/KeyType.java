/*
 * This file is part of the Advanced Keybind Manager project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Sakura-Ryoko and contributors
 *
 * Advanced Keybind Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Advanced Keybind Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Advanced Keybind Manager.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.vkm.util;

import com.mojang.blaze3d.platform.InputConstants;

public enum KeyType
{
    KEYBOARD    ("keyboard"),
    MOUSE       ("mouse"),
    UNKNOWN     ("unknown")
    ;

    private final String name;

    KeyType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public InputConstants.Type toVanilla()
    {
	    return switch (this.name)
	    {
		    case "mouse" -> InputConstants.Type.MOUSE;
		    case "keyboard" -> InputConstants.Type.KEYBOARD;
		    default -> null;
	    };
    }

    public static KeyType fromVanilla(InputConstants.Type type)
    {
        String name = type.defaultPrefix;

	    return switch (name)
	    {
		    case "key.mouse" -> KeyType.MOUSE;
		    case "keyboard" -> KeyType.KEYBOARD;
		    default -> KeyType.UNKNOWN;
	    };
    }

    public static KeyType fromString(String type)
    {
	    return switch (type)
	    {
		    case "mouse" -> KeyType.MOUSE;
		    case "keyboard" -> KeyType.KEYBOARD;
		    default -> KeyType.UNKNOWN;
	    };
    }
}
