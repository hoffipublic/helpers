package helpers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import helpers.tuple.Quadruple;

public class UtilWithMetaTests {

    // Sets the package level to INFO
    private static ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    
    public static Logger log = org.slf4j.LoggerFactory.getLogger(UtilTests.class);
    
    static MapCloneable<String, String> m1Prototype;
    static MapCloneable<String, Map<String, String>> m2Prototype;
    static MapCloneable<String, Map<String, Map<String, String>>> m3Prototype;
    static MapCloneable<String, Map<String, Map<String, Map<String, String>>>> m4Prototype;
    static MapCloneable<String, Map<String, Map<String, Map<String, Map<String, String>>>>> m5Prototype;
    
    static {
        // rootLogger.setLevel(Level.DEBUG);
        rootLogger.setLevel(Level.INFO);

        m1Prototype = new TreeMapCloneable<>();
        m2Prototype = new LinkedHashMapCloneable<>();
        m3Prototype = new HashMapCloneable<>();
        m4Prototype = new TreeMapCloneable<>();
        m5Prototype = new LinkedHashMapCloneable<>();
    }

    @Test
    @Disabled
    public void treeMapWithMeta2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new TreeMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        Pair<Integer, Integer> sizesPerLevel = fillData2L(h2);

        log.debug("====  forEach   TreeMap2L ===========================".toString());
        int count = 0;
        String prevR = "";
        String prevL2 = "";
        for (Triple<String, String, String> triple : h2) {
            count++;
            log.debug(triple.toString());
            // test if alphabetically ascending
            assertTrue(prevR.compareTo(triple.getLeft()) <= 0);
            if(!prevR.equals(triple.getLeft())) {
                prevL2 = "";
            }
            prevR = triple.getLeft();
            assertTrue(prevL2.compareTo(triple.getMiddle()) <= 0);
            prevL2 = triple.getMiddle();
        }
        assertEquals(sizesPerLevel.getLeft(), count);

