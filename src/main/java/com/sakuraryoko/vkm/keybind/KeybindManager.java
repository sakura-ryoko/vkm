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

package com.sakuraryoko.vkm.keybind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import com.sakuraryoko.vkm.Reference;
import com.sakuraryoko.vkm.VanKeyMngr;
import com.sakuraryoko.vkm.config.option.ConfigKeybindVanilla;

public class KeybindManager
{
	private static final KeybindManager INSTANCE = new KeybindManager();
	public static KeybindManager getInstance() { return INSTANCE; }
	private final List<KeybindWrapper> keybinds;
    private final Comparator<KeybindWrapper> CAT_COMPARATOR = Comparator.comparing(KeybindWrapper::getCategoryAsString);
    private final Comparator<KeybindWrapper> ID_COMPARATOR = Comparator.comparing(KeybindWrapper::getId);
    private final Comparator<KeybindWrapper> COMPARATOR = CAT_COMPARATOR.thenComparing(ID_COMPARATOR);
    public final List<ConfigKeybindVanilla> VANILLA_KEYBINDS;

	public KeybindManager()
	{
		this.keybinds = new ArrayList<>();
        this.VANILLA_KEYBINDS = new ArrayList<>();
	}

    public void reset(boolean isLogout)
    {
        // NO-OP
    }

    public void onWorldPre()
    {
        // NO-OP
    }

    public void onWorldJoin()
    {
        this.resync();
    }

    public void resync()
	{
		VanKeyMngr.debugLog("KeybindManager#resync()");
		this.clear();

		KeybindUtil.MAP_BY_ID.forEach(this::addEach);
        this.keybinds.sort(COMPARATOR);
        this.buildKeybinds();
	}

	private void addEach(String id, KeyMapping keybind)
	{
		this.keybinds.add(new KeybindWrapper(keybind));
	}

	private void clear()
	{
		this.keybinds.clear();
	}

    public void buildKeybinds()
    {
        ImmutableList.Builder<ConfigKeybindVanilla> builder = new ImmutableList.Builder<>();

        this.keybinds.forEach(
                (entry) ->
                        builder.add(new ConfigKeybindVanilla(entry))
        );

        this.VANILLA_KEYBINDS.clear();
        this.VANILLA_KEYBINDS.addAll(builder.build());

        InputEventHandler.getKeybindManager().addHotkeysForCategory(Reference.MOD_ID, "minecraft", this.VANILLA_KEYBINDS);
//        InputEventHandler.getKeybindManager().updateUsedKeys();
    }

	public void resetAllKeybinds()
	{
		Minecraft mc = Minecraft.getInstance();
		VanKeyMngr.debugLog("KeybindManager#resetAllKeybinds()");

		//#if MC >= 26.2
		//$$ if (mc.gui.screen() == null || mc.gui.screen() instanceof TitleScreen)
		//#else
		if (mc.screen == null || mc.screen instanceof TitleScreen)
		//#endif
		{
			this.resync();
			this.keybinds.forEach(KeybindWrapper::reset);
			InfoUtils.showInGameMessage(Message.MessageType.SUCCESS, "vkm.message.reset_all_keybinds.success");
		}
		else
		{
			InfoUtils.showInGameMessage(Message.MessageType.ERROR, "vkm.message.reset_all_keybinds.error");
		}
	}
}
