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

package com.sakuraryoko.vkm.keybind;

import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import fi.dy.masa.malilib.util.input.KeyCodes;
import fi.dy.masa.malilib.util.input.ScanCodes;
import com.sakuraryoko.vkm.VanKeyMngr;
import com.sakuraryoko.vkm.util.*;

public class KeybindWrapper
{
	private final String id;
	private final KeyCategoryWrapper category;
	private final KeyCodeWrapper defaultKeyCode;
	private KeyCodeWrapper keyCodeWrapper;
	private String translationKey;
	private boolean pressed;

	public KeybindWrapper(String id, int scanCode, int keyCode, KeyCategoryWrapper category)
	{
		this(id, scanCode, keyCode, category, id);
	}

	public KeybindWrapper(String id, int scanCode, int keyCode, KeyCategoryWrapper category, String translationKey)
	{
		KeyTypeWrapper type = KeyTypeWrapper.fromScanCode(scanCode, keyCode);

		if (type.getType() == KeyType.UNKNOWN && scanCode == -1)
		{
			scanCode = keyCode;
		}

		this.id = id;
		this.defaultKeyCode = new KeyCodeWrapper(id, scanCode, type);
		this.keyCodeWrapper = this.defaultKeyCode;
		this.category = category;
		this.translationKey = translationKey;
		this.pressed = false;
	}

	public KeybindWrapper(KeyMapping keyBinding)
	{
		this.id = keyBinding.getName();
		this.category = KeyCategoryWrapper.fromVanilla(keyBinding.getCategory());
		this.defaultKeyCode = new KeyCodeWrapper(keyBinding.getDefaultKey());
		this.keyCodeWrapper = new KeyCodeWrapper(keyBinding.key);
        this.translationKey = keyBinding.key.getName();
		this.pressed = false;
	}

	private KeybindWrapper(String id, KeyCategoryWrapper category, String translationKey, KeyCodeWrapper def, KeyCodeWrapper key)
	{
		this.id = id;
		this.category = category;
		this.translationKey = translationKey;
		this.defaultKeyCode = def;
		this.keyCodeWrapper = key;
	}

	private String buildVanillaTranslationKey(InputConstants.Key key)
	{
		String keyCodeName = key.getName();
		int code = key.getValue();

		String scanCodeName = switch (key.getType())
		{
			case KEYBOARD -> KeybindUtil.getScancodeName(code);
			case MOUSE -> KeybindUtil.getTypeName(InputConstants.Type.MOUSE) + "." + (code + 1);
		};

		return scanCodeName == null ? LangWrap.get(keyCodeName) : scanCodeName;
	}

	public String getId()
	{
		return this.id;
	}

	public KeyCategoryWrapper getCategory()
	{
		return this.category;
	}

    public String getCategoryAsString()
    {
        return this.category.toString();
    }

    public KeyCodeWrapper getDefaultKeyCode()
	{
		return this.defaultKeyCode;
	}

	public KeyCodeWrapper getKeyCodeWrapper()
	{
		return this.keyCodeWrapper;
	}

	public @Nullable InputConstants.Key getDefaultKeyCodeVanilla()
	{
		return this.defaultKeyCode.getVanilla();
	}

	public @Nullable InputConstants.Key getKeyCodeVanilla()
	{
		return this.keyCodeWrapper.getVanilla();
	}

	public void setKeyCodeWrapper(@Nonnull KeyCodeWrapper keyCodeWrapper)
	{
		this.keyCodeWrapper = keyCodeWrapper;
	}

	public void setKey(@Nonnull InputConstants.Key key)
	{
		this.keyCodeWrapper = new KeyCodeWrapper(key);
	}

	public void setPressed(boolean toggle)
	{
		this.pressed = toggle;
	}

	public boolean isPressed()
	{
		return this.pressed;
	}

	public boolean isBound()
	{
		return !(this.keyCodeWrapper.getScanCode() == ScanCodes.SCAN_UNKNOWN);
	}

	public void setTranslationKey(String translationKey)
	{
		this.translationKey = translationKey;
	}

	public String getTranslationKey()
	{
		return this.translationKey;
	}

	public String getTranslated()
	{
		if (LangWrap.has(this.translationKey))
		{
			return LangWrap.get(this.translationKey);
		}

        if (LangWrap.has(this.id))
        {
            return LangWrap.get(this.id);
        }

		return this.id;
	}

    public String getTranslatedCategory()
    {
        return this.category.getVanilla().label().getString();
    }

    public String getTranslatedId()
    {
	    if (LangWrap.has(this.id))
        {
            return LangWrap.get(this.id);
        }

        return this.id;
    }

    public String getBoundTranslationKey()
    {
        if (this.keyCodeWrapper != null)
        {
            return this.keyCodeWrapper.getTranslationKey();
        }

        return this.translationKey;
    }

    public Component getBoundTranslated()
    {
        if (this.keyCodeWrapper != null)
        {
            return this.keyCodeWrapper.getTranslated();
        }

        return Component.literal(this.getTranslated());
    }

