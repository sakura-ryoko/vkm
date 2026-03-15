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

//#if MC >= 12100
//$$ import java.nio.file.Files;
//$$ import java.nio.file.Path;
//#else
import java.io.File;
//#endif

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import com.sakuraryoko.vkm.Reference;
import com.sakuraryoko.vkm.VanKeyMngr;
import com.sakuraryoko.vkm.keybind.KeybindManager;
import com.sakuraryoko.vkm.util.JsonUtils;

public class Configs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID+".json";

    private static final String GENERIC_KEY = Reference.MOD_ID+".config.generic";
    public static class Generic
    {
//#if MC >= 12100
//$$        public static final ConfigString  CATEGORY_COLOR_PREFIX                 = new ConfigString  ("categoryColorPrefix", "§d").apply(GENERIC_KEY);
//$$        public static final ConfigBoolean DEBUG_MESSAGES                        = new ConfigBoolean ("debugMessages", false).apply(GENERIC_KEY);
//$$        public static final ConfigString  NAME_COLOR_PREFIX                     = new ConfigString  ("nameColorPrefix", "§b").apply(GENERIC_KEY);
//#else
        public static final ConfigString  CATEGORY_COLOR_PREFIX                 = new ConfigString  ("categoryColorPrefix", "§d", GENERIC_KEY+".comment.categoryColorPrefix");
        public static final ConfigBoolean DEBUG_MESSAGES                        = new ConfigBoolean ("debugMessages", false, GENERIC_KEY+".comment.debugMessages");
        public static final ConfigString  NAME_COLOR_PREFIX                     = new ConfigString  ("nameColorPrefix", "§b", GENERIC_KEY+".comment.nameColorPrefix");
//#endif

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                DEBUG_MESSAGES,
                CATEGORY_COLOR_PREFIX,
                NAME_COLOR_PREFIX
        );
    }

    @Override
    public void onConfigsChanged()
    {
        saveToFile();
        loadFromFile();
    }

    public static void loadFromFile()
    {
//#if MC >= 12100
        //$$ Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);

        //$$ if (Files.exists(configFile) && Files.isReadable(configFile))
        //$$ {
            //$$ JsonElement element = JsonUtils.parseJsonFile(configFile);

            //$$ if (element != null && element.isJsonObject())
            //$$ {
                //$$ JsonObject root = element.getAsJsonObject();

                //$$ ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                //$$ ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
            //$$ }
        //$$ }
        //$$ else
        //$$ {
            //$$ VanKeyMngr.LOGGER.error("loadFromFile(): Failed to load config file '{}'.", configFile.toAbsolutePath());
        //$$ }
//#else
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
            }
        }
        else
        {
            VanKeyMngr.LOGGER.error("loadFromFile(): Failed to load config file '{}'.", configFile.getAbsolutePath());
        }
//#endif

        KeybindManager.getInstance().resync();
    }

    public static void saveToFile()
    {
//#if MC >= 12100
        //$$ Path dir = FileUtils.getConfigDirectoryAsPath();

        //$$ if (!Files.exists(dir))
        //$$ {
            //$$ FileUtils.createDirectoriesIfMissing(dir);
        //$$ }

        //$$ if (Files.isDirectory(dir))
        //$$ {
            //$$ JsonObject root = new JsonObject();

            //$$ ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            //$$ ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);

            //$$ JsonUtils.writeJsonToFile(root, dir.resolve(CONFIG_FILE_NAME));
        //$$ }
        //$$ else
        //$$ {
            //$$ VanKeyMngr.LOGGER.error("saveToFile(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        //$$ }
//#else
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
        else
        {
            VanKeyMngr.LOGGER.error("saveToFile(): Config Folder '{}' does not exist!", dir.getAbsolutePath());
        }
//#endif
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
