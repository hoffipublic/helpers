package helpers.util;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import helpers.tuple.Quadruple;

public class UtilEmptyTests {

    // Sets the package level to INFO
    private static ch.qos.logback.classic.Logger rootLogger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public static Logger log = org.slf4j.LoggerFactory.getLogger(UtilEmptyTests.class);

    static MapCloneable<String, String> m1Prototype;
    static MapCloneable<String, Map<String, String>> m2Prototype;
    static MapCloneable<String, Map<String, Map<String, String>>> m3Prototype;

    static {
        // rootLogger.setLevel(Level.DEBUG);
        rootLogger.setLevel(Level.INFO);

        m1Prototype = new HashMapCloneable<>();
        m2Prototype = new LinkedHashMapCloneable<>();
        m3Prototype = new TreeMapCloneable<>();
    }

    @Test
    public void emptyTests_hashMap2LTest() {
        String ls;
        List<String> lls;
        boolean lb;
        int li;
        Map<String, String> lmss;

        HashMap2L<String, String, String> h2;

        h2 = h2();
        lls = h2.getAllItems();
        h2 = h2();
        lmss = h2.get("Key1");
        h2 = h2();
        ls = h2.getFirstBySubKey("Key2");
        h2 = h2();
        lmss = h2.getAllBySubKey("Key2");
        h2 = h2();
        lb = h2.containsKey("Key1", "Key2");
        h2 = h2();
        lb = h2.containsKey("Key1");
        h2 = h2();
        ls = h2.remove("Key1", "Key2");
        h2 = h2();
        ls = h2.removeValue("Key1", "Key2", "Value");
        h2 = h2();
        lmss = h2.remove("Key1");
        h2 = h2();
        li = h2.size();
        h2 = h2();
        lls = h2.getAllItems();
        h2 = h2();
        h2.clear();
    }

    @Test
    public void emptyTests_hashMap3LTest() {

        String ls;
        boolean lb;
        int li;
        Map<String, String> lmss;
        Map<String, Map<String, String>> lmsmss;
        List<String> lls;

        HashMap3L<String, String, String, String> h3;
        
        h3 = h3();
        ls = h3.put("key1", "key2", "key3", "value");
        h3 = h3();
        lmsmss = h3.get("key1");
        h3 = h3();
        lmss = h3.get("key1", "key2");
        h3 = h3();
        ls = h3.get("key1", "key2", "key3");
        h3 = h3();
        lmss = h3.getFirstBySubKey("key2");
        h3 = h3();
        ls = h3.getFirstBySubSubKey("key3");
        h3 = h3();
        lmsmss = h3.getAllBySubKey("key2");
        h3 = h3();
        lmsmss = h3.getAllBySubSubKey("key3");
        h3 = h3();
        lb = h3.containsKey("key1");
        h3 = h3();
        lb = h3.containsKey("key1", "key2");
        h3 = h3();
        lb = h3.containsKey("key1", "key2", "key3");
        h3 = h3();
        lmsmss = h3.remove("key1");
        h3 = h3();
        lmss = h3.remove("key1", "key2");
        h3 = h3();
        ls = h3.remove("key1", "key2", "key3");
        h3 = h3();
        ls = h3.removeValue("key1", "key2", "key3", "value");
        h3 = h3();
        li = h3.size();
        h3 = h3();
        li = h3.size("key1");
        h3 = h3();
        lls = h3.getAllItems();
        h3 = h3();
        lls = h3.getAllItems("key1");
        h3 = h3();
        h3.clear();
        h3 = h3();
        h3.clear("key1");
        h3 = h3();
        h3.clear("key1", "key2");

    }
    
