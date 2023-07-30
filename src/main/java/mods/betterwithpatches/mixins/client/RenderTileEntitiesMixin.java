package mods.betterwithpatches.mixins.client;

import betterwithmods.client.RenderTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderTileEntities.class)
public abstract class RenderTileEntitiesMixin {

    /**
     * Original method makes a separate draw call for every face. WHY?
     */
    @Inject(method = "renderItemBlock", at = @At("HEAD"), remap = false, cancellable = true)
    private static void lessDrawCalls(Block block, RenderBlocks render, int meta, CallbackInfo ctx) {
        ctx.cancel();
        block.setBlockBoundsForItemRender();
        Tessellator tes = Tessellator.instance;
        IIcon bTex = block.getIcon(0, meta);
        IIcon tTex = block.getIcon(1, meta);
        IIcon ewTex = block.getIcon(4, meta);
        IIcon nsTex = block.getIcon(2, meta);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        render.renderFaceYNeg(block, 0.0, 0.0, 0.0, bTex);
        tes.setNormal(0.0F, 1.0F, 0.0F);
        render.renderFaceYPos(block, 0.0, 0.0, 0.0, tTex);
        tes.setNormal(0.0F, 0.0F, -1.0F);
        render.renderFaceXPos(block, 0.0, 0.0, 0.0, ewTex);
        tes.setNormal(0.0F, 0.0F, 1.0F);
        render.renderFaceXNeg(block, 0.0, 0.0, 0.0, ewTex);
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        render.renderFaceZPos(block, 0.0, 0.0, 0.0, nsTex);
        tes.setNormal(1.0F, 0.0F, 0.0F);
        render.renderFaceZNeg(block, 0.0, 0.0, 0.0, nsTex);
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
