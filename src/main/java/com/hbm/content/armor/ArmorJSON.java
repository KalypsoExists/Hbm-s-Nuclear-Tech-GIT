package com.hbm.content.armor;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ArmorJSON {

	String id;
	int maxDamageFactor;
	int enchantability;
	int maxStackSize;
	int[] slots;
	int durability;
	int[] defense;
	SetBonus setBonus;

	@Getter
	public class SetBonus {

		int[] bonusSlotRequirement;
		boolean fireProof;
		int damageThreshold;
		int protectionYield;
		int blastProtection;
		boolean thermalVision;
		boolean hardLanding;
		boolean vats;
		Sounds sounds;
		Effect[] effects;
		Resistance[] resistances;

		@Getter
		public class Sounds {
			String step;
			String fall;
			String jump;
		}

		@Getter
		public class Effect {
			String id;
			int amplifier;
			boolean subtleParticles;
		}

		@Getter
		public class Resistance {
			String damageSource;
			float modifier;
		}

	}

}
