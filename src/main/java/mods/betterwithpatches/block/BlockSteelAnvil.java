package mods.betterwithpatches.block;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockAesthetic;
import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSteelAnvil extends BlockContainer {

    public BlockSteelAnvil() {
        super(Material.iron);
        this.setBlockName("bwm:steelAnvil");
        this.setCreativeTab(ClientProxy.bwpTab);
        this.setHardness(5f);
        this.setResistance(2000f);
        this.setHarvestLevel("pickaxe", 1);
        this.setStepSound(Block.soundTypeAnvil);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ClientProxy.renderAnvil;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return ((BlockAesthetic) BWRegistry.aesthetic).icons[2];
    }

    @Override
    protected String getTextureName() {
        return "betterwithmods:soulforgedSteel";
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        int l = (MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
        switch (l) {
            case 1:
            case 3:
                worldIn.setBlockMetadataWithNotify(x, y, z, 1, 2);
                break;
            case 0:
            case 2:
            default:
                worldIn.setBlockMetadataWithNotify(x, y, z, 0, 2);
                break;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int i = world.getBlockMetadata(x, y, z) & 1;
        if (i == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
        } else {
            this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySteelAnvil();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        player.openGui(BWPConstants.MODID, 0, world, x, y, z);
        return true;
    }
}
