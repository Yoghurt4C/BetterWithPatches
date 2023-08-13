package mods.betterwithpatches.mixins.client;

import mods.betterwithpatches.item.tool.MultiRenderPassArmor;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RenderBiped.class)
public class RenderBipedMixin {
    @Inject(method = "func_82408_c(Lnet/minecraft/entity/EntityLiving;IF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderBiped;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void doSillyThings(EntityLiving biped, int slot, float scale, CallbackInfo ctx, ItemStack stack, Item item) {
        if (item instanceof MultiRenderPassArmor) {
            ctx.cancel();
            int color = ((MultiRenderPassArmor) item).getColorForRenderPass(stack, 1);
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            GL11.glColor3f(r, g, b);
        }
    }
}
