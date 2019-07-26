package helpers.util;

import java.util.Map;

public class HashMap3LWithMetaStringKey<V, MV>
        extends HashMap3LWithMeta<String, String, String, V, String, MV> {

    public HashMap3LWithMetaStringKey(
            // maps for real values of extended HashMap3L
            MapCloneable<String, Map<String, Map<String, V>>> rootMapClonePrototype,
            MapCloneable<String, Map<String, V>> level2MapClonePrototype,
            MapCloneable<String, V> level3MapClonePrototype,
            // maps for meta values on all levels
            MapCloneable<String, MV> metaMapsClonePrototype,
            // maps for meta values on values
            MapCloneable<String, Map<String, Map<String, Map<V, Map<String, MV>>>>> metaValuesRootMapClonePrototype,
            MapCloneable<String, Map<String, Map<V, Map<String, MV>>>> metaValuesL2MapClonePrototype,
            MapCloneable<String, Map<V, Map<String, MV>>> metaValuesL3MapClonePrototype,
            MapCloneable<V, Map<String, MV>> metaValuesMapClonePrototype
    ) {
        super(rootMapClonePrototype, level2MapClonePrototype, level3MapClonePrototype);

        super.metaMapsClonePrototype = metaMapsClonePrototype;
        super.metaValuesMapClonePrototype = metaValuesMapClonePrototype;
        
        // maps for storing metaMap for rootLevel String
        MapCloneable<String, Map<String, MV>> metaRootMapClonePrototype = metaMapsClonePrototype.clone2();
        // maps for storing metMap for level2 String
        MapCloneable<String, Map<String, Map<String, MV>>> metaLevel2RootMapClonePrototype = metaMapsClonePrototype.clone3();
        MapCloneable<String, Map<String, MV>> metaLevel2L2MapClonePrototype = metaRootMapClonePrototype.clone();
        // maps for storing metaMap for level3 String
        MapCloneable<String, Map<String, Map<String, Map<String, MV>>>> metaLevel3RootMapClonePrototype = metaMapsClonePrototype.clone4();
        MapCloneable<String, Map<String, Map<String, MV>>> metaLevel3L2MapClonePrototype = metaLevel2RootMapClonePrototype.clone();
        MapCloneable<String, Map<String, MV>> metaLevel3L3MapClonePrototype = metaRootMapClonePrototype.clone();


        super.metaForRoot = new HashMap2L<>(metaRootMapClonePrototype, metaMapsClonePrototype);
        super.metaForLevel2 = new HashMap3L<>(metaLevel2RootMapClonePrototype, metaLevel2L2MapClonePrototype, metaMapsClonePrototype);
        super.metaForLevel3 = new HashMap3L<>(metaLevel3RootMapClonePrototype, metaLevel3L2MapClonePrototype, metaLevel3L3MapClonePrototype);
        super.metaForValues = new HashMap3L<>(metaValuesRootMapClonePrototype, metaValuesL2MapClonePrototype, metaValuesL3MapClonePrototype);
    }
}