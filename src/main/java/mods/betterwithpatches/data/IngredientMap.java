package mods.betterwithpatches.data;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class IngredientMap<T> implements Map<Ingredient, T> {
    public final Map<Ingredient, T> map = new Hashtable<>();
    public final T defaultValue;

    public IngredientMap(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return containsKey(key) ? this.map.get(key) : this.defaultValue;
    }

    public T get(ItemStack stack) {
        return get(new Ingredient(stack));
    }

    @Override
    public T put(Ingredient key, T value) {
        return this.map.put(key, value);
    }

    public T put(Item item, int meta, T value) {
        return put(new Ingredient(item, meta), value);
    }

    public T put(Block block, int meta, T value) {
        return put(new Ingredient(block, meta), value);
    }

    public T put(ItemStack stack, T value) {
        return put(new Ingredient(stack), value);
    }

    public T put(String oreDict, T value) {
        for (ItemStack ore : OreDictionary.getOres(oreDict, false)) {
            put(new Ingredient(ore), value);
        }
        return value;
    }

    @Override
    public T remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends Ingredient, ? extends T> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<Ingredient> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<T> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<Ingredient, T>> entrySet() {
        return this.map.entrySet();
    }
}
