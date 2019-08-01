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
import helpers.tuple.Quadruple;
import helpers.tuple.Sextuple;

public class HashMap3LWithMeta<K1, K2, K3, V, MK, MV> extends HashMap3L<K1, K2, K3, V> {

    public enum METAKEYSORT {
        HASH, TREE, LINKEDHASH
    };

    /** meta-data maps for each level */
    public Map<K1, Map<MK, MV>> metaForRoot;
    public Map<Pair<K1, K2>, Map<MK, MV>> metaForLevel2;
    public Map<Triple<K1, K2, K3>, Map<MK, MV>> metaForLevel3;
    public Map<Quadruple<K1, K2, K3, V>, Map<MK, MV>> metaForValues;

    /**
     * prototype of meta-map to clone meta-data from (for being able to use different Map
     * implementation types)
     */
    protected MapCloneable<MK, MV> metaMapClonePrototype;

    /** public constructor */
    public HashMap3LWithMeta(
            // maps for real values of extended HashMap3L
            MapCloneable<K1, Map<K2, Map<K3, V>>> rootMapClonePrototype,
            MapCloneable<K2, Map<K3, V>> level2MapClonePrototype,
            MapCloneable<K3, V> level3MapClonePrototype) {
        this(rootMapClonePrototype, level2MapClonePrototype, level3MapClonePrototype,
                METAKEYSORT.HASH);
    }

