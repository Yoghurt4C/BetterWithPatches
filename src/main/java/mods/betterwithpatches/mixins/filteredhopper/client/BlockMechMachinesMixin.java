package mods.betterwithpatches.mixins.filteredhopper.client;

import betterwithmods.blocks.BlockMechMachines;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockMechMachines.class)
public abstract class BlockMechMachinesMixin {
    @Shadow(remap = false) public static IIcon[] topTextures;
    @Shadow(remap = false) public static IIcon[] sideTextures;
    @Shadow(remap = false) public static IIcon[] bottomTextures;
    @Shadow(remap = false) public static String[] machine;

    @Inject(method = "registerIcons", at = @At(value = "JUMP", opcode = Opcodes.IF_ICMPGE, ordinal = 0), remap = false, cancellable = true)
    public void noTextureErrors(IIconRegister reg, CallbackInfo ctx) {
        ctx.cancel();
        for(int i = 0; i < 6; ++i) {
            topTextures[i] = reg.registerIcon("betterwithmods:" + machine[i] + "Top");
            if (i == 4) continue;
            sideTextures[i] = reg.registerIcon("betterwithmods:" + machine[i] + "Side");
            bottomTextures[i] = reg.registerIcon("betterwithmods:" + machine[i] + "Bottom");
        }

        bottomTextures[4] = reg.registerIcon("betterwithmods:" + machine[4] + "Top");
        sideTextures[4] = reg.registerIcon("planks_oak");
    }
}
