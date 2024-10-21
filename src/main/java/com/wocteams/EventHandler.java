package com.wocteams;

import com.wocteams.commands.ConfirmationHandler;
import com.wocteams.structure.player.Member;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EventHandler {

	private int intervalTickCounter = 0;
	private int tickCounter = 0;
	private ZonedDateTime lastRunTime;

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if(!event.player.worldObj.isRemote) handleMember(event.player);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		if(!event.player.worldObj.isRemote) handleMember(event.player);
	}

	// Maintains and makes sure a member exists
	private void handleMember(EntityPlayer player) {
		Member member = Core.getMember(player); // Let's make sure we have a valid member
		if(member == null) member = Core.registerMember(player);

		member.updateLastActive(); // Since, player logged in
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			tickCounter++;
			if (tickCounter >= 30 * 1200) {

				// Update intervals for all raid windows
				ConfirmationHandler.expireConfirmations();

				tickCounter = 0;
			}

			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));

			// 4 PM GMT
			if (now.getHour() == 16 && now.getMinute() == 0 && now.getSecond() == 0) {
				// Not run today? then continue
				if (lastRunTime == null || !lastRunTime.toLocalDate().isEqual(now.toLocalDate())) {
					// Do stuff
					lastRunTime = now;
				}
			}
		}
	}

}