    /** public constructor */
    public HashMap3LWithMeta(
            // maps for real values of extended HashMap3L
            MapCloneable<K1, Map<K2, Map<K3, V>>> rootMapClonePrototype,
            MapCloneable<K2, Map<K3, V>> level2MapClonePrototype,
            MapCloneable<K3, V> level3MapClonePrototype, METAKEYSORT metakeysort) {
        super(rootMapClonePrototype, level2MapClonePrototype, level3MapClonePrototype);
        switch (metakeysort) {
            case HASH:
                this.metaForRoot = new HashMap<>();
                this.metaForLevel2 = new HashMap<>();
                this.metaForLevel3 = new HashMap<>();
                this.metaForValues = new HashMap<>();
                this.metaMapClonePrototype = new HashMapCloneable<>();
                break;
            case TREE:
                this.metaForRoot = new TreeMap<>();
                this.metaForLevel2 = new TreeMap<>();
                this.metaForLevel3 = new TreeMap<>();
                this.metaForValues = new TreeMap<>();
                this.metaMapClonePrototype = new TreeMapCloneable<>();
                break;
            case LINKEDHASH:
                this.metaForRoot = new LinkedHashMap<>();
                this.metaForLevel2 = new LinkedHashMap<>();
                this.metaForLevel3 = new LinkedHashMap<>();
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

    /** gets the whole Map of MetaKeys to MetaValues for Triple<K1, K2, K3> */
    public Map<MK, MV> metaGetMap(K1 k1, K2 k2, K3 k3) {
        return this.metaGetMap(Triple.of(k1, k2, k3));
    }

    /** gets the whole Map of MetaKeys to MetaValues for Triple<K1, K2, K3> */
    public Map<MK, MV> metaGetMap(Triple<K1, K2, K3> key) {
        return metaForLevel3.get(key);
    }

    /** gets the MetaValue associated with Triple<K1, K2, K3> for MetaKey mk */
    public MV metaGet(K1 k1, K2 k2, K3 k3, MK mk) {
        return this.metaGet(Triple.of(k1, k2, k3), mk);
    }

    /** gets the MetaValue associated with Triple<K1, K2, K3> for MetaKey mk */
    public MV metaGet(Triple<K1, K2, K3> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.get(mk);
        }
        return null;
    }

    /** gets the whole Map of MetaKeys to MetaValues for Quadruple<K1, K2, K3, V> */
    public Map<MK, MV> metaGetMap(K1 k1, K2 k2, K3 k3, V v) {
        return this.metaGetMap(Quadruple.of(k1, k2, k3, v));
    }

    /** gets the whole Map of MetaKeys to MetaValues for Quadruple<K1, K2, K3, V> */
    public Map<MK, MV> metaGetMap(Quadruple<K1, K2, K3, V> key) {
        return metaForValues.get(key);
    }

    /** gets the MetaValue associated with Quadruple<K1, K2, K3, V> for MetaKey mk */
    public MV metaGet(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        return this.metaGet(Quadruple.of(k1, k2, k3, v), mk);
    }

    /** gets the MetaValue associated with Quadruple<K1, K2, K3, V> for MetaKey mk */
    public MV metaGet(Quadruple<K1, K2, K3, V> key, MK mk) {
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

    public MV metaPut(K1 k1, K2 k2, K3 k3, MK mk, MV mv) {
        return this.metaPut(Triple.of(k1, k2, k3), mk, mv);
    }

    public MV metaPut(Triple<K1, K2, K3> key, MK mk, MV mv) {
        if (super.get(key.getLeft(), key.getMiddle()).containsKey(key.getRight())) {
            Map<MK, MV> metaMap = this.metaGetMap(key);
            if (metaMap == null) {
                metaMap = metaMapClonePrototype.clone();
                this.metaForLevel3.put(key, metaMap);
            }
            return metaMap.put(mk, mv);
        }
        throw new NoSuchElementException(String.format(
                "no level3 key '%s' found for rootKey '%s' and level2 key '%s' to put meta-data '%s' on found",
                key.getRight(), key.getLeft(), key.getMiddle(), mk));
    }

    public MV metaPut(K1 k1, K2 k2, K3 k3, V v, MK mk, MV mv) {
        return this.metaPut(Quadruple.of(k1, k2, k3, v), mk, mv);
    }

    public MV metaPut(Quadruple<K1, K2, K3, V> key, MK mk, MV mv) {
        if (super.get(key.getRoot(), key.getL2(), key.getL3()) != null) {
            Map<MK, MV> metaMap = this.metaGetMap(key);
            if (metaMap == null) {
                metaMap = metaMapClonePrototype.clone();
                this.metaForValues.put(key, metaMap);
            }
            return metaMap.put(mk, mv);
        }
        throw new NoSuchElementException(String.format(
                "no value '%s' found for rootKey '%s', level2 key '%s' and level3 key '%s' to put meta-data '%s' on found",
                key.getLeaf(), key.getRoot(), key.getL2(), key.getL3(), mk));
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

    public Map<MK, MV> metaRemove(K1 k1, K2 k2, K3 k3) {
        return this.metaRemove(Triple.of(k1, k2, k3));
    }

    public Map<MK, MV> metaRemove(Triple<K1, K2, K3> key) {
        return metaForLevel3.remove(key);
    }

    public MV metaRemoveKey(K1 k1, K2 k2, K3 k3, MK mk) {
        return this.metaRemoveKey(Triple.of(k1, k2, k3), mk);
    }

    public MV metaRemoveKey(Triple<K1, K2, K3> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public boolean metaRemoveValue(K1 k1, K2 k2, K3 k3, MK mk, MV mv) {
        return this.metaRemoveValue(Triple.of(k1, k2, k3), mk, mv);
    }

    public boolean metaRemoveValue(Triple<K1, K2, K3> key, MK mk, MV mv) {
        Map<MK, MV> metaMap = metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk, mv);
        }
        return false;
    }

    public Map<MK, MV> metaRemove(K1 k1, K2 k2, K3 k3, V v) {
        return this.metaRemove(Quadruple.of(k1, k2, k3, v));
    }

    public Map<MK, MV> metaRemove(Quadruple<K1, K2, K3, V> key) {
        return metaForValues.remove(key);
    }

    public MV metaRemoveKey(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        return this.metaRemoveKey(Quadruple.of(k1, k2, k3, v), mk);
    }

    public MV metaRemoveKey(Quadruple<K1, K2, K3, V> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk);
        }
        return null;
    }

    public boolean metaRemoveValue(K1 k1, K2 k2, K3 k3, V v, MK mk, MV mv) {
        return this.metaRemoveValue(Quadruple.of(k1, k2, k3, v), mk, mv);
    }

    public boolean metaRemoveValue(Quadruple<K1, K2, K3, V> key, MK mk, MV mv) {
        Map<MK, MV> metaMap = metaGetMap(key);
        if (metaMap != null) {
            return metaMap.remove(mk, mv);
        }
        return false;
    }

