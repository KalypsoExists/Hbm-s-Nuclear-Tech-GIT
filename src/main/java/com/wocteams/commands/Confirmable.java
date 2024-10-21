package com.wocteams.commands;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

@Getter
public abstract class Confirmable {

	private final long requestMoment;
	private final long waitDuration;

	Confirmable(long waitDuration) {
		requestMoment = System.currentTimeMillis();
		this.waitDuration = waitDuration;
	}

	public abstract void accept(EntityPlayer player);
	public abstract void reject(EntityPlayer player);

	public void register(EntityPlayer player) {
		ConfirmationHandler.registerConfirmation(player, this);
	}

}
