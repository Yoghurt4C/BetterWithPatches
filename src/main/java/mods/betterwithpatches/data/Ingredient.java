package mods.betterwithpatches.data;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class Ingredient {
    public final Item item;
    public final int meta;
    public final byte type;

    public Ingredient(ItemStack stack) {
        this.item = stack.getItem();
        this.meta = stack.getItemDamage();
        this.type = (byte) (stack.getItem() instanceof ItemBlock ? 1 : 0);
    }

    public Ingredient(Item item, int meta) {
        this.item = item;
        this.meta = meta;
        this.type = 0;
    }

    public Ingredient(Block block, int meta) {
        this.item = Item.getItemFromBlock(block);
        this.meta = meta;
        this.type = 1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Ingredient) {
            Ingredient other = (Ingredient) o;
            if (meta == OreDictionary.WILDCARD_VALUE || other.meta == OreDictionary.WILDCARD_VALUE) {
                return other.item.equals(this.item);
            } else {
                return other.item.equals(this.item) && this.meta == other.meta;
            }
        } else return false;
    }

    @Override
    public String toString() {
        return String.format("%s:%s@%s", this.type, this.item, this.meta);
    }

}
