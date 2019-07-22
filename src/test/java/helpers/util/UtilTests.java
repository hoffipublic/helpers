package helpers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import helpers.tuple.Quadruple;

public class UtilTests {

    // Sets the package level to INFO
    private static ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    static {
        // rootLogger.setLevel(Level.DEBUG);
        rootLogger.setLevel(Level.INFO);
    }

    public static Logger log = org.slf4j.LoggerFactory.getLogger(UtilTests.class);
    @Test
    public void hashMap2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new HashMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new HashMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        Pair<Integer, Integer> sizesPerLevel = fillData2L(h2);

        log.debug("====  forEach   HashMap2L  ===========================".toString());
        int count = 0;
        for(Triple<String, String, String> triple : h2) {
            count++;
            log.debug(triple.toString());
        }
        assertEquals(sizesPerLevel.getLeft(), count);

        log.debug("====  2L 'eins' HashMap2L  ===========================".toString());
        count = 0;
        for (Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString());
        }
        assertEquals(sizesPerLevel.getRight(), count);

        log.debug("====  2L 'eins' HashMap2L conventional ==============".toString());
        count = 0;
        for(Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            count++;
            log.debug("{} : {}", entry.getKey(), entry.getValue());
        }
        assertEquals(sizesPerLevel.getRight(), count);
    }

    @Test
    public void treeMap2LTest() {
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

        log.debug("====  2L 'eins' TreeMap2L ============================".toString());
        count = 0;
        prevL2 = "";
        for (Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            count++;
            Pair<String, String> pair = iter.next();
            log.debug(pair.toString());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(pair.getLeft()) <= 0);
            prevL2 = pair.getLeft();
        }
        assertEquals(sizesPerLevel.getRight(), count);

        log.debug("====  2L 'eins' TreeMap2L conventional ===============".toString());
        count = 0;
        prevL2 = "";
        for (Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            count++;
            Pair<String, String> pair = Pair.of(entry.getKey(), entry.getValue());
            log.debug("{} : {}", entry.getKey(), entry.getValue());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(pair.getLeft()) <= 0);
            prevL2 = pair.getLeft();
        }
        assertEquals(sizesPerLevel.getRight(), count);
    }

    @Test
    public void linkedHashMap2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new LinkedHashMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        Pair<Integer, Integer> sizesPerLevel = fillData2L(h2);

        log.debug("====  forEach   LinkedHashMap2L =====================".toString());
        int count = 0;
        for (Triple<String, String, String> triple : h2) {
            count++;
            log.debug(triple.toString());
        }
        assertEquals(sizesPerLevel.getLeft(), count);

        log.debug("====  2L 'eins' LinkedHashMap2L ======================".toString());
        count = 0;
        for (Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            count++;
            log.debug(iter.next().toString());
        }
        assertEquals(sizesPerLevel.getRight(), count);

        log.debug("====  2L 'eins' LinkedHashMap2L conventional =========".toString());
        count = 0;
        for (Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            count++;
            log.debug("{} : {}", entry.getKey(), entry.getValue());
        }
        assertEquals(sizesPerLevel.getRight(), count);
    }

    private static Pair<Integer, Integer> fillData2L(HashMap2L<String, String, String> h2) {
        h2.put("drei", "3", "drei");
        
        h2.put("zwei", "2.2", "zwei.zwei");
        h2.put("zwei", "2.1", "zwei.eins");

        h2.put("eins", "1.55", "eins.fünfundfünfzig");
        h2.put("eins", "1.51", "eins.einundfünfzig");
        h2.put("eins", "1.3", "eins.drei");
        h2.put("eins", "1.2", "eins.zwei");
        h2.put("eins", "1.1", "eins.eins");
        h2.put("eins", "1.4", "eins.vier");

        // overall entries, entries for "eins"
        return Pair.of(9, 6);
    }


    @Test
    public void hashMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new HashMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new HashMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new HashMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        log.debug("====  forEach HashMap3L ==============================".toString());
        for (Quadruple<String, String, String, String> quadruple : h3) {
            log.debug(quadruple.toString());
        }
        log.debug("====  2L 'r1' HashMap3L ==============================".toString());
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
        log.debug("====  2L 'r2' HashMap3L ==============================".toString());
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
        log.debug("====  2L 'r1' HashMap3L conventional =================".toString());
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                log.debug("{} : {} : {}", outerEntry.getKey(), innerEntry.getKey(),
                        innerEntry.getValue());
            }
        }
        log.debug("===== 2L 'r2' HashMap3L conventional =================".toString());
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                log.debug("{} : {} : {}", outerEntry.getKey(), innerEntry.getKey(),
                        innerEntry.getValue());
            }
        }
        log.debug("==== 3L 'r1','r2' HashMap3L ==========================".toString());
        for (Iterator<Pair<String, String>> iter = h3.iterator("r1", "eins"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
    }

    @Test
    public void treeMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new TreeMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        Quadruple<Integer, Integer, Integer, Integer> quad =  fillData3L(h3);

        log.debug("====  forEach TreeMap3L ==============================".toString());
        int count = 0;
        String prevR = "";
        String prevL2 = "";
        String prevL3 = "";
        for (Quadruple<String, String, String, String> quadruple : h3) {
            count++;
            log.debug(quadruple.toString());
            // test if alphabetically ascending
            assertTrue(prevR.compareTo(quadruple.getRoot()) <= 0);
            if (!prevR.equals(quadruple.getRoot())) {
                prevL2 = "";
                prevL3 = "";
            } else if(!prevL2.equals(quadruple.getL2())) {
                prevL3 = "";
            }
            prevR = quadruple.getRoot();
            assertTrue(prevL2.compareTo(quadruple.getL2()) <= 0);
            prevL2 = quadruple.getL2();
            assertTrue(prevL3.compareTo(quadruple.getL3()) <= 0);
            prevL3 = quadruple.getL3();
        }
        assertEquals(quad.getRoot(), count);

        log.debug("====  2L 'r1' TreeMap3L ==============================".toString());
        count = 0;
        prevL2 = "";
        prevL3 = "";
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
            count++;
            Triple<String, String, String> triple = iter.next();
            log.debug(triple.toString());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(triple.getLeft()) <= 0);
            if (!prevL2.equals(triple.getLeft())) {
                prevL3 = "";
            }
            prevL2 = triple.getLeft();
            assertTrue(prevL3.compareTo(triple.getMiddle()) <= 0);
            prevL3 = triple.getMiddle();
        }
        assertEquals(quad.getL2(), count);
        
        log.debug("====  2L 'r2' TreeMap3L ==============================".toString());
        count = 0;
        prevL2 = "";
        prevL3 = "";
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
            count++;
            Triple<String, String, String> triple = iter.next();
            log.debug(triple.toString());
            // test if alphabetically ascending
            assertTrue(prevL2.compareTo(triple.getLeft()) <= 0);
            if (!prevL2.equals(triple.getLeft())) {
                prevL3 = "";
            }
            prevL2 = triple.getLeft();
            assertTrue(prevL3.compareTo(triple.getMiddle()) <= 0);
            prevL3 = triple.getMiddle();
        }
        assertEquals(quad.getL3(), count);

        log.debug("====  2L 'r1' TreeMap3L conventional =================".toString());
        count = 0;
        prevL2 = "";
        prevL3 = "";
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                count++;
                Triple<String, String, String> triple = Triple.of(outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
                log.debug("{} : {} : {}", triple.getLeft(), triple.getMiddle(), triple.getRight());
                // test if alphabetically ascending
                assertTrue(prevL2.compareTo(triple.getLeft()) <= 0);
                if (!prevL2.equals(triple.getLeft())) {
                    prevL3 = "";
                }
                prevL2 = triple.getLeft();
                assertTrue(prevL3.compareTo(triple.getMiddle()) <= 0);
                prevL3 = triple.getMiddle();
            }
        }
        assertEquals(quad.getL2(), count);

        log.debug("====  2L 'r2' TreeMap3L conventional =================".toString());
        count = 0;
        prevL2 = "";
        prevL3 = "";
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                count++;
                Triple<String, String, String> triple = Triple.of(outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
                log.debug("{} : {} : {}", triple.getLeft(), triple.getMiddle(), triple.getRight());
                // test if alphabetically ascending
                assertTrue(prevL2.compareTo(triple.getLeft()) <= 0);
                if (!prevL2.equals(triple.getLeft())) {
                    prevL3 = "";
                }
                prevL2 = triple.getLeft();
                assertTrue(prevL3.compareTo(triple.getMiddle()) <= 0);
                prevL3 = triple.getMiddle();
            }
        }
        assertEquals(quad.getL3(), count);

        log.debug("====  3L 'r1','eins' TreeMap3L =======================".toString());
        count = 0;
        prevL3 = "";
        for (Iterator<Pair<String, String>> iter = h3.iterator("r1", "eins"); iter.hasNext();) {
            count++;
            Pair<String, String> pair = iter.next();
            log.debug(pair.toString());
            // test if alphabetically ascending
            assertTrue(prevL3.compareTo(pair.getLeft()) <= 0);
            prevL3 = pair.getLeft();
        }
        assertEquals(quad.getLeaf(), count);
    }

    @Test
    public void linkedHashMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new LinkedHashMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        log.debug("====  forEach LinkedHashMap3L ========================".toString());
        for (Quadruple<String, String, String, String> quadruple : h3) {
            log.debug(quadruple.toString());
        }
        log.debug("====  2L 'r1' LinkedHashMap3L ========================".toString());
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
        log.debug("====  2L 'r2' LinkedHashMap3L ========================".toString());
        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
        log.debug("====  2L 'r1' LinkedHashMap3L conventional ===========".toString());
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                log.debug("{} : {} : {}", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        log.debug("====  2L 'r2' LinkedHashMap3L conventional ===========".toString());
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                log.debug("{} : {} : {}", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        log.debug("====  3L 'r1','eins'  LinkedHashMap3L =================".toString());
        for (Iterator<Pair<String, String>> iter = h3.iterator("r1", "eins"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
    }


    private static Quadruple<Integer, Integer, Integer, Integer> fillData3L(HashMap3L<String, String, String, String> h3) {
        h3.put("r2", "drei", "3", "drei");

        h3.put("r2", "zwei", "2.2", "zwei.zwei");
        h3.put("r2", "zwei", "2.1", "zwei.eins");

        h3.put("r1", "eins", "1.55", "eins.fünfundfünfzig");
        h3.put("r1", "eins", "1.51", "eins.einundfünfzig");
        h3.put("r1", "zwei", "2.2", "zwei.zwei");
        h3.put("r1", "eins", "1.3", "eins.drei");
        h3.put("r2", "eins", "1.2", "eins.zwei");
        h3.put("r1", "eins", "1.1", "eins.eins");
        h3.put("r1", "eins", "1.4", "eins.vier");
        h3.put("r1", "zwei", "2.11", "zwei.elf");

        // overallEntries, entriesOf "r1", entriesOf "r2", entriesOf "r1","eins" 
        return Quadruple.of(11, 7, 4, 5);
    }
}
