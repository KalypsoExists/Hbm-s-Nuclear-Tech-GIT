package com.wocteams.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatFormatter {

	public static IChatComponent format(String key, Object... values) {
		ChatComponentTranslation translation = new ChatComponentTranslation(key, values);
		return parseColor(translation.getFormattedText(), values);
	}

	public static IChatComponent parseColor(String message, Object... values) {
		IChatComponent finalMessage = new ChatComponentText("");
		String[] parts = message.split("&");

		EnumChatFormatting temp = EnumChatFormatting.WHITE;

		for (String part : parts) {
			if (part.isEmpty()) continue;

			EnumChatFormatting color = getColorFromCode(part.charAt(0));

			if (color != null) {
				temp = color;
				part = part.substring(1);
			}

			//part = replacePlaceholders(part, values, temp);

			ChatComponentText component = new ChatComponentText(part);
			component.getChatStyle().setColor(color);
			finalMessage.appendSibling(component);
		}

		return finalMessage;
	}

	// Map color codes to EnumChatFormatting
	private static EnumChatFormatting getColorFromCode(char code) {
		switch (code) {
			case '0':
				return EnumChatFormatting.BLACK;
			case '1':
				return EnumChatFormatting.DARK_BLUE;
			case '2':
				return EnumChatFormatting.DARK_GREEN;
			case '3':
				return EnumChatFormatting.DARK_AQUA;
			case '4':
				return EnumChatFormatting.DARK_RED;
			case '5':
				return EnumChatFormatting.DARK_PURPLE;
			case '6':
				return EnumChatFormatting.GOLD;
			case '7':
				return EnumChatFormatting.GRAY;
			case '8':
				return EnumChatFormatting.DARK_GRAY;
			case '9':
				return EnumChatFormatting.BLUE;
			case 'a':
				return EnumChatFormatting.GREEN;
			case 'b':
				return EnumChatFormatting.AQUA;
			case 'c':
				return EnumChatFormatting.RED;
			case 'd':
				return EnumChatFormatting.LIGHT_PURPLE;
			case 'e':
				return EnumChatFormatting.YELLOW;
			case 'f':
				return EnumChatFormatting.WHITE;
			default:
				return null;
		}
	}

	/*private static String replacePlaceholders(String part, Object[] args, EnumChatFormatting color) {
        for(Object arg : args) {
            if (part.contains("%s")) {
                String replacement = arg.toString();
                part = part.replaceFirst("%s", replacement);
            }
        }
		return part;
	}*/
}