        log.debug("====  2L 'one' TreeMap2L ============================".toString());
        count = 0;
        prevL2 = "";
        for (Iterator<Pair<String, String>> iter = h2.iterator("one"); iter.hasNext();) {
            count++;
            Pair<String, String> pair = iter.next();
            log.debug(pair.toString());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(pair.getLeft()) <= 0);
            prevL2 = pair.getLeft();
        }
        assertEquals(sizesPerLevel.getRight(), count);

        log.debug("====  2L 'one' TreeMap2L conventional ===============".toString());
        count = 0;
        prevL2 = "";
        for (Map.Entry<String, String> entry : h2.get("one").entrySet()) {
            count++;
            Pair<String, String> pair = Pair.of(entry.getKey(), entry.getValue());
            log.debug("{} : {}", entry.getKey(), entry.getValue());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(pair.getLeft()) <= 0);
            prevL2 = pair.getLeft();
        }
        assertEquals(sizesPerLevel.getRight(), count);
    }

    private static Pair<Integer, Integer> fillData2L(HashMap2L<String, String, String> h2) {
        h2.put("three", "3", "three");
        
        h2.put("two", "2.2", "two.two");
        h2.put("two", "2.1", "two.one");

        h2.put("one", "1.55", "one.fiftyfive");
        h2.put("one", "1.51", "one.fiftyone");
        h2.put("one", "1.3", "one.three");
        h2.put("one", "1.2", "one.two");
        h2.put("one", "1.1", "one.one");
        h2.put("one", "1.4", "one.vier");

        // overall entries, entries for "one"
        return Pair.of(9, 6);
    }

    @Test
    public void hashMapWithMeta_ExceptionsTest() {
        HashMap3LWithMeta<String, String, String, String, String, String> h3m;
        h3m = h3m();
        NoSuchElementException nsee = assertThrows(NoSuchElementException.class, () -> h3m.metaPut("nonex1", "m1", "mValue1"),
                "Expected metaPut(K1 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("m1"));
     
        nsee = assertThrows(NoSuchElementException.class, () -> h3m.metaPut("nonex1", "nonex2", "m1", "mValue1"),
                "Expected metaPut(K1 k1, K2 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("nonex2"));
        assertTrue(nsee.getMessage().contains("m1"));
     
        nsee = assertThrows(NoSuchElementException.class, () -> h3m.metaPut("nonex1", "nonex2", "nonex3", "m1", "mValue1"),
                "Expected metaPut(K1 k1, K2 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("nonex2"));
        assertTrue(nsee.getMessage().contains("nonex3"));
        assertTrue(nsee.getMessage().contains("m1"));
     
        nsee = assertThrows(NoSuchElementException.class, () -> h3m.metaPut("nonex1", "nonex2", "nonex3", "nonvalue", "m1", "mValue1"),
                "Expected metaPut(K1 k1, K2 k2, V v, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("nonex2"));
        assertTrue(nsee.getMessage().contains("nonex3"));
        assertTrue(nsee.getMessage().contains("nonvalue"));
        assertTrue(nsee.getMessage().contains("m1"));
    }


    @Test
    public void hashMapWithMeta3LTest() {
        HashMap3LWithMeta<String, String, String, String, String, String> h3m;
        h3m = h3m();
        Quadruple<Integer, Integer, Integer, Integer> quad =  fillData3L(h3m);

        String s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        s = h3m.metaRemove("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        String s2 = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        assertNull(s2);
        s = h3m.metaGet("r1", "mk1");
        assertEquals("m1Value", s);
        s = h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");
        assertNull(s);
        s = h3m.metaRemoveValue("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value_different");
        assertNull(s);
        s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        s = h3m.metaRemoveValue("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertNull(s);
        s = h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");


        log.debug("====  forEach HashMap3L ==============================".toString());
        for (Quadruple<String, String, String, String> quadruple : h3m) {
            log.debug(quadruple.toString());
        }
        log.debug("====  findAllInRootByMeta HashMap3L ==============================".toString());
        for (String mv : h3m.findAllInRootByMeta("mkToFind")) {
            log.debug(mv.toString());
        }
        log.debug("====  findAllInLevel3ByMeta HashMap3L =============================".toString());
        for (Triple<String, String, String> t : h3m.findAllInLevel3ByMeta("mkToFind")) {
            log.debug(t.toString());
        }
        log.debug("====  findAllInAllByMeta HashMap3L ================================".toString());
        for (Quadruple<String, String, String, String> q : h3m.findAllInAllByMeta("mkToFind")) {
            log.debug(q.toString());
        }
        log.debug("====  findAllInAllByMetaValue HashMap3L ===========================".toString());
        for (Quadruple<String, String, String, String> q : h3m.findAllInAllByMetaValue("mkToFind", "mkToFindValue_yes")) {
            log.debug(q.toString());
        }
    }


    private static Quadruple<Integer, Integer, Integer, Integer> fillData3L(HashMap3LWithMeta<String, String, String, String, String, String> h3m) {
        h3m.put("r2", "three", "3", "three");
            h3m.metaPut("r2", "mkToFind", "mkToFindValue_yes");
            h3m.metaPut("r2", "three", "3", "three", "mkr1.three.3.three.1", "mr1.three.3.three.1");
            h3m.metaPut("r2", "three", "3", "three", "mkr1.three.3.three.2", "mr1.three.3.three.2");
            h3m.metaPut("r2", "three", "3", "three", "mkr1.three.3.three.3", "mr1.three.3.three.3");
        h3m.put("r2", "two", "2.2", "two.two");
        h3m.put("r2", "two", "2.1", "two.one");

        h3m.put("r1", "one", "1.55", "one.fiftyfive");
            h3m.metaPut("r1", "mk1", "m1Value");
        h3m.put("r1", "one", "1.51", "one.fififtyone");
            h3m.metaPut("r1", "one", "mk1.one", "m1.one_Value");
        h3m.put("r1", "two", "2.2", "two.two");
            h3m.metaPut("r1", "two", "2.2", "mk1.two.2.2", "m1.two.2.2_Value");
            h3m.metaPut("r1", "two", "2.2", "mkToFind", "mkToFindValue_yes");
        h3m.put("r1", "one", "1.3", "one.three");
            h3m.metaPut("r1", "mkr1.2", "mr1.r1.2_Value");
            h3m.metaPut("r1", "mkr1.1", "mr1.r1.1_Value");
            h3m.metaPut("r1", "mkr1.3", "mr1.r1.3_Value");
            h3m.metaPut("r1", "one", "mkr1.one.1.3", "mr1.one.1.3_Value");
            h3m.metaPut("r1", "one", "mkr1.one.1.2", "mr1.one.1.2_Value");
            h3m.metaPut("r1", "one", "mkr1.one.1.1", "mr1.one.1.1_Value");
            h3m.metaPut("r1", "one", "1.3", "mkr1.one.1.3.3", "mr1.one.1.3.3_Value");
            h3m.metaPut("r1", "one", "1.3", "mkr1.one.1.3.1", "mr1.one.1.3.1_Value");
            h3m.metaPut("r1", "one", "1.3", "mkr1.one.1.3.2", "mr1.one.1.3.2_Value");
            h3m.metaPut("r1", "one", "1.3", "mkToFind", "mkToFindValue_yes");
            h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.3", "mr1.one.1.3.one.three.3_Value");
            h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.2", "mr1.one.1.3.one.three.2_Value");
            h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");
            h3m.metaPut("r1", "one", "1.3", "one.three", "mkToFind", "mkToFindValue_r1.other");
        h3m.put("r2", "one", "1.2", "one.two"); // <-- "r2"!
        h3m.put("r1", "one", "1.1", "one.one");
        h3m.put("r1", "one", "1.4", "one.vier");
        h3m.put("r1", "two", "2.11", "two.elf");

        // overallEntries, entriesOf "r1", entriesOf "r2", entriesOf "r1","one" 
        return Quadruple.of(11, 7, 4, 5);
    }


    // private static HashMap3LWithMeta<String, String, String, String, String, String> h3m() {
    //     return new HashMap3LWithMeta<>(
    //                m3Prototype, m2Prototype, m1Prototype, // maps for real values
    //                m1Prototype, // metadata maps
    //                m2Prototype, // rootKey K1 metadata
    //                m3Prototype, m2Prototype, // level2 K2 metadata
    //                m4Prototype, m3Prototype, m2Prototype, // level 3 K3 metadata
    //                m5Prototype, m4Prototype, m3Prototype, m2Prototype
    //     ); // values metadata
    // }

    private static HashMap3LWithMeta<String, String, String, String, String, String> h3m() {
        return new HashMap3LWithMetaSameKeys<String, String, String>(new TreeMapCloneableForMeta<String, String, String>());
    }
}
