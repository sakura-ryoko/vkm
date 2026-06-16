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

import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
//#if MC >= 11904
//#else
import net.minecraft.network.chat.TextComponent;
//#endif
//#if MC >= 12109
//$$ import net.minecraft.client.input.KeyEvent;
//#endif

import com.sakuraryoko.vkm.VanKeyMngr;
import com.sakuraryoko.vkm.util.*;

public class KeybindWrapper
{
	private final String id;
	private final KeyCategoryWrapper category;
	private final KeyCodeWrapper defaultKeyCode;
	private KeyCodeWrapper keyCode;
	private String translationKey;
	private boolean pressed;

	public KeybindWrapper(String id, int keyCode, int scanCode, KeyCategoryWrapper category)
	{
		this(id, keyCode, scanCode, category, id);
	}

	public KeybindWrapper(String id, int keyCode, int scanCode, KeyCategoryWrapper category, String translationKey)
	{
		KeyTypeWrapper type = KeyTypeWrapper.fromKeyCode(keyCode, scanCode);

		if (type.getType() == KeyType.SCANCODE && keyCode == -1)
		{
			keyCode = scanCode;
		}

		this.id = id;
		this.defaultKeyCode = new KeyCodeWrapper(id, keyCode, type);
		this.keyCode = this.defaultKeyCode;
		this.category = category;
		this.translationKey = translationKey;
		this.pressed = false;
	}

	public KeybindWrapper(KeyMapping keyBinding)
	{
		this.id = keyBinding.getName();
		this.category = KeyCategoryWrapper.fromVanilla(keyBinding.getCategory());
		this.defaultKeyCode = new KeyCodeWrapper(keyBinding.getDefaultKey());
		this.keyCode = new KeyCodeWrapper(keyBinding.key);
        //#if MC >= 11605
        //$$ this.translationKey = keyBinding.key.getName();
        //#else
        this.translationKey = this.buildVanillaTranslationKey(keyBinding.key);
        //#endif
		this.pressed = false;
	}

	private KeybindWrapper(String id, KeyCategoryWrapper category, String translationKey, KeyCodeWrapper def, KeyCodeWrapper key)
	{
		this.id = id;
		this.category = category;
		this.translationKey = translationKey;
		this.defaultKeyCode = def;
		this.keyCode = key;
	}

	private String buildVanillaTranslationKey(InputConstants.Key key)
	{
		String keyCodeName = key.getName();
		int code = key.getValue();
		String scanCodeName = null;

		switch (key.getType())
		{
			case KEYSYM:
				scanCodeName = KeybindUtil.getKeycodeName(code);
				break;
			case SCANCODE:
				scanCodeName = KeybindUtil.getScancodeName(code);
				break;
			case MOUSE:
				scanCodeName = KeybindUtil.getTypeName(InputConstants.Type.MOUSE) +"."+ (code + 1);
		}

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

	public KeyCodeWrapper getKeyCode()
	{
		return this.keyCode;
	}

	public @Nullable InputConstants.Key getDefaultKeyCodeVanilla()
	{
		return this.defaultKeyCode.getVanilla();
	}

	public @Nullable InputConstants.Key getKeyCodeVanilla()
	{
		return this.keyCode.getVanilla();
	}

	public void setKeyCode(@Nonnull KeyCodeWrapper keyCode)
	{
		this.keyCode = keyCode;
	}

	public void setKeyCode(@Nonnull InputConstants.Key key)
	{
		this.keyCode = new KeyCodeWrapper(key);
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
		return !(this.keyCode.getKeyCode() == -1);
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

//#if MC >= 12109
    //$$ public String getTranslatedCategory()
    //$$ {
        //$$ return this.category.getVanilla().label().getString();
    //$$ }
//#else
    public String getTranslatedCategory()
    {
        if (LangWrap.has(this.category.getVanilla()))
        {
            return LangWrap.get(this.category.getVanilla());
        }

        return this.category.getVanilla();
    }
//#endif

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
        if (this.keyCode != null)
        {
            return this.keyCode.getTranslationKey();
        }

        return this.translationKey;
    }

    public Component getBoundTranslated()
    {
        if (this.keyCode != null)
        {
            return this.keyCode.getTranslated();
        }

//#if MC >= 11904
        //$$ return Component.literal(this.getTranslated());
//#else
        return new TextComponent(this.getTranslated());
//#endif
    }

	public boolean matchesKey(int keyCode, int scanCode)
	{
		if (keyCode == -1)
		{
			return this.keyCode.getType().isScancode() && this.keyCode.getKeyCode() == scanCode;
		}
		else
		{
			return this.keyCode.getType().isKeyboard() && this.keyCode.getKeyCode() == keyCode;
		}
	}

	public boolean matchesMouse(int keyCode)
	{
		return this.keyCode.getType().isMouse() && this.keyCode.getKeyCode() == keyCode;
	}

	public boolean isDefault()
	{
		return this.keyCode.equals(this.defaultKeyCode);
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
			this.keyCode = this.defaultKeyCode;
			KeybindUtil.resetByID(this.id);
		}

        this.pressed = false;
    }

    public void update(int keyCode, int scanCode, KeyType type)
    {
		VanKeyMngr.debugLog("KeybindWrapper#update():IN: key: [{}], scanCode: [{}]", keyCode, scanCode);
		InputConstants.Key key;

		if (type == KeyType.KEYBOARD)
		{
//#if MC >= 12109
            //$$ key = InputConstants.getKey(new KeyEvent(keyCode, scanCode, -1));
//#else
			key = InputConstants.getKey(keyCode, scanCode);
//#endif
		}
		else
		{
			key = this.matchMouseKeyCode(keyCode);
		}

        this.reset();
        KeybindUtil.updateByID(this.id, key);
        this.keyCode = new KeyCodeWrapper(key);
		VanKeyMngr.debugLog("KeybindWrapper#update():OUT: name: [{}], keyCode: [{}]", this.keyCode.getName(), this.keyCode.getKeyCode());
    }

	public InputConstants.Key matchMouseKeyCode(int keyCode)
	{
		AtomicReference<InputConstants.Key> result = new AtomicReference<>(KeybindUtil.UNKNOWN_KEYCODE);

		KeybindUtil.MAP_BY_NAME.forEach(
				(str, key) ->
				{
					if (key.getValue() == keyCode)
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
        this.keyCode = new KeyCodeWrapper(key);
    }

	public JsonElement toJson()
	{
		JsonObject obj = new JsonObject();

		obj.addProperty("id", this.id);
		obj.addProperty("translationKey", this.translationKey);
        obj.add("category", this.category.toJson());
		obj.add("defaultKeyCode", this.defaultKeyCode.toJson());
		obj.add("keyCode", this.keyCode.toJson());

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
				if (JsonUtils.hasObject(obj, "keyCode"))
				{
					key = KeyCodeWrapper.fromJson(obj.get("keyCode").getAsJsonObject());
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
               ",{keyCode=" +
               (this.keyCode != null ? this.keyCode.toString() : "[NOT-BOUND]") +
               "}";
    }

    public void runDebug()
    {
        System.out.printf("[DB] %s\n", this.toString());
    }
}
