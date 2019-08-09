package helpers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import helpers.tuple.Quintuple;

public class HashMap2LWithMeta<K1, K2, V, MK, MV> extends HashMap2L<K1, K2, V> {

    public enum METAKEYSORT {
        HASH, TREE, LINKEDHASH
    };

    /** meta-data maps for each level */
    public Map<K1, Map<MK, MV>> metaForRoot;
    public Map<Pair<K1, K2>, Map<MK, MV>> metaForLevel2;
    public Map<Triple<K1, K2, V>, Map<MK, MV>> metaForValues;

    /**
     * prototype of meta-map to clone meta-data from (for being able to use different Map
     * implementation types)
     */
    protected MapCloneable<MK, MV> metaMapClonePrototype;

    /** public constructor */
    public HashMap2LWithMeta(
            // maps for real values of extended HashMap3L
            MapCloneable<K1, Map<K2, V>> rootMapClonePrototype,
            MapCloneable<K2, V> level2MapClonePrototype) {
        this(rootMapClonePrototype, level2MapClonePrototype, METAKEYSORT.HASH);
    }

    /** public constructor */
    public HashMap2LWithMeta(
            // maps for real values of extended HashMap3L
            MapCloneable<K1, Map<K2, V>> rootMapClonePrototype,
            MapCloneable<K2, V> level2MapClonePrototype, METAKEYSORT metakeysort) {
        super(rootMapClonePrototype, level2MapClonePrototype);
        switch (metakeysort) {
            case HASH:
                this.metaForRoot = new HashMap<>();
                this.metaForLevel2 = new HashMap<>();
                this.metaForValues = new HashMap<>();
                this.metaMapClonePrototype = new HashMapCloneable<>();
                break;
            case TREE:
                this.metaForRoot = new TreeMap<>();
                this.metaForLevel2 = new TreeMap<>();
                this.metaForValues = new TreeMap<>();
                this.metaMapClonePrototype = new TreeMapCloneable<>();
                break;
            case LINKEDHASH:
                this.metaForRoot = new LinkedHashMap<>();
                this.metaForLevel2 = new LinkedHashMap<>();
                this.metaForValues = new LinkedHashMap<>();
                this.metaMapClonePrototype = new LinkedHashMapCloneable<>();
                break;
        }
    }

    // ======= getMeta ===========================

    /** gets the whole Map of MetaKeys to MetaValues for rootKey K1 */
    public Map<MK, MV> metaGetMap(K1 k1) {
        return metaForRoot.get(k1);
    }

