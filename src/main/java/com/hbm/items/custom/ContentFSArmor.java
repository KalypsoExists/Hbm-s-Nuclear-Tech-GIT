package com.hbm.items.custom;

import com.hbm.extprop.HbmLivingProps;
import com.hbm.items.ModItems;
import com.hbm.items.armor.ArmorFSB;
import com.hbm.util.ContaminationUtil;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ContentFSArmor extends ArmorFSB {

	public int[] bonusReqSlots;
	@Getter
	public final String id;

	// When loading a content armor json, make sure slots has no duplicates and is not a value less than 0 or more than 3
	public ContentFSArmor(String id, ArmorMaterial material, int slot, String texture) {
		super(material, slot, texture);

		this.id = id;
	}

	public ContentFSArmor setBonusReqSlots(int[] bonusReqSlots) {
		this.bonusReqSlots = bonusReqSlots;
		return this;
	}

	public ContentFSArmor cloneStats(ContentFSArmor original) {
		this.effects = original.effects;
		this.resistance = original.resistance;
		this.damageCap = original.damageCap;
		this.damageMod = original.damageMod;
		this.damageThreshold = original.damageThreshold;
		this.protectionYield = original.protectionYield;
		this.blastProtection = original.blastProtection;
		this.projectileProtection = original.projectileProtection;
		this.fireproof = original.fireproof;
		this.noHelmet = original.noHelmet;
		this.vats = original.vats;
		this.thermal = original.thermal;
		this.geigerSound = original.geigerSound;
		this.customGeiger = original.customGeiger;
		this.hardLanding = original.hardLanding;
		this.gravity = original.gravity;
		this.dashCount = original.dashCount;
		this.stepSize = original.stepSize;
		this.step = original.step;
		this.jump = original.jump;
		this.fall = original.fall;

		this.bonusReqSlots = original.bonusReqSlots;

		return this;
	}

	public static boolean hasContentFSArmor(EntityPlayer player) {
        return getContentFSArmor(player) != null;
    }

	public static ContentFSArmor[] getContentFSArmor(EntityPlayer player) {

		ItemStack item = null;
		for(int i = 0; i < 4; i++) {
			item = player.inventory.armorInventory[i];
			if(item != null && item.getItem() instanceof ContentFSArmor) break;
		}
		if(item == null) return null;
		ContentFSArmor piece = (ContentFSArmor) item.getItem();

		ArrayList<ContentFSArmor> set = new ArrayList<>();

		for(int slot : remapSlotOrder(piece.bonusReqSlots)) {
			ItemStack slotItem = player.inventory.armorInventory[slot];
			if(slotItem == null || !(slotItem.getItem() instanceof ContentFSArmor)) return null;
			set.add((ContentFSArmor) slotItem.getItem());
		}

		ContentFSArmor prime = set.get(0);

		for(ContentFSArmor armor : set) {
			if(armor.getId().equals(prime.getId())) continue;
			return null;
		}

		return set.toArray(new ContentFSArmor[0]);
	}

	private static int[] remapSlotOrder(int[] slots) {
		int[] remapped = Arrays.copyOf(slots, slots.length);

		int[] fullMapping = {3, 2, 1, 0};

		int i = 0;
		for (int slot : slots) {
			if (slot < 0 || slot >= fullMapping.length) {
				throw new IllegalArgumentException("Slot value out of range: " + slot);
			}

			remapped[i] = fullMapping[slot];
			i++;
		}

		return remapped;
	}

	@Override
	public void handleAttack(LivingAttackEvent event) {

		EntityLivingBase e = event.entityLiving;

		if(e instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e;

			ContentFSArmor[] set = ContentFSArmor.getContentFSArmor(player);
			if(set != null && set.length != 0) {
				ContentFSArmor prime = set[0];

				if(prime.damageThreshold >= event.ammount && !event.source.isUnblockable()) {
					event.setCanceled(true);
				}

				if(prime.fireproof && event.source.isFireDamage()) {
					player.extinguish();
					event.setCanceled(true);
				}

				if(prime.resistance.get(event.source.getDamageType()) != null && prime.resistance.get(event.source.getDamageType()) <= 0) {
					event.setCanceled(true);
				}
			}
		}
	}

	@Override
	public void handleHurt(LivingHurtEvent event) {

		EntityLivingBase e = event.entityLiving;

		if(e instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e;

			ContentFSArmor[] set = ContentFSArmor.getContentFSArmor(player);
			if(set != null && set.length != 0) {
				ContentFSArmor prime = set[0];

				//store any damage above the yield
				float overFlow = Math.max(0, event.ammount - prime.protectionYield);
				//reduce the damage to the yield cap if it exceeds the yield
				event.ammount = Math.min(event.ammount, prime.protectionYield);

				if(!event.source.isUnblockable())
					event.ammount -= prime.damageThreshold;

				if(prime.damageMod != -1) {
					event.ammount *= prime.damageMod;
				}

				if(prime.resistance.get(event.source.getDamageType()) != null) {
					event.ammount *= prime.resistance.get(event.source.getDamageType());
				}

				if(prime.blastProtection != -1 && event.source.isExplosion()) {
					event.ammount *= prime.blastProtection;
				}

				if(prime.projectileProtection != -1 && event.source.isProjectile()) {
					event.ammount *= prime.projectileProtection;
				}

				if(prime.damageCap != -1) {
					event.ammount = Math.min(event.ammount, prime.damageCap);
				}

				//add back anything that was above the protection yield before
				event.ammount += overFlow;
			}
		}
	}

	@Override
	public void handleTick(TickEvent.PlayerTickEvent event) {

		EntityPlayer player = event.player;

		ContentFSArmor[] set = ContentFSArmor.getContentFSArmor(player);
		if(set != null && set.length != 0) {
			ContentFSArmor prime = set[0];

			if(!prime.effects.isEmpty()) {
				for(PotionEffect i : prime.effects) {
					player.addPotionEffect(new PotionEffect(i.getPotionID(), 20, i.getAmplifier(), true));
				}
			}

			if(!player.capabilities.isFlying && !player.isInWater())
				player.motionY -= prime.gravity;

			if(prime.step != null && player.worldObj.isRemote && player.onGround) {

				try {
					Field nextStepDistance = ReflectionHelper.findField(Entity.class, "nextStepDistance", "field_70150_b");
					Field distanceWalkedOnStepModified = ReflectionHelper.findField(Entity.class, "distanceWalkedOnStepModified", "field_82151_R");

					if(player.getEntityData().getFloat("hfr_nextStepDistance") == 0) {
						player.getEntityData().setFloat("hfr_nextStepDistance", nextStepDistance.getFloat(player));
					}

					int px = MathHelper.floor_double(player.posX);
					int py = MathHelper.floor_double(player.posY - 0.2D - (double) player.yOffset);
					int pz = MathHelper.floor_double(player.posZ);
					Block block = player.worldObj.getBlock(px, py, pz);

					if(block.getMaterial() != Material.air && player.getEntityData().getFloat("hfr_nextStepDistance") <= distanceWalkedOnStepModified.getFloat(player))
						player.playSound(prime.step, 1.0F, 1.0F);

					player.getEntityData().setFloat("hfr_nextStepDistance", nextStepDistance.getFloat(player));

				} catch(Exception ex) {}
			}
		}
	}

	@Override
	public void handleJump(EntityPlayer player) {

		ContentFSArmor[] set = ContentFSArmor.getContentFSArmor(player);

		if(set != null && set.length != 0) {
			ContentFSArmor prime = set[0];

			if(prime.jump != null)
				player.playSound(prime.jump, 1.0F, 1.0F);
		}
	}

	@Override
	public void handleFall(EntityPlayer player) {

		ContentFSArmor[] set = ContentFSArmor.getContentFSArmor(player);

		if(set != null && set.length != 0) {
			ContentFSArmor prime = set[0];

			if(prime.hardLanding && player.fallDistance > 10) {

				// player.playSound(Block.soundTypeAnvil.func_150496_b(), 2.0F,
				// 0.5F);

				List<Entity> entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(3, 0, 3));

				for(Entity e : entities) {

					if(e instanceof EntityItem)
						continue;

					Vec3 vec = Vec3.createVectorHelper(player.posX - e.posX, 0, player.posZ - e.posZ);

					if(vec.lengthVector() < 3) {

						double intensity = 3 - vec.lengthVector();
						e.motionX += vec.xCoord * intensity * -2;
						e.motionY += 0.1D * intensity;
						e.motionZ += vec.zCoord * intensity * -2;

						e.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageBypassesArmor(), (float) (intensity * 10));
					}
				}
				// return;
			}

			if(prime.fall != null)
				player.playSound(prime.fall, 1.0F, 1.0F);
		}
	}

	@Override
	public void onArmorTick(World world, EntityPlayer entity, ItemStack stack) {

		if(!hasContentFSArmor(entity) || !this.geigerSound)
			return;

		if(entity.inventory.hasItem(ModItems.geiger_counter) || entity.inventory.hasItem(ModItems.dosimeter))
			return;

		if(world.getTotalWorldTime() % 5 == 0) {

			// Armor piece dosimeters indicate radiation dosage inside the armor, so reduce the counts by the effective protection
			float mod = ContaminationUtil.calculateRadiationMod(entity);
			float x = HbmLivingProps.getRadBuf(entity) * mod;

			if(x > 1E-5) {
				List<Integer> list = new ArrayList<Integer>();

				if(x < 1) list.add(0);
				if(x < 5) list.add(0);
				if(x < 10) list.add(1);
				if(x > 5 && x < 15) list.add(2);
				if(x > 10 && x < 20) list.add(3);
				if(x > 15 && x < 25) list.add(4);
				if(x > 20 && x < 30) list.add(5);
				if(x > 25) list.add(6);

				int r = list.get(world.rand.nextInt(list.size()));

				if(r > 0)
					world.playSoundAtEntity(entity, "hbm:item.geiger" + r, 1.0F, 1.0F);
			}
		}
	}
}
