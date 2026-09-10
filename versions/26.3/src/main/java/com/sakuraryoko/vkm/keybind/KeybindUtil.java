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

package com.sakuraryoko.vkm.keybind;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import fi.dy.masa.malilib.util.input.KeyCodes;
import fi.dy.masa.malilib.util.input.ScanCodes;
import com.sakuraryoko.vkm.VanKeyMngr;
import com.sakuraryoko.vkm.config.Configs;

public class KeybindUtil
{
    protected static final Map<String, KeyMapping> MAP_BY_ID = KeyMapping.ALL;
    protected static final Map<InputConstants.Key, List<KeyMapping>> MAP_BY_CODE = KeyMapping.MAP;
    protected static final List<KeyMapping.Category> SET_CAT_LIST = KeyMapping.Category.SORT_ORDER;
    protected static final Map<String, InputConstants.Key> MAP_BY_NAME = InputConstants.Key.NAME_MAP;
	protected static final InputConstants.Key UNKNOWN_KEYCODE = InputConstants.UNKNOWN;

	@Nullable
    protected static KeyMapping getByIdVanilla(final String id)
	{
		return MAP_BY_ID.getOrDefault(id, null);
	}

    public static List<KeyMapping.Category> getCategoriesVanilla()
    {
        return SET_CAT_LIST;
    }

    @Nullable
    protected static InputConstants.Key getKeyCodeByName(final String name)
    {
        return MAP_BY_NAME.getOrDefault(name, null);
    }

    @Nullable
    protected static InputConstants.Key getKeyCodeByType(final InputConstants.Type type, final int key)
    {
        return type.map.getOrDefault(key, null);
    }

    @Nullable
    public static String getKeycodeName(int keyCode)
    {
        return KeyCodes.getNameForKey(keyCode);
    }

    @Nullable
    public static String getScancodeName(int scanCode)
    {
        return ScanCodes.getNameForScanCode(scanCode);
    }

    public static String getTypeName(InputConstants.Type type)
    {
        return type.name();
    }

    @Nullable
    protected static KeybindWrapper getById(final String id)
	{
		KeyMapping keyBinding = getByIdVanilla(id);

		if (keyBinding == null)
		{
			return null;
		}

		return new KeybindWrapper(keyBinding);
	}

    protected static boolean updateByID(final String id, InputConstants.Key newKey)
    {
        try
        {
            MAP_BY_ID.get(id).setKey(newKey);
            KeyMapping.resetMapping();
            return true;
        }
        catch (Exception err)
        {
            VanKeyMngr.LOGGER.error("updateByID: Exception while updating keybind '{}'; {}", id, err.getLocalizedMessage());
            return false;
        }
    }

    protected static void setPressed(KeyMapping keyBind, boolean toggle)
    {
        keyBind.setDown(toggle);
    }

    protected static boolean resetByID(final String id)
    {
        try
        {
            KeyMapping keybind = MAP_BY_ID.get(id);
            keybind.setKey(keybind.getDefaultKey());
            setPressed(keybind, false);
            MAP_BY_ID.put(id, keybind);
            KeyMapping.resetMapping();
            return true;
        }
        catch (Exception err)
        {
            VanKeyMngr.LOGGER.error("resetByID: Exception while resetting keybind '{}'; {}", id, err.getLocalizedMessage());
            return false;
        }
    }

    public static String buildConfigName(KeybindWrapper keybind)
    {
        final String cat = Configs.Generic.CATEGORY_COLOR_PREFIX.getStringValue();
        final String id = Configs.Generic.NAME_COLOR_PREFIX.getStringValue();

        return "["+cat+keybind.getTranslatedCategory()+"§r] ("+id+keybind.getTranslatedId()+"§r)";
    }

    public static String buildConfigString(KeybindWrapper keybind)
    {
        if (keybind.isDefault())
        {
            return ScanCodes.getNameForScanCode(keybind.getDefaultKeyCode().getScanCode());
        }

        return ScanCodes.getNameForScanCode(keybind.getKeyCodeWrapper().getScanCode());
    }

    public static String buildConfigComment(KeybindWrapper keybind)
    {
        final String cat = Configs.Generic.CATEGORY_COLOR_PREFIX.getStringValue();
        final String  id = Configs.Generic.NAME_COLOR_PREFIX.getStringValue();

        return "Minecraft Keybind:\nCategory: "+cat+keybind.getTranslatedCategory()+"§r\nName: "+id+keybind.getTranslatedId()+"§r";
    }

    public static String buildConfigPrettyName(KeybindWrapper keybind)
    {
        final String cat = Configs.Generic.CATEGORY_COLOR_PREFIX.getStringValue();
        final String  id = Configs.Generic.NAME_COLOR_PREFIX.getStringValue();

        return "["+cat+keybind.getTranslatedCategory()+"§r] ("+id+keybind.getTranslatedId()+"§r)";
    }

    public static int getDebugModifierKey()
    {
        KeyMapping keyBinding = getByIdVanilla("key.debug.modifier");

        if (keyBinding == null)
        {
            return -1;
        }

        InputConstants.Key key = keyBinding.key;
        return key.getValue();
    }
}
