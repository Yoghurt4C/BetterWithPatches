package mods.betterwithpatches.features;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.data.recipe.ItemStackMap;
import mods.betterwithpatches.data.Penalty;
import mods.betterwithpatches.data.PenaltyRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface HCArmor {
    ItemStackMap<Integer> weights = new ItemStackMap<>(3);

    static int getWeight(ItemStack stack) {
        return weights.get(stack);
    }

    static void registerHCArmor() {
        PenaltyRegistry.PENALTY_PREDICATES.put("HCArmor", new ArmorPenalty());

        int any = OreDictionary.WILDCARD_VALUE;
        weights.put(Items.chainmail_helmet, any, 3);
        weights.put(Items.chainmail_chestplate, any, 4);
        weights.put(Items.chainmail_leggings, any, 4);
        weights.put(Items.chainmail_boots, any, 2);

        weights.put(Items.iron_helmet, any, 5);
        weights.put(Items.iron_chestplate, any, 8);
        weights.put(Items.iron_leggings, any, 7);
        weights.put(Items.iron_boots, any, 4);

        weights.put(Items.diamond_helmet, any, 5);
        weights.put(Items.diamond_chestplate, any, 8);
        weights.put(Items.diamond_leggings, any, 7);
        weights.put(Items.diamond_boots, any, 4);

        weights.put(Items.golden_helmet, any, 5);
        weights.put(Items.golden_chestplate, any, 8);
        weights.put(Items.golden_leggings, any, 7);
        weights.put(Items.golden_boots, any, 2);

        weights.put(BWPRegistry.steelHelmet, any, 5);
        weights.put(BWPRegistry.steelChestplate, any, 8);
        weights.put(BWPRegistry.steelLeggings, any, 7);
        weights.put(BWPRegistry.steelBoots, any, 4);

        weights.put(BWPRegistry.dredgeHeavyHelmet, any, 4);
        weights.put(BWPRegistry.dredgeHeavyChestplate, any, 7);
        weights.put(BWPRegistry.dredgeHeavyLeggings, any, 5);
        weights.put(BWPRegistry.dredgeHeavyBoots, any, 4);
    }

    class ArmorPenalty implements Penalty {
        @Override
        public void apply(EntityPlayer player, PenaltyRegistry.PenaltyData data) {
            int weight = 0;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == null) continue;
                weight += getWeight(stack);
            }
            if (weight > 23) {
                data.speedMod *= 0.8f;
                data.canSwim -= 1;
            } else if (weight > 12) {
                data.speedMod *= 0.9f;
            }
        }
    }
}
