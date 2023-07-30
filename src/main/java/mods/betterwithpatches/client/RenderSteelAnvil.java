package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderSteelAnvil implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.25, 0.25, 0.1875, 0.75, 0.3125, 0.8125);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.375, 0.3125, 0.25, 0.625, 0.625, 0.75);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.1875, 0.625, 0, 0.8125, 1, 1);
        renderItemBlock(block, renderer, metadata);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
        renderer.renderStandardBlock(block, x, y, z);
        if (meta == 0) {
            renderer.setRenderBounds(0.1875, 0.25, 0.25, 0.8125, 0.3125, 0.75);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.25, 0.3125, 0.375, 0.75, 0.625, 0.625);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0, 0.625, 0.1875, 1, 1, 0.8175);
        } else {
            renderer.setRenderBounds(0.25, 0.25, 0.1875, 0.75, 0.3125, 0.8125);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.375, 0.3125, 0.25, 0.625, 0.625, 0.75);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.1875, 0.625, 0, 0.8125, 1, 1);
        }
        renderer.renderStandardBlock(block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderAnvil;
    }
}