    /** remove all metadata on each level if given key is the root-key of that meta-data */
    public Map<MK, MV> metaRemoveAll(K1 k1) {
        List<Quadruple<K1, K2, K3, V>> keysToRemoveV =
                this.metaForValues.entrySet().stream().filter(e -> e.getKey().getRoot().equals(k1))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        List<Triple<K1, K2, K3>> keysToRemoveL3 =
                this.metaForLevel3.entrySet().stream().filter(e -> e.getKey().getLeft().equals(k1))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        List<Pair<K1, K2>> keysToRemoveL2 =
                this.metaForLevel2.entrySet().stream().filter(e -> e.getKey().getLeft().equals(k1))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        keysToRemoveV.forEach(k -> this.metaForValues.remove(k));
        keysToRemoveL3.forEach(k -> this.metaForLevel3.remove(k));
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
        List<Quadruple<K1, K2, K3, V>> keysToRemoveV = this.metaForValues.entrySet().stream()
                .filter(e -> e.getKey().getRoot().equals(k1) && e.getKey().getL2().equals(k2))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        List<Triple<K1, K2, K3>> keysToRemoveL3 = this.metaForLevel3.entrySet().stream()
                .filter(e -> e.getKey().getLeft().equals(k1) && e.getKey().getMiddle().equals(k2))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        keysToRemoveV.forEach(k -> this.metaForValues.remove(k));
        keysToRemoveL3.forEach(k -> this.metaForLevel3.remove(k));
        return this.metaRemove(Pair.of(k1, k2));
    }

    /**
     * remove all metadata on each level if given key Triple is the root-key/level2/level3 of that
     * meta-data
     */
    public Map<MK, MV> metaRemoveAll(Triple<K1, K2, K3> key) {
        return metaRemoveAll(key.getLeft(), key.getMiddle(), key.getRight());
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2/level3 of that
     * meta-data
     */
    public Map<MK, MV> metaRemoveAll(K1 k1, K2 k2, K3 k3) {
        List<Quadruple<K1, K2, K3, V>> keysToRemoveV = this.metaForValues.entrySet().stream()
                .filter(e -> e.getKey().getRoot().equals(k1) && e.getKey().getL2().equals(k2)
                        && e.getKey().getL3().equals(k3))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        keysToRemoveV.forEach(k -> this.metaForValues.remove(k));
        return this.metaRemove(Triple.of(k1, k2, k3));
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2/level3/Value of
     * that meta-data
     */
    public Map<MK, MV> metaRemoveAll(K1 k1, K2 k2, K3 k3, V v) {
        return this.metaRemoveAll(Quadruple.of(k1, k2, k3, v));
    }

    /**
     * remove all metadata on each level if given key Pair is the root-key/level2/level3/Value of
     * that meta-data
     */
    public Map<MK, MV> metaRemoveAll(Quadruple<K1, K2, K3, V> key) {
        return metaRemove(key.getRoot(), key.getL2(), key.getL3(), key.getLeaf());
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

    public boolean metaContains(K1 k1, K2 k2, K3 k3, MK mk) {
        return metaContains(Triple.of(k1, k2, k3), mk);
    }

    public boolean metaContains(Triple<K1, K2, K3> key, MK mk) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            return metaMap.containsKey(mk);
        }
        return false;
    }

    public boolean metaContains(K1 k1, K2 k2, K3 k3, V v, MK mk) {
        return metaContains(Quadruple.of(k1, k2, k3, v), mk);
    }

    public boolean metaContains(Quadruple<K1, K2, K3, V> key, MK mk) {
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

    public void metaClearMapValues(K1 k1, K2 k2, K3 k3) {
        metaClearMapValues(Triple.of(k1, k2, k3));
    }

    public void metaClearMapValues(Triple<K1, K2, K3> key) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.clear();
        }
    }

    public void metaClearMapValues(K1 k1, K2 k2, K3 k3, V v) {
        metaGetMap(Quadruple.of(k1, k2, k3, v));
    }

