package com.hbm.content.loaders;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.hbm.content.armor.ArmorJSON;
import com.hbm.items.CustomItems;
import com.hbm.items.custom.ContentFSArmor;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.hbm.main.MainRegistry.load;
import static com.hbm.main.MainRegistry.logger;

public class ArmorLoader extends ContentLoader {

	private final HashMap<String, Item> content = new HashMap<>();

	@Override
	public String getFolderName() {
		return "armor";
	}

	@Override
	public String getTemplateName() {
		return "my_armor.json";
	}

	@Override
	public String getName() {
		return "armor";
	}

	@Override
	public boolean loadContent(String contentId, JsonObject content) {

		ArmorJSON armor = gson.fromJson(content, ArmorJSON.class);

		logger.info("Loading armor content from ["+contentId+"]"); // need to check if contentId contains file extension

		String id = armor.getId();
		int durability = armor.getDurability();
		int[] defense = armor.getDefense();
		int enchantability = armor.getEnchantability();
		int[] slots = armor.getSlots();
		ArmorJSON.SetBonus setBonus = armor.getSetBonus();

		if(id == null) {
			logger.error("Missing armor content ID");
			return false;
		}

		if(!id.matches("[A-Za-z_]+")) {
			id = id.replaceAll("[^A-Za-z_]", "");
			logger.warn("Invalid armor content ID, refactoring to "+id);
		}

		if(durability == 0) {
			durability = 1;
			logger.warn("Invalid durability, defaulting to (1)");
		}

		defense = validateBounds(defense, 1000, 0, 4, true, true);

		if(defense == null) {
			logger.error("Invalid/Missing defense stats array");
			return false;
		}

		ItemArmor.ArmorMaterial material = EnumHelper
			.addArmorMaterial("content_"+id, durability, defense, enchantability);

		// TODO
		// Load all the stats of helmet then clone into the other three
		// Put checks for stats, to change null to false/empty
		// Checking for missing important values then throw an exception while returning false

		String[] names = {"helmet", "chestplate", "leggings", "boots"};

		if(slots == null || slots.length == 0) {
			logger.error("Missing armor slots to load");
		}

		slots = validateBounds(slots, 3, 0, 4, false, false);

		if(slots == null) {
			logger.error("Invalid/Missing slots array");
			return false;
		}

		ContentFSArmor toClone = new ContentFSArmor("toClone", material, 0, "");

		if(setBonus.isFireProof()) toClone.setFireproof(true);
		if(setBonus.isVats()) toClone.enableVATS(true);
		if(setBonus.isHardLanding()) toClone.setHasHardLanding(true);
		if(setBonus.isThermalVision()) toClone.enableThermalSight(true);

		int num;
		if((num = setBonus.getBlastProtection()) != 0) toClone.setBlastProtection(num);
		if((num = setBonus.getDamageThreshold()) != 0) toClone.setThreshold(num);
		if((num = setBonus.getProtectionYield()) != 0) toClone.setProtectionLevel(num);

		ArmorJSON.SetBonus.Sounds sounds = setBonus.getSounds();

		String str;
		if((str = sounds.getFall()) != null) toClone.setFall(str);
		if((str = sounds.getStep()) != null) toClone.setStep(str);
		if((str = sounds.getJump()) != null) toClone.setJump(str);


		for(int slot : slots) {
			Item piece = new ContentFSArmor(id+"_"+names[slot], material, slot, RefStrings.MODID + ":custom/textures/armor/"+id+"_"+names[slot]+".png").cloneStats(toClone);

			this.content.put(piece.getUnlocalizedName(), piece);
			CustomItems.registerItem(piece);
		}

		return false;
	}

	private int[] validateBounds(int[] array, int valueMax, int valueMin, int arraySize, boolean requiredSize, boolean allowDuplicates) {
		if(array == null) return null;

		if(requiredSize && array.length != arraySize) {
			return null;
		} else if(array.length > arraySize) {
			return null;
		}

		List<Integer> temp = null;
		if(!allowDuplicates) temp = Arrays.stream(array).boxed().collect(Collectors.toList());

		int i = 0;
		for(int each : array) {
			if(each > valueMax || each < valueMin) {
				if(allowDuplicates) {
					logger.warn("Invalid array value ("+each+")");
					return null;
				} else {
					if(temp.contains(each)) {
						logger.error("Duplicate entry not allowed ("+each+")");
						return null;
					}

					temp.add(array[0]);
				}
			}
			i++;
		}

		return array;
	}

}
