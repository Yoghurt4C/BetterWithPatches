package mods.betterwithpatches.mixins.penalty;

import com.mojang.authlib.GameProfile;
import mods.betterwithpatches.data.PenaltyRegistry;
import mods.betterwithpatches.util.PenaltyTicker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin implements PenaltyTicker {
    @Unique
    public byte cooldown;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void ctor(World world, GameProfile profile, CallbackInfo ctx) {
        this.cooldown = 20;
    }

    @Override
    public void tickPenaltyCooldown() {
        if (this.cooldown >= 20) {
            this.cooldown = 0;
            PenaltyRegistry.computePlayerPenalties((EntityPlayer) (Object) this);
        } else {
            this.cooldown++;
        }
    }
}
