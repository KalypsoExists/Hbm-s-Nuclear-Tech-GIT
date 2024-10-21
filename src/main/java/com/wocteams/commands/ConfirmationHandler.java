package com.wocteams.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmationHandler {

	private static final Map<EntityPlayer, List<Confirmable>> confirmables = new HashMap<>();

	public static void registerConfirmation(EntityPlayer player, Confirmable request) {
		List<Confirmable> list;
		if((list = confirmables.get(player)) == null) list = new ArrayList<>();

		list.add(request); // Let's assume the request is valid

		confirmables.put(player, list); // Since if a key already exists it will just be added upon

		player.addChatMessage(new ChatComponentText("[/Accept] [/Reject] [Timeout: "+request.getWaitDuration()/1000+"s]"));
	}

	public static void expireConfirmations() {
		for(EntityPlayer player : confirmables.keySet()) {
			List<Confirmable> list = confirmables.get(player);
			if(list == null || list.isEmpty()) {
				confirmables.remove(player);
				continue;
			}
            list.removeIf(confirmable -> confirmable.getRequestMoment() + confirmable.getWaitDuration() < System.currentTimeMillis());
		}
	}

	public static class RejectCommand implements ICommand {

		@Override
		public String getCommandName() {
			return "reject";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
			return "/reject";
		}

		@Override
		public List<String> getCommandAliases() {
			return null;
		}

		@Override
		public void processCommand(ICommandSender sender, String[] strings) {
			if(sender instanceof EntityPlayer) {

				List<Confirmable> list = confirmables.get(sender);

				if(list == null || list.isEmpty()) {
					confirmables.remove((EntityPlayer) sender);
					sender.addChatMessage(new ChatComponentText("No confirmation request to accept"));
					return;
				}

				for(Confirmable confirmable : list) {
					if(confirmable.getRequestMoment()+confirmable.getWaitDuration() > System.currentTimeMillis()) {
						confirmable.reject((EntityPlayer) sender);
						list.remove(confirmable);
						sender.addChatMessage(new ChatComponentText("Rejected"));
						// Rejected then removed it
						// Now the command has to be run again for the next queue confirmation
						break;
					} else {
						// Ignore the expired confirmations
						// Remove them from the list
						// !!! Must also clear all expired confirmations every 30 minutes
						list.remove(confirmable);
					}
				}
			}
		}

		@Override
		public boolean canCommandSenderUseCommand(ICommandSender sender) {
			return sender instanceof EntityPlayer;
		}

		@Override
		public List<String> addTabCompletionOptions(ICommandSender sender, String[] strings) {
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

	public static class AcceptCommand implements ICommand {

		@Override
		public String getCommandName() {
			return "accept";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
			return "/accept";
		}

		@Override
		public List<String> getCommandAliases() {
			return null;
		}

		@Override
		public void processCommand(ICommandSender sender, String[] strings) {
			if(sender instanceof EntityPlayer) {

				List<Confirmable> list = confirmables.get(sender);

				if(list == null || list.isEmpty()) {
					confirmables.remove((EntityPlayer) sender);
					sender.addChatMessage(new ChatComponentText("No confirmation request to accept"));
					return;
				}

				for(Confirmable confirmable : list) {
					if(confirmable.getRequestMoment()+confirmable.getWaitDuration() > System.currentTimeMillis()) {
						confirmable.accept((EntityPlayer) sender);
						list.remove(confirmable);
						sender.addChatMessage(new ChatComponentText("Accepted"));
						// Accepted then removed it
						// Now the command has to be run again for the next queue confirmation
						break;
					} else {
						// Ignore the expired confirmations
						// Remove them from the list
						// !!! Must also clear all expired confirmations every 30 minutes
						list.remove(confirmable);
					}
				}
			}
		}

		@Override
		public boolean canCommandSenderUseCommand(ICommandSender sender) {
			return sender instanceof EntityPlayer;
		}

		@Override
		public List<String> addTabCompletionOptions(ICommandSender sender, String[] strings) {
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

}
