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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
//#if MC >= 12109
//$$ import net.minecraft.client.KeyMapping;
//$$ import net.minecraft.resources.ResourceLocation;
//#endif

import com.sakuraryoko.vkm.keybind.KeybindUtil;

public class KeyCategory
{
    private static final KeyCategory INSTANCE = new KeyCategory();
    public static KeyCategory getInstance() { return INSTANCE; }
    private final List<Entry> entries;

    private KeyCategory()
    {
        this.entries = new ArrayList<>();
        this.buildVanillaList();
    }

//#if MC >= 12109
    //$$ private void buildVanillaList()
    //$$ {
        //$$ List<KeyMapping.Category> set = KeybindUtil.getCategoriesVanilla();

        //$$ for (KeyMapping.Category e : set)
        //$$ {
            //$$ this.entries.add(new Entry(e));
        //$$ }
    //$$ }
//#else
    private void buildVanillaList()
    {
        Set<String> set = KeybindUtil.getCategoriesVanilla();

        for (String e : set)
        {
            this.entries.add(new Entry(e));
        }
    }
//#endif

    public void add(Entry entry)
    {
        if (this.get(entry.getName()) == null)
        {
            this.entries.add(entry);
        }
    }

    public boolean contains(Entry ent)
    {
        return this.entries.contains(ent);
    }

    public Entry get(String name)
    {
        for (Entry e : this.entries)
        {
            if (e.getName().equalsIgnoreCase(name))
            {
                return e;
            }
        }

        return null;
    }

    public void remove(Entry ent)
    {
        this.entries.removeIf(e -> e.getName().equalsIgnoreCase(ent.getName()));
    }

    public boolean isEmpty()
    {
        return this.entries.isEmpty();
    }

    public int size()
    {
        return this.entries.size();
    }

    public void clear()
    {
        this.entries.clear();
    }

    public static class Entry
    {
        private String name;

        Entry(String name)
        {
            this.name = name;
        }

//#if MC >= 12109
    //$$ Entry(KeyMapping.Category vanilla)
    //$$ {
            //$$ this.name = vanilla.id().toString();
    //$$ }

    //$$ public KeyMapping.Category toVanilla()
    //$$ {
        //$$ return new KeyMapping.Category(ResourceLocation.tryParse(this.getName()));
    //$$ }
//#else
        public String toVanilla()
        {
            return this.getName();
        }

//#endif
        public String getName()
{
    return this.name;
}

        public void setName(String name)
        {
            this.name = name;
        }

        public static Entry fromString(String str)
        {
            return new Entry(str);
        }

        public JsonElement toJson()
        {
            return new JsonPrimitive(this.getName()).getAsJsonObject();
        }

        @Override
        public String toString()
        {
            return this.getName();
        }
    }
}
