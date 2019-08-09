package helpers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import helpers.tuple.Quadruple;
import helpers.tuple.Quintuple;
import helpers.tuple.Sextuple;

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
    public void hashMap2LWithMeta_ExceptionsTest() {
        HashMap2LWithMeta<String, String, String, String, String> h2m;
        h2m = h2m();

        NoSuchElementException nsee = assertThrows(NoSuchElementException.class,
                () -> h2m.metaPut("nonex1", "m1", "mValue1"),
                "Expected metaPut(K1 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("m1"));

        nsee = assertThrows(NoSuchElementException.class,
                () -> h2m.metaPut("nonex1", "nonex2", "m1", "mValue1"),
                "Expected metaPut(K1 k1, K2 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("nonex2"));
        assertTrue(nsee.getMessage().contains("m1"));

        nsee = assertThrows(NoSuchElementException.class,
                () -> h2m.metaPut("nonex1", "nonex2", "nonex3", "m1", "mValue1"),
                "Expected metaPut(K1 k1, K2 k2, MK mk, MV mv) to throw NoSuchElementException, but it didn't");
        assertTrue(nsee.getMessage().contains("nonex1"));
        assertTrue(nsee.getMessage().contains("nonex2"));
        assertTrue(nsee.getMessage().contains("nonex3"));
        assertTrue(nsee.getMessage().contains("m1"));

    }

    @Test
    public void treeMap2LWithMeta2LTest() {
        HashMap2LWithMeta<String, String, String, String, String> h2m;
        h2m = h2m();

        fillData2L(h2m);

        boolean b;
        Map<String, String> m = h2m.metaGetMap("two");
        assertEquals(3, m.size());
        String s = h2m.metaGet("two", "KeyMetaForRoot_two.1");
        assertEquals("ValueMetaForRoot_two.1", s);
        s = h2m.metaGet("two", "2.2", "KeyMetaForLevel2_two2.2.2");
        assertEquals("ValueMetaForLevel2_two2.2.2", s);
        s = h2m.metaGet("two", "2.2", "twotwo", "KeyMetaForValue_two2.2.3.twotwo.2");
        assertEquals("ValueMetaForValue_two2.2.3.twotwo.2", s);
        
        s = h2m.metaRemoveKey("two", "KeyMetaForRoot_two.1");
        assertEquals("ValueMetaForRoot_two.1", s);
        assertEquals(2, m.size());
        String s2 = h2m.metaGet("two", "KeyMetaForRoot_two.1");
        assertEquals("ValueMetaForRoot_two.1", s);
        assertNull(s2);
        s = h2m.metaPut("two", "KeyMetaForRoot_two.1", "ValueMetaForRoot_two.1");
        assertNull(s);
        s = h2m.metaGet("two", "KeyMetaForRoot_two.1");
        assertEquals("ValueMetaForRoot_two.1", s);
        b = h2m.metaRemoveValue("two", "KeyMetaForRoot_two.1", "ValueMetaForRoot_two.1_different");
        assertFalse(b);

        m = h2m.metaGetMap("two", "2.2");
        assertEquals(4, m.size());
        s = h2m.metaRemoveKey("two", "2.2", "KeyMetaForLevel2_two2.2.2");
        assertEquals("ValueMetaForLevel2_two2.2.2", s);
        assertEquals(3, m.size());
        s2 = h2m.metaGet("two", "2.2", "KeyMetaForLevel2_two2.2.2");
        assertEquals("ValueMetaForLevel2_two2.2.2", s);
        assertNull(s2);
        s = h2m.metaPut("two", "2.2", "KeyMetaForLevel2_two2.2.2", "ValueMetaForLevel2_two2.2.2");
        assertNull(s);
        s = h2m.metaGet("two", "2.2", "KeyMetaForLevel2_two2.2.2");
        assertEquals("ValueMetaForLevel2_two2.2.2", s);
        b = h2m.metaRemoveValue("two", "2.2", "KeyMetaForLevel2_two2.2.2",
                "ValueMetaForLevel2_two2.2.2_different");
        assertFalse(b);


        
        h2m = h2m();
        Pair<Integer, Integer> quad = fillData2L(h2m);
        int count = 0;
        log.debug("====  forEach HashMap2L ==============================".toString());
        for (Triple<String, String, String> triple : h2m) {
            log.debug(triple.toString());
            count++;
        }
        assertEquals(9, count);
        count = 0;
        log.debug("====  findAllInRootByMeta HashMap2L ==============================".toString());
        for (String mv : h2m.findAllInRootByMeta("mkToFindKey_yes")) {
            log.debug(mv.toString());
            count++;
            assertTrue(Arrays.asList("two").contains(mv));
        }
        assertTrue(count > 0);
        log.debug("====  findAllInLevel2ByMeta HashMap2L =============================".toString());
        count = 0;
        for (Pair<String, String> p : h2m.findAllInLevel2ByMeta("mkToFindKey_yes")) {
            log.debug(p.toString());
            count++;
            assertTrue(Arrays.asList("(two,2.2)", "(one,1.2)").contains(p.toString()));
        }
        assertTrue(count > 0);
        log.debug("====  findAllInAllByMeta HashMap2L ================================".toString());
        count = 0;
        for (Triple<String, String, String> q : h2m.findAllInAllByMeta("mkToFindKey_yes")) {
            log.debug(q.toString());
            count++;
            assertTrue(
                Arrays.asList("(two,null,null)", "(two,2.2,null)", "(two,2.2,twoone)",
                "(two,2.2,twotwo)", "(one,1.2,null)").contains(q.toString()));
        }
        assertEquals(5, count);
        log.debug("====  findAllInAllByMetaValue HashMap2L ===========================".toString());
        count = 0;
        for (Triple<String, String, String> q : h2m.findAllInAllByMetaValue("mkToFindKey_yes", "mkToFindValue_yes")) {
            log.debug(q.toString());
            count++;
            assertTrue(Arrays.asList("(two,null,null)", "(two,2.2,null)", "(two,2.2,twoone)",
                    "(two,2.2,twotwo)").contains(q.toString()));
        }
        assertEquals(4, count);
    }

    @Test
    public void hashMap2LWithMeta_IteratorTest() {
        HashMap2LWithMeta<String, String, String, String, String> h2m;
        h2m = h2m();
        Pair<Integer, Integer> pair =
                fillData2L(h2m);

        int count = 0;
        log.debug("====  2L iteratorMetaBreadthFirst ====================================".toString());
        for (Iterator<Quintuple<String, String, String, String, String>> iter =
                h2m.iteratorMetaBreadthFirst(); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString().replaceAll(",null", ","));
        }
        assertEquals(pair.getLeft(), count);

        log.debug("====  2L iteratorMetaDepthFirst ======================================".toString());
        count = 0;
        for (Iterator<Quintuple<String, String, String, String, String>> iter =
                h2m.iteratorMetaDepthFirst(); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString().replaceAll(",null", ","));
        }
        assertEquals(pair.getLeft(), count);
    }


    private static Pair<Integer, Integer> fillData2L(HashMap2LWithMeta<String, String, String, String, String> h2m) {
        h2m.put("three", "3", "three");
        
        h2m.put("two", "2.2", "twotwo");
            h2m.metaPut("two", "KeyMetaForRoot_two.1", "ValueMetaForRoot_two.1");
            h2m.metaPut("two", "KeyMetaForRoot_two.2", "ValueMetaForRoot_two.2");
            h2m.metaPut("two", "mkToFindKey_yes", "mkToFindValue_yes");
            h2m.metaPut("two", "2.2", "KeyMetaForLevel2_two2.2.1", "ValueMetaForLevel2_two2.2.1");
            h2m.metaPut("two", "2.2", "mkToFindKey_yes", "mkToFindValue_yes");
            h2m.metaPut("two", "2.2", "KeyMetaForLevel2_two2.2.2", "ValueMetaForLevel2_two2.2.2");
            h2m.metaPut("two", "2.2", "KeyMetaForLevel2_two2.2.3", "ValueMetaForLevel2_two2.2.3");
            h2m.metaPut("two", "2.2", "twotwo", "KeyMetaForValue_two2.2.3.twotwo.1", "ValueMetaForValue_two2.2.3.twotwo.1");
            h2m.metaPut("two", "2.2", "twotwo", "KeyMetaForValue_two2.2.3.twotwo.2", "ValueMetaForValue_two2.2.3.twotwo.2");
            h2m.metaPut("two", "2.2", "twotwo", "mkToFindKey_yes", "mkToFindValue_yes");
        h2m.put("two", "2.1", "twoone");
            h2m.metaPut("two", "2.1", "twoone", "KeyMetaForValue_two2.1.twoone.1", "ValueMetaForValue_two2.1.twoone.1");
            h2m.metaPut("two", "2.1", "twoone", "KeyMetaForValue_two2.1.twoone.2", "ValueMetaForValue_two2.1.twoone.2");
            h2m.metaPut("two", "2.1", "twoone", "KeyMetaForValue_two2.1.twoone.3", "ValueMetaForValue_two2.1.twoone.3");
            h2m.metaPut("two", "2.2", "twoone", "mkToFindKey_yes", "mkToFindValue_yes");

        h2m.put("one", "1.55", "one.fiftyfive");
        h2m.put("one", "1.51", "one.fiftyone");
        h2m.put("one", "1.3", "one.three");
        h2m.put("one", "1.2", "one.two");
            h2m.metaPut("one", "1.2", "KeyMetaForLevel2_one1.2.onetwo.1", "ValueMetaForLevel2_one1.2.onetwo.1");
            h2m.metaPut("one", "1.2", "mkToFindKey_yes", "mkToFindValue_no");
        h2m.put("one", "1.1", "one.one");
        h2m.put("one", "1.4", "one.vier");

        // overall meta entries, entries for "two"
        return Pair.of(16, 14);
    }

    @Test
    public void hashMap3LWithMeta_ExceptionsTest() {
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
    public void hashMap3LWithMeta3LTest() {
        HashMap3LWithMeta<String, String, String, String, String, String> h3m;
        h3m = h3m();
        Quadruple<Integer, Quadruple<Integer, Integer, Integer, Integer>, Quadruple<Integer, Integer, Integer, Integer>, Integer>
            quad =  fillData3L(h3m);

        boolean b;
        String s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        s = h3m.metaRemoveKey("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        String s2 = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        assertNull(s2);
        s = h3m.metaGet("r1", "mk1");
        assertEquals("m1Value", s);
        s = h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");
        assertNull(s);
        b = h3m.metaRemoveValue("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value_different");
        assertFalse(b);
        s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertEquals("mr1.one.1.3.one.three.1_Value", s);
        b = h3m.metaRemoveValue("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");
        assertTrue(b);
        s = h3m.metaGet("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1");
        assertNull(s);
        s = h3m.metaPut("r1", "one", "1.3", "one.three", "mkr1.one.1.3.one.three.1", "mr1.one.1.3.one.three.1_Value");

        int count = 0;
        log.debug("====  forEach HashMap3L ==============================".toString());
        for (Quadruple<String, String, String, String> quadruple : h3m) {
            log.debug(quadruple.toString());
            count++;
        }
        assertEquals(11, count);
        count = 0;
        log.debug("====  findAllInRootByMeta HashMap3L ==============================".toString());
        for (String mv : h3m.findAllInRootByMeta("mkToFind")) {
            log.debug(mv.toString());
            count++;
            assertTrue(Arrays.asList("r2").contains(mv));
        }
        assertTrue(count > 0);
        log.debug("====  findAllInLevel2ByMeta HashMap3L =============================".toString());
        count = 0;
        for (Pair<String, String> p : h3m.findAllInLevel2ByMeta("mkToFind")) {
            log.debug(p.toString());
            count++;
            assertTrue(Arrays.asList("(r1,one)").contains(p.toString()));
        }
        assertTrue(count > 0);
        log.debug("====  findAllInLevel3ByMeta HashMap3L =============================".toString());
        count = 0;
        for (Triple<String, String, String> t : h3m.findAllInLevel3ByMeta("mkToFind")) {
            log.debug(t.toString());
            count++;
            assertTrue(Arrays.asList("(r1,one,1.3)", "(r1,two,2.2)").contains(t.toString()));
        }
        assertTrue(count > 0);
        log.debug("====  findAllInAllByMeta HashMap3L ================================".toString());
        count = 0;
        for (Quadruple<String, String, String, String> q : h3m.findAllInAllByMeta("mkToFind")) {
            log.debug(q.toString());
            count++;
            assertTrue(Arrays.asList("(r2,null,null,null)", "(r1,one,null,null)", "(r1,one,1.3,null)", "(r1,two,2.2,null)", "(r1,one,1.3,one.three)").contains(q.toString()));
        }
        assertEquals(5, count);
        log.debug("====  findAllInAllByMetaValue HashMap3L ===========================".toString());
        count = 0;
        for (Quadruple<String, String, String, String> q : h3m.findAllInAllByMetaValue("mkToFind", "mkToFindValue_yes")) {
            log.debug(q.toString());
            count++;
            assertTrue(Arrays.asList("(r2,null,null,null)", "(r1,one,null,null)", "(r1,one,1.3,null)", "(r1,two,2.2,null)").contains(q.toString()));
        }
        assertEquals(4, count);
    }

    @Test
    public void hashMap3LWithMeta_IteratorTest() {
        HashMap3LWithMeta<String, String, String, String, String, String> h3m;
        h3m = h3m();
        Quadruple<Integer, Quadruple<Integer, Integer, Integer, Integer>, Quadruple<Integer, Integer, Integer, Integer>, Integer>
            quad = fillData3L(h3m);

        int count = 0;
        log.debug("====  3L iteratorMetaBreadthFirst ====================================".toString());
        for(Iterator<Sextuple<String, String, String, String, String, String>> iter = h3m.iteratorMetaBreadthFirst(); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString().replaceAll(",null", ","));
        }
        assertEquals(quad.getRoot(), count, "iteratorMeta()");
        
        log.debug("====  3L iteratorMetaDepthFirst ======================================".toString());
        count = 0;
        for(Iterator<Sextuple<String, String, String, String, String, String>> iter = h3m.iteratorMetaDepthFirst(); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString().replaceAll(",null", ","));
        }
        assertEquals(quad.getRoot(), count, "iteratorMeta()");
    }


    private static Quadruple<Integer, Quadruple<Integer, Integer, Integer, Integer>, Quadruple<Integer, Integer, Integer, Integer>, Integer>
        fillData3L(HashMap3LWithMeta<String, String, String, String, String, String> h3m)
    {
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
            h3m.metaPut("r1", "one", "mkToFind", "mkToFindValue_yes");
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
            h3m.metaPut("r2", "one", "1.2", "one.two", "special", "root r2");
        h3m.put("r1", "one", "1.1", "one.one");
        h3m.put("r1", "one", "1.4", "one.vier");
        h3m.put("r1", "two", "2.11", "two.elf");

        // overallEntries,
        // Quadruple(entries on root,        entries on level2,             entrie on level 3,                     entries on level v),
        // Quadruple(entries on root("r1"), entries on level2("r1", "one"), entries on level3("r1", "one", "1.3"), entries on levelV("r1", "one", "1.3", "one.three")) 
        // entries on "r2", "one", "1.2" "one.two"
        return Quadruple.of(24, Quadruple.of(6, 4, 6, 8), Quadruple.of(4, 4, 4, 4), 1);
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

    private static HashMap2LWithMeta<String, String, String, String, String> h2m() {
        return new HashMap2LWithMetaSameKeys<String, String, String>(new TreeMapCloneable<String, String>());
    }

    private static HashMap3LWithMeta<String, String, String, String, String, String> h3m() {
        return new HashMap3LWithMetaSameKeys<String, String, String>(new TreeMapCloneable<String, String>());
    }
}
