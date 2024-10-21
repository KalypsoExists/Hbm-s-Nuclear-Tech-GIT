package com.wocteams.structure;

import net.minecraft.item.ItemStack;

public interface IGuiIcon {
	// For templating a method that returns an item stack if the implementing object
	// is checked and asked for

	ItemStack getIconStack();

}
