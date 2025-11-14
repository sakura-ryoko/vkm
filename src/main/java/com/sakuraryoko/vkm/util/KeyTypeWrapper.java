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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.mojang.blaze3d.platform.InputConstants;

public class KeyTypeWrapper
{
    private final InputConstants.Type vanilla;
    private final KeyType type;

    public KeyTypeWrapper(KeyType type)
    {
        this.type = type;
        this.vanilla = type.toVanilla();
    }

    public KeyTypeWrapper(InputConstants.Type type)
    {
        this.type = KeyType.fromVanilla(type);
        this.vanilla = type;
    }

    public KeyType getType()
    {
        return this.type;
    }

    public InputConstants.Type getVanilla()
    {
        return this.vanilla;
    }

    public String getName()
    {
        return this.type.getName();
    }

	public boolean isKeyboard()
	{
		return this.type == KeyType.KEYBOARD;
	}

	public boolean isMouse()
	{
		return this.type == KeyType.MOUSE;
	}

	public boolean isScancode()
	{
		return this.type == KeyType.SCANCODE;
	}

	protected JsonElement toJson()
    {
        return new JsonPrimitive(this.type.getName());
    }

	public static KeyTypeWrapper fromKeyCode(int keyCode, int scanCode)
	{
		if (keyCode < -1)
		{
			return new KeyTypeWrapper(KeyType.MOUSE);
		}
		else if (keyCode == -1)
		{
			return new KeyTypeWrapper(KeyType.SCANCODE);
		}
		else if (keyCode < 8)
		{
			return new KeyTypeWrapper(KeyType.MOUSE);
		}
		else
		{
			return new KeyTypeWrapper(KeyType.KEYBOARD);
		}
	}

    protected static KeyTypeWrapper fromJson(JsonObject obj)
    {
        if (obj.isJsonPrimitive())
        {
            try
            {
                return new KeyTypeWrapper(KeyType.fromString(obj.getAsString()));
            }
            catch (Exception ignored) { }
        }

        return new KeyTypeWrapper(KeyType.KEYBOARD);
    }

    @Override
    public String toString()
    {
        return "KeyType:[" +
               "{name=" + this.getName() + "}"+
               "]";
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof KeyTypeWrapper)
        {
            return ((KeyTypeWrapper) other).getName().equals(this.getName());
        }

        return false;
    }

    public void runDebug()
    {
        System.out.printf("[DB] %s\n", this.toString());
    }
}
