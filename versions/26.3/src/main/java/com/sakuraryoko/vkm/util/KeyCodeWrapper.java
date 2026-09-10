/*
 * This file is part of the Vanilla Keybind Manager project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura-Ryoko and contributors
 *
 * Vanilla Keybind Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla Keybind Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vanilla Keybind Manager.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.vkm.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;

import fi.dy.masa.malilib.util.input.ScanCodes;

public class KeyCodeWrapper
{
    @Nullable
    private final InputConstants.Key vanilla;
    private final String name;
    private final KeyTypeWrapper type;
    private final int scanCode;

    public KeyCodeWrapper(InputConstants.Key key)
    {
        this.vanilla = key;
        this.name = key.getName();
        this.scanCode = key.getValue();
        this.type = new KeyTypeWrapper(key.getType());
    }

    public KeyCodeWrapper(String name, int scanCode, KeyTypeWrapper type)
    {
        this.name = name;
		this.type = type;
        this.scanCode = scanCode;
        this.vanilla = InputConstants.getKey(name);
    }

    @Nullable
    public InputConstants.Key getVanilla()
    {
        return this.vanilla;
    }

    public String getName()
    {
        return this.name;
    }

    public KeyTypeWrapper getType()
    {
        return this.type;
    }

    public int getScanCode()
    {
        return this.scanCode;
    }

    public String getTranslationKey()
    {
        if (this.vanilla != null)
        {
            return this.vanilla.getName();
        }

        return this.name;
    }

    public Component getTranslated()
    {
        if (this.vanilla != null)
        {
            return this.vanilla.getDisplayName();
        }

        if (LangWrap.has(this.getTranslationKey()))
        {
            return Component.literal(LangWrap.get(this.getTranslationKey()));
        }

        return Component.literal(this.getTranslationKey());
    }

    public JsonElement toJson()
    {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", this.getName());
        obj.addProperty("scanCode", this.getScanCode());
        obj.add("type", this.getType().toJson());

        return obj;
    }

    @Nullable
    public static KeyCodeWrapper fromJson(JsonElement element)
    {
        try
        {
            if (element.isJsonObject())
            {
                JsonObject obj = element.getAsJsonObject();

                String name = "";
                int scanCode = -1;
                KeyTypeWrapper type = null;

                if (JsonUtils.hasString(obj, "name"))
                {
                    name = obj.get("name").getAsString();
                }
                if (JsonUtils.hasInteger(obj, "scanCode"))
                {
                    scanCode = obj.get("scanCode").getAsInt();
                }
                if (JsonUtils.hasObject(obj, "type"))
                {
                    type = KeyTypeWrapper.fromJson(obj.get("type").getAsJsonObject());
                }

                if (name.isEmpty() || scanCode == ScanCodes.SCAN_UNKNOWN || type == null)
                {
                    return null;
                }

                return new KeyCodeWrapper(name, scanCode, type);
            }
        }
        catch (Exception e)
        {
            // ERROR
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "KeyCode[" +
               "{name=" + this.name + "}" +
               ",{type=" + this.type.getName() + "}" +
               ",{scanCode=" + this.scanCode + "}" +
               ",{translationKey=" + this.getTranslationKey() + "}" +
               "]";
    }

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (obj != null)
		{
			if (this.getClass() == obj.getClass())
			{
				KeyCodeWrapper wrapper = (KeyCodeWrapper) obj;

				return (wrapper.scanCode == this.scanCode && wrapper.type.equals(this.type));
			}
			else if (InputConstants.Key.class == obj.getClass() &&
					this.getVanilla() != null)
			{
				return this.getVanilla().equals(obj);
			}
		}

		return false;
	}

    public void runDebug()
    {
        System.out.printf("[DB] --> %s\n", this.toString());
    }
}
