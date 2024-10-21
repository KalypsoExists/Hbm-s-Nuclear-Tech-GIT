package com.hbm.items.custom;

import com.hbm.items.CustomItems;
import com.hbm.items.ModItems;
import com.hbm.items.tool.ItemToolAbility;
import com.hbm.main.MainRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.List;

public class ItemKeyCard extends ItemToolAbility {

	private final int level;

	public ItemKeyCard(int level) {
        super(0, 0, MainRegistry.tMatCard, EnumToolType.NONE);
        this.level = level;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
        if (this.equals(CustomItems.level1_key_card)) {
            return EnumRarity.uncommon;
        } else if (this.equals(CustomItems.level2_key_card)) {
            return EnumRarity.rare;
        } else if (this.equals(CustomItems.level3_key_card)) {
            return EnumRarity.epic;
        }
		return EnumRarity.common;
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {
		list.add("Gives access at structures");
	}
}
