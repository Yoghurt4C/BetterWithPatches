package mods.betterwithpatches.mixins;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import mods.betterwithpatches.util.BWMRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(CraftingManagerBulk.class)
public abstract class CraftingManagerBulkMixin implements BWMRecipeAccessor {
    @Shadow(remap = false)
    private List<BulkRecipe> recipes;

    @Override
    @Unique
    public List<BulkRecipe> getRecipes() {
        return this.recipes;
    }
}
