package mods.betterwithpatches.mixins.fixes;

import betterwithmods.items.ItemMaterial;
import mods.betterwithpatches.util.BWMaterials;
import net.minecraft.item.Item;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemMaterial.class)
public abstract class ItemMaterialMixin extends Item {
    @Shadow(remap = false) public String[] names;

    @Inject(method = "<init>", at = @At(value = "RETURN"), remap = false)
    public void noNames(CallbackInfo ctx) {
        this.names = null;
    }

    @Redirect(method = "getUnlocalizedName", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lbetterwithmods/items/ItemMaterial;names:[Ljava/lang/String;", remap = false))
    public String[] useOtherArray(ItemMaterial instance) {
        return BWMaterials.materialNames;
    }
}
