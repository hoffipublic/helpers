package helpers.util;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import helpers.tuple.Quadruple;

public class UtilTests {

    @Test
    @Disabled
    public void hashMap2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new HashMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new HashMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        fillData2L(h2);

        for(Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("======================================================");
        for (Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            System.out.println(iter.next());
        }
        System.out.println("======================================================");
        for(Triple<String, String, String> triple : h2) {
            System.out.println(triple);
        }
    }

    @Test
    //@Disabled
    public void treeMap2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new TreeMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        fillData2L(h2);

        for(Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("======================================================");
        for (Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            System.out.println(iter.next());
        }
        System.out.println("======================================================");
        for(Triple<String, String, String> triple : h2) {
            System.out.println(triple);
        }
    }

    @Test
    //@Disabled
    public void linkedHashMap2LTest() {
        MapCloneable<String, Map<String, String>> rootMapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, String> level2MapPrototype = new LinkedHashMapCloneable<>();
        HashMap2L<String, String, String> h2 = new HashMap2L<>(rootMapPrototype, level2MapPrototype);

        fillData2L(h2);

        for(Map.Entry<String, String> entry : h2.get("eins").entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("======================================================");
        for(Iterator<Pair<String, String>> iter = h2.iterator("eins"); iter.hasNext();) {
            System.out.println(iter.next());
        }
        System.out.println("======================================================");
        for(Triple<String, String, String> triple : h2) {
            System.out.println(triple);
        }
    }

    private static void fillData2L(HashMap2L<String, String, String> h2) {
        h2.put("drei", "3", "drei");
        
        h2.put("zwei", "2.2", "zwei.zwei");
        h2.put("zwei", "2.1", "zwei.eins");

        h2.put("eins", "1.55", "eins.fünfundfünfzig");
        h2.put("eins", "1.51", "eins.einundfünfzig");
        h2.put("eins", "1.3", "eins.drei");
        h2.put("eins", "1.2", "eins.zwei");
        h2.put("eins", "1.1", "eins.eins");
        h2.put("eins", "1.4", "eins.vier");
    }


    @Test
    @Disabled
    public void hashMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new HashMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new HashMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new HashMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        System.out.println("======================================================");
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        System.out.println("======================================================");
        for (Quadruple<String, String, String, String> quadruple : h3) {
            System.out.println(quadruple);
        }
    }

    @Test
    public void treeMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new TreeMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new TreeMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        System.out.println("======================================================");
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        System.out.println("======================================================");
        for (Quadruple<String, String, String, String> quadruple : h3) {
            System.out.println(quadruple);
        }
    }

    @Test
    public void linkedHashMap3LTest() {
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, Map<String, String>> level2MapPrototype = new LinkedHashMapCloneable<>();
        MapCloneable<String, String> level3MapPrototype = new LinkedHashMapCloneable<>();
        HashMap3L<String, String, String, String> h3 =
                new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r1").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        System.out.println("======================================================");
        for (Map.Entry<String, Map<String, String>> outerEntry : h3.get("r2").entrySet()) {
            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                System.out.printf("%s : %s : %s\n", outerEntry.getKey(), innerEntry.getKey(), innerEntry.getValue());
            }
        }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        // System.out.println("======================================================");
        // for (Iterator<Pair<String, String>> iter = h3.iterator("r2"); iter.hasNext();) {
        //     System.out.println(iter.next());
        // }
        System.out.println("======================================================");
        for (Quadruple<String, String, String, String> quadruple : h3) {
            System.out.println(quadruple);
        }
    }


    private static void fillData3L(HashMap3L<String, String, String, String> h3) {
        h3.put("r2", "drei", "3", "drei");

        h3.put("r2", "zwei", "2.2", "zwei.zwei");
        h3.put("r2", "zwei", "2.1", "zwei.eins");

        h3.put("r1", "eins", "1.55", "eins.fünfundfünfzig");
        h3.put("r1", "eins", "1.51", "eins.einundfünfzig");
        h3.put("r1", "eins", "1.3", "eins.drei");
        h3.put("r2", "eins", "1.2", "eins.zwei");
        h3.put("r1", "eins", "1.1", "eins.eins");
        h3.put("r1", "eins", "1.4", "eins.vier");
    }
}
