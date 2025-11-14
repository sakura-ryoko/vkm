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

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
//#if MC >= 11502
//$$ import net.minecraft.client.multiplayer.ClientLevel;
//#else
import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif

import fi.dy.masa.malilib.interfaces.IWorldLoadListener;
import com.sakuraryoko.vkm.keybind.KeybindManager;

public class WorldLoadListener implements IWorldLoadListener
{
    @Override
//#if MC >= 11502
//$$	public void onWorldLoadPre(@Nullable ClientLevel worldBefore, @Nullable ClientLevel worldAfter, Minecraft mc)
//#else
    public void onWorldLoadPre(@Nullable MultiPlayerLevel worldBefore, @Nullable MultiPlayerLevel worldAfter, Minecraft mc)
//#endif
    {
        // Save the settings before the integrated server gets shut down
        if (worldBefore != null)
        {
            // TODO
        }

        // On World Pre()
        if (worldAfter != null)
        {
            KeybindManager.getInstance().onWorldPre();
        }
    }

    @Override
//#if MC >= 11502
//$$	public void onWorldLoadPost(@Nullable ClientLevel worldBefore, @Nullable ClientLevel worldAfter, Minecraft mc)
//#else
    public void onWorldLoadPost(@Nullable MultiPlayerLevel worldBefore, @Nullable MultiPlayerLevel worldAfter, Minecraft mc)
//#endif
    {
        // Dimension Change / Logout
        KeybindManager.getInstance().reset(worldAfter == null);

        if (worldAfter != null)
        {
            // onWorldJoin
            KeybindManager.getInstance().onWorldJoin();
        }
    }
}
