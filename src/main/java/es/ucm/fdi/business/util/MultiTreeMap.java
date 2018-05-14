/**
 * This file is part of Dimension.
 * Dimension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Dimension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.util;

import java.util.*;

/**
 * A TreeMap that supports multiple values for the same key, via ArrayLists.
 *
 * Values for the same key will be returned and traversed in order of insertion;
 * that is, newer values with the same key will be stored after any other values
 * with the same key.
 *
 * @param <K> key
 * @param <V> value
 */
public class MultiTreeMap<K, V> extends TreeMap<K, ArrayList<V>> {

    public MultiTreeMap() {
    }

    public MultiTreeMap(Comparator<K> comparator) {
        super(comparator);
    }

    /**
     * Adds a value at the end of the list of values for the specified key.
     *
     * @param key to add the value under
     * @param value to add
     */
    public void putValue(K key, V value) {
        if (!containsKey(key)) {
            put(key, new ArrayList<>());
        }
        get(key).add(value);
    }

    /**
     * Removes the first occurrence of a value from the list found at a given
     * key. Efficiency is O(size-of-that-list)
     *
     * @param key to look into
     * @param value within the list found at that key to remove. The first
     * element that is equals to this one will be removed.
     * @return true if removed, false if not found
     */
    public boolean removeValue(K key, V value) {
        if (!containsKey(key)) {
            return false;
        }
        ArrayList<V> bucket = get(key);
        boolean removed = bucket.remove(value);
        if (bucket.isEmpty()) {
            remove(key);
        }
        return removed;
    }

    /**
     * Returns the total number of values stored in this multimap
     *
     * @return size of values
     */
    public int sizeOfValues() {
        int total = 0;
        for (List<V> l : values()) {
            total += l.size();
        }
        return total;
    }

    /**
     * Returns the values as a read-only list. Changes to this structure will be
     * immediately reflected in the list.
     *
     * @return values list
     */
    public List<V> valuesList() {
        return new InnerList();
    }

    /**
     * A logical, read-only list containing all elements in correct order.
     */
    private class InnerList extends AbstractList<V> {

        @Override
        public V get(int index) {

            if (index < 0 || isEmpty()) {
                throw new IndexOutOfBoundsException(
                        "Index " + index + " is out of bounds");
            }

            Iterator<ArrayList<V>> it = values().iterator();
            ArrayList<V> current = it.next(); // not empty, therefore hasNext()
            int start = 0;

            while (index >= (start + current.size())) {
                if (!it.hasNext()) {
                    throw new IndexOutOfBoundsException(
                            "Index " + index + " is out of bounds");
                }
                start += current.size();
                current = it.next();
            }

            return current.get(index - start);
        }

        @Override
        public int size() {
            return sizeOfValues();
        }
    }

    /**
     * Iterates through all internal values (not the arraylists themselves),
     * first by key order, and within each bucket, by insertion order.
     */
    private class InnerIterator implements Iterator<V> {

        private Iterator<ArrayList<V>> arrayIterator;
        private Iterator<V> valueIterator;
        private boolean finished = false;
        private V nextElement;

        private InnerIterator() {
            arrayIterator = values().iterator();
            advance();
        }

        private void advance() {
            if (valueIterator == null || !valueIterator.hasNext()) {
                if (arrayIterator.hasNext()) {
                    valueIterator = arrayIterator.next().iterator();
                    if (valueIterator.hasNext()) {
                        nextElement = valueIterator.next();
                    }
                } else {
                    finished = true;
                }
            } else {
                nextElement = valueIterator.next();
            }
        }

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public V next() {
            V current = nextElement;
            advance();
            return current;
        }
    }

    /**
     * Allows iteration by base values.
     *
     * @return iterable values, ordered by key and then by order-of-insertion
     */
    public Iterable<V> innerValues() {
        return () -> new InnerIterator();
    }
}
