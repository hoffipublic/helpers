package helpers.util;

import java.util.ArrayList;
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

/**
 * HashMap2L provides to store values with two level hierarchy of keys, super key (K1) and sub key
 * (K2). The objects are inserted using super and sub keys. It is not mandatory to use both keys as
 * hierarchy, user can use two keys to store the values and use either of the key to retrieve it.
 * 
 * @author Prathab K
 *
 */
public class HashMap2L<K1, K2, V>  implements Iterable<Triple<K1, K2, V>> {

    /** Map structure holding another Map structure to implement HashMap2L */
    public MapCloneable<K1, Map<K2, V>> rootMap;
    /** prototype of map to clone from (for being able to use different Map implementation types) */
    private MapCloneable<K2, V> level2MapClonePrototype;


    /** Initializes the HashMap2L */
    public HashMap2L(MapCloneable<K1, Map<K2, V>> rootMapCloneable,
                     MapCloneable<K2, V> level2MapClonePrototype) {
        this.level2MapClonePrototype = level2MapClonePrototype;
        rootMap = rootMapCloneable.clone();
    }

    /**
     * Puts the value object based on the (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param v  value object
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    public V put(K1 k1, K2 k2, V v) {
        Map<K2, V> l2Map = null;
        if (rootMap.containsKey(k1)) {
            l2Map = rootMap.get(k1);
        } else {
            l2Map = (MapCloneable<K2, V>)level2MapClonePrototype.clone();
            rootMap.put(k1, l2Map);
        }
        return l2Map.put(k2, v);
    }

    /**
     * Gets the value object for the specified (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V get(K1 k1, K2 k2) {
        if (rootMap.containsKey(k1)) {
            Map<K2, V> l2Map = rootMap.get(k1);
            return l2Map.get(k2);
        }
        return null;
    }

    /**
     * Gets the value object for the specified (super)key K1
     * 
     * @param k1 key1 (super-key)
     * @return HashMap structure contains the values for the key k1 if exists or <tt>null</tt> if
     *         does not exists
     */
    public Map<K2, V> get(K1 k1) {
        return rootMap.get(k1);
    }

    /**
     * Gets the first value object found for the specified (sub)key K2
     * 
     * @param k2 key2 (sub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V getFirstBySubKey(K2 k2) {
        for (Map<K2, V> l2Map : rootMap.values()) {
            if (l2Map.containsKey(k2)) {
                return l2Map.get(k2);
            }
        }
        return null;
    }

    /**
     * Gets the first value object found for the specified (sub)key K2
     * 
     * @param k2 key2 (sub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public Map<K2, V> getAllBySubKey(K2 k2) {
        Map<K2, V> resultMap = (MapCloneable<K2, V>) level2MapClonePrototype.clone();
        for (Map<K2, V> l2Map : rootMap.values()) {
            if (l2Map.containsKey(k2)) {
                resultMap.put(k2, l2Map.get(k2));
            }
        }
        return resultMap;
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
        if (rootMap.containsKey(k1)) {
            Map<K2, V> l2Map = rootMap.get(k1);
            return l2Map.containsKey(k2);
        }
        return false;
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
     * Removes the value object for the specified (super)key K1 and (sub)key K2
     * 
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return previous value associated with specified key, or <tt>null</tt> if there was no
     *         mapping for key.
     */
    public V remove(K1 k1, K2 k2) {
        if (rootMap.containsKey(k1)) {
            Map<K2, V> l2Map = rootMap.get(k1);
            return l2Map.remove(k2);
        }
        return null;
    }

    /**
     * Removes the value object(s) for the specified (super)key K1
     * 
     * @param k1 key1 (super-key)
     * @return previous value (HashMap structure) associated with specified key, or <tt>null</tt> if
     *         there was no mapping for key.
     */
    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    public Map<K2, V> remove(K1 k1) {
        return rootMap.remove(k1);
    }

