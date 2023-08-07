package mods.betterwithpatches.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ModelDredgeHeavyArmor extends ModelBiped {

    private final ModelRenderer flap;
    public static final ResourceLocation texPath = new ResourceLocation("betterwithpatches:textures/models/armor/dredge_heavy_layer_0.png");
    public static final ResourceLocation overPath = new ResourceLocation("betterwithpatches:textures/models/armor/dredge_heavy_layer_1.png");

    public ModelDredgeHeavyArmor(int slot) {
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
                backplate.cubeList.add(new ModelBox(backplate, 50, 10, -3.0F, -0.1F, 2.0F, 6, 6, 1, inflate - 0.1F));

                ModelRenderer breastplate = new ModelRenderer(this);
                breastplate.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedBody.addChild(breastplate);
                setRotationAngle(breastplate, 0.0873F, 0.0F, 0.0F);
                breastplate.cubeList.add(new ModelBox(breastplate, 32, 10, -4.0F, -0.35F, -3.2F, 8, 6, 1, inflate - 0.25F));

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
                rLegGuard.cubeList.add(new ModelBox(rLegGuard, 44, 17, -2.85F, -2.0F, -3.0F, 4, 7, 6, inflate - 0.55F));

                this.bipedLeftLeg.isHidden = false;
                this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
                this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 112, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, inflate - 0.001f));
                ModelRenderer lLegGuard = new ModelRenderer(this);
                lLegGuard.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedLeftLeg.addChild(lLegGuard);
                setRotationAngle(lLegGuard, 0.0F, 0.0F, -0.0873F);
                lLegGuard.cubeList.add(new ModelBox(lLegGuard, 24, 17, -1.15F, -2.0F, -3.0F, 4, 7, 6, inflate - 0.55F));
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
    public void render(Entity entity, float limbSwing, float prevLimbSwing, float wrappedYaw, float yawHead, float pitch, float scale) {
        this.setRotationAngles(limbSwing, prevLimbSwing, wrappedYaw, yawHead, pitch, scale, entity);
        if (entity instanceof EntityZombie) {
            float f6 = MathHelper.sin(this.onGround * (float)Math.PI);
            float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
            this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
            this.bipedRightArm.rotateAngleX = -((float)Math.PI * 0.5F);
            this.bipedLeftArm.rotateAngleX = -((float)Math.PI * 0.5F);
            this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(prevLimbSwing * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(prevLimbSwing * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(prevLimbSwing * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(prevLimbSwing * 0.067F) * 0.05F;
        }

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

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float prevLimbSwing, float tickDelta) {
        super.setLivingAnimations(entity, limbSwing, prevLimbSwing, tickDelta);
        if (!this.bipedBody.isHidden && entity.isSwingInProgress && entity.worldObj.rand.nextBoolean()) {
            Random rand = entity.worldObj.rand;
            Vec3 vector = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight() - 0.07f, entity.posZ);
            Vec3 v2 = Vec3.createVectorHelper(0.36, 0, 0);
            v2.rotateAroundY(-entity.rotationYaw * 0.01745329251994329576923690768489f);
            vector = v2.subtract(vector);
            entity.worldObj.spawnParticle("smoke", vector.xCoord, vector.yCoord, vector.zCoord, rand.nextFloat() * 0.0125f, 0.05f, rand.nextFloat() * 0.0125f);
        }
        this.isSneak = entity.isSneaking();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

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
        } else if (entity instanceof EntitySkeleton) {
            this.aimedBow = ((EntitySkeleton) entity).getSkeletonType() == 1;
        }
        float mot = (float) Math.max(Math.abs(entity.motionX), Math.abs(entity.motionZ));
        mot = Math.min(mot * 8, 1.2f);
        float motY = Math.min(0.1f, (float) entity.motionY * 2);
        mot -= Math.max(-1f, motY);
        if (this.isSneak) mot += 0.35f;
        this.flap.rotateAngleX = 0.15f + mot;
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
}