package com.hbm.render.model;

import com.hbm.main.ResourceManager;
import com.hbm.render.loader.ModelRendererObj;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelMyArmor extends ModelArmorBase {

	public ModelMyArmor(int type) {
		super(type);

		head = new ModelRendererObj(ResourceManager.my_armor, "Head");
		body = new ModelRendererObj(ResourceManager.my_armor, "Body");
		leftArm = new ModelRendererObj(ResourceManager.my_armor, "LeftArm").setRotationPoint(-5.0F, 2.0F, 0.0F);
		rightArm = new ModelRendererObj(ResourceManager.my_armor, "RightArm").setRotationPoint(5.0F, 2.0F, 0.0F);
		leftLeg = new ModelRendererObj(ResourceManager.my_armor, "LeftLeg").setRotationPoint(1.9F, 12.0F, 0.0F);
		rightLeg = new ModelRendererObj(ResourceManager.my_armor, "RightLeg").setRotationPoint(-1.9F, 12.0F, 0.0F);
		leftFoot = new ModelRendererObj(ResourceManager.my_armor, "LeftBoot").setRotationPoint(1.9F, 12.0F, 0.0F);
		rightFoot = new ModelRendererObj(ResourceManager.my_armor, "RightBoot").setRotationPoint(-1.9F, 12.0F, 0.0F);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {

		setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

		GL11.glPushMatrix();
		GL11.glShadeModel(GL11.GL_SMOOTH);

		if(type == 0) {
			Minecraft.getMinecraft().renderEngine.bindTexture(ResourceManager.my_armor_texture);
			head.render(par7);
		}
		if(type == 1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(ResourceManager.my_armor_texture);
			body.render(par7);
			Minecraft.getMinecraft().renderEngine.bindTexture(ResourceManager.my_armor_texture);
			leftArm.render(par7);
			rightArm.render(par7);
		}
		if(type == 2) {
			Minecraft.getMinecraft().renderEngine.bindTexture(ResourceManager.my_armor_texture);
			leftLeg.render(par7);
			rightLeg.render(par7);
		}
		if(type == 3) {
			Minecraft.getMinecraft().renderEngine.bindTexture(ResourceManager.my_armor_texture);
			leftFoot.render(par7);
			rightFoot.render(par7);
		}

		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
	}
}
