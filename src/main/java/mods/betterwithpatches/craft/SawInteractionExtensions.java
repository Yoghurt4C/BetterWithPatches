package mods.betterwithpatches.craft;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.data.BWPMobDrops;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import java.util.Hashtable;
import java.util.function.BiFunction;

import static mods.betterwithpatches.util.BWPConstants.getId;

public interface SawInteractionExtensions {
    Hashtable<String, ItemStack[]> blockOverrides = new Hashtable<>();
    Hashtable<String, BWPMobDrops> entityDrops = new Hashtable<>();

    static void setBlock(Block block, ItemStack... product) {
        blockOverrides.put(getId(block), product);
    }

    static void setBlock(Block block, int meta, ItemStack... product) {
        blockOverrides.put(getId(block) + "@" + meta, product);
    }

    /**
     * @param entity        Any mob that will be able to drop additional loot when it dies.
     * @param choppingBlock If false, adds drops to any matching mob dying to a saw. If true, only adds drops to mobs that die to a saw while standing on a chopping block.
     * @param dropChance    1 ~ 100 chance for all of these items to drop. Will get clamped to either of these 2 values.
     * @param stacks        One or more ItemStacks that can drop from this mob.
     */
    static void setEntityDrop(Class<? extends EntityLivingBase> entity, boolean choppingBlock, int dropChance, ItemStack... stacks) {
        entityDrops.put(entity.getName(), choppingBlock
                ? new BWPMobDrops(0, null, dropChance, stacks)
                : new BWPMobDrops(MathHelper.clamp_int(dropChance, 1, 100), stacks, 0, null));
    }

    static void setEntityDrop(String entityClassName, boolean choppingBlock, int dropChance, ItemStack... stacks) {
        entityDrops.put(entityClassName, choppingBlock
                ? new BWPMobDrops(0, null, MathHelper.clamp_int(dropChance, 1, 100), stacks)
                : new BWPMobDrops(MathHelper.clamp_int(dropChance, 1, 100), stacks, 0, null));
    }

    /**
     * Same as above, but adds both kinds of drops at the same time.
     */
    static void setEntityDrop(Class<? extends EntityLivingBase> entity, int dropChance, ItemStack[] stacks, int chopChance, ItemStack[] chopStacks) {
        entityDrops.put(entity.getName(), new BWPMobDrops(MathHelper.clamp_int(dropChance, 1, 100), stacks, MathHelper.clamp_int(chopChance, 1, 100), chopStacks));
    }

    static void setEntityDrop(String entityClassName, int dropChance, ItemStack[] stacks, int chopChance, ItemStack[] chopStacks) {
        entityDrops.put(entityClassName, new BWPMobDrops(MathHelper.clamp_int(dropChance, 1, 100), stacks, MathHelper.clamp_int(chopChance, 1, 100), chopStacks));
    }

    /**
     * @param entity       Any mob that will be able to drop additional loot when it dies.
     * @param dropFunction Function that gets access to:
     *                     boolean chopping -> Whether the Mob got sawed on top of a Chopping Block;
     *                     EntityLivingBase entity -> The Mob that died.
     */
    static void setAdvancedEntityDrop(Class<? extends EntityLivingBase> entity, BiFunction<Boolean, EntityLivingBase, ItemStack[]> dropFunction) {
        entityDrops.put(entity.getName(), new BWPMobDrops(dropFunction));
    }

    static boolean containsBlock(Block block, int meta) {
        return blockOverrides.containsKey(getId(block) + "@" + meta);
    }

    static boolean containsEntity(EntityLivingBase entity) {
        return entityDrops.containsKey(entity.getClass().getName());
    }

    static ItemStack[] getBlockOverrides(Block block, int meta) {
        return blockOverrides.get(getId(block) + "@" + meta);
    }

    static BWPMobDrops getMobDrops(EntityLivingBase entity) {
        return entityDrops.get(entity.getClass().getName());
    }

    static ItemStack[] getSkeletonHead(boolean chopping, EntityLivingBase entity) {
        if (chopping && entity instanceof EntitySkeleton) {
            if (Config.choppingBlockHeadDropChance >= entity.worldObj.rand.nextInt(101)) {
                EntitySkeleton skeltal = (EntitySkeleton) entity;
                return new ItemStack[]{new ItemStack(Items.skull, 1, skeltal.getSkeletonType())};
            }
        }
        return null;
    }

    static ItemStack[] getPlayerHead(boolean chopping, EntityLivingBase entity) {
        if (chopping && entity instanceof EntityPlayer) {
            if (Config.choppingBlockHeadDropChance >= entity.worldObj.rand.nextInt(101)) {
                ItemStack stack = new ItemStack(Items.skull, 1, 3);
                EntityPlayer skeleplayer = (EntityPlayer) entity;
                NBTTagCompound name = new NBTTagCompound();
                name.setString("SkullOwner", skeleplayer.getDisplayName());
                stack.setTagCompound(name);
                return new ItemStack[]{stack};
            }
        }
        return null;
    }
}
