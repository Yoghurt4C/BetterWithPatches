package mods.betterwithpatches.data.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemStackMap<T> implements Map<ItemStack, T> {
    private Entry<?>[] table;
    private int count;
    private int threshold;
    private final float loadFactor;
    private int modCount;
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    public final T defaultValue;

    public ItemStackMap(T defaultValue) {
        this.loadFactor = 0.75f;
        this.table = new Entry<?>[11];
        this.threshold = (int) Math.min(11 * loadFactor, MAX_ARRAY_SIZE + 1);
        this.defaultValue = defaultValue;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @SuppressWarnings("all")
    public int stackHash(ItemStack stack) {
        return stack.getItem().hashCode();
    }

    @SuppressWarnings("all")
    public boolean compareStacks(ItemStack stack, ItemStack other) {
        boolean equals;
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE || other.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            equals = other.getItem().equals(stack.getItem());
        } else {
            equals = other.getItem().equals(stack.getItem()) && stack.getItemDamage() == other.getItemDamage();
        }
        if (!equals) return false;

        boolean othertag = other.stackTagCompound != null;
        boolean thistag = stack.stackTagCompound != null;
        if (othertag && thistag) {
            for (String s : stack.stackTagCompound.func_150296_c()) {
                if (!other.stackTagCompound.hasKey(s) || stack.stackTagCompound.getTag(s).hashCode() != other.stackTagCompound.getTag(s).hashCode())
                    equals = false;
            }
        } else equals = !thistag;
        return equals;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof ItemStack) {
            ItemStack stack = (ItemStack) key;
            if (stack.getItem() == null) return false;
            int hash = stackHash(stack);
            Entry<?>[] tab = table;
            int index = (hash & 0x7FFFFFFF) % tab.length;
            for (Entry<?> e = tab[index]; e != null; e = e.next) {
                if ((e.hash == hash) && compareStacks(e.key, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        Entry<?>[] tab = table;
        for (int i = tab.length; i-- > 0; ) {
            for (Entry<?> e = tab[i]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object key) {
        if (key instanceof ItemStack) {
            ItemStack stack = (ItemStack) key;
            if (stack.getItem() == null) return defaultValue;
            int hash = stackHash(stack);
            Entry<?>[] tab = table;
            int index = (hash & 0x7FFFFFFF) % tab.length;
            for (Entry<?> e = tab[index]; e != null; e = e.next) {
                if ((e.hash == hash) && compareStacks(e.key, stack)) {
                    return (T) e.value;
                }
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    protected void rehash() {
        int oldCapacity = table.length;
        Entry<?>[] oldMap = table;

        // overflow-conscious code
        int newCapacity = (oldCapacity << 1) + 1;
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            if (oldCapacity == MAX_ARRAY_SIZE)
                // Keep running with MAX_ARRAY_SIZE buckets
                return;
            newCapacity = MAX_ARRAY_SIZE;
        }
        Entry<?>[] newMap = new Entry<?>[newCapacity];

        modCount++;
        threshold = (int) Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
        table = newMap;

        for (int i = oldCapacity; i-- > 0; ) {
            for (Entry<T> old = (Entry<T>) oldMap[i]; old != null; ) {
                Entry<T> e = old;
                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = (Entry<T>) newMap[index];
                newMap[index] = e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private T addEntry(int hash, ItemStack key, T value, int index) {
        modCount++;

        Entry<?>[] tab = table;
        if (count >= threshold) {
            // Rehash the table if the threshold is exceeded
            rehash();

            tab = table;
            hash = stackHash(key);
            index = (hash & 0x7FFFFFFF) % tab.length;
        }

        // Creates the new entry.
        Entry<T> e = (Entry<T>) tab[index];
        tab[index] = new Entry<>(hash, key, value, e);
        count++;
        return value;
    }

    @Override
    public T put(ItemStack key, T value) {
        if (key == null) {
            return defaultValue;
        }
        // Make sure the value is not null
        else if (value == null) {
            throw new NullPointerException();
        }

        // Makes sure the key is not already in the hashtable.
        Entry<?>[] tab = table;
        int hash = stackHash(key);
        int index = (hash & 0x7FFFFFFF) % tab.length;
        @SuppressWarnings("unchecked")
        Entry<T> entry = (Entry<T>) tab[index];
        for (; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && compareStacks(entry.key, key)) {
                T old = entry.value;
                entry.value = value;
                return old;
            }
        }

        return addEntry(hash, key, value, index);
    }

    public T put(Item item, int meta, T value) {
        return put(new ItemStack(item, 1, meta), value);
    }

    public T put(Block block, int meta, T value) {
        return put(new ItemStack(block, 1, meta), value);
    }

    public T put(String oreDict, T value) {
        for (ItemStack ore : OreDictionary.getOres(oreDict, false)) {
            put(ore, value);
        }
        return value;
    }

    @Override
    public T remove(Object key) {
        if (key instanceof ItemStack) {
            Entry<?>[] tab = table;
            int hash = stackHash((ItemStack) key);
            int index = (hash & 0x7FFFFFFF) % tab.length;
            @SuppressWarnings("unchecked")
            Entry<T> e = (Entry<T>) tab[index];
            for (Entry<T> prev = null; e != null; prev = e, e = e.next) {
                if ((e.hash == hash) && compareStacks(e.key, (ItemStack) key)) {
                    modCount++;
                    if (prev != null) {
                        prev.next = e.next;
                    } else {
                        tab[index] = e.next;
                    }
                    count--;
                    T oldValue = e.value;
                    e.value = null;
                    return oldValue;
                }
            }
        }
        return null;
    }

    @Override
    public void putAll(@Nonnull Map<? extends ItemStack, ? extends T> m) {
        for (Map.Entry<? extends ItemStack, ? extends T> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() {
        Entry<?>[] tab = table;
        modCount++;
        for (int index = tab.length; --index >= 0; )
            tab[index] = null;
        count = 0;
    }

    private <R> Enumeration<R> getEnumeration(int type) {
        if (count == 0) {
            return Collections.emptyEnumeration();
        } else {
            return new Enumerator<>(type, false);
        }
    }

    private <R> Iterator<R> getIterator(int type) {
        if (count == 0) {
            return Collections.emptyIterator();
        } else {
            return new Enumerator<>(type, true);
        }
    }

    private transient volatile Set<ItemStack> keySet;
    private transient volatile Set<Map.Entry<ItemStack, T>> entrySet;
    private transient volatile Collection<T> values;

    @Override
    public Set<ItemStack> keySet() {
        if (keySet == null) keySet = Collections.synchronizedSet(new KeySet());
        return keySet;
    }

    private class KeySet extends AbstractSet<ItemStack> {
        public Iterator<ItemStack> iterator() {
            return getIterator(KEYS);
        }

        public int size() {
            return count;
        }

        public boolean contains(Object o) {
            return containsKey(o);
        }

        public boolean remove(Object o) {
            return ItemStackMap.this.remove(o) != null;
        }

        public void clear() {
            ItemStackMap.this.clear();
        }
    }

    public Set<Map.Entry<ItemStack, T>> entrySet() {
        if (entrySet == null)
            entrySet = Collections.synchronizedSet(new EntrySet());
        return entrySet;
    }

    private class EntrySet extends AbstractSet<Map.Entry<ItemStack, T>> {

        public Iterator<Map.Entry<ItemStack, T>> iterator() {
            return getIterator(ENTRIES);
        }

        public boolean add(Map.Entry<ItemStack, T> o) {
            return super.add(o);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            Entry<?>[] tab = table;
            int hash = key.hashCode();
            int index = (hash & 0x7FFFFFFF) % tab.length;

            for (Entry<?> e = tab[index]; e != null; e = e.next)
                if (e.hash == hash && e.equals(entry))
                    return true;
            return false;
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            Entry<?>[] tab = table;
            int hash = key.hashCode();
            int index = (hash & 0x7FFFFFFF) % tab.length;

            @SuppressWarnings("unchecked")
            Entry<T> e = (Entry<T>) tab[index];
            for (Entry<T> prev = null; e != null; prev = e, e = e.next) {
                if (e.hash == hash && e.equals(entry)) {
                    modCount++;
                    if (prev != null)
                        prev.next = e.next;
                    else
                        tab[index] = e.next;

                    count--;
                    e.value = null;
                    return true;
                }
            }
            return false;
        }

        public int size() {
            return count;
        }

        public void clear() {
            ItemStackMap.this.clear();
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @since 1.2
     */
    public Collection<T> values() {
        if (values == null)
            values = Collections.synchronizedCollection(new ValueCollection());
        return values;
    }

    private class ValueCollection extends AbstractCollection<T> {
        public Iterator<T> iterator() {
            return getIterator(VALUES);
        }

        public int size() {
            return count;
        }

        public boolean contains(Object o) {
            return containsValue(o);
        }

        public void clear() {
            ItemStackMap.this.clear();
        }
    }

    private static class Entry<V> implements Map.Entry<ItemStack, V> {
        final int hash;
        final ItemStack key;
        V value;
        Entry<V> next;

        protected Entry(int hash, ItemStack key, V value, Entry<V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Object clone() {
            return new Entry<>(hash, key, value, (next == null ? null : (Entry<V>) next.clone()));
        }

        // Map.Entry Ops

        @Override
        public ItemStack getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            if (value == null)
                throw new NullPointerException();

            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

            return (key == null ? e.getKey() == null : key.equals(e.getKey())) &&
                    (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }

    private static final int KEYS = 0;
    private static final int VALUES = 1;
    private static final int ENTRIES = 2;

    private class Enumerator<R> implements Enumeration<R>, Iterator<R> {
        Entry<?>[] table = ItemStackMap.this.table;
        int index = table.length;
        Entry<?> entry;
        Entry<?> lastReturned;
        int type;

        /**
         * Indicates whether this Enumerator is serving as an Iterator
         * or an Enumeration.  (true -> Iterator).
         */
        boolean iterator;

        /**
         * The modCount value that the iterator believes that the backing
         * Hashtable should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        protected int expectedModCount = modCount;

        Enumerator(int type, boolean iterator) {
            this.type = type;
            this.iterator = iterator;
        }

        public boolean hasMoreElements() {
            Entry<?> e = entry;
            int i = index;
            Entry<?>[] t = table;
            /* Use locals for faster loop iteration */
            while (e == null && i > 0) {
                e = t[--i];
            }
            entry = e;
            index = i;
            return e != null;
        }

        @SuppressWarnings("unchecked")
        public R nextElement() {
            Entry<?> et = entry;
            int i = index;
            Entry<?>[] t = table;
            /* Use locals for faster loop iteration */
            while (et == null && i > 0) {
                et = t[--i];
            }
            entry = et;
            index = i;
            if (et != null) {
                Entry<?> e = lastReturned = entry;
                entry = e.next;
                return type == KEYS ? (R) e.key : (type == VALUES ? (R) e.value : (R) e);
            }
            throw new NoSuchElementException("Hashtable Enumerator");
        }

        // Iterator methods
        public boolean hasNext() {
            return hasMoreElements();
        }

        public R next() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            return nextElement();
        }

        public void remove() {
            if (!iterator)
                throw new UnsupportedOperationException();
            if (lastReturned == null)
                throw new IllegalStateException("Hashtable Enumerator");
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            synchronized (ItemStackMap.this) {
                Entry<?>[] tab = ItemStackMap.this.table;
                int index = (lastReturned.hash & 0x7FFFFFFF) % tab.length;

                @SuppressWarnings("unchecked")
                Entry<T> e = (Entry<T>) tab[index];
                for (Entry<T> prev = null; e != null; prev = e, e = e.next) {
                    if (e == lastReturned) {
                        modCount++;
                        expectedModCount++;
                        if (prev == null)
                            tab[index] = e.next;
                        else
                            prev.next = e.next;
                        count--;
                        lastReturned = null;
                        return;
                    }
                }
                throw new ConcurrentModificationException();
            }
        }
    }
}
