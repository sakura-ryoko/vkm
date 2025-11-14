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

package com.sakuraryoko.vkm.config;

import java.util.List;
import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import com.sakuraryoko.vkm.Reference;

public class Hotkeys
{
    private static final String HOTKEYS_KEY = Reference.MOD_ID + ".config.hotkeys";

//#if MC >= 12100
//$$    public static final ConfigHotkey OPEN_CONFIG_GUI				= new ConfigHotkey("openConfigGui", "O,C").apply(HOTKEYS_KEY);
//$$    public static final ConfigHotkey RESET_ALL_VANILLA_KEYBINDS		= new ConfigHotkey("resetAllVanillaKeybinds", "").apply(HOTKEYS_KEY);
//#else
    public static final ConfigHotkey OPEN_CONFIG_GUI            = new ConfigHotkey("openConfigGui", "O,C", HOTKEYS_KEY+".comment.openConfigGui");
	public static final ConfigHotkey RESET_ALL_VANILLA_KEYBINDS = new ConfigHotkey("resetAllVanillaKeybinds", "", HOTKEYS_KEY+".comment.resetAllVanillaKeybinds");
//#endif

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
			RESET_ALL_VANILLA_KEYBINDS
    );
}
