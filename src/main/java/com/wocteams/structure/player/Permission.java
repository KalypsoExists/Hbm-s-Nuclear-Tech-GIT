package com.wocteams.structure.player;

import lombok.Getter;

@Getter
public class Permission {

	private final String identifier;
	private String description = "";

	public Permission(String identifier) {
		this.identifier = identifier;
	}

	public Permission description(String desc) {
		description = desc;
		return this;
	}

}
