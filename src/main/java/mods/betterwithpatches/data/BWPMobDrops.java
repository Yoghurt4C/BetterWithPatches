package mods.betterwithpatches.data;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

/**
 * Holder class for {@link mods.betterwithpatches.craft.SawInteractionExtensions#entityDrops}. Currently very poorly implemented and is subject to change.
 */
public class BWPMobDrops {
    public final ItemStack[] stacks, chopStacks;
    public final int dropChance, chopChance;

    public final BiFunction<Boolean, EntityLivingBase, ItemStack[]> advancedDropBehaviour;

    public BWPMobDrops(int dropChance, ItemStack[] stacks, int chopChance, ItemStack[] chopStacks) {
        this.stacks = stacks;
        this.chopStacks = chopStacks;
        this.dropChance = dropChance;
        this.chopChance = chopChance;
        this.advancedDropBehaviour = null;
    }

    public BWPMobDrops(BiFunction<Boolean, EntityLivingBase, ItemStack[]> advancedDropBehaviour) {
        this.stacks = null;
        this.chopStacks = null;
        this.dropChance = 0;
        this.chopChance = 0;
        this.advancedDropBehaviour = advancedDropBehaviour;
    }
}
