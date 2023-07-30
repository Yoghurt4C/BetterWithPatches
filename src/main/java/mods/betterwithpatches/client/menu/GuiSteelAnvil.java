package mods.betterwithpatches.client.menu;

import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.menu.MenuSteelAnvil;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSteelAnvil extends GuiContainer {
    private final TileEntitySteelAnvil anvil;

    public GuiSteelAnvil(InventoryPlayer player, TileEntitySteelAnvil anvil) {
        super(new MenuSteelAnvil(player, anvil));
        this.anvil = anvil;
        this.ySize = 183;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRendererObj.drawString(I18n.format(anvil.getInventoryName()), 10, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(BWPConstants.MODID, "textures/gui/steel_anvil.png"));

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
    }

}
