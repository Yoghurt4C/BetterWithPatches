package mods.betterwithpatches.craft;

import mods.betterwithpatches.item.tool.MultiRenderPassArmor;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

public class RecipeMultiRenderPassArmorDyes implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        boolean hasArmor = false, hasDye = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            else if (stack.getItem() instanceof MultiRenderPassArmor && !hasArmor) {
                hasArmor = true;
            } else {
                boolean pass = false;
                int[] ids = OreDictionary.getOreIDs(stack);
                for (int dyeOreId : BWPConstants.dyeOreIds) {
                    if (ArrayUtils.contains(ids, dyeOreId)) {
                        pass = hasDye = true;
                        break;
                    }
                }
                if (!pass) return false;
            }
        }
        return hasArmor && hasDye;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack armor = null;
        MultiRenderPassArmor mrpa = null;
        int pass = 0;
        int colors = 0;
        float[] rgb = new float[3];
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            else if (stack.getItem() instanceof MultiRenderPassArmor) {
                mrpa = (MultiRenderPassArmor) stack.getItem();
                for (int j = 0; j < stack.getItem().getRenderPasses(stack.getItemDamage()); j++) {
                    int color = mrpa.getColorForRenderPass(stack, j);
                    if (color == mrpa.getDefaultColorForRenderPass(j)) {
                        armor = stack.copy();
                        pass = j;
                        break;
                    }

                }
            } else {
                int[] oreStack = OreDictionary.getOreIDs(stack);
                for (int j = 0; j < BWPConstants.dyeOreIds.length; j++) {
                    if (ArrayUtils.contains(oreStack, BWPConstants.dyeOreIds[j])) {
                        float[] dye = EntitySheep.fleeceColorTable[15 - j];
                        int r = (int) (dye[0] * 255);
                        int b = (int) (dye[1] * 255);
                        int g = (int) (dye[2] * 255);
                        rgb[0] += r * r;
                        rgb[1] += g * g;
                        rgb[2] += b * b;
                        colors++;
                        break;
                    }
                }
            }
        }

        if (armor == null) return null;
        else if (colors > 0) {
            int r = (int) Math.min(255, Math.sqrt(rgb[0] / colors)) & 0xFF;
            int b = (int) Math.min(255, Math.sqrt(rgb[1] / colors)) & 0xFF;
            int g = (int) Math.min(255, Math.sqrt(rgb[2] / colors)) & 0xFF;
            int color = r << 16
                    | g << 8
                    | b;
            mrpa.setColor(armor, pass, color);
        }

        return armor;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
