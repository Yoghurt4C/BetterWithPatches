package mods.betterwithpatches.menu;

import cpw.mods.fml.common.network.IGuiHandler;
import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.client.menu.GuiSteelAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BWPMenuHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntitySteelAnvil)
                return new MenuSteelAnvil(player.inventory, (TileEntitySteelAnvil) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntitySteelAnvil)
                return new GuiSteelAnvil(player.inventory, (TileEntitySteelAnvil) tile);
        }
        return null;
    }
}
