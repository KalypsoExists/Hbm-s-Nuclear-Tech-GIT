package com.hbm.blocks.custom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMachineCardReader extends Container {

	private TileEntityMachineCardReader reader;

	public ContainerMachineCardReader(InventoryPlayer invPlayer, TileEntityMachineCardReader te) {

		reader = te;

		this.addSlotToContainer(new Slot(te, 0, 26, 17));

		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int slotIndex)
    {
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

            if (slotIndex <= 1) {
				if (!this.mergeItemStack(slotStack, 2, this.inventorySlots.size(), true))
					return null;
			}
			else if (!this.mergeItemStack(slotStack, 0, 2, false))
				return null;

			if (slotStack.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();
		}

		return stack;
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return reader.isUseableByPlayer(player);
	}
}
