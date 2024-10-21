package com.wocteams.structure.player;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Role {

	public final String identifier;
	@Getter
	private final boolean finalRole;
	@Getter
	@Setter
	private boolean immutable;
	private final List<Permission> permissions = new ArrayList<>();

	public Role(String identifier, boolean finalRole, boolean immutable) {
		this.identifier = identifier;
		this.finalRole = finalRole;
		this.immutable = immutable;
	}

	public Role addPermission(Permission perm) {
		if(!permissions.contains(perm)) permissions.add(perm);
		return this;
	}

	// Has a set of permissions
	// Identifier
	// Icon item?
}
