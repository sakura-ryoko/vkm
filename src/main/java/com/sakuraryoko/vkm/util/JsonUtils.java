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

//#if MC >= 1.21.00
//$$ import java.nio.file.Path;
//$$ import com.google.gson.JsonElement;
//$$ import com.google.gson.JsonObject;
//#endif

public class JsonUtils
	//#if MC >= 1.21.11
//$$		extends fi.dy.masa.malilib.util.data.json.JsonUtils
	//#else
		extends fi.dy.masa.malilib.util.JsonUtils
	//#endif
{
	//#if MC >= 1.21.11
	//$$ public static JsonElement parseJsonFile(Path file)
	//$$ {
	//$$ return fi.dy.masa.malilib.util.data.json.JsonUtils.parseJsonFile(file);
	//$$ }

	//$$ public static void writeJsonToFile(JsonObject root, Path file)
	//$$ {
	//$$ fi.dy.masa.malilib.util.data.json.JsonUtils.writeJsonToFile(root, file);
	//$$ }
	//#elseif MC >= 1.21.00
	//$$ public static JsonElement parseJsonFile(Path file)
	//$$ {
	//$$ return fi.dy.masa.malilib.util.JsonUtils.parseJsonFileAsPath(file);
	//$$ }

	//$$ public static void writeJsonToFile(JsonObject root, Path file)
	//$$ {
	//$$ fi.dy.masa.malilib.util.JsonUtils.writeJsonToFileAsPath(root, file);
	//$$ }
	//#else
	//#endif
}
