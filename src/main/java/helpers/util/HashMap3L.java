package helpers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import helpers.tuple.Quadruple;

/**
 * HashMap3L provides to store values with three level hierarchy of keys, super key (K1), sub key
 * (K2) and subsubkey (K3). The objects are inserted using keys for ALL three levels. It is not
 * mandatory to use all keys as hierarchy, user can use three keys to store the values and use
 * either of the key to retrieve it.
 * 
 * @author Dirk Hoffmann
 *
 */
public class HashMap3L<K1, K2, K3, V> implements Iterable<Quadruple<K1, K2, K3, V>> {

    /** root Map structure holding other Map structures to implement HashMap3L */
    public Map<K1, Map<K2, Map<K3, V>>> rootMap;
    /** global metadata, not part of the hierarchy */
    public Map<String, Object> global = new HashMap<>();
    /**
     * prototypes of maps to clone from (for being able to use different Map implementation types)
     */
    protected MapCloneable<K2, Map<K3, V>> level2MapClonePrototype;
    protected MapCloneable<K3, V> level3MapClonePrototype;

    /** Initializes the HashMap3L */
    public HashMap3L(MapCloneable<K1, Map<K2, Map<K3, V>>> rootMapClonePrototype,
            MapCloneable<K2, Map<K3, V>> level2MapClonePrototype,
            MapCloneable<K3, V> level3MapClonePrototype) {
        this.level2MapClonePrototype = level2MapClonePrototype;
        this.level3MapClonePrototype = level3MapClonePrototype;
        rootMap = rootMapClonePrototype.clone();
    }

    /**
     * Puts the value object based on the (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param k3 key3 (subsub-key)
     * @param v  value object
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    public V put(K1 k1, K2 k2, K3 k3, V v) {
        Map<K3, V> l3Map = this.get(k1, k2); // initializes l2/l3-map
        return l3Map.put(k3, v);
    }

    public V put(Triple<K1, K2, K3> keys, V v) {
        return put(keys.getLeft(), keys.getMiddle(), keys.getRight(), v);
    }

    /**
     * returns level 2 Map (initializing it if non-existent yet)
     * 
     * @param k1 key1 (super-key)
     * @return existing or empty level 1 Map.
     */
    public Map<K2, Map<K3, V>> get(K1 k1) {
        Map<K2, Map<K3, V>> l2Map = rootMap.get(k1);
        if (l2Map == null) {
            l2Map = (MapCloneable<K2, Map<K3, V>>) level2MapClonePrototype.clone();
            rootMap.put(k1, l2Map);
        }
        return l2Map;
    }

    /**
     * returns level 2 Map (initializing it (and also upto l1 if needed) if non-existent yet)
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return existing or empty level 3 Map.
     */
    public Map<K3, V> get(K1 k1, K2 k2) {
        Map<K2, Map<K3, V>> l2Map = this.get(k1); // initializes l2map
        Map<K3, V> l3Map = l2Map.get(k2);
        if (l3Map == null) {
            l3Map = (MapCloneable<K3, V>) level3MapClonePrototype.clone();
            l2Map.put(k2, l3Map);
        }
        return l3Map;
    }

    public Map<K3, V> get(Pair<K1, K2> keys) {
        return get(keys.getLeft(), keys.getRight());
    }

