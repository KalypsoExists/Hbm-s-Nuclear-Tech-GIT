package com.hbm.items.custom;

import com.hbm.items.armor.ArmorFSB;
import com.hbm.render.model.ModelMyArmor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class MyArmor extends ArmorFSB {

	public MyArmor(ArmorMaterial material, int slot, String texture) {
		super(material, slot, texture);
	}

	@SideOnly(Side.CLIENT)
	ModelMyArmor[] models;

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {

		if(models == null) {
			models = new ModelMyArmor[4];

			for(int i = 0; i < 4; i++)
				models[i] = new ModelMyArmor(i);
		}

		return models[armorSlot];
	}

}
