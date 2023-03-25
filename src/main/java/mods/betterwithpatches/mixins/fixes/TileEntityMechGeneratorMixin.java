package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.tile.gen.TileEntityMechGenerator;
import mods.betterwithpatches.Config;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityMechGenerator.class)
public abstract class TileEntityMechGeneratorMixin extends TileEntity {
    @Shadow(remap = false)
    public abstract boolean verifyIntegrity();

    @Shadow(remap = false)
    public abstract void updateSpeed();

    @Shadow(remap = false)
    public byte runningState;
    @Shadow(remap = false)
    public int overpowerTime;

    @Shadow(remap = false)
    public abstract void overpower();

    @Unique
    private int timer = 0;

    /**
     * @reason Having these attached to the daylight cycle is quite silly, and so is wasting processing time on recalculating validity every second.
     */
    @Inject(method = "updateEntity", at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    public void patchGenerators(CallbackInfo ctx) {
        ctx.cancel();
        if (this.timer >= (this.runningState == 1 ? Config.lazyGeneratorDelay : 20)) {
            this.verifyIntegrity();
            this.updateSpeed();
            if (this.runningState == 2 && this.overpowerTime < 1) {
                this.overpower();
            } else if (this.runningState != 2 && this.overpowerTime != 30) {
                this.overpowerTime = 30;
            } else if (this.runningState == 2) {
                --this.overpowerTime;
            }
            this.timer = 0;
        }
        this.timer++;
    }
}
