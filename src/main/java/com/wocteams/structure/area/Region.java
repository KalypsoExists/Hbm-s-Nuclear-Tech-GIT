package com.wocteams.structure.area;

import com.wocteams.Core;
import com.wocteams.structure.PlotPermissions;
import com.wocteams.structure.RaidWindow;
import com.wocteams.structure.player.Group;
import com.wocteams.structure.player.Member;
import com.wocteams.structure.player.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Region {

	public static final Map<Region, RaidWindow> raidWindows = new HashMap<>();
	public static final Map<Region, List<Plot>> regions = new HashMap<>();

	private static final Role leaderRole = new Role("Leader", true, true)
		.addPermission(PlotPermissions.BREAK_BLOCK)
		.addPermission(PlotPermissions.PLACE_BLOCK);

	@Getter
	private final Group group = new Group();
	@Getter
	private final List<Plot> plots = new ArrayList<>();

	@Getter
	private Domain domain; // Null if not part of one

	@Getter
	private RaidWindow raidWindow; // Can be null for like a safe zone

	@Getter
	private Plot homePlot; // Must not be null!

	@Getter
	private String name; // Must not be blank and be validName()

	@Getter
	@Setter
	private boolean ruined = false;

	public Region(String name, Plot homePlot) {
		this.homePlot = homePlot;
		group.registerRole(leaderRole);
	}

	// Naming convention
	public static boolean validName(String name) {
		return name != null && !name.isEmpty() && name.matches("^[A-Za-z._\\s-]+$");
	}

	public boolean isLeader(Member member) {
		return group.getMemberRoles(member).contains(leaderRole);
	}

	public void registerRegion() {
		regions.put(this, plots);
	}

	// Has a group

	// Plots
	// Members
	// Ranks

	// #getPlots
	// #getChunks
	// #getOuterChunks
	// #getOuterPlots

}
