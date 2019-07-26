package helpers.util;

import java.util.Map;
import java.util.Set;

public interface MapCloneable<K, V> extends Map<K, V> {

    MapCloneable<K, V> clone();

    MapCloneable<K, Map<K, V>> clone2();

    MapCloneable<K, Map<K, Map<K, V>>> clone3();
    
    MapCloneable<K, Map<K, Map<String, Map<K, V>>>> clone4();


    boolean isEmpty();

    boolean containsValue(Object value);

    boolean containsKey(Object key);

    V get(Object key);

    V put(K key, V value);

    V remove(Object key);

    void clear();

    Set<K> keySet();

    Set<Entry<K, V>> entrySet();

}