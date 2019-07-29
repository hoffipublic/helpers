package helpers.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import helpers.tuple.Quadruple;

public class HashMap3LWithMeta<K1, K2, K3, V, MK, MV> extends HashMap3L<K1, K2, K3, V> {

    /** meta-data maps for each level */
    public HashMap2L<K1, MK, MV> metaForRoot;
    public HashMap3L<K1, K2, MK, MV> metaForLevel2;
    public HashMap3L<K1, K2, K3, Map<MK, MV>> metaForLevel3;
    public HashMap3L<K1, K2, K3, Map<V, Map<MK, MV>>> metaForValues;

    /**
     * prototypes of maps to clone meta-data from (for being able to use different Map implementation types)
     */
    protected MapCloneable<V, Map<MK, MV>> metaValuesMapClonePrototype;
    protected MapCloneable<MK, MV> metaMapsClonePrototype;

    /** protected constructor in support of subtyping of this*/
    protected HashMap3LWithMeta(
        MapCloneable<K1, Map<K2, Map<K3, V>>> rootMapClonePrototype,
        MapCloneable<K2, Map<K3, V>> level2MapClonePrototype,
        MapCloneable<K3, V> level3MapClonePrototype) {
            super(rootMapClonePrototype, level2MapClonePrototype, level3MapClonePrototype);
    }


    /** public constructor */
    public HashMap3LWithMeta(
            // maps for real values of extended HashMap3L
            MapCloneable<K1, Map<K2, Map<K3, V>>> rootMapClonePrototype,
            MapCloneable<K2, Map<K3, V>>  level2MapClonePrototype,
            MapCloneable<K3, V> level3MapClonePrototype,
            // map for storing meta-keys to meta-values
            MapCloneable<MK, MV> metaMapsClonePrototype,
            // maps for storing metaMap for rootLevel K1
            MapCloneable<K1, Map<MK, MV>> metaRootMapClonePrototype,
            // maps for storing metMap for level2 K2
            MapCloneable<K1, Map<K2, Map<MK, MV>>> metaLevel2RootMapClonePrototype,
            MapCloneable<K2, Map<MK, MV>> metaLevel2L2MapClonePrototype,
            // maps for storing metaMap for level3 K3
            MapCloneable<K1, Map<K2, Map<K3, Map<MK, MV>>>> metaLevel3RootMapClonePrototype,
            MapCloneable<K2, Map<K3, Map<MK, MV>>> metaLevel3L2MapClonePrototype,
            MapCloneable<K3, Map<MK, MV>> metaLevel3L3MapClonePrototype,
            // maps for storing metaMap for values V
            MapCloneable<K1, Map<K2, Map<K3, Map<V, Map<MK, MV>>>>> metaValuesRootMapClonePrototype,
            MapCloneable<K2, Map<K3, Map<V, Map<MK, MV>>>> metaValuesL2MapClonePrototype,
            MapCloneable<K3, Map<V, Map<MK, MV>>> metaValuesL3MapClonePrototype,
            MapCloneable<V, Map<MK, MV>> metaValuesMapClonePrototype
        ) {
        super(rootMapClonePrototype, level2MapClonePrototype, level3MapClonePrototype);    

        this.metaMapsClonePrototype = metaMapsClonePrototype;
        this.metaValuesMapClonePrototype = metaValuesMapClonePrototype;

        this.metaForRoot = new HashMap2L<>(metaRootMapClonePrototype, metaMapsClonePrototype);
        this.metaForLevel2 = new HashMap3L<>(metaLevel2RootMapClonePrototype, metaLevel2L2MapClonePrototype, metaMapsClonePrototype);
        this.metaForLevel3 = new HashMap3L<>(metaLevel3RootMapClonePrototype, metaLevel3L2MapClonePrototype, metaLevel3L3MapClonePrototype);
        this.metaForValues = new HashMap3L<>(metaValuesRootMapClonePrototype, metaValuesL2MapClonePrototype, metaValuesL3MapClonePrototype);
    }    

    // ======= getMeta ===========================

