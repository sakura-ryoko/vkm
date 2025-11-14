/*
 * This file is part of the Vanilla Keybind Manager project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Sakura-Ryoko and contributors
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

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
//#if MC >= 12109
//$$ import net.minecraft.client.KeyMapping;
//#endif

public class KeyCategoryWrapper
{
    private final KeyCategory.Entry entry;
//#if MC >= 12109
    //$$ private final KeyMapping.Category vanilla;
//#else
    private final String vanilla;
//#endif

    public KeyCategoryWrapper(KeyCategory.Entry entry)
    {
        this.entry = entry;
        this.vanilla = entry.toVanilla();
        this.putIfEmpty(entry);
    }

    private void putIfEmpty(KeyCategory.Entry entry)
    {
        if (!KeyCategory.getInstance().contains(entry))
        {
            KeyCategory.getInstance().add(entry);
        }
    }

    public KeyCategory.Entry getEntry()
    {
        return this.entry;
    }

//#if MC >= 12109
    //$$ private KeyCategoryWrapper(KeyMapping.Category vanilla)
    //$$ {
        //$$ KeyCategory.Entry entry = new KeyCategory.Entry(vanilla);
        //$$ this.vanilla = vanilla;
        //$$ this.entry = entry;
        //$$ this.putIfEmpty(entry);
    //$$ }

    //$$ public static KeyCategoryWrapper fromVanilla(KeyMapping.Category vanilla)
    //$$ {
        //$$ return new KeyCategoryWrapper(vanilla);
    //$$ }

    //$$ public KeyMapping.Category getVanilla()
    //$$ {
        //$$ return this.vanilla;
    //$$ }

//#else
    private KeyCategoryWrapper(String vanilla)
    {
        KeyCategory.Entry entry = new KeyCategory.Entry(vanilla);
        this.vanilla = vanilla;
        this.entry = entry;
        this.putIfEmpty(entry);
    }

    public static KeyCategoryWrapper fromVanilla(String vanilla)
    {
        return new KeyCategoryWrapper(vanilla);
    }

    public String getVanilla()
    {
        return this.vanilla;
    }
//#endif

    public JsonElement toJson()
    {
        return this.entry.toJson();
    }

    @Nullable
    public static KeyCategoryWrapper fromJson(JsonElement obj)
    {
        try
        {
            if (obj.isJsonPrimitive())
            {
                return new KeyCategoryWrapper(KeyCategory.Entry.fromString(obj.getAsString()));
            }
        }
        catch (Exception ignored) { }

        return null;
    }

    @Override
    public String toString()
    {
        return this.getEntry().toString();
    }
}
