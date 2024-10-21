package com.wocteams.structure.player;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

public class Member {
	// Player

	// #getCurrentChunk

	@Getter
	private final EntityPlayer player;
	@Getter
	private long lastActiveSeconds = 0;

	public Member(EntityPlayer player) {
		this.player = player;
		lastActiveSeconds = System.currentTimeMillis()/1000; // Should be updated on player join/leave
		// New unit should be created on player join if doesnt exist already (objects to be maintained in main registry)
		// Unit#updateLastActive should be called on both player join and player leave always
	}

	// Argument -> How long does one need to be off for to be inactive
	public boolean isActive(long seconds) {
		if(lastActive() > seconds) return false;
		return true;
	}

	public void updateLastActive() {
		lastActiveSeconds = System.currentTimeMillis()/1000;
	}

	// How long has it been since the player was last active
	public long lastActive() {
		return System.currentTimeMillis()/1000 - lastActiveSeconds;
	}

	public int[] getCurrentChunk() {
		return new int[]{((int) player.posX) >> 4, ((int) player.posZ) >> 4};
	}
}
