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

package com.sakuraryoko.vkm.event;

import net.minecraft.client.Minecraft;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import com.sakuraryoko.vkm.config.Hotkeys;
import com.sakuraryoko.vkm.gui.GuiConfigs;
import com.sakuraryoko.vkm.keybind.KeybindManager;

public class KeybindCallbacks implements IHotkeyCallback
{
    private static final KeybindCallbacks INSTANCE = new KeybindCallbacks();
    public static KeybindCallbacks getInstance()
    {
        return INSTANCE;
    }

    private KeybindCallbacks() { }

    public void setCallbacks()
    {
        for (ConfigHotkey hotkey : Hotkeys.HOTKEY_LIST)
        {
            hotkey.getKeybind().setCallback(this);
        }
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key)
    {
        return this.onKeyActionImpl(action, key);
    }

    private boolean onKeyActionImpl(KeyAction action, IKeybind key)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null)
        {
            return false;
        }

        if (key == Hotkeys.OPEN_CONFIG_GUI.getKeybind())
        {
            GuiBase.openGui(new GuiConfigs());
            return true;
        }
		else if (key == Hotkeys.RESET_ALL_VANILLA_KEYBINDS.getKeybind())
		{
			KeybindManager.getInstance().resetAllKeybinds();
			return true;
		}

        return false;
    }
}
