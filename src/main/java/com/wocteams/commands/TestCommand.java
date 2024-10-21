package com.wocteams.commands;

import com.wocteams.util.ChatFormatter;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class TestCommand implements ICommand {
	@Override
	public String getCommandName() {
		return "test";
	}

	@Override
	public String getCommandUsage(ICommandSender iCommandSender) {
		return "/test";
	}

	@Override
	public List<String> getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender iCommandSender, String[] strings) {
		iCommandSender.addChatMessage(ChatFormatter.format("chat.formatter.test", iCommandSender.getCommandSenderName()));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] strings, int i) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
