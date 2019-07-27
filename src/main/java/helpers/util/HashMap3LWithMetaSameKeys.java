package helpers.util;

public class HashMap3LWithMetaSameKeys<K, V, MV>
        extends HashMap3LWithMeta<K, K, K, V, K, MV> {

    public HashMap3LWithMetaSameKeys(
               MapCloneableForMeta<K, V, MV> mapsClonePrototype
          ) {
        super(
            // maps for real values of extended HashMap3L
            mapsClonePrototype.clone3(), mapsClonePrototype.clone2(), mapsClonePrototype.clone(),
            // map for storing meta-keys to meta-values
            mapsClonePrototype.clonem(),
            // maps for storing metaMap for rootLevel K1
            mapsClonePrototype.clone2m(),
            // maps for storing metMap for level2 K2
            mapsClonePrototype.clone3m(),
            mapsClonePrototype.clone2m(),
            // maps for storing metaMap for level3 K3
            mapsClonePrototype.clone4m(),
            mapsClonePrototype.clone3m(),
            mapsClonePrototype.clone2m(),
            // maps for storing metaMap for values V
            mapsClonePrototype.clone5v(),
            mapsClonePrototype.clone4v(),
            mapsClonePrototype.clone3v(),
            mapsClonePrototype.clone2v()
        );
    }

    public HashMap3LWithMetaSameKeys(
               MapCloneableForMeta<K, V, MV> mapsClonePrototypeRoot,
               MapCloneableForMeta<K, V, MV> mapsClonePrototypeL2,
               MapCloneableForMeta<K, V, MV> mapsClonePrototypeL3,
               MapCloneableForMeta<K, V, MV> mapsClonePrototypeMeta
          ) {
        super(
            // maps for real values of extended HashMap3L
                mapsClonePrototypeL3.clone3(), mapsClonePrototypeL2.clone2(), mapsClonePrototypeRoot.clone(),
            // map for storing meta-keys to meta-values
            mapsClonePrototypeMeta.clonem(),
            // maps for storing metaMap for rootLevel K1
            mapsClonePrototypeMeta.clone2m(),
            // maps for storing metMap for level2 K2
            mapsClonePrototypeMeta.clone3m(),
            mapsClonePrototypeMeta.clone2m(),
            // maps for storing metaMap for level3 K3
            mapsClonePrototypeMeta.clone4m(),
            mapsClonePrototypeMeta.clone3m(),
            mapsClonePrototypeMeta.clone2m(),
            // maps for storing metaMap for values V
            mapsClonePrototypeMeta.clone5v(),
            mapsClonePrototypeMeta.clone4v(),
            mapsClonePrototypeMeta.clone3v(),
            mapsClonePrototypeMeta.clone2v()
        );
    }
}