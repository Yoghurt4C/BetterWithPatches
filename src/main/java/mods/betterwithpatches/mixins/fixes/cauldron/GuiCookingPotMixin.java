package mods.betterwithpatches.mixins.fixes.cauldron;

import betterwithmods.blocks.tile.TileEntityCookingPot;
import betterwithmods.client.container.GuiCookingPot;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiCookingPot.class)
public abstract class GuiCookingPotMixin {

    @Shadow(remap = false)
    private TileEntityCookingPot tile;

    @Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    private void modid(TextureManager tm, ResourceLocation identifier) {
        tm.bindTexture(new ResourceLocation(BWPConstants.MODID, "textures/gui/cauldron.png"));
    }

    @ModifyArg(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lbetterwithmods/client/container/GuiCookingPot;drawTexturedModalRect(IIIIII)V", ordinal = 1), index = 3)
    private int getY(int y) {
        if (this.tile.fireIntensity > 4)
            return y + 14;
        return y;
    }
}
