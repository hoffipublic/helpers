package helpers.util;

import java.util.Map;

public interface MapCloneableForMeta<K, V, MV> extends MapCloneable<K, V> {

    // for attaching metadata to keys
    MapCloneable<K, MV> clonem();
    MapCloneable<K, Map<K, MV>> clone2m();
    MapCloneable<K, Map<K, Map<K, MV>>> clone3m();
    MapCloneable<K, Map<K, Map<K, Map<K, MV>>>> clone4m();

    // for attaching metadata to values
    MapCloneable<V, Map<K, MV>> clone2v();
    MapCloneable<K, Map<V, Map<K, MV>>> clone3v();
    MapCloneable<K, Map<K, Map<V, Map<K, MV>>>> clone4v();
    MapCloneable<K, Map<K, Map<K, Map<V, Map<K, MV>>>>> clone5v();
}