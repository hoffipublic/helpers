package helpers.util;

public class HashMap2LWithMetaSameKeys<K, V, MV>
        extends HashMap2LWithMeta<K, K, V, K, MV> {

    public HashMap2LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototype
          ) {
        this(mapsClonePrototype, METAKEYSORT.TREE);
    }

    public HashMap2LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototype,
               METAKEYSORT metakeysort
          ) {
        this(mapsClonePrototype.clone(), mapsClonePrototype.clone(), mapsClonePrototype.clone(), mapsClonePrototype.clone(), metakeysort);
    }

    public HashMap2LWithMetaSameKeys(
               MapCloneable<K, V> mapsClonePrototypeRoot,
               MapCloneable<K, V> mapsClonePrototypeL2,
               MapCloneable<K, V> mapsClonePrototypeL3,
               MapCloneable<K, V> mapsClonePrototypeMeta,
               METAKEYSORT metakeysort
          ) {
        super(mapsClonePrototypeL2.clone2(), mapsClonePrototypeRoot.clone(), metakeysort);
    }
}