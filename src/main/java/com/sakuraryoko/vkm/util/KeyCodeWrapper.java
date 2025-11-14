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
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
//#if MC >= 11904
//#else
import net.minecraft.network.chat.TextComponent;
//#endif

import fi.dy.masa.malilib.util.JsonUtils;

public class KeyCodeWrapper
{
    @Nullable
    private final InputConstants.Key vanilla;
    private final String name;
    private final KeyTypeWrapper type;
    private final int keyCode;

    public KeyCodeWrapper(InputConstants.Key keyCode)
    {
        this.vanilla = keyCode;
        this.name = keyCode.getName();
        this.keyCode = keyCode.getValue();
        this.type = new KeyTypeWrapper(keyCode.getType());
    }

    public KeyCodeWrapper(String name, int keyCode, KeyTypeWrapper type)
    {
        this.name = name;
		this.type = type;
        this.keyCode = keyCode;
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

    public int getKeyCode()
    {
        return this.keyCode;
    }

    public String getTranslationKey()
    {
//#if MC >= 11605
        //$$ if (this.vanilla != null)
        //$$ {
            //$$ return this.vanilla.getName();
        //$$ }
//#endif

        return this.name;
    }

    public Component getTranslated()
    {
        //#if MC >= 11605
        //$$ if (this.vanilla != null)
        //$$ {
            //$$ return this.vanilla.getDisplayName();
        //$$ }
//#endif

        if (I18n.exists(this.getTranslationKey()))
        {
//#if MC >= 11904
            //$$ return Component.literal(I18n.get(this.getTranslationKey()));
//#else
            return new TextComponent(I18n.get(this.getTranslationKey()));
//#endif
        }

//#if MC >= 11904
        //$$ return Component.literal(this.getTranslationKey());
//#else
        return new TextComponent(this.getTranslationKey());
//#endif
    }

    public JsonElement toJson()
    {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", this.getName());
        obj.addProperty("keyCode", this.getKeyCode());
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
                int keyCode = -1;
                KeyTypeWrapper type = null;

                if (JsonUtils.hasString(obj, "name"))
                {
                    name = obj.get("name").getAsString();
                }
                if (JsonUtils.hasInteger(obj, "keyCode"))
                {
                    keyCode = obj.get("keyCode").getAsInt();
                }
                if (JsonUtils.hasObject(obj, "type"))
                {
                    type = KeyTypeWrapper.fromJson(obj.get("type").getAsJsonObject());
                }

                if (name.isEmpty() || keyCode == -1 || type == null)
                {
                    return null;
                }

                return new KeyCodeWrapper(name, keyCode, type);
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
               ",{keyCode=" + this.keyCode + "}" +
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

				return (wrapper.keyCode == this.keyCode && wrapper.type.equals(this.type));
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
