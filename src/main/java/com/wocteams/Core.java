package com.wocteams;

import com.wocteams.commands.ConfirmationHandler;
import com.wocteams.commands.SectorCommand;
import com.wocteams.commands.TestCommand;
import com.wocteams.structure.RaidWindow;
import com.wocteams.structure.player.Member;
import com.wocteams.structure.area.Plot;
import com.wocteams.structure.area.Region;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class Core {

	// To get all plots in the world for any sector
	// New sector, put (sector, new ArrayList()), get (sector) add homeArea
	// Delete sector, doesn't remove the sector from the list instantly, ruining mechanism~
	// Moved to region -> public static final Map<Region, List<Plot>> regions = new HashMap<>();
	// To get what sector does a member belong to
	// If a member is not in the map it means he is a nomad
	// New sector, put (memberLeader, sector)
	// Delete sector, for any key with the value (sector) remove it from the map
	public static final Map<Member, Region> members = new HashMap<>();
	public static final Map<EntityPlayer, Member> players = new HashMap<>();
	// To get a raid window of a sector
	// Moved to region -> public static final Map<Region, RaidWindow> raidWindows = new HashMap<>();
	public static final List<Plot> plots = new ArrayList<>();

	@Getter
	private static final LuckPerms perms = LuckPermsProvider.get();

	public static Member getMember(EntityPlayer player) {
		return players.get(player);
	}

	public static Region getMemberRegion(EntityPlayer player) {
		return members.get(getMember(player));
	}

	public static Plot getPlot(int[] chunk) {
		for(Plot plot : plots) {
			if(Arrays.equals(plot.getChunk(), chunk)) return plot;
		}
		return null;
	}

	// To check if the chunk has any plot in its vicinity
	// Argument is how far is part of vicinity
	public static boolean plotInVicinity(int[] chunk, int radius) {

		for (Plot plot : plots) {
			// Get the chunk coordinates for the current plot
			int[] plotChunk = plot.getChunk();

			// Calculate the Manhattan distance
			int distanceX = Math.abs(plotChunk[0] - chunk[0]);
			int distanceZ = Math.abs(plotChunk[1] - chunk[1]);

			// Check if the distance is within the radius
			if (distanceX <= radius && distanceZ <= radius) {
				return true; // A plot is within vicinity
			}
		}

		return false; // No plots found in the vicinity
	}

	public static Member registerMember(EntityPlayer player) {
		Member member = new Member(player);
		players.put(player, member);
		return member;
	}

	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new SectorCommand());
		event.registerServerCommand(new ConfirmationHandler.RejectCommand());
		event.registerServerCommand(new ConfirmationHandler.AcceptCommand());
		event.registerServerCommand(new TestCommand());
	}

}