    public void metaClearMapValues(Quadruple<K1, K2, K3, V> key) {
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

    public void metaMergeMapValues(K1 k1, K2 k2, K3 k3, Map<MK, MV> toMergeMetaMap) {
        metaMergeMapValues(Triple.of(k1, k2, k3), toMergeMetaMap);
    }

    public void metaMergeMapValues(Triple<K1, K2, K3> key, Map<MK, MV> toMergeMetaMap) {
        Map<MK, MV> metaMap = this.metaGetMap(key);
        if (metaMap != null) {
            metaMap.putAll(toMergeMetaMap);
        }
    }

    public void metaMergeMapValues(K1 k1, K2 k2, K3 k3, V v, Map<MK, MV> toMergeMetaMap) {
        metaMergeMapValues(Quadruple.of(k1, k2, k3, v), toMergeMetaMap);
    }

    public void metaMergeMapValues(Quadruple<K1, K2, K3, V> key, Map<MK, MV> toMergeMetaMap) {
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

    public void metaReplaceMapValues(K1 k1, K2 k2, K3 k3, Map<MK, MV> metaMap) {
        metaReplaceMapValues(Triple.of(k1, k2, k3), metaMap);
    }

    public void metaReplaceMapValues(Triple<K1, K2, K3> key, Map<MK, MV> metaMap) {
        metaClearMapValues(key);
        metaMergeMapValues(key, metaMap);
    }

    public void metaReplaceMapValues(K1 k1, K2 k2, K3 k3, V v, Map<MK, MV> metaMap) {
        metaReplaceMapValues(Quadruple.of(k1, k2, k3, v), metaMap);
    }

    public void metaReplaceMapValues(Quadruple<K1, K2, K3, V> key, Map<MK, MV> metaMap) {
        metaClearMapValues(key);
        metaMergeMapValues(key, metaMap);
    }


    // ======= findOneByMeta ===========================

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for
     * root-level keys and drilling down to value level
     */
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
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(),
                    null);
        }
        return findFirstInValuesByMeta(mk);
    }

    /**
     * find first key that matches meta-key starting search on metadata for values and then
     * searchingup to root key level
     */
    public Quadruple<K1, K2, K3, V> findLeastInAllByMeta(MK mk) {
        Quadruple<K1, K2, K3, V> valueKey = findFirstInValuesByMeta(mk);
        if (valueKey != null) {
            return valueKey;
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMeta(mk);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(),
                    null);
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

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for
     * root-level keys and drilling down to value level
     */
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
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(),
                    null);
        }
        return findFirstInValuesByMetaValue(mk, mv);
    }

    /**
     * find first key that matches meta-key and meta-value starting search on metadata for values
     * and then searchingup to root key level
     */
    public Quadruple<K1, K2, K3, V> findLeastInAllByMetaValue(MK mk, MV mv) {
        Quadruple<K1, K2, K3, V> valueKey = findFirstInValuesByMetaValue(mk, mv);
        if (valueKey != null) {
            return valueKey;
        }
        Triple<K1, K2, K3> level3Key = findFirstInLevel3ByMetaValue(mk, mv);
        if (level3Key != null) {
            return Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(), level3Key.getRight(),
                    null);
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

    public Triple<K1, K2, K3> findFirstInLevel3ByMeta(MK mk) {
        for (Entry<Triple<K1, K2, K3>, Map<MK, MV>> entry : metaForLevel3.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Triple<K1, K2, K3> findFirstInLevel3ByMetaValue(MK mk, MV mv) {
        for (Entry<Triple<K1, K2, K3>, Map<MK, MV>> entry : metaForLevel3.entrySet()) {
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

    public Quadruple<K1, K2, K3, V> findFirstInValuesByMeta(MK mk) {
        for (Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Quadruple<K1, K2, K3, V> findFirstInValuesByMetaValue(MK mk, MV mv) {
        for (Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
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
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(),
                    level3Key.getRight(), null));
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
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(),
                    level3Key.getRight(), null));
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
            resultList.add(Quadruple.of(level3Key.getLeft(), level3Key.getMiddle(),
                    level3Key.getRight(), null));
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
                } else {
                    break;
                }
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, K3>> findAllInLevel3ByMeta(MK mk) {
        List<Triple<K1, K2, K3>> resultList = new ArrayList<>();
        for (Entry<Triple<K1, K2, K3>, Map<MK, MV>> entry : metaForLevel3.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                resultList.add(entry.getKey());
            }
        }
        return resultList;
    }

    public List<Triple<K1, K2, K3>> findAllInLevel3ByMetaValue(MK mk, MV mv) {
        List<Triple<K1, K2, K3>> resultList = new ArrayList<>();
        for (Entry<Triple<K1, K2, K3>, Map<MK, MV>> entry : metaForLevel3.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    resultList.add(entry.getKey());
                } else {
                    break;
                }
            }
        }
        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInValuesByMeta(MK mk) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        for (Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                resultList.add(entry.getKey());
            }
        }
        return resultList;
    }

    public List<Quadruple<K1, K2, K3, V>> findAllInValuesByMetaValue(MK mk, MV mv) {
        List<Quadruple<K1, K2, K3, V>> resultList = new ArrayList<>();
        for (Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> entry : metaForValues.entrySet()) {
            if (entry.getValue().containsKey(mk)) {
                if (mv.equals(entry.getValue().get(mk))) {
                    resultList.add(entry.getKey());
                } else {
                    break;
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
        ROOTITER, L2ITER, L3ITER, LVITER
    };
    class HashMap3LWithMetaIteratorBase {
        Iterator<Entry<MK, MV>> entryIter = null;
        Iterator<Entry<K1, Map<MK, MV>>> rootIter =
                HashMap3LWithMeta.this.metaForRoot.entrySet().iterator();
        Iterator<Entry<Pair<K1, K2>, Map<MK, MV>>> level2Iter =
                HashMap3LWithMeta.this.metaForLevel2.entrySet().iterator();
        Iterator<Entry<Triple<K1, K2, K3>, Map<MK, MV>>> level3Iter =
                HashMap3LWithMeta.this.metaForLevel3.entrySet().iterator();
        Iterator<Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>>> levelVIter =
                HashMap3LWithMeta.this.metaForValues.entrySet().iterator();

        Sextuple<K1, K2, K3, V, MK, MV> next = null; // next entry to return
        Sextuple<K1, K2, K3, V, MK, MV> current = null; // current entry
        STATE state = STATE.ROOTITER;
        Sextuple<K1, K2, K3, V, MK, MV> context = null;
    }

    class HashMap3LWithMetaIteratorBreadthFirst extends HashMap3LWithMetaIteratorBase
            implements Iterator<Sextuple<K1, K2, K3, V, MK, MV>> {

        HashMap3LWithMetaIteratorBreadthFirst() {
            next = findNextBreadth();
        }


        Sextuple<K1, K2, K3, V, MK, MV> findNextBreadth() {
            next = null;
            while (next == null && state == STATE.ROOTITER
                    && ((entryIter != null && entryIter.hasNext()) || rootIter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Sextuple.of(context.root, null, null, null, entry.getKey(),
                            entry.getValue());
                    break;
                } else if (rootIter.hasNext()) {
                    Entry<K1, Map<MK, MV>> rootMeta = rootIter.next();
                    entryIter = rootMeta.getValue().entrySet().iterator();
                    context = Sextuple.of(rootMeta.getKey(), null, null, null, null, null);
                }
            }
            if (next == null && state == STATE.ROOTITER) {
                context = Sextuple.nullSextuple();
                state = STATE.L2ITER;
            }

            while (next == null && state == STATE.L2ITER
                    && ((entryIter != null && entryIter.hasNext()) || level2Iter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Sextuple.of(context.root, context.l2, null, null, entry.getKey(),
                            entry.getValue());
                    break;
                } else if (level2Iter.hasNext()) {
                    Entry<Pair<K1, K2>, Map<MK, MV>> keyMeta = level2Iter.next();
                    entryIter = keyMeta.getValue().entrySet().iterator();
                    context = Sextuple.of(keyMeta.getKey().getLeft(), keyMeta.getKey().getRight(),
                            null, null, null, null);
                }
            }
            if (next == null && state == STATE.L2ITER) {
                state = STATE.L3ITER;
            }
            while (next == null && state == STATE.L3ITER
                    && ((entryIter != null && entryIter.hasNext()) || level3Iter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Sextuple.of(context.root, context.l2, context.l3, null, entry.getKey(),
                            entry.getValue());
                    break;
                } else if (level3Iter.hasNext()) {
                    Entry<Triple<K1, K2, K3>, Map<MK, MV>> keyMeta = level3Iter.next();
                    entryIter = keyMeta.getValue().entrySet().iterator();
                    context = Sextuple.of(keyMeta.getKey().getLeft(), keyMeta.getKey().getMiddle(),
                            keyMeta.getKey().getRight(), null, null, null);
                }
            }
            if (next == null && state == STATE.L3ITER) {
                state = STATE.LVITER;
            }
            while (next == null && state == STATE.LVITER
                    && ((entryIter != null && entryIter.hasNext()) || levelVIter.hasNext())) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Sextuple.of(context.root, context.l2, context.l3, context.l4,
                            entry.getKey(), entry.getValue());
                    break;
                } else if (levelVIter.hasNext()) {
                    Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> keyMeta = levelVIter.next();
                    entryIter = keyMeta.getValue().entrySet().iterator();
                    context = Sextuple.of(keyMeta.getKey().getRoot(), keyMeta.getKey().getL2(),
                            keyMeta.getKey().getL3(), keyMeta.getKey().getLeaf(), null, null);
                }
            }
            return next;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Sextuple<K1, K2, K3, V, MK, MV> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Sextuple<K1, K2, K3, V, MK, MV> toReturn = next; // new next will be computed now ...
            current = next;
            next = findNextBreadth();
            return toReturn;
        }

        public final void remove() {
            throw new RuntimeException("implementation not possible, sorry!");
        }
    }

    public Iterator<Sextuple<K1, K2, K3, V, MK, MV>> iteratorMetaBreadthFirst() {
        return new HashMap3LWithMetaIteratorBreadthFirst();
    }

    public Iterator<Sextuple<K1, K2, K3, V, MK, MV>> iteratorMetaBreadthFirstReverse() {
        throw new NotImplementedException("XXX sorry");
    }

    class HashMap3LWithMetaIteratorDepthFirst extends HashMap3LWithMetaIteratorBase
            implements Iterator<Sextuple<K1, K2, K3, V, MK, MV>> {

        HashMap3LWithMetaIteratorDepthFirst() {
            next = findNextDepth();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Sextuple<K1, K2, K3, V, MK, MV> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Sextuple<K1, K2, K3, V, MK, MV> toReturn = next; // new next will be computed now ...
            current = next;
            next = findNextDepth();
            return toReturn;
        }

        Sextuple<K1, K2, K3, V, MK, MV> findNextDepth() {
            next = null;
            while (next == null) {
                if (entryIter != null && entryIter.hasNext()) {
                    Entry<MK, MV> entry = entryIter.next();
                    next = Sextuple.of(context.root, context.l2, context.l3, context.l4, entry.getKey(), entry.getValue());
                    break;
                } else if (levelVIter.hasNext()) {
                    Entry<Quadruple<K1, K2, K3, V>, Map<MK, MV>> levelVMeta = levelVIter.next();
                    Quadruple<K1, K2, K3, V> key = levelVMeta.getKey();
                    context = Sextuple.of(key.getRoot(), key.getL2(), key.getL3(), key.getLeaf(), null, null);
                    Map<MK, MV> metaMap = levelVMeta.getValue();
                    if(metaMap != null) {
                        entryIter = metaMap.entrySet().iterator();
                        continue;
                    }
                } else if (level3Iter.hasNext()) {
                    Entry<Triple<K1, K2, K3>, Map<MK, MV>> level3Meta = level3Iter.next();
                    Triple<K1, K2, K3> key = level3Meta.getKey();
                    context = Sextuple.of(key.getLeft(), key.getMiddle(), key.getRight(), null, null, null);
                    Map<MK, MV> metaMap = level3Meta.getValue();
                    if(metaMap != null) {
                        entryIter = metaMap.entrySet().iterator();
                        continue;
                    }
                } else if (level2Iter.hasNext()) {
                    Entry<Pair<K1, K2>, Map<MK, MV>> level2Meta = level2Iter.next();
                    Pair<K1, K2> key = level2Meta.getKey();
                    context = Sextuple.of(key.getLeft(), key.getRight(), null, null, null, null);
                    Map<MK, MV> metaMap = level2Meta.getValue();
                    if(metaMap != null) {
                        entryIter = metaMap.entrySet().iterator();
                        continue;
                    }
                } else if (rootIter.hasNext()) {
                    Entry<K1, Map<MK, MV>> rootMeta = rootIter.next();
                    context = Sextuple.of(rootMeta.getKey(), null, null, null, null, null);
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

    public Iterator<Sextuple<K1, K2, K3, V, MK, MV>> iteratorMetaDepthFirst() {
        return new HashMap3LWithMetaIteratorDepthFirst();
    }

    public Iterator<Sextuple<K1, K2, K3, V, MK, MV>> iteratorMetaDepthFirstReverse() {
        throw new NotImplementedException("XXX sorry");
    }


    // ========================================================================
    // ==== Override parent methods to deal with Meta-Data accordingly ======
    // ========================================================================

    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public Map<K2, Map<K3, V>> remove(K1 k1) {
        this.metaRemoveAll(k1);
        return super.remove(k1);
    }

    // CAREFULL!! This might remove the contents of sub-map without user-knowledge
    @Override
    public Map<K3, V> remove(K1 k1, K2 k2) {
        this.metaRemoveAll(k1, k2);
        return super.remove(k1, k2);
    }

    @Override
    public V remove(K1 k1, K2 k2, K3 k3) {
        this.metaRemoveAll(k1, k2, k3);
        return super.remove(k1, k2, k3);
    }

    @Override
    public V removeValue(K1 k1, K2 k2, K3 k3, V v) {
        this.metaRemoveAll(k1, k2, k3, v);
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
        this.metaClearMapValues(k1);
        super.clear(k1);
    }

    @Override
    public void clear(K1 k1, K2 k2) {
        this.metaClearMapValues(k1, k2);
        super.clear(k1, k2);
    }
}
