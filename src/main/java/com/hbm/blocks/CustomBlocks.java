package com.hbm.blocks;

import com.hbm.blocks.custom.MachineCardReader;
import com.hbm.items.block.*;
import com.hbm.main.MainRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class CustomBlocks {

	public static void mainRegistry()
	{
		initializeBlock();
		registerBlock();
	}

	public static Block level1_card_reader;
	public static Block level2_card_reader;
	public static Block level3_card_reader;

	public static Material materialGas = new MaterialGas();

	private static void initializeBlock() {
		level1_card_reader = new MachineCardReader(Material.iron, 1).setBlockName("level1_card_reader").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.consumableTab);
		level2_card_reader = new MachineCardReader(Material.iron, 2).setBlockName("level2_card_reader").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.consumableTab);
		level3_card_reader = new MachineCardReader(Material.iron, 3).setBlockName("level3_card_reader").setHardness(5.0F).setResistance(10.0F).setCreativeTab(MainRegistry.consumableTab);
	}

	private static void registerBlock() {
		register(level1_card_reader);
		register(level2_card_reader);
		register(level3_card_reader);
	}

	private static void register(Block b) {
		GameRegistry.registerBlock(b, ItemBlockBase.class, b.getUnlocalizedName());
	}

	private static void register(Block b, Class<? extends ItemBlock> clazz) {
		GameRegistry.registerBlock(b, clazz, b.getUnlocalizedName());
	}

	public static void addRemap(String unloc, Block block, int meta) {
		Block remap = new BlockRemap(block, meta).setBlockName(unloc);
		register(remap, ItemBlockRemap.class);
	}

}

