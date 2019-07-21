package helpers.util;

import java.util.Map;
import java.util.Set;

public interface MapCloneable<K, V> extends Map<K, V> {

    MapCloneable<K, V> clone();


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