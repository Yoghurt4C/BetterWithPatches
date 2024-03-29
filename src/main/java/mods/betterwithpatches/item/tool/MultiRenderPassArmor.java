package mods.betterwithpatches.item.tool;

import net.minecraft.item.ItemStack;

public interface MultiRenderPassArmor {
    int getDefaultColorForRenderPass(int pass);

    boolean hasColor(ItemStack stack);

    int getColorForRenderPass(ItemStack stack, int pass);

    void setColor(ItemStack stack, int pass, int color);

    void removeColor(ItemStack stack, int pass);
}
