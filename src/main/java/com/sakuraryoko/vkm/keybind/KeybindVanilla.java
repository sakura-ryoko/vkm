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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

//#if MC >= 12105
//$$ import com.mojang.serialization.Codec;
//#else
//#endif

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.KeyCodes;
import com.sakuraryoko.vkm.util.KeyCodeWrapper;
import com.sakuraryoko.vkm.util.KeyType;

public class KeybindVanilla implements IKeybind
{
    private final KeybindSettings settings = KeybindSettings.DEFAULT;
    private final KeybindWrapper keybind;
    @Nullable
    private IHotkeyCallback callback;
    private boolean dirty = false;

    public KeybindVanilla(KeybindWrapper keybind)
    {
        this.keybind = keybind;
        this.callback = null;
    }

    public int getDefaultKeyCode()
    {
		KeyCodeWrapper keyCode = this.keybind.getDefaultKeyCode();
	    return keyCode.getType().isMouse() ? keyCode.getKeyCode() - 100 : keyCode.getKeyCode();
    }

    public int getKeyCode()
    {
		KeyCodeWrapper keyCode = this.keybind.getKeyCode();
	    return keyCode.getType().isMouse() ? keyCode.getKeyCode() - 100 : keyCode.getKeyCode();
    }

    @Override
    public boolean isValid()
    {
        return this.keybind != null;
    }

    public boolean isDirty()
    {
        return this.dirty;
    }

    public void markDirty()
    {
        this.dirty = true;
    }

    public void markClean()
    {
        this.dirty = false;
    }

//#if MC >= 12109
//#elseif MC >= 12105
    //$$ @Override
    //$$ public Codec<? extends IKeybind> codec()
    //$$ {
        //$$ return null;
    //$$ }
//#else
//#endif

    @Override
    public boolean isPressed()
    {
        return this.keybind.isPressed();
    }

    @Override
    public boolean isKeybindHeld()
    {
        return this.keybind.isPressed();
    }

    @Override
    public boolean updateIsPressed()
    {
        boolean cancel = false;

        this.keybind.setPressed(!this.isPressed());

        if (this.callback != null)
        {
            cancel = this.callback.onKeyAction(KeyAction.PRESS, this);
        }

        return cancel;
    }

    @Override
    public KeybindSettings getSettings()
    {
        return this.settings;
    }

    @Override
    public void setSettings(KeybindSettings settings)
    {
        // Cannot change Settings of Vanilla Keybinds
    }

    @Override
    public void clearKeys()
    {
        this.keybind.clearKey();
    }

    @Override
    public void addKey(int keyCode)
    {
		KeyType type = KeyType.KEYBOARD;
	    int origKeyCode = keyCode;

		if (keyCode < -1)
		{
			type = KeyType.MOUSE;
			keyCode += 100;
		}

		this.keybind.update(keyCode, -1, type);
    }

    @Override
    public void removeKey(int keyCode)
    {
        this.keybind.clearKey();
    }

    @Override
    public boolean matches(int keyCode)
    {
        return this.keybind.matchesKey(keyCode, -1) || this.keybind.matchesMouse(keyCode);
    }

    @Override
    public boolean overlaps(IKeybind other)
    {
        if (other == this ||
            other.getKeys().isEmpty() || other.getKeys().getFirst() == -1)
        {
            return false;
        }

		int keyCode = this.getKeyCode();
		if (keyCode == -1) return false;

        if (other instanceof KeybindVanilla)
        {
            KeybindVanilla kbv = (KeybindVanilla) other;

            if (Objects.equals(kbv.keybind.getId(), this.keybind.getId()))
            {
                return false;
            }

            return kbv.keybind.matchesKey(keyCode, -1) || kbv.keybind.matchesMouse(keyCode);
        }

        // Cloned from KeybindMulti to have similar behavior.
        if (this.contextOverlaps(other))
        {
            KeybindSettings settingsOther = other.getSettings();
            boolean o1 = this.settings.isOrderSensitive();
            boolean o2 = settingsOther.isOrderSensitive();
            List<Integer> keys1 = this.getKeys();
            List<Integer> keys2 = other.getKeys();
            int l1 = keys1.size();
            int l2 = keys2.size();

            if (l1 == 0 || l2 == 0)
            {
                return false;
            }

            if ((this.settings.getAllowExtraKeys() == false && l1 < l2 && keys1.get(0) != keys2.get(0)) ||
                (settingsOther.getAllowExtraKeys() == false && l2 < l1 && keys1.get(0) != keys2.get(0)))
            {
                return false;
            }

            // Both are order sensitive, try to "slide the shorter sequence over the longer sequence" to find a match
            if (o1 && o2)
            {
                return l1 < l2 ? Collections.indexOfSubList(keys2, keys1) != -1 : Collections.indexOfSubList(keys1, keys2) != -1;
            }
            // At least one of the keybinds is not order sensitive
            else
            {
                return l1 <= l2 ? keys2.containsAll(keys1) : keys1.containsAll(keys2);
            }
        }

		return false;
    }

	// Cloned from KeybindMulti
	public boolean contextOverlaps(IKeybind other)
	{
		KeybindSettings settingsOther = other.getSettings();
		KeybindSettings.Context c1 = this.settings.getContext();
		KeybindSettings.Context c2 = settingsOther.getContext();

		if (c1 == KeybindSettings.Context.ANY || c2 == KeybindSettings.Context.ANY || c1 == c2)
		{
			KeyAction a1 = this.settings.getActivateOn();
			KeyAction a2 = settingsOther.getActivateOn();

			if (a1 == KeyAction.BOTH || a2 == KeyAction.BOTH || a1 == a2)
            {
                return true;
            }
		}

		return false;
	}

	@Override
    public void tick()
    {
        // NO-OP
    }

    @Override
    public String getKeysDisplayString()
    {
        return this.getStringValue();
    }

    @Override
    public List<Integer> getKeys()
    {
        List<Integer> list = new ArrayList<>();

        list.add(this.getKeyCode());

        return list;
    }

    @Override
    public void setCallback(@Nullable IHotkeyCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public boolean areSettingsModified()
    {
        return false;
    }

    @Override
    public void resetSettingsToDefaults()
    {
        // NO-OP
    }

    @Override
    public boolean isModified()
    {
        return !this.keybind.isDefault();
    }

    @Override
    public void resetToDefault()
    {
        this.keybind.reset();
    }

    @Override
    public String getDefaultStringValue()
    {
        return KeyCodes.getNameForKey(this.getDefaultKeyCode());
    }

    @Override
    public void setValueFromString(String value)
    {
        String[] split = value.split(",");

		int keyCode = KeyCodes.getKeyCodeFromName(split[0]);

        this.addKey(keyCode);
    }

    @Override
    public boolean isModified(String newValue)
    {
        return !this.getDefaultStringValue().equals(newValue);
    }

    @Override
    public String getStringValue()
    {
        String name = KeyCodes.getNameForKey(this.getKeyCode());

        // For empty Keybinds (-1 keyCode)
        if (name == null)
        {
            name = "";
        }

        return name;
    }
}