	public boolean matchesKey(int scanCode, int keyCode)
	{
		if (scanCode == ScanCodes.SCAN_UNKNOWN)
		{
			return this.keyCodeWrapper.getType().isUnknown() && this.keyCodeWrapper.getScanCode() == scanCode;
		}
		else
		{
			return this.keyCodeWrapper.getType().isKeyboard() && this.keyCodeWrapper.getScanCode() == scanCode;
		}
	}

	public boolean matchesMouse(int scanCode)
	{
		return this.keyCodeWrapper.getType().isMouse() && this.keyCodeWrapper.getScanCode() == scanCode;
	}

	public boolean isDefault()
	{
		return this.keyCodeWrapper.equals(this.defaultKeyCode);
	}

	public KeyMapping getVanillaById()
	{
		return KeybindUtil.getByIdVanilla(this.id);
	}

    public void reset()
    {
		if (!this.isDefault())
		{
			VanKeyMngr.debugLog("KeybindWrapper#reset(): id: [{}]", this.id);
			this.keyCodeWrapper = this.defaultKeyCode;
			KeybindUtil.resetByID(this.id);
		}

        this.pressed = false;
    }

    public void update(int scanCode, int keyCode, KeyType type)
    {
		VanKeyMngr.debugLog("KeybindWrapper#update():IN: scanCode: [{}], keyCode: [{}]", scanCode, keyCode);
		InputConstants.Key key;

		if (type == KeyType.KEYBOARD)
		{
            key = InputConstants.getKey(new KeyEvent(scanCode, keyCode, KeyCodes.KMOD_NONE));
		}
		else
		{
			key = this.matchMouseKeyCode(scanCode);
		}

        this.reset();
        KeybindUtil.updateByID(this.id, key);
        this.keyCodeWrapper = new KeyCodeWrapper(key);
		VanKeyMngr.debugLog("KeybindWrapper#update():OUT: name: [{}], scanCode: [{}]", this.keyCodeWrapper.getName(), this.keyCodeWrapper.getScanCode());
    }

	public InputConstants.Key matchMouseKeyCode(int scanCode)
	{
		AtomicReference<InputConstants.Key> result = new AtomicReference<>(KeybindUtil.UNKNOWN_KEYCODE);

		KeybindUtil.MAP_BY_NAME.forEach(
				(str, key) ->
				{
					if (key.getValue() == scanCode)
					{
						result.set(key);
					}
				}
		);

		return result.get();
	}

    public void clearKey()
    {
		VanKeyMngr.debugLog("KeybindWrapper#clearKey(): id: [{}]", this.id);
        InputConstants.Key key = KeybindUtil.UNKNOWN_KEYCODE;
        this.reset();
        KeybindUtil.updateByID(this.id, key);
        this.keyCodeWrapper = new KeyCodeWrapper(key);
    }

	public JsonElement toJson()
	{
		JsonObject obj = new JsonObject();

		obj.addProperty("id", this.id);
		obj.addProperty("translationKey", this.translationKey);
        obj.add("category", this.category.toJson());
		obj.add("defaultKeyCode", this.defaultKeyCode.toJson());
		obj.add("keyCodeWrapper", this.keyCodeWrapper.toJson());

		return obj;
	}

	@Nullable
	public static KeybindWrapper fromJson(JsonElement element)
	{
		try
		{
			if (element.isJsonObject())
			{
				JsonObject obj = element.getAsJsonObject();

				String id = "";
				KeyCategoryWrapper cat = null;
				String transKey = "";
				KeyCodeWrapper def = null;
				KeyCodeWrapper key = null;

				if (JsonUtils.hasString(obj, "id"))
				{
					id = obj.get("id").getAsString();
				}
				if (JsonUtils.hasString(obj, "translationKey"))
				{
					transKey = obj.get("translationKey").getAsString();
				}
                if (JsonUtils.hasObject(obj, "category"))
                {
                    cat = KeyCategoryWrapper.fromJson(obj.get("category").getAsJsonObject());
                }
				if (JsonUtils.hasObject(obj, "defaultKeyCode"))
				{
					def = KeyCodeWrapper.fromJson(obj.get("defaultKeyCode").getAsJsonObject());
				}
				if (JsonUtils.hasObject(obj, "keyCodeWrapper"))
				{
					key = KeyCodeWrapper.fromJson(obj.get("keyCodeWrapper").getAsJsonObject());
				}

				if (!id.isEmpty() && cat != null && def != null && key != null)
				{
					return new KeybindWrapper(id, cat, transKey, def, key);
				}
			}
		}
		catch (Exception e)
		{
			// Error
		}

		return null;
	}

	@Override
    public String toString()
    {
        return "Keybind[" +
               "{id=" + this.id + "}" +
               ",{translationKey=" + this.translationKey + "}" +
               ",{category=" + this.category.toString() + "}" +
               ",{defaultKeyCode=" + this.defaultKeyCode.toString() + "}" +
               ",{keyCodeWrapper=" +
               (this.keyCodeWrapper != null ? this.keyCodeWrapper.toString() : "[NOT-BOUND]") +
               "}";
    }

    public void runDebug()
    {
        System.out.printf("[DB] %s\n", this.toString());
    }
}
