package mods.betterwithpatches.mixins;

import betterwithmods.BWRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.BlockSteelAnvil;
import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import net.minecraft.item.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BWRegistry.class)
public abstract class BWRegistryMixin {

    /**
     * Circumventing Forge's idiotic registry limitations by registering items through coremodding - because that's much saner than being able to pass your own modid.
     */
    @Inject(method = "init", at = @At("TAIL"), remap = false)
    private static void moreThings(CallbackInfo ctx) {
        BWPRegistry.steelAnvil = GameRegistry.registerBlock(new BlockSteelAnvil(), ItemBlock.class, "steelAnvil");
        GameRegistry.registerTileEntity(TileEntitySteelAnvil.class, "bwm.steelAnvil");
    }
}