    /**
     * Gets the value object for the specified (super)key K1, (sub)key K2 and (subsub)key K3</br>
     * (initializing it (and also upto l1 if needed) if non-existent yet)
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param k3 key3 (subsub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V get(K1 k1, K2 k2, K3 k3) {
        Map<K3, V> l3Map = this.get(k1, k2);
        return l3Map.get(k3);
    }

    public V get(Triple<K1, K2, K3> keys) {
        return get(keys.getLeft(), keys.getMiddle(), keys.getRight());
    }

    /**
     * Gets the first found l3 map for the specified (sub)key K2
     * 
     * @param k2 key2 (sub-key)
     * @return first level 3 map if exists or <tt>null</tt> if does not exists
     */
    public Map<K3, V> getFirstBySubKey(K2 k2) {
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            if (l2Map.containsKey(k2)) {
                return l2Map.get(k2);
            }
        }
        return null;
    }

    /**
     * Gets the first found value for the specified (subsub)key K3
     * 
     * @param k3 key3 (subsub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V getFirstBySubSubKey(K3 k3) {
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            for (Map<K3, V> l3Map : l2Map.values()) {
                if (l3Map.containsKey(k3)) {
                    return l3Map.get(k3);
                }
            }
        }
        return null;
    }

    /**
     * Gets all l3 maps for the specified (sub)key K2
     * 
     * @param k2 key2 (sub-key)
     * @return a Map of all l3 maps or an empty l3 map
     */
    public Map<K2, Map<K3, V>> getAllBySubKey(K2 k2) {
        Map<K2, Map<K3, V>> resultMap =
                (MapCloneable<K2, Map<K3, V>>) level2MapClonePrototype.clone();
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            if (l2Map.containsKey(k2))
                resultMap.put(k2, l2Map.get(k2));
        }
        return resultMap;
    }

    /**
     * Gets all l3 maps for the specified (subsub)key K3
     * 
     * @param k3 key2 (subsub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public Map<K2, Map<K3, V>> getAllBySubSubKey(K3 k3) {
        Map<K2, Map<K3, V>> resultMap =
                (MapCloneable<K2, Map<K3, V>>) level2MapClonePrototype.clone();
        Map<K3, V> resultL3Map = null;
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            for (Map.Entry<K2, Map<K3, V>> l3Entry : l2Map.entrySet()) {
                if (l3Entry.getValue().containsKey(k3)) {
                    resultL3Map = resultMap.get(l3Entry.getKey());
                    if (resultL3Map == null) {
                        resultL3Map = (MapCloneable<K3, V>) level3MapClonePrototype.clone();
                        resultMap.put(l3Entry.getKey(), resultL3Map);
                    }
                    resultL3Map.put(k3, l3Entry.getValue().get(k3));
                }
            }
        }
        return resultMap;
    }

    /**
     * Returns <tt>true</tt> if value object is present for the specified (super)key K1
     * 
     * @param k1 key1 (super-key)
     * @return <tt>true</tt> if value object present
     */
    public boolean containsKey(K1 k1) {
        return rootMap.containsKey(k1);
    }

     /**
     * Returns <tt>true</tt> if value object is present for the specified (super)key K1 and (sub)key
     * K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return <tt>true</tt> if value object present
     */
    public boolean containsKey(K1 k1, K2 k2) {
        return this.get(k1).containsKey(k2);
    }

    public boolean containsKey(Pair<K1, K2> keys) {
        return containsKey(keys.getLeft(), keys.getRight());
    }

     /**
     * Returns <tt>true</tt> if value object is present for the specified (super)key K1 and (sub)key
     * K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return <tt>true</tt> if value object present
     */
    public boolean containsKey(K1 k1, K2 k2, K3 k3) {
        return this.get(k1, k2).containsKey(k3);
    }

    public boolean containsKey(Triple<K1, K2, K3> keys) {
            return containsKey(keys.getLeft(), keys.getMiddle(), keys.getRight());
    }

    /**
     * Removes the value object(s) for the specified (super)key K1
     * 
     * @param k1 key1 (super-key)
     * @return previous value (HashMap structure) associated with specified key, or <tt>null</tt> if
     *         there was no mapping for key.
     */
    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    public Map<K2, Map<K3, V>> remove(K1 k1) {
        return this.rootMap.remove(k1);
    }

    /**
     * Removes the value object (HashMap) for the specified (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    public Map<K3, V> remove(K1 k1, K2 k2) {
        return this.get(k1).remove(k2);
    }

    public Map<K3, V> remove(Pair<K1, K2> keys) {
        return remove(keys.getLeft(), keys.getRight());
    }

    /**
     * Removes the value object for the specified (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param k3 key2 (subsub-key)
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    public V remove(K1 k1, K2 k2, K3 k3) {
        return this.get(k1, k2).remove(k3);
    }

    public V remove(Triple<K1, K2, K3> keys) {
        return remove(keys.getLeft(), keys.getMiddle(), keys.getRight());
    }
    
    /**
     * Removes the value object for the specified Keys</br>
     * only if the associated value object is equal to the given value object
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param k3 key2 (subsub-key)
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    public V removeValue(K1 k1, K2 k2, K3 k3, V v) {
        Map<K3, V> l3Map = this.get(k1, k2);
        V theValue = l3Map.get(k3);
        if (v.equals(theValue)) {
            return l3Map.remove(k3);
        }
        return null;
    }

    public V removeValue(Triple<K1, K2, K3> keys, V v) {
        return removeValue(keys.getLeft(), keys.getMiddle(), keys.getRight(), v);
    }

    /**
     * Size of MultiKeyHashMap
     * 
     * @return MultiKeyHashMap size
     */
    public int size() {
        int size = 0;
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            size++;
            for (Map<K3, V> l3Map : l2Map.values()) {
                size++;
                size += l3Map.size();
            }
        }
        return size;
    }

    /**
     * Size of MultiKeyHashMap
     * 
     * @return MultiKeyHashMap size
     */
    public int size(K1 k1) {
        int size = 0;
        for (Map<K3, V> l2Map : this.get(k1).values()) {
            size++;
            size += l2Map.size();
        }
        return size;
    }

    /**
     * Returns all the value objects in the MultiKeyHashMap
     * 
     * @return value objects as List
     */
    public List<V> getAllItems() {
        List<V> items = new ArrayList<V>();
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            for (Map<K3, V> l3Map : l2Map.values()) {
                items.addAll(l3Map.values());
            }
        }
        return items;
    }

    /**
     * Returns all the value objects in the MultiKeyHashMap
     * 
     * @return value objects as List
     */
    public List<V> getAllItems(K1 k1) {
        List<V> items = new ArrayList<V>();
        Map<K2, Map<K3, V>> l2Map = this.get(k1);
        for (Map<K3, V> l3Map : l2Map.values()) {
            items.addAll(l3Map.values());
        }
        return items;
    }

    /**
     * clears the entire hash map
     */
    public void clear() {
        for (Map<K2, Map<K3, V>> l2Map : rootMap.values()) {
            for (Map<K3, V> l3Map : l2Map.values()) {
                l3Map.clear();
            }
            l2Map.clear();
        }
        rootMap.clear();
    }

    /**
     * clears all level 2 maps (and their level3 maps) of given rootKey
     */
    public void clear(K1 k1) {
        Map<K2, Map<K3, V>> l2Map = this.get(k1);
        for (Map<K3, V> l3Map : l2Map.values()) {
            l3Map.clear();
        }
        l2Map.clear();
    }

    /**
     * clears all level 3 maps of given K1 and K2
     */
    public void clear(K1 k1, K2 k2) {
        Map<K3, V> l3Map = this.get(k1, k2);
        l3Map.clear();
    }


    class HashMap3LIterator implements Iterator<Quadruple<K1, K2, K3, V>> {
        Iterator<Map.Entry<K1, Map<K2, Map<K3, V>>>> rootEntrySetIter = null;
        Iterator<Map.Entry<K2, Map<K3, V>>> level2EntrySetIter = null;
        Iterator<Map.Entry<K3, V>> level3EntrySetIter = null;

        Quadruple<K1, K2, K3, V> next; // next entry to return
        Quadruple<K1, K2, K3, V> current; // current entry

        HashMap3LIterator() {
            current = next = null;
            rootEntrySetIter = rootMap.entrySet().iterator();
            if (rootEntrySetIter.hasNext()) {
                Entry<K1, Map<K2, Map<K3, V>>> firstRootEntry = rootEntrySetIter.next();
                level2EntrySetIter = firstRootEntry.getValue().entrySet().iterator();
                if (level2EntrySetIter.hasNext()) {
                    Entry<K2, Map<K3, V>> firstLevel2Entry = level2EntrySetIter.next();
                    level3EntrySetIter = firstLevel2Entry.getValue().entrySet().iterator();
                    if (level3EntrySetIter.hasNext()) {
                        Entry<K3, V> firstLevel3Entry = level3EntrySetIter.next();
                        next = Quadruple.of(firstRootEntry.getKey(), firstLevel2Entry.getKey(),
                                firstLevel3Entry.getKey(), firstLevel3Entry.getValue());
                    }
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public Quadruple<K1, K2, K3, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Quadruple<K1, K2, K3, V> toReturn = next; // new next will be computed now ...

            if (level3EntrySetIter.hasNext()) {
                Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                current = next;
                next = Quadruple.of(current.getRoot(), current.getL2(), nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
            } else if (level2EntrySetIter.hasNext()) {
                current = next;
                next = null;
                // level3Maps might be associated to a level2 key, but if they are empty, level3.hasNext() -> false
                // so we are searching for the next level3 entry that has any Level 3 values associated,
                // which might not be in this or the next level2Map entry
                boolean nextLevel2KeyWithElementsInLevel3MapFOUND = false;
                while (!nextLevel2KeyWithElementsInLevel3MapFOUND && level2EntrySetIter.hasNext()) {
                    // advance in level2Map
                    Entry<K2, Map<K3, V>> nextLevel2Entry = level2EntrySetIter.next();
                    level3EntrySetIter = nextLevel2Entry.getValue().entrySet().iterator(); // hasNext() checked above
                    if (level3EntrySetIter.hasNext()) {
                        Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                        next = Quadruple.of(current.getRoot(), nextLevel2Entry.getKey(), nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
                        nextLevel2KeyWithElementsInLevel3MapFOUND = true;
                    }
                    // else this level2Key had a Map which was empty
                }
                if (!nextLevel2KeyWithElementsInLevel3MapFOUND) {
                    if (rootEntrySetIter.hasNext()) {
                        current = next;
                        next = null;
                        findNextValueFromRoot();
                    }
                }
            } else {
                current = next;
                next = null;
                findNextValueFromRoot();
            }
            return toReturn;
        }

        private void findNextValueFromRoot() {
            // Level2Maps might be associated to a root key, but if they are empty, level2.hasNext() -> false
            // so we are searching for the next level2 entry that has any Level 2 values associated,
            // which might not be in this or the next rootMap entry
            boolean nextRootKeyWithElementsInLevel2MapFOUND = false;
            while(!nextRootKeyWithElementsInLevel2MapFOUND && rootEntrySetIter.hasNext()) {
                // advance in rootMap
                Entry<K1, Map<K2, Map<K3, V>>> nextRootEntry = rootEntrySetIter.next();
                level2EntrySetIter = nextRootEntry.getValue().entrySet().iterator(); // hasNext() checked above
                if (level2EntrySetIter.hasNext()) {
                    // so now all the way down to find at least one level2Map which has a level3Map with values associated
                    boolean nextLevel2KeyWithElementsInLevel3MapFOUND = false; 
                    while(!nextLevel2KeyWithElementsInLevel3MapFOUND && level2EntrySetIter.hasNext()) {
                        Entry<K2, Map<K3, V>> nextLevel2Entry= level2EntrySetIter.next();
                        level3EntrySetIter = nextLevel2Entry.getValue().entrySet().iterator(); // hasNext() checked above
                        if (level3EntrySetIter.hasNext()) {
                            Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                            next = Quadruple.of(nextRootEntry.getKey(), nextLevel2Entry.getKey(), nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
                            nextLevel2KeyWithElementsInLevel3MapFOUND = true;
                            nextRootKeyWithElementsInLevel2MapFOUND = true;
                        }
                    }
                }
            }
        }

        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    @Override
    public Iterator<Quadruple<K1, K2, K3, V>> iterator() {
        return new HashMap3LIterator();
    }

    @Override
    public Spliterator<Quadruple<K1, K2, K3, V>> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

    public Stream<Quadruple<K1, K2, K3, V>>	stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    class HashMap3LIterator2L implements Iterator<Triple<K2, K3, V>> {
        Iterator<Map.Entry<K2, Map<K3, V>>> level2EntrySetIter = null;
        Iterator<Map.Entry<K3, V>> level3EntrySetIter = null;

        Triple<K2, K3, V> next; // next entry to return
        Triple<K2, K3, V> current; // current entry

        HashMap3LIterator2L(K1 rootKey) {
            current = next = null;
            Map<K2, Map<K3, V>> level2Map = HashMap3L.this.get(rootKey);
            if (level2Map != null) {
                level2EntrySetIter = level2Map.entrySet().iterator();
                if (level2EntrySetIter.hasNext()) {
                    Entry<K2, Map<K3, V>> firstLevel2Entry = level2EntrySetIter.next();
                    level3EntrySetIter = firstLevel2Entry.getValue().entrySet().iterator();
                    if (level3EntrySetIter.hasNext()) {
                        Entry<K3, V> firstLevel3Entry = level3EntrySetIter.next();
                        next = Triple.of(firstLevel2Entry.getKey(), firstLevel3Entry.getKey(), firstLevel3Entry.getValue());
                    }
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public Triple<K2, K3, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Triple<K2, K3, V> toReturn = next; // new next will be computed now ...

            if (level3EntrySetIter.hasNext()) {
                Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                current = next;
                next = Triple.of(current.getLeft(), nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
            } else if (level2EntrySetIter.hasNext()) {
                current = next;
                next = null;
                // level3Maps might be associated to a level2 key, but if they are empty, level3.hasNext() -> false
                // so we are searching for the next level3 entry that has any Level 3 values associated,
                // which might not be in this or the next level2Map entry
                boolean nextLevel2KeyWithElementsInLevel3MapFOUND = false;
                while (!nextLevel2KeyWithElementsInLevel3MapFOUND && level2EntrySetIter.hasNext()) {
                    // advance in level2Map
                    Entry<K2, Map<K3, V>> nextLevel2Entry = level2EntrySetIter.next();
                    level3EntrySetIter = nextLevel2Entry.getValue().entrySet().iterator(); // hasNext() checked above
                    if (level3EntrySetIter.hasNext()) {
                        Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                        next = Triple.of(nextLevel2Entry.getKey(), nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
                        nextLevel2KeyWithElementsInLevel3MapFOUND = true;
                    }
                    // else this level2Key had a Map which was empty
                }
            } else {
                current = next;
                next = null;
            }
            return toReturn;
        }
    }

    public Iterator<Triple<K2, K3, V>> iterator(K1 rootKey) {
        return new HashMap3LIterator2L(rootKey);
    }

    public Spliterator<Triple<K2, K3, V>> spliterator(K1 rootKey) {
        return Spliterators.spliteratorUnknownSize(iterator(rootKey), 0);
    }

    public Stream<Triple<K2, K3, V>> stream(K1 rootKey) {
        return StreamSupport.stream(spliterator(rootKey), false);
    }



    class HashMap3LIterator3L implements Iterator<Pair<K3, V>> {
        Iterator<Map.Entry<K3, V>> level3EntrySetIter = null;

        Pair<K3, V> next; // next entry to return
        Pair<K3, V> current; // current entry

        HashMap3LIterator3L(K1 rootKey, K2 k2) {
            current = next = null;
            Map<K2, Map<K3, V>> level2Map = HashMap3L.this.get(rootKey);
            if (level2Map != null) {
                Map<K3, V> level3Map = level2Map.get(k2);
                if (level2Map != null) {
                    level3EntrySetIter = level3Map.entrySet().iterator();
                    if (level3EntrySetIter.hasNext()) {
                        Entry<K3, V> firstLevel3Entry = level3EntrySetIter.next();
                        next = Pair.of(firstLevel3Entry.getKey(), firstLevel3Entry.getValue());
                    }
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public Pair<K3, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Pair<K3, V> toReturn = next; // new next will be computed now ...

            if (level3EntrySetIter.hasNext()) {
                Entry<K3, V> nextLevel3Entry = level3EntrySetIter.next();
                current = next;
                next = Pair.of(nextLevel3Entry.getKey(), nextLevel3Entry.getValue());
            } else {
                current = next;
                next = null;
            }
            return toReturn;
        }
    }

    public Iterator<Pair<K3, V>> iterator(K1 rootKey, K2 k2) {
        return new HashMap3LIterator3L(rootKey, k2);
    }

    public Spliterator<Pair<K3, V>> spliterator(K1 rootKey, K2 k2) {
        return Spliterators.spliteratorUnknownSize(iterator(rootKey, k2), 0);
    }

    public Stream<Pair<K3, V>> stream(K1 rootKey, K2 k2) {
        return StreamSupport.stream(spliterator(rootKey, k2), false);
    }
}