    /**
     * Size of MultiKeyHashMap
     * 
     * @return MultiKeyHashMap size
     */
    public int size() {
        int size = 0;
        for (Map<K2, V> l2Map : rootMap.values()) {
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
        for (Map<K2, V> l2Map : rootMap.values()) {
            items.addAll(l2Map.values());
        }
        return items;
    }

    /**
     * Clears the entire hash map
     */
    public void clear() {
        for (Map<K2, V> l2Map : rootMap.values()) {
            l2Map.clear();
        }

        rootMap.clear();
    }


    class HashMap2LIterator implements Iterator<Triple<K1, K2, V>> {
        Iterator<Map.Entry<K1, Map<K2, V>>> rootEntrySetIter = null;
        Iterator<Map.Entry<K2, V>> level2EntrySetIter = null;

        Triple<K1, K2, V> next; // next entry to return
        Triple<K1, K2, V> current; // current entry

        HashMap2LIterator() {
            current =  next = null;
            rootEntrySetIter = rootMap.entrySet().iterator();
            if(rootEntrySetIter.hasNext()) {
                Entry<K1, Map<K2, V>> firstRootEntry = rootEntrySetIter.next();
                level2EntrySetIter = firstRootEntry.getValue().entrySet().iterator();
                if(level2EntrySetIter.hasNext()) {
                    Entry<K2, V> firstLevel2Entry = level2EntrySetIter.next();
                    next = Triple.of(firstRootEntry.getKey(), firstLevel2Entry.getKey(), firstLevel2Entry.getValue());
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public Triple<K1, K2, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Triple<K1, K2, V> toReturn = next; // new next will be computed now ...
            
            if(level2EntrySetIter.hasNext()) {
                Entry<K2, V> nextLevel2Entry = level2EntrySetIter.next();
                current = next;
                next = Triple.of(current.getLeft(), nextLevel2Entry.getKey(), nextLevel2Entry.getValue());
            } else {
                current = next;
                next = null;
                // level2Maps might be associated to a root key, but if they are empty, level2.hasNext() -> false
                // so we are searching for the next level2 entry that has any values associated,
                // which might not be in this or the next rootKey Map
                boolean nextRootKeyWithElementsInLevel2MapFOUND = false;
                while (!nextRootKeyWithElementsInLevel2MapFOUND && rootEntrySetIter.hasNext()) {
                    // advance in rootMap
                    Entry<K1, Map<K2, V>> nextRootEntry = rootEntrySetIter.next();
                    level2EntrySetIter = nextRootEntry.getValue().entrySet().iterator();
                    if(level2EntrySetIter.hasNext()) {
                        Entry<K2, V> nextLevel2Entry = level2EntrySetIter.next();
                        next = Triple.of(nextRootEntry.getKey(), nextLevel2Entry.getKey(), nextLevel2Entry.getValue());
                        nextRootKeyWithElementsInLevel2MapFOUND = true;
                    }
                    // else this rootKey had a Map which was empty
                }
            }
            return toReturn;
        }

        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    @Override
    public Iterator<Triple<K1, K2, V>> iterator() {
        return new HashMap2LIterator();
    }

    @Override
    public Spliterator<Triple<K1, K2, V>> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

    public Stream<Triple<K1, K2, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }


    class HashMap2LIteratorLevel2 implements Iterator<Pair<K2, V>> {
        Iterator<Map.Entry<K2, V>> level2EntrySetIter = null;

        Pair<K2, V> next; // next entry to return
        Pair<K2, V> current; // current entry

        HashMap2LIteratorLevel2(K1 rootKey) {
            current = next = null;
            Map<K2, V> level2Map = HashMap2L.this.rootMap.get(rootKey);
            if (level2Map != null) {
                level2EntrySetIter = level2Map.entrySet().iterator();
                if (level2EntrySetIter.hasNext()) {
                    Entry<K2, V> firstLevel2Entry = level2EntrySetIter.next();
                    next = Pair.of(firstLevel2Entry.getKey(), firstLevel2Entry.getValue());
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public Pair<K2, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Pair<K2, V> toReturn = next; // new next will be computed now ...
            if (level2EntrySetIter.hasNext()) {
                Entry<K2, V> nextLevel2Entry = level2EntrySetIter.next();
                current = next;
                next = Pair.of(nextLevel2Entry.getKey(), nextLevel2Entry.getValue());
            } else {
                next = null;
            }
            return toReturn;
        }

        public final void remove() {
            throw new RuntimeException("Implementation not possible, sorry!");
        }
    }

    public Iterator<Pair<K2, V>> iterator(K1 rootKey) {
        return new HashMap2LIteratorLevel2(rootKey);
    }

    public Spliterator<Pair<K2, V>> spliterator(K1 rootKey) {
        return Spliterators.spliteratorUnknownSize(iterator(rootKey), 0);
    }

    public Stream<Pair<K2, V>> stream(K1 rootKey) {
        return StreamSupport.stream(spliterator(rootKey), false);
    }

}