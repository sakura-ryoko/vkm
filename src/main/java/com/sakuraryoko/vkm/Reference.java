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

package com.sakuraryoko.vkm;

import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.SharedConstants;

public class Reference
{
    public static final String MOD_ID = "vkm";
    public static final String MOD_NAME = "Vanilla Keybind Manager";
    public static final String MOD_SHORT_NAME = "Vanilla Key Mngr";
    public static final String MOD_VERSION = StringUtils.getModVersionString(MOD_ID);
//#if MC >= 12106
//$$ public static final String MC_VERSION = SharedConstants.getCurrentVersion().name();
//#else
    public static final String MC_VERSION = SharedConstants.getCurrentVersion().getName();
//#endif
    public static final String MOD_TYPE = "fabric";
    public static final String MOD_STRING = MOD_ID + "-" + MOD_TYPE + "-" + MC_VERSION + "-" + MOD_VERSION;
}
