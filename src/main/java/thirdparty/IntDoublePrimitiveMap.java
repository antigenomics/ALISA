package thirdparty;

// Taken from https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/IntIntMap.java

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An unordered map where the keys and values are integers. This implementation is a cuckoo hash map using 3 hashes, random walking,
 * and a small stash for problematic keys. No allocation is done except when growing the table size. <br>
 * <br>
 * This map performs very fast get, containsKey, and remove (typically O(1), worst case O(log(n))). Put may be a bit slower,
 * depending on hash collisions. Load factors greater than 0.91 greatly increase the chances the map will have to rehash to the
 * next higher POT size.
 *
 * Modified from https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/IntIntMap.java
 * by removing some methods & replacing int value with double
 *
 * @author Nathan Sweet
 */
public class IntDoublePrimitiveMap implements Iterable<IntDoublePrimitiveMap.Entry> {
    private static final int PRIME2 = 0xb4b82e39;
    private static final int PRIME3 = 0xced1c241;
    private static final int EMPTY = 0;

    public int size;

    int[] keyTable;
    double[] valueTable;
    int capacity, stashSize;
    double zeroValue;
    boolean hasZeroValue;

    private float loadFactor;
    private int hashShift, mask, threshold;
    private int stashCapacity;
    private int pushIterations;

    private Entries entries1, entries2;
    private Values values1, values2;
    private Keys keys1, keys2;

    /**
     * Creates a new map with an initial capacity of 51 and a load factor of 0.8.
     */
    public IntDoublePrimitiveMap() {
        this(51, 0.8f);
    }

    /**
     * Creates a new map with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public IntDoublePrimitiveMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    /**
     * Creates a new map with the specified initial capacity and load factor. This map will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public IntDoublePrimitiveMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
        initialCapacity = nextPowerOfTwo((int) Math.ceil(initialCapacity / loadFactor));
        if (initialCapacity > 1 << 30)
            throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
        capacity = initialCapacity;

        if (loadFactor <= 0) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor);
        this.loadFactor = loadFactor;

        threshold = (int) (capacity * loadFactor);
        mask = capacity - 1;
        hashShift = 31 - Integer.numberOfTrailingZeros(capacity);
        stashCapacity = Math.max(3, (int) Math.ceil(Math.log(capacity)) * 2);
        pushIterations = Math.max(Math.min(capacity, 8), (int) Math.sqrt(capacity) / 8);

        keyTable = new int[capacity + stashCapacity];
        valueTable = new double[keyTable.length];
    }

    /**
     * Creates a new map identical to the specified map.
     */
    public IntDoublePrimitiveMap(IntDoublePrimitiveMap map) {
        this((int) Math.floor(map.capacity * map.loadFactor), map.loadFactor);
        stashSize = map.stashSize;
        System.arraycopy(map.keyTable, 0, keyTable, 0, map.keyTable.length);
        System.arraycopy(map.valueTable, 0, valueTable, 0, map.valueTable.length);
        size = map.size;
        zeroValue = map.zeroValue;
        hasZeroValue = map.hasZeroValue;
    }

