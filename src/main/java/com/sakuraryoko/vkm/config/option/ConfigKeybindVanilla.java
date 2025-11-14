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

package com.sakuraryoko.vkm.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IStringRepresentable;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import com.sakuraryoko.vkm.keybind.KeybindUtil;
import com.sakuraryoko.vkm.keybind.KeybindVanilla;
import com.sakuraryoko.vkm.keybind.KeybindWrapper;

public class ConfigKeybindVanilla extends ConfigBase<ConfigHotkey> implements IHotkey, IStringRepresentable
{
    private final KeybindVanilla vanillaKeybind;

    public ConfigKeybindVanilla(KeybindWrapper keybind)
    {
//#if MC >= 12100
        //$$ super(ConfigType.HOTKEY, KeybindUtil.buildConfigName(keybind), KeybindUtil.buildConfigComment(keybind), KeybindUtil.buildConfigPrettyName(keybind), "");
//#else
        super(ConfigType.HOTKEY, KeybindUtil.buildConfigName(keybind), KeybindUtil.buildConfigComment(keybind), KeybindUtil.buildConfigPrettyName(keybind));
//#endif
        this.vanillaKeybind = new KeybindVanilla(keybind);
    }

    @Override
    public IKeybind getKeybind()
    {
        return this.vanillaKeybind;
    }

    @Override
    public String getStringValue()
    {
        return this.vanillaKeybind.getStringValue();
    }

    @Override
    public String getDefaultStringValue()
    {
        return this.vanillaKeybind.getDefaultStringValue();
    }

    @Override
    public void setValueFromString(String value)
    {
        this.vanillaKeybind.setValueFromString(value);
    }

    @Override
    public boolean isModified()
    {
        return this.vanillaKeybind.isModified();
    }

    @Override
    public boolean isModified(String newValue)
    {
        return this.vanillaKeybind.isModified(newValue);
    }

    @Override
    public void resetToDefault()
    {
        this.vanillaKeybind.resetToDefault();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element)
    {
        // NO-OP
    }

    @Override
    public JsonElement getAsJsonElement()
    {
        JsonObject obj = new JsonObject();

        obj.add("keys", new JsonPrimitive(this.getKeybind().getStringValue()));

        return obj;
    }
}
