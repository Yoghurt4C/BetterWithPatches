package mods.betterwithpatches.mixins.hcwood;

import betterwithmods.items.ItemBark;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBark.class)
public abstract class ItemBarkMixin extends Item {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lbetterwithmods/items/ItemBark;setHasSubtypes(Z)Lnet/minecraft/item/Item;"))
    public boolean noMeta(boolean bl) {
        return false;
    }
    
    @Inject(method = "getUnlocalizedName", at = @At("HEAD"), cancellable = true)
    public void justBark(ItemStack stack, CallbackInfoReturnable<String> ctx) {
        ctx.setReturnValue("item.bwm:bark.0");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.stackTagCompound;
            String id = tag.getString("logId");
            int meta = tag.getInteger("logMeta");
            Item log = GameData.getItemRegistry().getObject(id);
            String name = log.getItemStackDisplayName(new ItemStack(log, 1, meta));
            name += " " + I18n.format("text.bwp:bark");
            return name;
        }
        return I18n.format("tile.log.oak.name") + " " + I18n.format("text.bwp:bark");
    }
}
