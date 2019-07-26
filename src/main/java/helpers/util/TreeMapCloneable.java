package helpers.util;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * As AbstractMap's clone() method is protected you cannot put HashMap, TreeMap, LinkedHashMap, ...
 * as field-member in a class which does not extend AbstractMap and clone it. As you might want to
 * operate on different kinds of AbstractMaps by giving a prototype instance of a HashMap, TreeMap,
 * LinkedHashMap, ... for cloning you can use this class instead of its original TreeMap.
 * 
 * @param <K> Hash Key
 * @param <V> Hash Value
 */
public class TreeMapCloneable<K, V> extends TreeMap<K, V> implements MapCloneable<K, V> {

    private static final long serialVersionUID = -2050889148342752463L;

    public TreeMapCloneable() {
        super();
    }

    public TreeMapCloneable(Comparator<? super K> comparator) {
        super(comparator);
    }

    public TreeMapCloneable(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public TreeMapCloneable(SortedMap<K, ? extends V> m) {
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
    public MapCloneable<V, Map<K, K>> clone2v() {
        return (MapCloneable<V, Map<K, K>>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<V, Map<K, K>>> clone3v() {
        return (MapCloneable<K, Map<V, Map<K, K>>>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<V, Map<K, K>>>> clone4v() {
        return (MapCloneable<K, Map<K, Map<V, Map<K, K>>>>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, Map<V, Map<K, K>>>>> clone5v() {
        return (MapCloneable<K, Map<K, Map<K, Map<V, Map<K, K>>>>>) super.clone();
    }
}