    public void put(int key, double value) {
        if (key == 0) {
            zeroValue = value;
            if (!hasZeroValue) {
                hasZeroValue = true;
                size++;
            }
            return;
        }

        int[] keyTable = this.keyTable;

        // Check for existing keys.
        int index1 = key & mask;
        int key1 = keyTable[index1];
        if (key == key1) {
            valueTable[index1] = value;
            return;
        }

        int index2 = hash2(key);
        int key2 = keyTable[index2];
        if (key == key2) {
            valueTable[index2] = value;
            return;
        }

        int index3 = hash3(key);
        int key3 = keyTable[index3];
        if (key == key3) {
            valueTable[index3] = value;
            return;
        }

        // Update key in the stash.
        for (int i = capacity, n = i + stashSize; i < n; i++) {
            if (key == keyTable[i]) {
                valueTable[i] = value;
                return;
            }
        }

        // Check for empty buckets.
        if (key1 == EMPTY) {
            keyTable[index1] = key;
            valueTable[index1] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        if (key2 == EMPTY) {
            keyTable[index2] = key;
            valueTable[index2] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        if (key3 == EMPTY) {
            keyTable[index3] = key;
            valueTable[index3] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        push(key, value, index1, key1, index2, key2, index3, key3);
    }

    public void putAll(IntDoublePrimitiveMap map) {
        for (Entry entry : map.entries())
            put(entry.key, entry.value);
    }

    /**
     * Skips checks for existing keys.
     */
    private void putResize(int key, double value) {
        if (key == 0) {
            zeroValue = value;
            hasZeroValue = true;
            return;
        }

        // Check for empty buckets.
        int index1 = key & mask;
        int key1 = keyTable[index1];
        if (key1 == EMPTY) {
            keyTable[index1] = key;
            valueTable[index1] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        int index2 = hash2(key);
        int key2 = keyTable[index2];
        if (key2 == EMPTY) {
            keyTable[index2] = key;
            valueTable[index2] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        int index3 = hash3(key);
        int key3 = keyTable[index3];
        if (key3 == EMPTY) {
            keyTable[index3] = key;
            valueTable[index3] = value;
            if (size++ >= threshold) resize(capacity << 1);
            return;
        }

        push(key, value, index1, key1, index2, key2, index3, key3);
    }

    private void push(int insertKey, double insertValue, int index1, int key1, int index2, int key2, int index3, int key3) {
        int[] keyTable = this.keyTable;
        double[] valueTable = this.valueTable;
        int mask = this.mask;

        // Push keys until an empty bucket is found.
        int evictedKey;
        double evictedValue;
        int i = 0, pushIterations = this.pushIterations;
        do {
            // Replace the key and value for one of the hashes.
            switch (rnd.nextInt(3)) {
                case 0:
                    evictedKey = key1;
                    evictedValue = valueTable[index1];
                    keyTable[index1] = insertKey;
                    valueTable[index1] = insertValue;
                    break;
                case 1:
                    evictedKey = key2;
                    evictedValue = valueTable[index2];
                    keyTable[index2] = insertKey;
                    valueTable[index2] = insertValue;
                    break;
                default:
                    evictedKey = key3;
                    evictedValue = valueTable[index3];
                    keyTable[index3] = insertKey;
                    valueTable[index3] = insertValue;
                    break;
            }

            // If the evicted key hashes to an empty bucket, put it there and stop.
            index1 = evictedKey & mask;
            key1 = keyTable[index1];
            if (key1 == EMPTY) {
                keyTable[index1] = evictedKey;
                valueTable[index1] = evictedValue;
                if (size++ >= threshold) resize(capacity << 1);
                return;
            }

            index2 = hash2(evictedKey);
            key2 = keyTable[index2];
            if (key2 == EMPTY) {
                keyTable[index2] = evictedKey;
                valueTable[index2] = evictedValue;
                if (size++ >= threshold) resize(capacity << 1);
                return;
            }

            index3 = hash3(evictedKey);
            key3 = keyTable[index3];
            if (key3 == EMPTY) {
                keyTable[index3] = evictedKey;
                valueTable[index3] = evictedValue;
                if (size++ >= threshold) resize(capacity << 1);
                return;
            }

            if (++i == pushIterations) break;

            insertKey = evictedKey;
            insertValue = evictedValue;
        } while (true);

        putStash(evictedKey, evictedValue);
    }

    private void putStash(int key, double value) {
        if (stashSize == stashCapacity) {
            // Too many pushes occurred and the stash is full, increase the table size.
            resize(capacity << 1);
            put(key, value);
            return;
        }
        // Store key in the stash.
        int index = capacity + stashSize;
        keyTable[index] = key;
        valueTable[index] = value;
        stashSize++;
        size++;
    }

    /**
     * @param defaultValue Returned if the key was not associated with a value.
     */
    public double get(int key, int defaultValue) {
        if (key == 0) {
            if (!hasZeroValue) return defaultValue;
            return zeroValue;
        }
        int index = key & mask;
        if (keyTable[index] != key) {
            index = hash2(key);
            if (keyTable[index] != key) {
                index = hash3(key);
                if (keyTable[index] != key) return getStash(key, defaultValue);
            }
        }
        return valueTable[index];
    }

    private double getStash(int key, int defaultValue) {
        int[] keyTable = this.keyTable;
        for (int i = capacity, n = i + stashSize; i < n; i++)
            if (key == keyTable[i]) return valueTable[i];
        return defaultValue;
    }

    public double remove(int key, double defaultValue) {
        if (key == 0) {
            if (!hasZeroValue) return defaultValue;
            hasZeroValue = false;
            size--;
            return zeroValue;
        }

        int index = key & mask;
        if (key == keyTable[index]) {
            keyTable[index] = EMPTY;
            double oldValue = valueTable[index];
            size--;
            return oldValue;
        }

        index = hash2(key);
        if (key == keyTable[index]) {
            keyTable[index] = EMPTY;
            double oldValue = valueTable[index];
            size--;
            return oldValue;
        }

        index = hash3(key);
        if (key == keyTable[index]) {
            keyTable[index] = EMPTY;
            double oldValue = valueTable[index];
            size--;
            return oldValue;
        }

        return removeStash(key, defaultValue);
    }

    double removeStash(int key, double defaultValue) {
        int[] keyTable = this.keyTable;
        for (int i = capacity, n = i + stashSize; i < n; i++) {
            if (key == keyTable[i]) {
                double oldValue = valueTable[i];
                removeStashIndex(i);
                size--;
                return oldValue;
            }
        }
        return defaultValue;
    }

    void removeStashIndex(int index) {
        // If the removed location was not last, move the last tuple to the removed location.
        stashSize--;
        int lastIndex = capacity + stashSize;
        if (index < lastIndex) {
            keyTable[index] = keyTable[lastIndex];
            valueTable[index] = valueTable[lastIndex];
        }
    }

    /**
     * Reduces the size of the backing arrays to be the specified capacity or less. If the capacity is already less, nothing is
     * done. If the map contains more items than the specified capacity, the next highest power of two capacity is used instead.
     */
    public void shrink(int maximumCapacity) {
        if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity);
        if (size > maximumCapacity) maximumCapacity = size;
        if (capacity <= maximumCapacity) return;
        maximumCapacity = nextPowerOfTwo(maximumCapacity);
        resize(maximumCapacity);
    }

    /**
     * Clears the map and reduces the size of the backing arrays to be the specified capacity if they are larger.
     */
    public void clear(int maximumCapacity) {
        if (capacity <= maximumCapacity) {
            clear();
            return;
        }
        hasZeroValue = false;
        size = 0;
        resize(maximumCapacity);
    }

    public void clear() {
        if (size == 0) return;
        int[] keyTable = this.keyTable;
        for (int i = capacity + stashSize; i-- > 0; )
            keyTable[i] = EMPTY;
        size = 0;
        stashSize = 0;
        hasZeroValue = false;
    }

    /**
     * Returns true if the specified value is in the map. Note this traverses the entire map and compares every value, which may be
     * an expensive operation.
     */
    public boolean containsValue(double value) {
        if (hasZeroValue && zeroValue == value) return true;
        int[] keyTable = this.keyTable;
        double[] valueTable = this.valueTable;
        for (int i = capacity + stashSize; i-- > 0; )
            if (keyTable[i] != 0 && valueTable[i] == value) return true;
        return false;
    }

    public boolean containsKey(int key) {
        if (key == 0) return hasZeroValue;
        int index = key & mask;
        if (keyTable[index] != key) {
            index = hash2(key);
            if (keyTable[index] != key) {
                index = hash3(key);
                if (keyTable[index] != key) return containsKeyStash(key);
            }
        }
        return true;
    }

    private boolean containsKeyStash(int key) {
        int[] keyTable = this.keyTable;
        for (int i = capacity, n = i + stashSize; i < n; i++)
            if (key == keyTable[i]) return true;
        return false;
    }

    /**
     * Returns the key for the specified value, or null if it is not in the map. Note this traverses the entire map and compares
     * every value, which may be an expensive operation.
     */
    public int findKey(double value, int notFound) {
        if (hasZeroValue && zeroValue == value) return 0;
        int[] keyTable = this.keyTable;
        double[] valueTable = this.valueTable;
        for (int i = capacity + stashSize; i-- > 0; )
            if (keyTable[i] != 0 && valueTable[i] == value) return keyTable[i];
        return notFound;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     */
    public void ensureCapacity(int additionalCapacity) {
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded >= threshold) resize(nextPowerOfTwo((int) Math.ceil(sizeNeeded / loadFactor)));
    }

    private void resize(int newSize) {
        int oldEndIndex = capacity + stashSize;

        capacity = newSize;
        threshold = (int) (newSize * loadFactor);
        mask = newSize - 1;
        hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
        stashCapacity = Math.max(3, (int) Math.ceil(Math.log(newSize)) * 2);
        pushIterations = Math.max(Math.min(newSize, 8), (int) Math.sqrt(newSize) / 8);

        int[] oldKeyTable = keyTable;
        double[] oldValueTable = valueTable;

        keyTable = new int[newSize + stashCapacity];
        valueTable = new double[newSize + stashCapacity];

        int oldSize = size;
        size = hasZeroValue ? 1 : 0;
        stashSize = 0;
        if (oldSize > 0) {
            for (int i = 0; i < oldEndIndex; i++) {
                int key = oldKeyTable[i];
                if (key != EMPTY) putResize(key, oldValueTable[i]);
            }
        }
    }

    private int hash2(int h) {
        h *= PRIME2;
        return (h ^ h >>> hashShift) & mask;
    }

    private int hash3(int h) {
        h *= PRIME3;
        return (h ^ h >>> hashShift) & mask;
    }

    public Iterator<Entry> iterator() {
        return entries();
    }

    /**
     * Returns an iterator for the entries in the map. Remove is supported. Note that the same iterator instance is returned each
     * time this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Entries entries() {
        if (entries1 == null) {
            entries1 = new Entries(this);
            entries2 = new Entries(this);
        }
        if (!entries1.valid) {
            entries1.reset();
            entries1.valid = true;
            entries2.valid = false;
            return entries1;
        }
        entries2.reset();
        entries2.valid = true;
        entries1.valid = false;
        return entries2;
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported. Note that the same iterator instance is returned each
     * time this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Values values() {
        if (values1 == null) {
            values1 = new Values(this);
            values2 = new Values(this);
        }
        if (!values1.valid) {
            values1.reset();
            values1.valid = true;
            values2.valid = false;
            return values1;
        }
        values2.reset();
        values2.valid = true;
        values1.valid = false;
        return values2;
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported. Note that the same iterator instance is returned each time
     * this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Keys keys() {
        if (keys1 == null) {
            keys1 = new Keys(this);
            keys2 = new Keys(this);
        }
        if (!keys1.valid) {
            keys1.reset();
            keys1.valid = true;
            keys2.valid = false;
            return keys1;
        }
        keys2.reset();
        keys2.valid = true;
        keys1.valid = false;
        return keys2;
    }

    static public class Entry {
        public int key;
        public double value;

        public String toString() {
            return key + "=" + value;
        }
    }

    static private class MapIterator {
        static final int INDEX_ILLEGAL = -2;
        static final int INDEX_ZERO = -1;

        public boolean hasNext;

        final IntDoublePrimitiveMap map;
        int nextIndex, currentIndex;
        boolean valid = true;

        public MapIterator(IntDoublePrimitiveMap map) {
            this.map = map;
            reset();
        }

        public void reset() {
            currentIndex = INDEX_ILLEGAL;
            nextIndex = INDEX_ZERO;
            if (map.hasZeroValue)
                hasNext = true;
            else
                findNextIndex();
        }

        void findNextIndex() {
            hasNext = false;
            int[] keyTable = map.keyTable;
            for (int n = map.capacity + map.stashSize; ++nextIndex < n; ) {
                if (keyTable[nextIndex] != EMPTY) {
                    hasNext = true;
                    break;
                }
            }
        }

        public void remove() {
            if (currentIndex == INDEX_ZERO && map.hasZeroValue) {
                map.hasZeroValue = false;
            } else if (currentIndex < 0) {
                throw new IllegalStateException("next must be called before remove.");
            } else if (currentIndex >= map.capacity) {
                map.removeStashIndex(currentIndex);
                nextIndex = currentIndex - 1;
                findNextIndex();
            } else {
                map.keyTable[currentIndex] = EMPTY;
            }
            currentIndex = INDEX_ILLEGAL;
            map.size--;
        }
    }

    static public class Entries extends MapIterator implements Iterable<Entry>, Iterator<Entry> {
        private Entry entry = new Entry();

        public Entries(IntDoublePrimitiveMap map) {
            super(map);
        }

        /**
         * Note the same entry instance is returned each time this method is called.
         */
        public Entry next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            int[] keyTable = map.keyTable;
            if (nextIndex == INDEX_ZERO) {
                entry.key = 0;
                entry.value = map.zeroValue;
            } else {
                entry.key = keyTable[nextIndex];
                entry.value = map.valueTable[nextIndex];
            }
            currentIndex = nextIndex;
            findNextIndex();
            return entry;
        }

        public boolean hasNext() {
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            return hasNext;
        }

        public Iterator<Entry> iterator() {
            return this;
        }

        public void remove() {
            super.remove();
        }
    }

    static public class Values extends MapIterator {
        public Values(IntDoublePrimitiveMap map) {
            super(map);
        }

        public boolean hasNext() {
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            return hasNext;
        }

        public double next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            double value;
            if (nextIndex == INDEX_ZERO)
                value = map.zeroValue;
            else
                value = map.valueTable[nextIndex];
            currentIndex = nextIndex;
            findNextIndex();
            return value;
        }
    }

    static public class Keys extends MapIterator {
        public Keys(IntDoublePrimitiveMap map) {
            super(map);
        }

        public boolean hasNext() {
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            return hasNext;
        }

        public int next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            int key = nextIndex == INDEX_ZERO ? 0 : map.keyTable[nextIndex];
            currentIndex = nextIndex;
            findNextIndex();
            return key;
        }
    }

    private static int nextPowerOfTwo (int value) {
        if (value == 0) return 1;
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }

    private static final Random rnd = ThreadLocalRandom.current();
}