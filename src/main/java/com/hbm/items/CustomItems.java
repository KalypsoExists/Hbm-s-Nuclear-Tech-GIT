package com.hbm.items;

import com.hbm.items.armor.ArmorFSB;
import com.hbm.items.armor.IArmorDisableModel;
import com.hbm.items.custom.ItemKeyCard;
import com.hbm.items.custom.MyArmor;
import com.hbm.items.tool.ItemKey;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import java.sql.Ref;

public class CustomItems {

	public static void mainRegistry() {
		initializeItems();
		registerItems();
	}

	public static Item my_helmet;
	public static Item my_plate;
	public static Item my_legs;
	public static Item my_boots;

	public static Item level1_key_card;
	public static Item level2_key_card;
	public static Item level3_key_card;

	public static void initializeItems() {

		my_helmet = new MyArmor(ItemArmor.ArmorMaterial.DIAMOND, 0, RefStrings.MODID + ":custom/textures/armor/my_armor.png")
			.setStep("hbm:step.metal")
			.setJump("hbm:step.iron_jump")
			.setFall("hbm:step.iron_land")
			.hides(IArmorDisableModel.EnumPlayerPart.HAT, IArmorDisableModel.EnumPlayerPart.HEAD)
			.setUnlocalizedName("my_helmet").setMaxStackSize(1).setTextureName(RefStrings.MODID + ":my_helmet");
		my_plate = new MyArmor(ItemArmor.ArmorMaterial.DIAMOND, 1, RefStrings.MODID + ":custom/textures/armor/my_armor.png")
			.cloneStats((ArmorFSB) my_helmet)
			//.hides(IArmorDisableModel.EnumPlayerPart.BODY)
			.setUnlocalizedName("my_chestplate").setMaxStackSize(1).setTextureName(RefStrings.MODID + ":my_chestplate");
		my_legs = new MyArmor(ItemArmor.ArmorMaterial.DIAMOND, 2, RefStrings.MODID + ":custom/textures/armor/my_armor.png")
			//.hides(IArmorDisableModel.EnumPlayerPart.LEFT_LEG, IArmorDisableModel.EnumPlayerPart.RIGHT_LEG)
			.cloneStats((ArmorFSB) my_helmet)
			.setUnlocalizedName("my_leggings").setMaxStackSize(1).setTextureName(RefStrings.MODID + ":my_leggings");
		my_boots = new MyArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, RefStrings.MODID + ":custom/textures/armor/my_armor.png")
			//.hides(IArmorDisableModel.EnumPlayerPart.LEFT_LEG, IArmorDisableModel.EnumPlayerPart.RIGHT_LEG)
			.cloneStats((ArmorFSB) my_helmet)
			.setUnlocalizedName("my_boots").setMaxStackSize(1).setTextureName(RefStrings.MODID + ":my_boots");

		level1_key_card = new ItemKeyCard(1)
			.setCreativeTab(MainRegistry.consumableTab)
			.setMaxStackSize(1).setTextureName(RefStrings.MODID + ":level1_key_card").setUnlocalizedName("level1_key_card");

		level2_key_card = new ItemKeyCard(2)
			.setCreativeTab(MainRegistry.consumableTab)
			.setMaxStackSize(1).setTextureName(RefStrings.MODID + ":level2_key_card").setUnlocalizedName("level2_key_card");

		level3_key_card = new ItemKeyCard(3)
			.setCreativeTab(MainRegistry.consumableTab)
			.setMaxStackSize(1).setTextureName(RefStrings.MODID + ":level3_key_card").setUnlocalizedName("level3_key_card");

	}

	private static void registerItems() {
		GameRegistry.registerItem(my_helmet, my_helmet.getUnlocalizedName());
		GameRegistry.registerItem(my_plate, my_plate.getUnlocalizedName());
		GameRegistry.registerItem(my_legs, my_legs.getUnlocalizedName());
		GameRegistry.registerItem(my_boots, my_boots.getUnlocalizedName());
	}

	public static void registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName());
	}
}
