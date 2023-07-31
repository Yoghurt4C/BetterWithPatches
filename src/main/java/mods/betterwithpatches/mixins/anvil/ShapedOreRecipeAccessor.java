package mods.betterwithpatches.mixins.anvil;

import net.minecraftforge.oredict.ShapedOreRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapedOreRecipe.class)
public interface ShapedOreRecipeAccessor {
    @Accessor(remap = false)
    int getWidth();

    @Accessor(remap = false)
    int getHeight();
}
