package mods.betterwithpatches.event;

import betterwithmods.event.MobDropEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.data.BWPMobDrops;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class MobDropEventReplacement extends MobDropEvent {

    /**
     * Mobs can now do many funny things when killed by a Saw! Usually they just drop more items, but more behaviours can be defined externally.
     */
    @Override
    @SubscribeEvent
    public void mobDiesBySaw(LivingDropsEvent evt) {
        boolean chop = evt.source.damageType.equals("choppingBlock");
        if (evt.source.damageType.equals("saw") || chop) {
            for (EntityItem itemEntity : evt.drops) {
                ItemStack drop = itemEntity.getEntityItem();
                if (drop.getMaxStackSize() != 1 && evt.entity.worldObj.rand.nextBoolean()) {
                    itemEntity.setEntityItemStack(new ItemStack(drop.getItem(), drop.stackSize + 1, drop.getItemDamage()));
                }
            }

            if (SawInteractionExtensions.containsEntity(evt.entityLiving)) {
                BWPMobDrops drops = SawInteractionExtensions.getMobDrops(evt.entityLiving);
                if (drops.advancedDropBehaviour == null) {
                    int rand = evt.entityLiving.worldObj.rand.nextInt(101);
                    if (drops.stacks != null && drops.dropChance >= rand) {
                        for (ItemStack stack : drops.stacks) {
                            this.addDrop(evt, stack);
                        }
                    }
                    if (chop && drops.chopStacks != null && drops.chopChance >= rand) {
                        for (ItemStack stack : drops.chopStacks) {
                            this.addDrop(evt, stack);
                        }
                    }
                } else {
                    ItemStack[] stacks = drops.advancedDropBehaviour.apply(chop, evt.entityLiving);
                    if (stacks == null) return;
                    for (ItemStack stack : stacks) {
                        this.addDrop(evt, stack);
                    }
                }
            }
        }
    }
}
