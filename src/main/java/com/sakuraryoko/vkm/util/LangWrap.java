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

//#if MC >= 26.2
//$$ import net.minecraft.locale.Language;
//#else
import net.minecraft.client.resources.language.I18n;
//#endif

public class LangWrap
{
	public static boolean has(String id)
	{
		//#if MC >= 26.2
		//$$ return Language.getInstance().has(id);
		//#else
		return I18n.exists(id);
		//#endif
	}

	public static String get(String id)
	{
		//#if MC >= 26.2
		//$$ return Language.getInstance().getOrDefault(id);
		//#else
		return I18n.get(id);
		//#endif
	}
}