    @Test
    public void emptyTests_hashMap3LWithMetaTest() {
        String ls;
        List<String> lls;
        boolean lb;
        int li;
        Map<String, String> lmss;
        Map<String, Map<String, String>> lmsmss;
        Quadruple<String, String, String, String> lq;
        Triple<String, String, String> lt;
        Pair<String, String> lp;
        List<Quadruple<String, String, String, String>> llq;
        List<Triple<String, String, String>> llt;
        List<Pair<String, String>> llp;

        HashMap3LWithMeta<String, String, String, String, String, String> h3m;

        h3m = h3m();
        lmss = h3m.metaGetMap("key1");
        h3m = h3m();
        ls = h3m.metaGet("key1", "metaKey");
        h3m = h3m();
        lmss = h3m.metaGetMap("key1", "key2");
        h3m = h3m();
        ls = h3m.metaGet("key1", "key2", "metaKey");
        h3m = h3m();
        lmss = h3m.metaGetMap("key1", "key2", "key3");
        h3m = h3m();
        ls = h3m.metaGet("key1", "key2", "key3", "metaKey");
        h3m = h3m();
        lmss = h3m.metaGetMap("key1", "key2", "key3", "value");
        h3m = h3m();
        ls = h3m.metaGet("key1", "key2", "key3", "value", "metaKey");
        h3m = h3m();
        lmss = h3m.metaRemoveKey("key1");
        h3m = h3m();
        ls = h3m.metaRemove("key1", "metaKey");
        h3m = h3m();
        ls = h3m.metaRemoveValue("key1", "metaKey", "metaValue");
        h3m = h3m();
        h3m.metaClearMapValues("key1");
        h3m = h3m();
        h3m.metaMergeMapValues("key1", lmss);
        h3m = h3m();
        h3m.metaReplaceMapValues("key1", lmss);
        h3m = h3m();
        lb = h3m.metaContains("key1", "metaKey");
        h3m = h3m();
        lmss = h3m.metaRemoveKey("key1", "key2");
        h3m = h3m();
        ls = h3m.metaRemove("key1", "key2", "metaKey");
        h3m = h3m();
        ls = h3m.metaRemoveValue("key1", "key2", "metaKey", "metaValue");
        h3m = h3m();
        h3m.metaClearMapValues("key1", "key2");
        h3m = h3m();
        h3m.metaMergeMapValues("key1", "key2", lmss);
        h3m = h3m();
        h3m.metaReplaceMapValues("key1", "key2", lmss);
        h3m = h3m();
        lb = h3m.metaContains("key1", "key2", "metaKey");
        h3m = h3m();
        lmss = h3m.metaRemoveKey("key1", "key2", "key3");
        h3m = h3m();
        ls = h3m.metaRemove("key1", "key2", "key3", "metaKey");
        h3m = h3m();
        ls = h3m.metaRemoveValue("key1", "key2", "key3", "metaKey", "metaValue");
        h3m = h3m();
        h3m.metaClearMapValues("key1", "key2", "key3");
        h3m = h3m();
        h3m.metaMergeMapValues("key1", "key2", "key3", lmss);
        h3m = h3m();
        h3m.metaReplaceMapValues("key1", "key2", "key3", lmss);
        h3m = h3m();
        lb = h3m.metaContains("key1", "key2", "key3", "metaKey");
        h3m = h3m();
        lmss = h3m.metaRemoveKey("key1", "key2", "key3", "value");
        h3m = h3m();
        ls = h3m.metaRemove("key1", "key2", "key3", "value", "metaKey");
        h3m = h3m();
        ls = h3m.metaRemoveValue("key1", "key2", "key3", "value", "metaKey", "metaValue");
        h3m = h3m();
        h3m.metaClearMapValues("key1", "key2", "key3", "value");
        h3m = h3m();
        h3m.metaMergeMapValues("key1", "key2", "key3", "value", lmss);
        h3m = h3m();
        h3m.metaReplaceMapValues("key1", "key2", "key3", "value", lmss);
        h3m = h3m();
        lb = h3m.metaContains("key1", "key2", "key3", "value", "metaKey");
        h3m = h3m();
        lq =h3m.findFirstInAllByMeta("metaKey");
        h3m = h3m();
        lq =h3m.findLeastInAllByMeta("metaKey");
        h3m = h3m();
        lq =h3m.findFirstInAllByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        lq =h3m.findLeastInAllByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        ls = h3m.findFirstInRootByMeta("metaKey");
        h3m = h3m();
        ls = h3m.findFirstinRootByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        lp =h3m.findFirstInLevel2ByMeta("metaKey");
        h3m = h3m();
        lp =h3m.findFirstInLevel2ByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        lt = h3m.findFirstInLevel3ByMeta("metaKey");
        h3m = h3m();
        lt = h3m.findFirstInLevel3ByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        lq =h3m.findFirstInValuesByMeta("metaKey");
        h3m = h3m();
        lq =h3m.findFirstInValuesByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        llq =h3m.findAllInAllByMeta("metaKey");
        h3m = h3m();
        llq =h3m.findAllInAllByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        llq =h3m.findAllInAllByMetaReverse("metaKey");
        h3m = h3m();
        llq =h3m.findAllInAllByMetaValueReverse("metaKey", "metaValue");
        h3m = h3m();
        lls = h3m.findAllInRootByMeta("metaKey");
        h3m = h3m();
        lls = h3m.findAllinRootByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        llp =h3m.findAllInLevel2ByMeta("metaKey");
        h3m = h3m();
        llp =h3m.findAllInLevel2ByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        llt = h3m.findAllInLevel3ByMeta("metaKey");
        h3m = h3m();
        llt = h3m.findAllInLevel3ByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        llq =h3m.findAllInValuesByMeta("metaKey");
        h3m = h3m();
        llq =h3m.findAllInValuesByMetaValue("metaKey", "metaValue");
        h3m = h3m();
        lmsmss = h3m.remove("key1");
        h3m = h3m();
        lmss = h3m.remove("key1", "key2");
        h3m = h3m();
        ls = h3m.remove("key1", "key2", "key3");
        h3m = h3m();
        ls = h3m.removeValue("key1", "key2", "key3", "value");
        h3m = h3m();
        h3m.clear();
        h3m = h3m();
        h3m.clear("key1");
        h3m = h3m();
        h3m.clear("key1", "key2");
        h3m = h3m();
    }

    private static HashMap2L<String, String, String> h2() {
        return new HashMap2L<>(m2Prototype, m1Prototype);
    }
    
    private static HashMap3L<String, String, String, String> h3() {
        return new HashMap3L<>(m3Prototype, m2Prototype, m1Prototype);
    }

    // private static HashMap3LWithMeta<String, String, String, String, String, String> h3m() {
    //     return new HashMap3LWithMeta<>(m3Prototype, m2Prototype, m1Prototype, // maps for real values
    //                                    m1Prototype, // metadata maps
    //                                    m2Prototype, // rootKey K1 metadata
    //                                    m3Prototype, m2Prototype, // level2 K2 metadata
    //                                    m4Prototype, m3Prototype, m2Prototype, // level 3 K3 metadata
    //                                    m5Prototype, m4Prototype, m3Prototype, m2Prototype); // values metadata
    // }

    private static HashMap3LWithMeta<String, String, String, String, String, String> h3m() {
        return new HashMap3LWithMetaSameKeys<String, String, String>(
                        new HashMapCloneableForMeta<>(),
                        new LinkedHashMapCloneableForMeta<>(),
                        new TreeMapCloneableForMeta<>(),
                        new TreeMapCloneableForMeta<>()
                    );
    }
}
