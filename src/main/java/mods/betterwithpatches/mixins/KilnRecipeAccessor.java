package mods.betterwithpatches.mixins;

import betterwithmods.craft.KilnInteraction;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Hashtable;

@Mixin(KilnInteraction.class)
public interface KilnRecipeAccessor {
    @Accessor(remap = false)
    static Hashtable<String, ItemStack> getCookables() {
        throw new RuntimeException("tee hee");
    }
}
