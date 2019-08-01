package helpers.util;

public class HashMap3LWithMetaSameKeys<K, V, MV>
        extends HashMap3LWithMeta<K, K, K, V, K, MV> {

    public HashMap3LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototype
          ) {
        this(mapsClonePrototype, METAKEYSORT.TREE);
    }

    public HashMap3LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototype,
               METAKEYSORT metakeysort
          ) {
        this(mapsClonePrototype.clone(), mapsClonePrototype.clone(), mapsClonePrototype.clone(), mapsClonePrototype.clone(), metakeysort);
    }

    public HashMap3LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototypeRoot,
               MapCloneable<K, V> mapsClonePrototypeL2,
               MapCloneable<K, V> mapsClonePrototypeL3,
               MapCloneable<K, V> mapsClonePrototypeMeta,
               METAKEYSORT metakeysort
          ) {
        super(mapsClonePrototypeL3.clone3(), mapsClonePrototypeL2.clone2(), mapsClonePrototypeRoot.clone(), metakeysort);
    }
}