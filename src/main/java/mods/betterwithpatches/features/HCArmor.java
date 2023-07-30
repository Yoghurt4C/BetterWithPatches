package mods.betterwithpatches.features;

import mods.betterwithpatches.data.IngredientMap;
import mods.betterwithpatches.data.Penalty;
import mods.betterwithpatches.data.PenaltyRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface HCArmor {
    IngredientMap<Integer> weights = new IngredientMap<>(3);

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

        /*
        weights.put(BWRegistry.STEEL_HELMET, any, 5);
        weights.put(BWMItems.STEEL_CHEST, any, 8);
        weights.put(BWMItems.STEEL_PANTS, any, 7);
        weights.put(BWMItems.STEEL_BOOTS, any, 4);
         */
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
