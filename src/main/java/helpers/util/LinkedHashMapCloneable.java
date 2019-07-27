package helpers.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * As AbstractMap's clone() method is protected you cannot put HashMap, TreeMap, LinkedHashMap, ...
 * as field-member in a class which does not extend AbstractMap and clone it. As you might want to
 * operate on different kinds of AbstractMaps by giving a prototype instance of a HashMap, TreeMap,
 * LinkedHashMap, ... for cloning you can use this class instead of its original LinkedHashMap.
 * 
 * @param <K> Hash Key
 * @param <V> Hash Value
 */
public class LinkedHashMapCloneable<K, V> extends LinkedHashMap<K, V>
        implements MapCloneable<K, V> {

    private static final long serialVersionUID = -6788356153024101564L;

    public LinkedHashMapCloneable() {
        super();
    }

    public LinkedHashMapCloneable(int initialCapacity) {
        super(initialCapacity);
    }

    public LinkedHashMapCloneable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LinkedHashMapCloneable(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public LinkedHashMapCloneable(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, V> clone() {
        return (MapCloneable<K, V>) super.clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, V>> clone2() {
        return (MapCloneable<K, Map<K, V>>) super.clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, V>>> clone3() {
        return (MapCloneable<K, Map<K, Map<K, V>>>) super.clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, Map<K, V>>>> clone4() {
        return (MapCloneable<K, Map<K, Map<K, Map<K, V>>>>) super.clone();
    }
}
