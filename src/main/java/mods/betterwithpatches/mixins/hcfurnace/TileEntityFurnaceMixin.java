package mods.betterwithpatches.mixins.hcfurnace;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.features.HCFurnace;
import mods.betterwithpatches.util.IHCFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity implements IHCFurnace {
    @Shadow
    private ItemStack[] furnaceItemStacks;

    @Unique
    public int cachedCookTime = 0;


    @Inject(method = "updateEntity", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/tileentity/TileEntityFurnace;furnaceCookTime:I", ordinal = 0))
    public void setCachedCookTime(CallbackInfo ctx) {
        if (this.cachedCookTime == 0) {
            this.cachedCookTime = HCFurnace.getCookingTime(this.furnaceItemStacks[0]);
        }
    }

    @Inject(method = "updateEntity", at = {
            @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/tileentity/TileEntityFurnace;furnaceCookTime:I", shift = At.Shift.AFTER, ordinal = 1),
            @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/tileentity/TileEntityFurnace;furnaceCookTime:I", shift = At.Shift.AFTER, ordinal = 2)
    })
    public void clearCachedCookTime(CallbackInfo ctx) {
        this.cachedCookTime = 0;
    }

    @ModifyConstant(method = "updateEntity", constant = @Constant(intValue = 200))
    private int substichuchu(int twohundred) {
        return this.getCookTime();
    }

    @Override
    @Unique
    public int getCookTime() {
        return this.cachedCookTime == 0 ? Config.hcFurnaceDefaultCookTime : this.cachedCookTime;
    }

    @Override
    @Unique
    public void setCookTime(int ticks) {
        this.cachedCookTime = ticks;
    }
}
