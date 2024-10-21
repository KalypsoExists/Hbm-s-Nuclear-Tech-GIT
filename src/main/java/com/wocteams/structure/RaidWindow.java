package com.wocteams.structure;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class RaidWindow {

	@Getter
	private int avgPeakMoment = 0, lastPeakMoment = 0;

	@Getter
	@Setter
	private boolean paused = false;

	private final List<int[]> intervals = new ArrayList<>(); // We're expecting this to have 48 intervals

	// Is supposed to be created along with a region, it is updated for every region every day
	public RaidWindow() {
		// No need to do anything
    }

	// Supposed to be run by world tick event after the raid window is accessed from map
	// Ratio is of current active online players to total active players

	// We run it with ZonedDateTime when minutes are 0 or 30
	// A moment is a unit equal to 30 minutes
	// so 2 units = 1 hour
	public boolean updateInterval(int moment, int ratio) {
		// Ratio should be less than 1 more than 0
		if(ratio < 0 || ratio > 1) return false;
		// Should be in a range of 0 to 47
		if(moment > 47 || moment < 0) return false;

		intervals.add(new int[]{moment, ratio});

		return true;
	}

	public void updatePeakTime() {

		int ratio = 0, moment = 0;

		for(int[] interval : intervals) {
			if(interval[1] > ratio) {
				ratio = interval[1];
				moment = interval[0];
			}
		}

		intervals.clear();

		lastPeakMoment = moment;

		if(avgPeakMoment == 0) avgPeakMoment = lastPeakMoment;
		else avgPeakMoment = ( avgPeakMoment + lastPeakMoment ) / 2;
	}

}
