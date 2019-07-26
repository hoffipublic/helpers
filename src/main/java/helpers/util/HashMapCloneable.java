package helpers.util;

import java.util.HashMap;
import java.util.Map;

/** 
 * As AbstractMap's clone() method is protected you cannot put HashMap, TreeMap, LinkedHashMap, ...
 * as field-member in a class which does not extend AbstractMap and clone it.
 * As you might want to operate on different kinds of AbstractMaps by giving a prototype instance 
 * of a HashMap, TreeMap, LinkedHashMap, ... for cloning you can use this class instead of its original HashMap. 
 * 
 * @param <K> Hash Key
 * @param <V> Hash Value
 */
public class HashMapCloneable<K, V> extends HashMap<K, V> implements MapCloneable<K, V> {

    private static final long serialVersionUID = -2395474779471946575L;

    public HashMapCloneable() {
        super();
    }

    HashMapCloneable(int initialCapacity) {
        super(initialCapacity);
    }

    HashMapCloneable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    HashMapCloneable(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, V> clone() {
        return (MapCloneable<K, V>)super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, V>> clone2() {
        return (MapCloneable<K, Map<K, V>>)super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<K, V>>> clone3() {
        return (MapCloneable<K, Map<K, Map<K, V>>>)super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCloneable<K, Map<K, Map<String, Map<K, V>>>> clone4() {
        return (MapCloneable<K, Map<K, Map<String, Map<K, V>>>>)super.clone();
    }
}