    /** gets the MetaValue associated with rootKey K1 for MetaKey mk */
    public MV metaGet(K1 k1, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            return metaMap.get(mk);
        }
        return null;
    }

    /** gets the whole Map of MetaKeys to MetaValues for Pair<K1, K2> */
    public Map<MK, MV> metaGetMap(K1 k1, K2 k2) {
        return this.metaGetMap(Pair.of(k1, k2));
    }

    /** gets the whole Map of MetaKeys to MetaValues for Pair<K1, K2> */
    public Map<MK, MV> metaGetMap(Pair<K1, K2> key) {
        return metaForLevel2.get(key);
    }

    /** gets the MetaValue associated with Pair<K1, K2> for MetaKey mk */
    public MV metaGet(K1 k1, K2 k2, MK mk) {

        return this.metaGet(Pair.of(k1, k2), mk);
    }

    /** gets the MetaValue associated with Pair<K1, K2> for MetaKey mk */
    public MV metaGet(Pair<K1, K2> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.get(mk);
        }
        return null;
    }

    /** gets the whole Map of MetaKeys to MetaValues for Quadruple<K1, K2, K3, V> */
    public Map<MK, MV> metaGetMap(K1 k1, K2 k2, V v) {
        return this.metaGetMap(Triple.of(k1, k2, v));
    }

    /** gets the whole Map of MetaKeys to MetaValues for Quadruple<K1, K2, K3, V> */
    public Map<MK, MV> metaGetMap(Triple<K1, K2, V> key) {
        return metaForValues.get(key);
    }

    /** gets the MetaValue associated with Quadruple<K1, K2, K3, V> for MetaKey mk */
    public MV metaGet(K1 k1, K2 k2, V v, MK mk) {
        return this.metaGet(Triple.of(k1, k2, v), mk);
    }

    /** gets the MetaValue associated with Quadruple<K1, K2, K3, V> for MetaKey mk */
    public MV metaGet(Triple<K1, K2, V> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.get(mk);
        }
        return null;
    }

    // ======= putMeta ===========================

    public MV metaPut(K1 k1, MK mk, MV mv) {
        if (super.rootMap.containsKey(k1)) {
            Map<MK, MV> metaMap = this.metaGetMap(k1);
            if (metaMap == null) {
                metaMap = metaMapClonePrototype.clone();
                this.metaForRoot.put(k1, metaMap);
            }
            return metaMap.put(mk, mv);
        }
        throw new NoSuchElementException(
                String.format("no rootKey '%s' to put meta-data '%s' on found", k1, mk));
    }

    public MV metaPut(K1 k1, K2 k2, MK mk, MV mv) {
        return this.metaPut(Pair.of(k1, k2), mk, mv);
    }

    public MV metaPut(Pair<K1, K2> key, MK mk, MV mv) {
        if (super.get(key.getLeft()).containsKey(key.getRight())) {
            Map<MK, MV> metaMap = this.metaGetMap(key);
            if (metaMap == null) {
                metaMap = metaMapClonePrototype.clone();
                this.metaForLevel2.put(key, metaMap);
            }
            return metaMap.put(mk, mv);
        }
        throw new NoSuchElementException(String.format(
                "no level2 key '%s' found for rootKey '%s' to put meta-data '%s' on found",
                key.getRight(), key.getLeft(), mk));
    }

    public MV metaPut(K1 k1, K2 k2, V v, MK mk, MV mv) {
        return this.metaPut(Triple.of(k1, k2, v), mk, mv);
    }

    public MV metaPut(Triple<K1, K2, V> key, MK mk, MV mv) {
        if (super.get(key.getLeft(), key.getMiddle()) != null) {
            Map<MK, MV> metaMap = this.metaGetMap(key);
            if (metaMap == null) {
                metaMap = metaMapClonePrototype.clone();
                this.metaForValues.put(key, metaMap);
            }
            return metaMap.put(mk, mv);
        }
        throw new NoSuchElementException(String.format(
                "no value '%s' found for rootKey '%s', level2 key '%s' to put meta-data '%s' on found",
                key.getRight(), key.getLeft(), key.getMiddle(), mk));
    }

    /** removes all meta-data for given root-key (meta-data on other levels will not be removed) */
    public Map<MK, MV> metaRemove(K1 k1) {
        return this.metaForRoot.remove(k1);
    }

    /** removes specific meta-data for given root-key */
    public MV metaRemoveKey(K1 k1, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public boolean metaRemoveValue(K1 k1, MK mk, MV mv) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            MV theMetaValue = metaMap.get(mk);
            if (mv.equals(theMetaValue)) {
                return metaMap.remove(mk, mv);
            }
        }
        return false;
    }

    /**
     * removes all meta-data for given K2 under root-key K1 (meta-data on other levels will not be
     * removed)
     */
    public Map<MK, MV> metaRemove(K1 k1, K2 k2) {
        return this.metaRemove(Pair.of(k1, k2));
    }

    public Map<MK, MV> metaRemove(Pair<K1, K2> key) {
        return this.metaForLevel2.get(key);
    }

    /** removes specific meta-data for given root-key */
    public MV metaRemoveKey(K1 k1, K2 k2, MK mk) {
        return this.metaRemoveKey(Pair.of(k1, k2), mk);
    }

    public MV metaRemoveKey(Pair<K1, K2> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public boolean metaRemoveValue(K1 k1, K2 k2, MK mk, MV mv) {
        return metaRemoveValue(Pair.of(k1, k2), mk, mv);
    }

    public boolean metaRemoveValue(Pair<K1, K2> key, MK mk, MV mv) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.remove(mk, mv);
        }
        return false;
    }

    public Map<MK, MV> metaRemove(K1 k1, K2 k2, V v) {
        return this.metaRemove(Triple.of(k1, k2, v));
    }

    public Map<MK, MV> metaRemove(Triple<K1, K2, V> key) {
        return metaForValues.remove(key);
    }

    public MV metaRemoveKey(K1 k1, K2 k2, V v, MK mk) {
        return this.metaRemoveKey(Triple.of(k1, k2, v), mk);
    }

    public MV metaRemoveKey(Triple<K1, K2, V> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public boolean metaRemoveValue(K1 k1, K2 k2, V v, MK mk, MV mv) {
        return this.metaRemoveValue(Triple.of(k1, k2, v), mk, mv);
    }

    public boolean metaRemoveValue(Triple<K1, K2, V> key, MK mk, MV mv) {
        Map<MK, MV> metaMap = metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk, mv);
        }
        return false;
    }

    /** remove all metadata on each level if given key is the root-key of that meta-data */
    public Map<MK, MV> metaRemoveAll(K1 k1) {
        List<Triple<K1, K2, V>> keysToRemoveV =
                this.metaForValues.entrySet().stream().filter(e -> e.getKey().getLeft().equals(k1))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        List<Pair<K1, K2>> keysToRemoveL2 =
                this.metaForLevel2.entrySet().stream().filter(e -> e.getKey().getLeft().equals(k1))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        keysToRemoveV.forEach(k -> this.metaForValues.remove(k));
        keysToRemoveL2.forEach(k -> this.metaForLevel2.remove(k));
        return this.metaRemove(k1);
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2 of that meta-data
     */
    public Map<MK, MV> metaRemoveAll(Pair<K1, K2> key) {
        return metaRemoveAll(key.getLeft(), key.getRight());
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2 of that meta-data
     */
    public Map<MK, MV> metaRemoveAll(K1 k1, K2 k2) {
        List<Triple<K1, K2, V>> keysToRemoveV = this.metaForValues.entrySet().stream()
                .filter(e -> e.getKey().getLeft().equals(k1) && e.getKey().getMiddle().equals(k2))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        List<Pair<K1, K2>> keysToRemoveL2 = this.metaForLevel2.entrySet().stream()
                .filter(e -> e.getKey().getLeft().equals(k1) && e.getKey().getRight().equals(k2))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        keysToRemoveV.forEach(k -> this.metaForValues.remove(k));
        keysToRemoveL2.forEach(k -> this.metaForLevel2.remove(k));
        return this.metaRemove(Pair.of(k1, k2));
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2/level3/Value of
     * that meta-data
     */
    public Map<MK, MV> metaRemoveAll(K1 k1, K2 k2, V v) {
        return this.metaRemoveAll(Triple.of(k1, k2, v));
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2/level3/Value of
     * that meta-data
     */
    public Map<MK, MV> metaRemoveAll(Triple<K1, K2, V> key) {
        return metaRemove(key.getLeft(), key.getMiddle(), key.getRight());
    }

    public boolean metaContains(K1 k1, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            return metaMap.containsKey(mk);
        }
        return false;
    }

    public boolean metaContains(K1 k1, K2 k2, MK mk) {
        return metaContains(Pair.of(k1, k2), mk);
    }

    public boolean metaContains(Pair<K1, K2> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.containsKey(mk);
        }
        return false;
    }

    public boolean metaContains(K1 k1, K2 k2, V v, MK mk) {
        return metaContains(Triple.of(k1, k2, v), mk);
    }

    public boolean metaContains(Triple<K1, K2, V> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.containsKey(mk);
        }
        return false;
    }


    public void metaClearMapValues(K1 k1) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            metaMap.clear();
        }
    }

    public void metaClearMapValues(K1 k1, K2 k2) {
        metaClearMapValues(Pair.of(k1, k2));
    }

    public void metaClearMapValues(Pair<K1, K2> key) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.clear();
        }
    }

    public void metaClearMapValues(K1 k1, K2 k2, V v) {
        metaGetMap(Triple.of(k1, k2, v));
    }

    public void metaClearMapValues(Triple<K1, K2, V> key) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.clear();
        }
    }

    public void metaMergeMapValues(K1 k1, Map<MK, MV> toMergeMetaMap) {
        Map<MK, MV> metaMap = this.metaGetMap(k1);
        if (metaMap != null) {
            metaMap.putAll(toMergeMetaMap);
        }
    }

    public void metaMergeMapValues(K1 k1, K2 k2, Map<MK, MV> toMergeMetaMap) {
        metaMergeMapValues(Pair.of(k1, k2), toMergeMetaMap);
    }

    public void metaMergeMapValues(Pair<K1, K2> key, Map<MK, MV> toMergeMetaMap) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.putAll(toMergeMetaMap);
        }
    }

    public void metaMergeMapValues(K1 k1, K2 k2, V v, Map<MK, MV> toMergeMetaMap) {
        metaMergeMapValues(Triple.of(k1, k2, v), toMergeMetaMap);
    }

    public void metaMergeMapValues(Triple<K1, K2, V> key, Map<MK, MV> toMergeMetaMap) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.putAll(toMergeMetaMap);
        }
    }

    public void metaReplaceMapValues(K1 k1, Map<MK, MV> metaMap) {
        metaClearMapValues(k1);
        metaMergeMapValues(k1, metaMap);
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, Map<MK, MV> metaMap) {
        metaReplaceMapValues(Pair.of(k1, k2), metaMap);
    }

    public void metaReplaceMapValues(Pair<K1, K2> key, Map<MK, MV> metaMap) {
        metaClearMapValues(key);
        metaMergeMapValues(key, metaMap);
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, V v, Map<MK, MV> metaMap) {
        metaReplaceMapValues(Triple.of(k1, k2, v), metaMap);
    }

    public void metaReplaceMapValues(Triple<K1, K2, V> key, Map<MK, MV> metaMap) {
        metaClearMapValues(key);
        metaMergeMapValues(key, metaMap);
    }


    // ======= findOneByMeta ===========================

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for
     * root-level keys and drilling down to value level
     */
    public Triple<K1, K2, V> findFirstInAllByMeta(MK mk) {
        K1 rootKey = findFirstInRootByMeta(mk);
        if (rootKey != null) {
            return Triple.of(rootKey, null, null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMeta(mk);
        if (level2Key != null) {
            return Triple.of(level2Key.getLeft(), level2Key.getRight(), null);
        }
        return findFirstInValuesByMeta(mk);
    }

    /**
     * find first key that matches meta-key starting search on metadata for values and then
     * searchingup to root key level
     */
    public Triple<K1, K2, V> findLeastInAllByMeta(MK mk) {
        Triple<K1, K2, V> valueKey = findFirstInValuesByMeta(mk);
        if (valueKey != null) {
            return valueKey;
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMeta(mk);
        if (level2Key != null) {
            return Triple.of(level2Key.getLeft(), level2Key.getRight(), null);
        }
        K1 rootKey = findFirstInRootByMeta(mk);
        if (rootKey != null) {
            return Triple.of(rootKey, null, null);
        }
        return Triple.of(null, null, null);
    }

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for
     * root-level keys and drilling down to value level
     */
    public Triple<K1, K2, V> findFirstInAllByMetaValue(MK mk, MV mv) {
        K1 rootKey = findFirstinRootByMetaValue(mk, mv);
        if (rootKey != null) {
            return Triple.of(rootKey, null, null);
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMetaValue(mk, mv);
        if (level2Key != null) {
            return Triple.of(level2Key.getLeft(), level2Key.getRight(), null);
        }
        return findFirstInValuesByMetaValue(mk, mv);
    }

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for values
     * and then searchingup to root key level
     */
    public Triple<K1, K2, V> findLeastInAllByMetaValue(MK mk, MV mv) {
        Triple<K1, K2, V> valueKey = findFirstInValuesByMetaValue(mk, mv);
        if (valueKey != null) {
            return valueKey;
        }
        Pair<K1, K2> level2Key = findFirstInLevel2ByMetaValue(mk, mv);
        if (level2Key != null) {
            return Triple.of(level2Key.getLeft(), level2Key.getRight(), null);
        }
        K1 rootKey = findFirstinRootByMetaValue(mk, mv);
        if (rootKey != null) {
            return Triple.of(rootKey, null, null);
        }
        return Triple.of(null, null, null);
    }

    public K1 findFirstInRootByMeta(MK mk) {
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                return rootEntry.getKey();
            }
        }
        return null;
    }

    public K1 findFirstinRootByMetaValue(MK mk, MV mv) {
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.entrySet()) {
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
        for (Entry<Pair<K1, K2>, Map<MK, MV>> entry : metaForLevel2.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Pair<K1, K2> findFirstInLevel2ByMetaValue(MK mk, MV mv) {
        for (Entry<Pair<K1, K2>, Map<MK, MV>> entry : metaForLevel2.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    return entry.getKey();
                } else {
                    break;
                }
            }
        }
        return null;
    }

    public Triple<K1, K2, V> findFirstInValuesByMeta(MK mk) {
        for (Entry<Triple<K1, K2, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Triple<K1, K2, V> findFirstInValuesByMetaValue(MK mk, MV mv) {
        for (Entry<Triple<K1, K2, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    return entry.getKey();
                } else {
                    break;
                }
            }
        }
        return null;
    }

    // ======= findAllByMeta ===========================

    public List<Triple<K1, K2, V>> findAllInAllByMeta(MK mk) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        List<K1> rootKeyList = findAllInRootByMeta(mk);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Triple.of(rootKey, null, null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMeta(mk);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Triple.of(level2Key.getLeft(), level2Key.getRight(), null));
        }
        List<Triple<K1, K2, V>> levelValuesKeyList = findAllInValuesByMeta(mk);
        resultList.addAll(levelValuesKeyList);

        return resultList;
    }

    public List<Triple<K1, K2, V>> findAllInAllByMetaValue(MK mk, MV mv) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        List<K1> rootKeyList = findAllinRootByMetaValue(mk, mv);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Triple.of(rootKey, null, null));
        }
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMetaValue(mk, mv);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Triple.of(level2Key.getLeft(), level2Key.getRight(), null));
        }
        List<Triple<K1, K2, V>> levelValuesKeyList = findAllInValuesByMetaValue(mk, mv);
        resultList.addAll(levelValuesKeyList);

        return resultList;
    }

    public List<Triple<K1, K2, V>> findAllInAllByMetaReverse(MK mk) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        List<Triple<K1, K2, V>> levelValuesKeyList = findAllInValuesByMeta(mk);
        resultList.addAll(levelValuesKeyList);
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMeta(mk);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Triple.of(level2Key.getLeft(), level2Key.getRight(), null));
        }
        List<K1> rootKeyList = findAllInRootByMeta(mk);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Triple.of(rootKey, null, null));
        }

        return resultList;
    }

    public List<Triple<K1, K2, V>> findAllInAllByMetaValueReverse(MK mk, MV mv) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        List<Triple<K1, K2, V>> levelValuesKeyList = findAllInValuesByMetaValue(mk, mv);
        resultList.addAll(levelValuesKeyList);
        List<Pair<K1, K2>> level2KeyList = findAllInLevel2ByMetaValue(mk, mv);
        for (Pair<K1, K2> level2Key : level2KeyList) {
            resultList.add(Triple.of(level2Key.getLeft(), level2Key.getRight(), null));
        }
        List<K1> rootKeyList = findAllinRootByMetaValue(mk, mv);
        for (K1 rootKey : rootKeyList) {
            resultList.add(Triple.of(rootKey, null, null));
        }

        return resultList;
    }


    public List<K1> findAllInRootByMeta(MK mk) {
        List<K1> resultList = new ArrayList<>();
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.entrySet()) {
            if (rootEntry.getValue().containsKey(mk)) {
                resultList.add(rootEntry.getKey());
            }
        }
        return resultList;
    }

    public List<K1> findAllinRootByMetaValue(MK mk, MV mv) {
        List<K1> resultList = new ArrayList<>();
        for (Entry<K1, Map<MK, MV>> rootEntry : metaForRoot.entrySet()) {
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
        for (Entry<Pair<K1, K2>, Map<MK, MV>> entry : metaForLevel2.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                resultList.add(entry.getKey());
            }
        }
        return resultList;
    }

    public List<Pair<K1, K2>> findAllInLevel2ByMetaValue(MK mk, MV mv) {
        List<Pair<K1, K2>> resultList = new ArrayList<>();
        for (Entry<Pair<K1, K2>, Map<MK, MV>> entry : metaForLevel2.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    resultList.add(entry.getKey());
                }
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, V>> findAllInValuesByMeta(MK mk) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        for (Entry<Triple<K1, K2, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                resultList.add(entry.getKey());
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, V>> findAllInValuesByMetaValue(MK mk, MV mv) {
        List<Triple<K1, K2, V>> resultList = new ArrayList<>();
        for (Entry<Triple<K1, K2, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    resultList.add(entry.getKey());
                }
            }
        }
        return resultList;
    }


    // ======= iterators ======================================================
    // ======= for iterating the individual key levels ========================
    // ======= use the iterators of the public fields of this class directly ==
    // ========================================================================

    enum STATE {
        ROOTITER, L2ITER, LVITER
    };
    class HashMap3LWithMetaIteratorBase {
        Iterator<Entry<MK, MV>> entryIter = null;
        Iterator<Entry<K1, Map<MK, MV>>> rootIter =
                HashMap2LWithMeta.this.metaForRoot.entrySet().iterator();
        Iterator<Entry<Pair<K1, K2>, Map<MK, MV>>> level2Iter =
                HashMap2LWithMeta.this.metaForLevel2.entrySet().iterator();
        Iterator<Entry<Triple<K1, K2, V>, Map<MK, MV>>> levelVIter =
                HashMap2LWithMeta.this.metaForValues.entrySet().iterator();

        Quintuple<K1, K2, V, MK, MV> next = null; // next entry to return
        Quintuple<K1, K2, V, MK, MV> current = null; // current entry
        STATE state = STATE.ROOTITER;
        Quintuple<K1, K2, V, MK, MV> context = null;
    }

    class HashMap3LWithMetaIteratorBreadthFirst extends HashMap3LWithMetaIteratorBase
            implements Iterator<Quintuple<K1, K2, V, MK, MV>> {

        HashMap3LWithMetaIteratorBreadthFirst() {
            next = findNextBreadth();
        }


        Quintuple<K1, K2, V, MK, MV> findNextBreadth() {
            next = null;
            while (next == null && state == STATE.ROOTITER
                    && ((entryIter != null && entryIter.hasNext()) || rootIter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Quintuple.of(context.root, null, null, entry.getKey(),
                            entry.getValue());
                    break;
                } else if (rootIter.hasNext()) {
                    Entry<K1, Map<MK, MV>> rootMeta = rootIter.next();
                    entryIter = rootMeta.getValue().entrySet().iterator();
                    context = Quintuple.of(rootMeta.getKey(), null, null, null, null);
                }
            }
            if (next == null && state == STATE.ROOTITER) {
                context = Quintuple.nullQuintuple();
                state = STATE.L2ITER;
            }

            while (next == null && state == STATE.L2ITER
                    && ((entryIter != null && entryIter.hasNext()) || level2Iter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Quintuple.of(context.root, context.l2, null, entry.getKey(), entry.getValue());
                    break;
                } else if (level2Iter.hasNext()) {
                    Entry<Pair<K1, K2>, Map<MK, MV>> keyMeta = level2Iter.next();
                    entryIter = keyMeta.getValue().entrySet().iterator();
                    context = Quintuple.of(keyMeta.getKey().getLeft(), keyMeta.getKey().getRight(), null, null, null);
                }
            }
            if (next == null && state == STATE.L2ITER) {
                state = STATE.LVITER;
            }
            while (next == null && state == STATE.LVITER
                    && ((entryIter != null && entryIter.hasNext()) || levelVIter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Quintuple.of(context.root, context.l2, context.l3, entry.getKey(), entry.getValue());
                    break;
                } else if (levelVIter.hasNext()) {
                    Entry<Triple<K1, K2, V>, Map<MK, MV>> keyMeta = levelVIter.next();
                    entryIter = keyMeta.getValue().entrySet().iterator();
                    context = Quintuple.of(keyMeta.getKey().getLeft(), keyMeta.getKey().getMiddle(), keyMeta.getKey().getRight(), null, null);
                }
            }
            return next;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Quintuple<K1, K2, V, MK, MV> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Quintuple<K1, K2, V, MK, MV> toReturn = next; // new next will be computed now ...
            current = next;
            next = findNextBreadth();
            return toReturn;
        }

        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    public Iterator<Quintuple<K1, K2, V, MK, MV>> iteratorMetaBreadthFirst() {
        return new HashMap3LWithMetaIteratorBreadthFirst();
    }

    public Iterator<Quintuple<K1, K2, V, MK, MV>> iteratorMetaBreadthFirstReverse() {
        throw new NotImplementedException("XXX sorry");
    }

    class HashMap3LWithMetaIteratorDepthFirst extends HashMap3LWithMetaIteratorBase
            implements Iterator<Quintuple<K1, K2, V, MK, MV>> {

        HashMap3LWithMetaIteratorDepthFirst() {
            next = findNextDepth();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Quintuple<K1, K2, V, MK, MV> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Quintuple<K1, K2, V, MK, MV> toReturn = next; // new next will be computed now ...
            current = next;
            next = findNextDepth();
            return toReturn;
        }

        Quintuple<K1, K2, V, MK, MV> findNextDepth() {
            next = null;
            while (next == null) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Quintuple.of(context.root, context.l2, context.l3, entry.getKey(), entry.getValue());
                    break;
                } else if (levelVIter.hasNext()) {
                    Entry<Triple<K1, K2, V>, Map<MK, MV>> levelVMeta = levelVIter.next();
                    Triple<K1, K2, V> key = levelVMeta.getKey();
                    context = Quintuple.of(key.getLeft(), key.getMiddle(), key.getRight(), null, null);
                    Map<MK, MV> metaMap = levelVMeta.getValue();
                    if(metaMap != null) {
                        entryIter = metaMap.entrySet().iterator();
                        continue;
                    }
                } else if (level2Iter.hasNext()) {
                    Entry<Pair<K1, K2>, Map<MK, MV>> level2Meta = level2Iter.next();
                    Pair<K1, K2> key = level2Meta.getKey();
                    context = Quintuple.of(key.getLeft(), key.getRight(), null, null, null);
                    Map<MK, MV> metaMap = level2Meta.getValue();
                    if(metaMap != null) {
                        entryIter = metaMap.entrySet().iterator();
                        continue;
                    }
                } else if (rootIter.hasNext()) {
                    Entry<K1, Map<MK, MV>> rootMeta = rootIter.next();
                    context = Quintuple.of(rootMeta.getKey(), null, null, null, null);
                    Map<MK, MV> metaMap = rootMeta.getValue();
                    if(metaMap != null) {
                        entryIter = rootMeta.getValue().entrySet().iterator();
                    }
                } else {
                    break;
                }
            }

            return next;
        }

        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    public Iterator<Quintuple<K1, K2, V, MK, MV>> iteratorMetaDepthFirst() {
        return new HashMap3LWithMetaIteratorDepthFirst();
    }

    public Iterator<Quintuple<K1, K2, V, MK, MV>> iteratorMetaDepthFirstReverse() {
        throw new NotImplementedException("XXX sorry");
    }


    // ========================================================================
    // ==== Override parent methods to deal with Meta-Data accordingly ======
    // ========================================================================

    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public Map<K2, V> remove(K1 k1) {
        this.metaRemoveAll(k1);
        return super.remove(k1);
    }

    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public V remove(K1 k1, K2 k2) {
        this.metaRemoveAll(k1, k2);
        return super.remove(k1, k2);
    }

    @Override
    public V removeValue(K1 k1, K2 k2, V v) {
        this.metaRemoveAll(k1, k2, v);
        return super.removeValue(k1, k2, v);
    }

    @Override
    public void clear() {
        this.metaForRoot.clear();
        this.metaForLevel2.clear();
        this.metaForValues.clear();
        super.clear();
    }

    @Override
    public void clear(K1 k1) {
        this.metaClearMapValues(k1);
        super.clear(k1);
    }
}
