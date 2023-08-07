package mods.betterwithpatches.mixins.fixes.client;

import betterwithmods.items.ItemMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mods.betterwithpatches.util.BWMaterials.*;

@Mixin(ItemMaterial.class)
public abstract class ItemMaterialMixin {
    @Shadow(remap = false) public IIcon[] icons;

    @ModifyConstant(method = "getSubItems", constant = @Constant(intValue = 42, ordinal = 0))
    private int entireArray(int fortytwo) {
        return materialNames.length;
    }

    @Inject(method = "registerIcons", at = @At("HEAD"), cancellable = true)
    public void replaceEntireMethod(IIconRegister reg, CallbackInfo ctx) {
        ctx.cancel();
        this.icons = new IIcon[materialNames.length];
        for (int i = 0; i < materialNames.length; i++) {
            String modid;
            switch (i) {
                case FUSE:
                case COAL_DUST:
                    modid = "betterwithpatches:item";
                    break;
                default:
                    modid = "betterwithmods:item";
            }
            this.icons[i] = reg.registerIcon(modid + materialNames[i]);
        }
    }
}
