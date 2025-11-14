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

package com.sakuraryoko.vkm.compat.modmenu;

//#if MC >= 11605
//$$ import com.terraformersmc.modmenu.api.ConfigScreenFactory;
//$$ import com.terraformersmc.modmenu.api.ModMenuApi;
//#else
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Function;
//#endif

import com.sakuraryoko.vkm.Reference;
import com.sakuraryoko.vkm.gui.GuiConfigs;

public class ModMenuImpl implements ModMenuApi
{
//#if MC >= 11605
//#else
    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }
//#endif

    @Override
//#if MC >= 11605
//$$ public ConfigScreenFactory<?> getModConfigScreenFactory()
//#else
public Function<Screen, ? extends Screen> getConfigScreenFactory()
//#endif
    {
        return (screen) -> {
            GuiConfigs gui = new GuiConfigs();
            gui.setParent(screen);
            return gui;
        };
    }
}