    /** gets the whole Map of MetaKeys to MetaValues for rootKey K1 */
    public Map<MK, MV> metaGetMap(K1 k1) {
        return metaForRoot.get(k1); // initializes l2map
    }

    /** gets the MetaValue associated with rootKey K1 for MetaKey mk */
    public MV metaGet(K1 k1, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1); // initializes l2map
        return metaMap.get(mk);
    }

    public Map<MK, MV> metaGetMap(K1 k1, K2 k2) {
        return metaForLevel2.get(k1, k2); // initializes l2map and l3map
    }

    public MV metaGet(K1 k1, K2 k2, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1, k2); // initializes l2map and l3map
        return metaMap.get(mk);
    }

    public Map<MK, MV> metaGetMap(K1 k1, K2 k2, K3 k3) {
        Map<MK, MV> metaMap = metaForLevel3.get(k1, k2, k3); // initializes l2map and l3map
        if (metaMap == null) {
            metaMap = metaMapsClonePrototype.clone();
            metaForLevel3.put(k1, k2, k3, metaMap);
        }
        return metaMap;
    }

    public MV metaGet(K1 k1, K2 k2, K3 k3, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1, k2, k3); // initializes l4map
        return metaMap.get(mk);
    }

    public Map<MK, MV> metaGetMap(K1 k1, K2 k2, K3 k3, V v) {
        Map<V, Map<MK, MV>> metaMap = metaForValues.get(k1, k2, k3);
        if (metaMap == null) {
            metaMap = metaValuesMapClonePrototype.clone();
            this.metaForValues.put(k1, k2, k3, metaMap);
        }
        Map<MK, MV> metaMetaMap = metaMap.get(v);
        if (metaMetaMap == null) {
            metaMetaMap = metaMapsClonePrototype.clone();
            metaMap.put(v, metaMetaMap);
        }
        return metaMap.get(v);
    }

    public MV metaGet(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        Map<MK, MV> metaMap = metaGetMap(k1, k2, k3, v);
        return metaMap.get(mk);
    }

    //=======  putMeta ===========================

    public MV metaPut(K1 k1, MK mk, MV mv) {
        if( super.rootMap.containsKey(k1)) {
            return metaGetMap(k1).put(mk, mv);
        }
        throw new NoSuchElementException(String.format("no rootKey '%s' to put meta-data '%s' on found", k1, mk));
    }

    public Map<MK, MV> metaRemoveKey(K1 k1) {
        this.metaForValues.remove(k1);
        this.metaForLevel3.remove(k1);
        this.metaForLevel2.remove(k1);
        return this.metaForRoot.remove(k1);
    }

    public MV metaRemove(K1 k1, MK mk) {
        return metaForRoot.remove(k1, mk);
    }

    public MV metaRemoveValue(K1 k1, MK mk, MV mv) {
        return metaForRoot.removeValue(k1, mk, mv);
    }

    public void metaClearMapValues(K1 k1) {
        metaGetMap(k1).clear();
    }
    public void metaMergeMapValues(K1 k1, Map<MK, MV> metaMap) {
        if (metaMap != null) {
            metaGetMap(k1).putAll(metaMap);
        }
    }

    public void metaReplaceMapValues(K1 k1, Map<MK, MV> metaMap) {
        metaClearMapValues(k1);
        metaMergeMapValues(k1, metaMap);
    }

    public boolean metaContains(K1 k1, MK mk) {
        return metaForRoot.containsKey(k1, mk);
    }


    public MV metaPut(K1 k1, K2 k2, MK mk, MV mv) {
        if(super.get(k1).containsKey(k2)) {
            return metaGetMap(k1, k2).put(mk, mv);
        }
        throw new NoSuchElementException(String.format("no level2 key '%s' found for rootKey '%s' to put meta-data '%s' on found", k2, k1, mk));
    }

    public Map<MK, MV> metaRemoveKey(K1 k1, K2 k2) {
        Map<K2, Map<MK, MV>> metaMap = this.metaForLevel2.get(k1);
        if (metaMap != null) {
            return metaMap.remove(k2);
        }
        return null;
    }

    public MV metaRemove(K1 k1, K2 k2, MK mk) {
        return metaForLevel2.remove(k1, k2, mk);
    }

    public MV metaRemoveValue(K1 k1, K2 k2, MK mk, MV mv) {
        return metaForLevel2.removeValue(k1, k2, mk, mv);
    }

    public void metaClearMapValues(K1 k1, K2 k2) {
        metaGetMap(k1, k2).clear();
    }
    public void metaMergeMapValues(K1 k1, K2 k2, Map<MK, MV> metaMap) {
        if (metaMap != null) {
            metaGetMap(k1, k2).putAll(metaMap);
        }
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, Map<MK, MV> metaMap) {
        metaClearMapValues(k1, k2);
        metaMergeMapValues(k1, k2, metaMap);
    }

    public boolean metaContains(K1 k1, K2 k2, MK mk) {
        return metaForLevel2.containsKey(k1, k2, mk);
    }


    public MV metaPut(K1 k1, K2 k2, K3 k3, MK mk, MV mv) {
        if (super.get(k1, k2).containsKey(k3)) {
            return metaGetMap(k1, k2, k3).put(mk, mv);
        }
        throw new NoSuchElementException(String.format("no level3 key '%s' found for rootKey '%s' and level2 key '%s' to put meta-data '%s' on found", k3, k1, k2, mk));
    }

    public Map<MK, MV> metaRemoveKey(K1 k1, K2 k2, K3 k3) {
        Map<K3, Map<MK, MV>> metaMap = metaForLevel3.get(k1, k2);
        return metaMap.remove(k3);
    }

    public MV metaRemove(K1 k1, K2 k2, K3 k3, MK mk) {
        return metaGetMap(k1, k2, k3).remove(mk);
    }

    public MV metaRemoveValue(K1 k1, K2 k2, K3 k3, MK mk, MV mv) {
        Map<MK, MV> metaMap = metaGetMap(k1, k2, k3);
        if(metaMap.containsKey(mk) && mv.equals(metaMap.get(mk))) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public void metaClearMapValues(K1 k1, K2 k2, K3 k3) {
        metaGetMap(k1, k2, k3).clear();
    }

    public void metaMergeMapValues(K1 k1, K2 k2, K3 k3, Map<MK, MV> metaMap) {
        if (metaMap != null) {
            metaGetMap(k1, k2, k3).putAll(metaMap);
        }
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, K3 k3, Map<MK, MV> metaMap) {
        metaClearMapValues(k1, k2, k3);
        metaMergeMapValues(k1, k2, k3, metaMap);
    }

    public boolean metaContains(K1 k1, K2 k2, K3 k3, MK mk) {
        return metaGetMap(k1, k2, k3).containsKey(mk);
    }


    public MV metaPut(K1 k1, K2 k2, K3 k3, V v, MK mk, MV mv) {
        if(super.get(k1, k2, k3) != null) {
            return metaGetMap(k1, k2, k3, v).put(mk, mv);
        }
        throw new NoSuchElementException(String.format("no value '%s' found for rootKey '%s', level2 key '%s' and level3 key '%s' to put meta-data '%s' on found", v, k1, k2, k3, mk));
    }

    public Map<MK, MV> metaRemoveKey(K1 k1, K2 k2, K3 k3, V v) {
        Map<V, Map<MK, MV>> metaMap = this.metaForValues.get(k1, k2, k3);
        if (metaMap != null) {
            return metaMap.remove(v);
        }
        return null;
    }

    public MV metaRemove(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        return metaGetMap(k1, k2, k3, v).remove(mk);
    }

    public MV metaRemoveValue(K1 k1, K2 k2, K3 k3, V v, MK mk, MV mv) {
        Map<MK, MV> metaMap = metaGetMap(k1, k2, k3, v);
        if (metaMap.containsKey(mk) && mv.equals(metaMap.get(mk))) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public void metaClearMapValues(K1 k1, K2 k2, K3 k3, V v) {
        metaGetMap(k1, k2, k3, v).clear();
    }

    public void metaMergeMapValues(K1 k1, K2 k2, K3 k3, V v, Map<MK, MV> metaMap) {
        if (metaMap != null) {
            metaGetMap(k1, k2, k3, v).putAll(metaMap);
        }
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, K3 k3, V v, Map<MK, MV> metaMap) {
        metaClearMapValues(k1, k2, k3, v);
        metaMergeMapValues(k1, k2, k3, v, metaMap);
    }

    public boolean metaContains(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        return metaGetMap(k1, k2, k3, v).containsKey(mk);
    }


    // ======= findOneByMeta ===========================

    public Quadruple<K1, K2, K3, V> findFirstInAllByMeta(MK mk) {
        K1 rootKey = findFirstInRootByMeta(mk);
        if (rootKey != null) {
            return Quadruple.of(rootKey, null, null, null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMeta(mk);
        if (level2Key != null) {
            return Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null);
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMeta(mk);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null);
        }
        return findFirstInValuesByMeta(mk);
    }

    public Quadruple<K1, K2, K3, V> findLeastInAllByMeta(MK mk) {
        Quadruple<K1, K2, K3, V> valueKey = findFirstInValuesByMeta(mk);
        if (valueKey != null) {
            return valueKey;
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMeta(mk);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMeta(mk);
        if (level2Key != null) {
            return Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null);
        }
        K1 rootKey = findFirstInRootByMeta(mk);
        if (rootKey != null) {
            return Quadruple.of(rootKey, null, null, null);
        }
        return Quadruple.of(null, null, null, null);
    }

    public Quadruple<K1, K2, K3, V> findFirstInAllByMetaValue(MK mk, MV mv) {
        K1 rootKey = findFirstinRootByMetaValue(mk, mv);
        if (rootKey != null) {
            return Quadruple.of(rootKey, null, null, null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMetaValue(mk, mv);
        if (level2Key != null) {
            return Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null);
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMetaValue(mk, mv);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null);
        }
        return findFirstInValuesByMetaValue(mk, mv);
    }

    public Quadruple<K1, K2, K3, V> findLeastInAllByMetaValue(MK mk, MV mv) {
        Quadruple<K1, K2, K3, V> valueKey = findFirstInValuesByMetaValue(mk, mv);
        if (valueKey != null) {
            return valueKey;
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMetaValue(mk, mv);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMetaValue(mk, mv);
        if (level2Key != null) {
            return Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null);
        }
        K1 rootKey = findFirstinRootByMetaValue(mk, mv);
        if (rootKey != null) {
            return Quadruple.of(rootKey, null, null, null);
        }
        return Quadruple.of(null, null, null, null);
    }

    public K1 findFirstInRootByMeta(MK mk) {
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.rootMap.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                return rootEntry.getKey();
            }
        }
        return null;
    }

    public K1 findFirstinRootByMetaValue(MK mk, MV mv) {
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.rootMap.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                if (mv.equals(rootEntry.getValue().get(mk))) {
                    return rootEntry.getKey();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public Pair<K1, K2> findFirstInLevel2ByMeta(MK mk) {
        for (Entry<K1, Map<K2, Map<MK, MV>>> rootEntry : metaForLevel2.rootMap.entrySet()) {
            for (Entry<K2, Map<MK, MV>> l2Entry : rootEntry.getValue().entrySet()) {
                if (l2Entry.getValue().containsKey(mk)) {
                    return Pair.of(rootEntry.getKey(), l2Entry.getKey());
                }
            }
        }
        return null;
    }
    
    public Pair<K1, K2> findFirstInLevel2ByMetaValue(MK mk, MV mv) {
        for (Entry<K1, Map<K2, Map<MK, MV>>> rootEntry : metaForLevel2.rootMap.entrySet()) {
            for (Entry<K2, Map<MK, MV>> l2Entry : rootEntry.getValue().entrySet()) {
                if (l2Entry.getValue().containsKey(mk)) {
                    if (mv.equals(l2Entry.getValue().get(mk))) {
                        return Pair.of(rootEntry.getKey(), l2Entry.getKey());
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    public Triple<K1, K2, K3> findFirstInLevel3ByMeta(MK mk) {
        for (Entry<K1, Map<K2, Map<K3, Map<MK, MV>>>> rootEntry : metaForLevel3.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<MK, MV>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<MK, MV>> l3Entry : l2Entry.getValue().entrySet()) {
                    if(l3Entry.getValue().containsKey(mk)) {
                        return Triple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey());
                    }
                }
            }
        }
        return null;
    }

    public Triple<K1, K2, K3> findFirstInLevel3ByMetaValue(MK mk, MV mv) {
        for (Entry<K1, Map<K2, Map<K3, Map<MK, MV>>>> rootEntry : metaForLevel3.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<MK, MV>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<MK, MV>> l3Entry : l2Entry.getValue().entrySet()) {
                    if (l3Entry.getValue().containsKey(mk)) {
                        if (mv.equals(l3Entry.getValue().get(mk))) {
                            return Triple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey());
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Quadruple<K1, K2, K3, V> findFirstInValuesByMeta(MK mk) {
        for (Entry<K1, Map<K2, Map<K3, Map<V, Map<MK, MV>>>>> rootEntry : metaForValues.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<V, Map<MK, MV>>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<V, Map<MK, MV>>> l3Entry : l2Entry.getValue().entrySet()) {
                    for (Entry<V, Map<MK, MV>> valueEntry : l3Entry.getValue().entrySet()) {
                        if(valueEntry.getValue().containsKey(mk)) {
                            return Quadruple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey(), valueEntry.getKey());
                        }
                    }
                }
            }
        }
        return null;
    }

    public Quadruple<K1, K2, K3, V> findFirstInValuesByMetaValue(MK mk, MV mv) {
        for (Entry<K1, Map<K2, Map<K3, Map<V, Map<MK, MV>>>>> rootEntry : metaForValues.rootMap
                .entrySet()) {
            for (Entry<K2, Map<K3, Map<V, Map<MK, MV>>>> l2Entry : rootEntry.getValue()
                    .entrySet()) {
                for (Entry<K3, Map<V, Map<MK, MV>>> l3Entry : l2Entry.getValue().entrySet()) {
                    for (Entry<V, Map<MK, MV>> valueEntry : l3Entry.getValue().entrySet()) {
                        if (valueEntry.getValue().containsKey(mk)) {
                            if(mv.equals(valueEntry.getValue().get(mk))) {
                                return Quadruple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey(), valueEntry.getKey());
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // ======= findAllByMeta ===========================

    public List<Quadruple<K1, K2, K3, V>> findAllInAllByMeta(MK mk) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        List<K1> rootKeyList = findAllInRootByMeta(mk);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Quadruple.of(rootKey, null, null, null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMeta(mk);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null));
        }
        List<Triple<K1, K2, K3>> level3KeyList = findAllInLevel3ByMeta(mk);
        for (Triple<K1, K2, K3> level3Key : level3KeyList) {
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null));
        }
        List<Quadruple<K1, K2, K3, V>> levelValuesKeyList = findAllInValuesByMeta(mk);
        resultList.addAll(levelValuesKeyList);

        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInAllByMetaValue(MK mk, MV mv) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        List<K1> rootKeyList = findAllinRootByMetaValue(mk, mv);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Quadruple.of(rootKey, null, null, null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMetaValue(mk, mv);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null));
        }
        List<Triple<K1, K2, K3>> level3KeyList = findAllInLevel3ByMetaValue(mk, mv);
        for (Triple<K1, K2, K3> level3Key : level3KeyList) {
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null));
        }
        List<Quadruple<K1, K2, K3, V>> levelValuesKeyList = findAllInValuesByMetaValue(mk, mv);
        resultList.addAll(levelValuesKeyList);

        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInAllByMetaReverse(MK mk) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        List<Quadruple<K1, K2, K3, V>> levelValuesKeyList = findAllInValuesByMeta(mk);
        resultList.addAll(levelValuesKeyList);
        List<Triple<K1, K2, K3>> level3KeyList = findAllInLevel3ByMeta(mk);
        for (Triple<K1, K2, K3> level3Key : level3KeyList) {
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(),
                    level3Key.getRight(), null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMeta(mk);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null));
        }
        List<K1> rootKeyList = findAllInRootByMeta(mk);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Quadruple.of(rootKey, null, null, null));
        }

        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInAllByMetaValueReverse(MK mk, MV mv) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        List<Quadruple<K1, K2, K3, V>> levelValuesKeyList = findAllInValuesByMetaValue(mk, mv);
        resultList.addAll(levelValuesKeyList);
        List<Triple<K1, K2, K3>> level3KeyList = findAllInLevel3ByMetaValue(mk, mv);
        for (Triple<K1, K2, K3> level3Key : level3KeyList) {
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(), null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMetaValue(mk, mv);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Quadruple.of(level2Key.getLeft(), level2Key.getRight(), null, null));
        }
        List<K1> rootKeyList = findAllinRootByMetaValue(mk, mv);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Quadruple.of(rootKey, null, null, null));
        }

        return resultList;
    }


    public List<K1> findAllInRootByMeta(MK mk) {
        List<K1> resultList = new ArrayList<>();
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.rootMap.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                resultList.add(rootEntry.getKey());
            }
        }
        return resultList;
    }

    public List<K1> findAllinRootByMetaValue(MK mk, MV mv) {
        List<K1> resultList = new ArrayList<>();
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.rootMap.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                if (mv.equals(rootEntry.getValue().get(mk))) {
                    resultList.add(rootEntry.getKey());
                }
            }
        }
        return resultList;
    }

    public List<Pair<K1, K2>> findAllInLevel2ByMeta(MK mk) {
        List<Pair<K1, K2>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<MK, MV>>> rootEntry : metaForLevel2.rootMap.entrySet()) {
            for (Entry<K2, Map<MK, MV>> l2Entry : rootEntry.getValue().entrySet()) {
                if (l2Entry.getValue().containsKey(mk)) {
                    resultList.add(Pair.of(rootEntry.getKey(), l2Entry.getKey()));
                }
            }
        }
        return resultList;
    }

    public List<Pair<K1, K2>> findAllInLevel2ByMetaValue(MK mk, MV mv) {
        List<Pair<K1, K2>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<MK, MV>>> rootEntry : metaForLevel2.rootMap.entrySet()) {
            for (Entry<K2, Map<MK, MV>> l2Entry : rootEntry.getValue().entrySet()) {
                if (l2Entry.getValue().containsKey(mk)) {
                    if (mv.equals(l2Entry.getValue().get(mk))) {
                        resultList.add(Pair.of(rootEntry.getKey(), l2Entry.getKey()));
                    } else {
                        break;
                    }
                }
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, K3>> findAllInLevel3ByMeta(MK mk) {
        List<Triple<K1, K2, K3>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<K3, Map<MK, MV>>>> rootEntry : metaForLevel3.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<MK, MV>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<MK, MV>> l3Entry : l2Entry.getValue().entrySet()) {
                    if (l3Entry.getValue().containsKey(mk)) {
                        resultList.add(Triple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey()));
                    }
                }
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, K3>> findAllInLevel3ByMetaValue(MK mk, MV mv) {
        List<Triple<K1, K2, K3>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<K3, Map<MK, MV>>>> rootEntry : metaForLevel3.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<MK, MV>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<MK, MV>> l3Entry : l2Entry.getValue().entrySet()) {
                    if (l3Entry.getValue().containsKey(mk)) {
                        if (mv.equals(l3Entry.getValue().get(mk))) {
                            resultList.add(Triple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey()));
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInValuesByMeta(MK mk) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<K3, Map<V, Map<MK, MV>>>>> rootEntry : metaForValues.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<V, Map<MK, MV>>>> l2Entry : rootEntry.getValue()
                    .entrySet()) {
                for (Entry<K3, Map<V, Map<MK, MV>>> l3Entry : l2Entry.getValue().entrySet()) {
                    for (Entry<V, Map<MK, MV>> valueEntry : l3Entry.getValue().entrySet()) {
                        if (valueEntry.getValue().containsKey(mk)) {
                            resultList.add(Quadruple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey(), valueEntry.getKey()));
                        }
                    }
                }
            }
        }
        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInValuesByMetaValue(MK mk, MV mv) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        for (Entry<K1, Map<K2, Map<K3, Map<V, Map<MK, MV>>>>> rootEntry : metaForValues.rootMap.entrySet()) {
            for (Entry<K2, Map<K3, Map<V, Map<MK, MV>>>> l2Entry : rootEntry.getValue().entrySet()) {
                for (Entry<K3, Map<V, Map<MK, MV>>> l3Entry : l2Entry.getValue().entrySet()) {
                    for (Entry<V, Map<MK, MV>> valueEntry : l3Entry.getValue().entrySet()) {
                        if (valueEntry.getValue().containsKey(mk)) {
                            if (mv.equals(valueEntry.getValue().get(mk))) {
                                resultList.add(Quadruple.of(rootEntry.getKey(), l2Entry.getKey(), l3Entry.getKey(), valueEntry.getKey()));
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }


    // ======= iterators ======================================================
    // ======= for iterating the individual key levels ========================
    // ======= use the iterators of the public fields of this class directly ==
    // ========================================================================

    class HashMap3LWithMetaIterator implements Iterator<Object[]> {
        Iterator<Triple<K1, MK, MV>> rootIter = HashMap3LWithMeta.this.metaForRoot.iterator();
        Iterator<Quadruple<K1, K2, MK, MV>> level2Iter = HashMap3LWithMeta.this.metaForLevel2.iterator();
        Iterator<Quadruple<K1, K2, K3, Map<MK, MV>>> level3Iter = HashMap3LWithMeta.this.metaForLevel3.iterator();
        Iterator<Entry<MK, MV>> level3EntriesIter = null;
        Iterator<Quadruple<K1, K2, K3, Map<V, Map<MK, MV>>>> levelVIter = HashMap3LWithMeta.this.metaForValues.iterator();
        Iterator<Entry<V, Map<MK, MV>>> levelVMapIter = null;
        Iterator<Entry<MK, MV>> levelVEntriesIter = null;

        Object[] next = null; // next entry to return
        Object[] current = null; // current entry

        HashMap3LWithMetaIterator() {
            if(rootIter.hasNext()) {
                Triple<K1, MK, MV> rootMeta = rootIter.next();
                next = new Object[] {rootMeta.getLeft(), rootMeta.getMiddle(), rootMeta.getRight(), null, null, null};
            } else {
                if(level2Iter.hasNext()) {
                    Quadruple<K1, K2, MK, MV> level2Meta = level2Iter.next();
                    next = new Object[] {level2Meta.getRoot(), level2Meta.getL2(), level2Meta.getL3(), level2Meta.getLeaf(), null, null};
                } else {
                    Object[] level3Next = level3Next();
                    if (level3Next != null) {
                        next = level3Next;
                    } else {
                        Object[] levelVNext = levelVNext();
                        if (levelVNext != null) {
                            next = levelVNext;
                        }
                    }
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Object[] toReturn = next; // new next will be computed now ...
            current = next;
            next = null;

            if (rootIter.hasNext()) {
                Triple<K1, MK, MV> rootMeta = rootIter.next();
                next = new Object[] {rootMeta.getLeft(), rootMeta.getMiddle(), rootMeta.getRight(), null, null, null};
            }
            if ((next == null)  && (level2Iter.hasNext())) {
                Quadruple<K1, K2, MK, MV> level2Meta = level2Iter.next();
                next = new Object[] {level2Meta.getRoot(), level2Meta.getL2(), level2Meta.getL3(), level2Meta.getLeaf(), null, null};
            }
            if ((next == null)  && (level3EntriesIter != null)) {
                Object[] level3Next = level3Next();
                if (level3Next != null) {
                    next = level3Next;
                }
            }
            if ((next == null) && (level3Iter.hasNext())) {

            }
            if ((next == null)  && (levelVEntriesIter.hasNext())) {
                Entry<MK, MV> levelVMetaEntry = levelVEntriesIter.next();
                next = new Object[] {current[0], current[1], current[2], current[3], levelVMetaEntry.getKey(), levelVMetaEntry.getValue()};
            }
            if ((next == null) && (levelVMapIter.hasNext())) {

            }
            if (next == null) {
                Object[] levelVNext = levelVNext();
                if (levelVNext != null) {
                    next = levelVNext;
                }
            }

            return toReturn;
        }

        private Object[] level3Next() {
            Quadruple<K1, K2, K3, Map<MK, MV>> level3Meta = null;
            // first (if not null) iterate Map<MK, MV>
            // ...
            // THEN iterate to next K3
            //
            if (level3Iter.hasNext()) {
                level3Meta = level3Iter.next();
            }
            if ((level3Meta != null) && !level3Meta.getLeaf().isEmpty()) {
                level3EntriesIter = level3Meta.getLeaf().entrySet().iterator();
                Entry<MK, MV> level3MetaEntry = level3EntriesIter.next();
                return new Object[] {level3Meta.getRoot(), level3Meta.getL2(), level3Meta.getL3(),
                        level3MetaEntry.getKey(), level3MetaEntry.getValue(), null};
            }
            return null;
        }

        private Object[] levelVNext() {
            // iterate  Map<V, Map<MK, MV>>>
            //
            Quadruple<K1, K2, K3, Map<V, Map<MK, MV>>> levelVMetaMap = null;
            if (levelVIter.hasNext()) {
                levelVMetaMap = levelVIter.next();
            }
            if ((levelVMetaMap != null) && !levelVMetaMap.getLeaf().isEmpty()) {
                levelVMapIter = levelVMetaMap.getLeaf().entrySet().iterator();
                Entry<V, Map<MK, MV>> levelVMetaMapEntry = levelVMapIter.next();
                if ((levelVMetaMapEntry.getValue() != null)
                        && (!levelVMetaMapEntry.getValue().isEmpty())) {
                    levelVEntriesIter = levelVMetaMapEntry.getValue().entrySet().iterator();
                    Entry<MK, MV> levelVMetaEntry = levelVEntriesIter.next();
                    return new Object[] {levelVMetaMap.getRoot(), levelVMetaMap.getL2(),
                            levelVMetaMap.getL3(), levelVMetaMapEntry.getKey(),
                            levelVMetaEntry.getKey(), levelVMetaEntry.getValue()};
                }
            }
            return null;
        }


        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    public Iterator<Object[]> iteratorMeta() {
        return new HashMap3LWithMetaIterator();
    }

    public Iterator<Object[]> iteratorMetaReverse() {
        throw new NotImplementedException("XXX sorry");
    }


    // ========================================================================
    // ====  Override parent methods to deal with Meta-Data accordingly  ======
    // ========================================================================

    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public Map<K2, Map<K3, V>> remove(K1 k1) {
        this.metaRemoveKey(k1);
        return super.remove(k1);
    }
    
    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public Map<K3, V> remove(K1 k1, K2 k2) {
        this.metaRemoveKey(k1, k2);
        return super.remove(k1, k2);
    }

    @Override
    public V remove(K1 k1, K2 k2, K3 k3) {
        this.metaRemoveKey(k1, k2, k3);
        return super.remove(k1, k2, k3);
    }

    @Override
    public V removeValue(K1 k1, K2 k2, K3 k3, V v) {
        this.metaRemoveKey(k1, k2, k3, v);
        return super.removeValue(k1, k2, k3, v);
    }

    @Override
    public void clear() {
        this.metaForRoot.clear();
        this.metaForLevel2.clear();
        this.metaForLevel3.clear();
        this.metaForValues.clear();
        super.clear();
    }

    @Override
    public void clear(K1 k1) {
        this.metaRemoveKey(k1);
        this.metaClearMapValues(k1);
        super.clear(k1);
    }

    @Override
    public void clear(K1 k1, K2 k2)
    {
        this.metaRemoveKey(k1, k2);
        this.metaClearMapValues(k1, k2);
        super.clear(k1, k2);
    }

}