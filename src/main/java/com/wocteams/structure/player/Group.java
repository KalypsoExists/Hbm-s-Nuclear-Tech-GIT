package com.wocteams.structure.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {

	// Individual member permission sets [Sector, Area]
	private final Map<Member, List<Permission>> membersToPermissions = new HashMap<>();
	// Get the roles a member has [Sector]
	private final Map<Member, List<Role>> memberToRoles = new HashMap<>();

	// All roles that exist [Sector]
	private final List<Role> roles = new ArrayList<>();

	public boolean registerRole(Role role) {
		// Make sure not a duplicate identifier
		for(Role entry : roles) if(entry.identifier.equals(role.identifier)) return false;

		roles.add(role);
		return true;
	}

	public List<Role> getMemberRoles(Member member) {
		return memberToRoles.get(member); // Hope/should be a list, not null, empty if no role
	}

	// Safe adding members
	// Safe making roles/editing

}
