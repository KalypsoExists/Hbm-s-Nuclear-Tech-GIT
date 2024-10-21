package com.wocteams.structure.area;

import com.wocteams.structure.player.Permission;
import com.wocteams.structure.player.Member;
import com.wocteams.structure.player.Role;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plot {

	// What any member is able to do in this plot
	private final List<Permission> permissions = new ArrayList<>();
	// What more, a specific member can do in this plot
	private final Map<Member, List<Permission>> permittedMembers = new HashMap<>();
	// What more, a specific role can do in this plot
	private final Map<Role, List<Permission>> permittedRoles = new HashMap<>();

	@Getter
	private final int[] chunk;

	public Plot(int[] chunk) {
		this.chunk = chunk;
	}

	public Plot(int x, int y) {
		chunk = new int[]{x, y};
	}

	// Chunk
	// Members:Permissions
	// Ranks:Permissions

	// #getRadius
	// #getOuterChunks
	// #hasPermission Unit Permission
	// #getPermissions Unit
	// #getChunk
	// #getRankPermissions Rank

}
