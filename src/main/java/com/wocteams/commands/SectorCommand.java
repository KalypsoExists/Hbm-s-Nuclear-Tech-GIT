package com.wocteams.commands;

import com.wocteams.Core;
import com.wocteams.structure.area.Plot;
import com.wocteams.structure.area.Region;
import net.luckperms.api.model.user.User;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SectorCommand implements ICommand {

	private final List<String> tab = new ArrayList<>();
	private boolean canUse = true;

	@Override
	public String getCommandName() {
		return "sector";
	}

	@Override
	public String getCommandUsage(ICommandSender iCommandSender) {
		return "/sector <options>";
	}

	@Override
	public List<String> getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		boolean isPlayer = sender instanceof EntityPlayer;
		User user = isPlayer ? Core.getPerms().getUserManager().getUser(((EntityPlayer) sender).getUniqueID()) : null;

		if(args.length == 0) tabComplete("create", "delete"); // Must have args
		else if(safe(args, 0).equals("create")) { // In case of "create"
			// If false means either "" null or not a valid name
			tabComplete("<name>");

			if(Region.validName(safe(args, 1))) {
				if(!isPlayer) {
					sender.addChatMessage(new ChatComponentText("Player only command"));
					canUse = false;
					return;
				}

				if(Core.getMemberRegion((EntityPlayer) sender) != null) {
					sender.addChatMessage(new ChatComponentText("You're already part of a sector!"));
					return;
				}

				int[] chunk = chunkify(sender.getPlayerCoordinates());

				if(Core.getPlot(chunk) != null) {
					sender.addChatMessage(new ChatComponentText("Chunk already part of another region!"));
				}

				if(Core.plotInVicinity(chunk, 8)) {
					sender.addChatMessage(new ChatComponentText("Too close to another region"));
				}

				String name = args[1];
				Plot homePlot = new Plot(chunk);

				new Region(name, homePlot).registerRegion();

			} else {
				sender.addChatMessage(new ChatComponentText("Invalid/Missing <name>"));
			}
		} else if(safe(args, 0).equals("delete")) { // In case of "create"
			// If false means either "" null or not a valid name

			if(!isPlayer) {
				sender.addChatMessage(new ChatComponentText("Player only command"));
				canUse = false;
				return;
			}

			Region region = Core.getMemberRegion((EntityPlayer) sender);

			if(region == null) {
				sender.addChatMessage(new ChatComponentText("You do not belong to a sector!"));
				return;
			}

			if(!region.isLeader(Core.getMember(((EntityPlayer) sender)))) {
				sender.addChatMessage(new ChatComponentText("You are not the leader of the sector!"));
				return;
			}

			long waitDuration = 30*1000;

			sender.addChatMessage(new ChatComponentText("Are you sure you want to delete your sector?"));

			new Confirmable(waitDuration) {

				@Override
				public void accept(EntityPlayer player) {
					region.setRuined(true);
					// (Instead confirmation handles does this) ->
					// player.addChatMessage(new ChatComponentText("Confirmed"));
				}

				@Override
				public void reject(EntityPlayer player) {

				}

			}.register((EntityPlayer) sender);

			// If command continued
			// New confirm request
			// -> Sends a message asking player to do /confirm or /reject
			// (Each confirm request being unique to the player and in a queueing order to previous requests for a member)
			// When a confirm request is supposedly accepted it should be able to run a piece of code for
			// confirmed or rejected

		} else {
			sender.addChatMessage(new ChatComponentText("Invalid/Missing <option>"));
		}
	}

	private int[] chunkify(ChunkCoordinates coords) {
		return new int[]{coords.posX >> 4, coords.posZ >> 4};
	}

	// To safely get a value from an array
	private String safe(String[] array, int index) {
		String entry;
		try {
			entry = array[index];
		} catch (ArrayIndexOutOfBoundsException ex) {
			return "";
		}
		return entry;
	}

	private void tabComplete(String... values) {
		tab.clear();
        tab.addAll(Arrays.asList(values));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return canUse;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings) {
		return tab;
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
