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
public class LinkedHashMapCloneableForMeta<K, V, MV> extends LinkedHashMap<K, V>
        implements MapCloneableForMeta<K, V, MV> {

    private static final long serialVersionUID = -4408981080054694330L;

    public LinkedHashMapCloneableForMeta() {
        super();
    }

    public LinkedHashMapCloneableForMeta(int initialCapacity) {
        super(initialCapacity);
    }

    public LinkedHashMapCloneableForMeta(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LinkedHashMapCloneableForMeta(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public LinkedHashMapCloneableForMeta(Map<? extends K, ? extends V> m) {
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

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, MV> clonem() {
        return (MapCloneable<K, MV>) clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, MV>> clone2m() {
        return (MapCloneable<K, Map<K, MV>>) clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, MV>>> clone3m() {
        return (MapCloneable<K, Map<K, Map<K, MV>>>) clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, Map<K, MV>>>> clone4m() {
        return (MapCloneable<K, Map<K, Map<K, Map<K, MV>>>>) clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<V, Map<K, MV>> clone2v() {
        return (MapCloneable<V, Map<K, MV>>) clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<V, Map<K, MV>>> clone3v() {
        return (MapCloneable<K, Map<V, Map<K, MV>>>) clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<V, Map<K, MV>>>> clone4v() {
        return (MapCloneable<K, Map<K, Map<V, Map<K, MV>>>>) clone();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, Map<V, Map<K, MV>>>>> clone5v() {
        return (MapCloneable<K, Map<K, Map<K, Map<V, Map<K, MV>>>>>) clone();
    }
}
