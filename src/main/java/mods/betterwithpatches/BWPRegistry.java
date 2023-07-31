package mods.betterwithpatches;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.betterwithpatches.block.BlockSteelAnvil;
import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.data.BWPCreativeTab;
import mods.betterwithpatches.item.tool.*;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;

public class BWPRegistry {
    public static final CreativeTabs bwpTab = new BWPCreativeTab();
    public static final Item.ToolMaterial SOULFORGED = EnumHelper.addToolMaterial("SOULFORGED_STEEL", 4, 2250, 10f, 3, 22);
    public static Block steelAnvil;
    public static Item steelAxe, steelHoe, steelPickaxe, steelShovel, steelSword;

    public static void init() {
        steelAnvil = GameRegistry.registerBlock(new BlockSteelAnvil(), ItemBlock.class, "steelAnvil");
        GameRegistry.registerTileEntity(TileEntitySteelAnvil.class, "bwm.steelAnvil");

        steelAxe = registerItem("steelAxe", new ItemSoulforgedAxe());
        steelHoe = registerItem("steelHoe", new ItemSoulforgedHoe());
        steelPickaxe = registerItem("steelPickaxe", new ItemSoulforgedPickaxe());
        steelShovel = registerItem("steelShovel", new ItemSoulforgedShovel());
        steelSword = registerItem("steelSword", new ItemSoulforgedSword());
    }

    public static Item registerItem(String id, Item item) {
        item.setUnlocalizedName("bwm:" + id);
        item.setTextureName(String.format("%s:%s", BWPConstants.MODID, id));
        item.setCreativeTab(bwpTab);
        GameRegistry.registerItem(item, id);
        return item;
    }
}
