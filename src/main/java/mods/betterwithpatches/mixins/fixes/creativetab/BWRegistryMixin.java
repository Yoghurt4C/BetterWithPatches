package mods.betterwithpatches.mixins.fixes.creativetab;

import betterwithmods.BWRegistry;
import mods.betterwithpatches.BWPRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BWRegistry.class)
public abstract class BWRegistryMixin {
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/registry/GameRegistry;registerBlock(Lnet/minecraft/block/Block;Ljava/lang/String;)Lnet/minecraft/block/Block;", remap = false),
            slice = @Slice(to = @At(value = "INVOKE", target = "Lbetterwithmods/BWRegistry;registerOres()V", remap = false)), remap = false)
    private static Block fix1(Block block) {
        return block.setCreativeTab(BWPRegistry.bwpTab);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/registry/GameRegistry;registerBlock(Lnet/minecraft/block/Block;Ljava/lang/Class;Ljava/lang/String;)Lnet/minecraft/block/Block;", remap = false),
            slice = @Slice(to = @At(value = "INVOKE", target = "Lbetterwithmods/BWRegistry;registerOres()V", remap = false)), remap = false)
    private static Block fix2(Block block) {
        return block.setCreativeTab(BWPRegistry.bwpTab);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/registry/GameRegistry;registerItem(Lnet/minecraft/item/Item;Ljava/lang/String;)V", remap = false),
            slice = @Slice(to = @At(value = "INVOKE", target = "Lbetterwithmods/BWRegistry;registerOres()V", remap = false)), remap = false)
    private static Item fix3(Item item) {
        return item.setCreativeTab(BWPRegistry.bwpTab);
    }
}
