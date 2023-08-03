package mods.betterwithpatches.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DredgeHeavyArmorRenderer extends ModelBiped {

	private final int slot;
	private final ModelRenderer flap;
	public static final ResourceLocation texPath = new ResourceLocation("betterwithpatches:textures/models/armor/dredge_heavy_layer_0.png");
	public static final ResourceLocation overPath = new ResourceLocation("betterwithpatches:textures/models/armor/dredge_heavy_layer_1.png");

	public DredgeHeavyArmorRenderer(int slot) {
		this.slot = 4 - slot;
		this.textureWidth = 128;
		this.textureHeight = 64;
		float inflate = 0.3f;

		//unused
		this.bipedHeadwear = newPart();
		this.bipedEars = newPart();
		this.bipedCloak = newPart();

		this.bipedHead = newPart();
		this.bipedBody = newPart();
		this.bipedRightArm = newPart();
		this.bipedLeftArm = newPart();
		this.flap = newPart();
		this.bipedLeftLeg = newPart();
		this.bipedRightLeg = newPart();
		switch (slot) {
			case 0:
				this.bipedHead.isHidden = false;
				this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, inflate + 0.2f));
				this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 96, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, inflate + 0.4F));
				break;
			case 1:
				this.bipedBody.isHidden = false;
				this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedBody.cubeList.add(new ModelBox(this.bipedBody, 0, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, inflate - 0.1F));
				this.bipedBody.cubeList.add(new ModelBox(this.bipedBody, 90, 0, -3.0F, 10.25F, -2.25F, 6, 3, 1, inflate - 0.2F));

				this.flap.isHidden = false;
				this.flap.setRotationPoint(0f, 9f, 2f);
				this.flap.cubeList.add(new ModelBox(this.flap, 104, 16, -4.0F, 0.0F, -3.25f, 8, 12, 4, inflate + 0.05f));

				ModelRenderer backplate = new ModelRenderer(this);
				backplate.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedBody.addChild(backplate);
				setRotationAngle(backplate, -0.0873F, 0.0F, 0.0F);
				backplate.cubeList.add(new ModelBox(backplate, 50, 10, -3.0F, -0.1F, 2.0F, 6, 6, 1, inflate-0.1F));

				ModelRenderer breastplate = new ModelRenderer(this);
				breastplate.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedBody.addChild(breastplate);
				setRotationAngle(breastplate, 0.0873F, 0.0F, 0.0F);
				breastplate.cubeList.add(new ModelBox(breastplate, 32, 10, -4.0F, -0.35F, -3.2F, 8, 6, 1, inflate-0.25F));

				this.bipedRightArm.isHidden = false;
				this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
				this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 112, 32, -3.0F, -2.0F, -2.0F, 4, 12, 4, inflate));
				this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 0, 32, -3.0F, -2.0F, -2.0F, 4, 12, 4, inflate + 0.5F));
				this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 0, 48, -2.5F, -5.5F, -1.5F, 3, 3, 3, inflate));
				this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 12, 48, -3.75F, -3.75F, -2.5F, 5, 2, 5, inflate + 0.25F));

				this.bipedLeftArm.isHidden = false;
				this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
				this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 112, 32, -1.0F, -2.0F, -2.0F, 4, 12, 4, inflate));
				this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 16, 32, -1.0F, -2.0F, -2.0F, 4, 12, 4, inflate + 0.5F));
				this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 32, 48, -1.25F, -3.75F, -2.5F, 5, 2, 5, inflate + 0.25F));
				break;
			case 2:
				this.bipedRightLeg.isHidden = false;
				this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
				this.bipedRightLeg.cubeList.add(new ModelBox(this.bipedRightLeg, 112, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, inflate));
				ModelRenderer rLegGuard = new ModelRenderer(this);
				rLegGuard.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedRightLeg.addChild(rLegGuard);
				setRotationAngle(rLegGuard, 0.0F, 0.0F, 0.0873F);
				rLegGuard.cubeList.add(new ModelBox(rLegGuard, 44, 17, -2.85F, -2.0F, -3.0F, 4, 7, 6, inflate-0.55F));

				this.bipedLeftLeg.isHidden = false;
				this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
				this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 112, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, inflate - 0.001f));
				ModelRenderer lLegGuard = new ModelRenderer(this);
				lLegGuard.setRotationPoint(0.0F, 0.0F, 0.0F);
				this.bipedLeftLeg.addChild(lLegGuard);
				setRotationAngle(lLegGuard, 0.0F, 0.0F, -0.0873F);
				lLegGuard.cubeList.add(new ModelBox(lLegGuard, 24, 17, -1.15F, -2.0F, -3.0F, 4, 7, 6, inflate-0.55F));
				break;
			case 3:
				this.bipedRightLeg.isHidden = false;
				this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
				this.bipedRightLeg.cubeList.add(new ModelBox(this.bipedRightLeg, 32, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, inflate + 0.25F));

				this.bipedLeftLeg.isHidden = false;
				this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
				this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 48, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, inflate + 0.249F));
				break;
		}
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.prepareForRender(entity);
		float mot = (float) Math.max(Math.abs(entity.motionX), Math.abs(entity.motionZ));
		float rot = Math.min(mot * 8, 1.2f);
		if (this.isSneak) rot += 0.35f;
		this.setRotationAngles(f, f1, f2, f3, f5, f5, entity);
		this.flap.rotateAngleX = 0.15f + rot;

		ItemStack stack = ((EntityLivingBase) entity).getEquipmentInSlot(this.slot);
		if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("color")) {
			NBTTagCompound color = stack.getTagCompound().getCompoundTag("color");
			int steel = color.getInteger("steel");
			int leather = color.getInteger("leather");
			setRenderColor(steel);
			this.renderModel(f5);
			setRenderColor(leather);
		} else {
			this.renderModel(f5);
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(overPath);
		this.renderModel(f5);
		//Minecraft.getMinecraft().getTextureManager().bindTexture(texPath);
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public void prepareForRender(Entity entity) {
		EntityLivingBase living = (EntityLivingBase) entity;
		this.isSneak = living != null && living.isSneaking();
		if(living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;

			ItemStack itemstack = player.inventory.getCurrentItem();
			this.heldItemRight = itemstack != null ? 1 : 0;

			this.aimedBow = false;
			if (itemstack != null && player.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.block)
					this.heldItemRight = 3;
				else if (enumaction == EnumAction.bow)
					this.aimedBow = true;
			}
		}
	}

	public ModelRenderer newPart() {
		ModelRenderer part = new ModelRenderer(this);
		part.isHidden = true;
		return part;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	private void setRenderColor(int color) {
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 0.75f);
	}

	public void renderModel(float scale) {
		if (this.isChild) {
			GL11.glPushMatrix();
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			GL11.glTranslatef(0.0F, 16.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			GL11.glPopMatrix();
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F);
		} else {
			this.bipedHead.render(scale);
		}
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);
		if (this.isSneak) {
			GL11.glTranslatef(0, -0.18f, 0.28f);
			this.flap.render(scale);
			GL11.glTranslatef(0, 0.18f, -0.28f);
		} else {
			this.flap.render(scale);
		}
	}
}