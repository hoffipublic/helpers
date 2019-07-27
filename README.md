# hoffi's helpers

## util

Following examples show the usage of the 3-Leveled `HashMap3L<K1, K2, K3, V>` HashMap of HashMaps of HashMaps.

There is also an implemenation of a 2-Leveled `HashMap2L<K1, K2, V>` HashMap of HashMaps.

single dependency of implementation (beside `MapClonable` implementations in `helpers.util`)</br>
is `"org.apache.commons:commons-lang3:3.9"`</br>
(for `Pair` and `Triple` implementations (made my own `Quadruple` implementation on top of these))

implemented methods are:

```java
V                    put(K1 k1, K2 k2, K3 k3, V v);
Map<K2, Map<K3, V>>  get(K1 k1);
Map<K3, V>           get(K1 k1, K2 k2);
V                    get(K1 k1, K2 k2, K3 k3);
Map<K3, V>           getFirstBySubKey(K2 k2);
V                    getFirstBySubSubKey(K3 k3);
Map<K2, Map<K3, V>>  getAllBySubKey(K2 k2);
Map<K2, Map<K3, V>>  getAllBySubSubKey(K3 k3);
boolean              containsKey(K1 k1);
boolean              containsKey(K1 k1, K2 k2);
boolean              containsKey(K1 k1, K2 k2, K3 k3);
Map<K2, Map<K3, V>>  remove(K1 k1);
Map<K3, V>           remove(K1 k1, K2 k2);
V                    remove(K1 k1, K2 k2, K3 k3);
V                    removeValue(K1 k1, K2 k2, K3 k3, V v);
int                  size();
int                  size(K1 k1);
List<V>              getAllItems();
List<V>              getAllItems(K1 k1);
void                 clear();
void                 clear(K1 k1);
void                 clear(K1 k1, K2 k2);

Iterator<Quadruple<K1, K2, K3, V>>    iterator()
Spliterator<Quadruple<K1, K2, K3, V>> spliterator()
Stream<Quadruple<K1, K2, K3, V>>      stream()

Iterator<Triple<K2, K3, V>>           iterator(K1 rootKey)
Spliterator<Triple<K2, K3, V>>        spliterator(K1 rootKey)
Stream<Triple<K2, K3, V>>             stream(K1 rootKey)

Iterator<Pair<K3, V>>                 iterator(K1 rootKey, K2 k2)
Spliterator<Pair<K3, V>>              spliterator(K1 rootKey, K2 k2)
Stream<Pair<K3, V>>                   stream(K1 rootKey, K2 k2)

```

example usage:

```java
    private static void fillData3L(HashMap3L<String, String, String, String> h3) {
        h3.put("r2", "three", "3", "three");

        h3.put("r2", "two", "2.2", "two.two");
        h3.put("r2", "two", "2.1", "two.one");

        h3.put("r1", "one", "1.55", "one.fiftyfive");
        h3.put("r1", "one", "1.51", "one.fififtyone");
        h3.put("r1", "two", "2.2", "two.two");
        h3.put("r1", "one", "1.3", "one.three");
        h3.put("r2", "one", "1.2", "one.two"); // <-- "r2"!
        h3.put("r1", "one", "1.1", "one.one");
        h3.put("r1", "one", "1.4", "one.vier");
        h3.put("r1", "two", "2.11", "two.elf");
    }
```

</br>
</br>
mixed Maps three level (3L) HashMap of HashMap of HashMap using `stream()`</br>
(non-lazy implementation as it has to use `iterator()` in its implemenation):

```java
        // prototype definitions have to implement custom `MapClonable` interface
        // as Map implemenations `clone()` method is `protected`
        MapCloneable<String, Map<String, Map<String, String>>> rootMapPrototype;
        MapCloneable<String, Map<String, String>> level2MapPrototype;
        MapCloneable<String, String> level3MapPrototype;

        // use different types of Map implementations for the different levels of our HashMap to HashMap to HashMap
        rootMapPrototype = new TreeMapCloneable<>();
        level2MapPrototype = new LinkedHashMapCloneable<>();
        level3MapPrototype = new HashMapCloneable<>();

        // HashMap3L<rootKeysType, L2KeysType, L3KeysType, ValueType>
        HashMap3L<String, String, String, String> h3 = new HashMap3L<>(rootMapPrototype, level2MapPrototype, level3MapPrototype);

        fillData3L(h3);

        h3.stream().forEach(System.out::println);

        System.out.println("======================================================".toString());

        h3.stream().filter(i -> i.getRoot().equals("r1")).forEach(System.out::println);

        System.out.println("======================================================".toString());

        h3.stream("r1").forEach(System.out::println);

        System.out.println("======================================================".toString());

        h3.stream("r1", "one").forEach(System.out::println);
```

result:

```text
    (r1,one,1.1,one.one)
    (r1,one,1.3,one.three)
    (r1,one,1.4,one.vier)
    (r1,one,1.55,one.fiftyfive)
    (r1,one,1.51,one.fififtyone)
    (r1,two,2.2,two.two)
    (r1,two,2.11,two.elf)
    (r2,three,3,three)
    (r2,two,2.1,two.one)
    (r2,two,2.2,two.two)
    (r2,one,1.2,one.two)
    ======================================================
    (r1,one,1.1,one.one)
    (r1,one,1.3,one.three)
    (r1,one,1.4,one.vier)
    (r1,one,1.55,one.fiftyfive)
    (r1,one,1.51,one.fififtyone)
    (r1,two,2.2,two.two)
    (r1,two,2.11,two.elf)
    ======================================================
    (one,1.1,one.one)
    (one,1.3,one.three)
    (one,1.4,one.vier)
    (one,1.55,one.fiftyfive)
    (one,1.51,one.fififtyone)
    (two,2.2,two.two)
    (two,2.11,two.elf)
    ======================================================
    (1.1,one.one)
    (1.3,one.three)
    (1.4,one.vier)
    (1.55,one.fiftyfive)
    (1.51,one.fififtyone)
```

</br>
</br>
mixed Maps three level (3L) HashMap of HashMap of HashMap using `iterator()`:

```java
        for (Quadruple<String, String, String, String> quadruple : h3) {
            log.debug(quadruple.toString());
        }

        System.out.println("======================================================".toString());

        for (Iterator<Triple<String, String, String>> iter = h3.iterator("r1"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }

        System.out.println("======================================================".toString());

        for (Iterator<Pair<String, String>> iter = h3.iterator("r1", "one"); iter.hasNext();) {
            log.debug(iter.next().toString());
        }
```

result:

```text
    (r1,one,1.1,one.one)
    (r1,one,1.3,one.three)
    (r1,one,1.4,one.vier)
    (r1,one,1.55,one.fiftyfive)
    (r1,one,1.51,one.fififtyone)
    (r1,two,2.2,two.two)
    (r1,two,2.11,two.elf)
    (r2,three,3,three)
    (r2,two,2.1,two.one)
    (r2,two,2.2,two.two)
    (r2,one,1.2,one.two)
    ======================================================
    (one,1.1,one.one)
    (one,1.3,one.three)
    (one,1.4,one.vier)
    (one,1.55,one.fiftyfive)
    (one,1.51,one.fififtyone)
    (two,2.2,two.two)
    (two,2.11,two.elf)
    ======================================================
    (1.1,one.one)
    (1.3,one.three)
    (1.4,one.vier)
    (1.55,one.fiftyfive)
    (1.51,one.fififtyone)
```

### `HashMap3LWithMeta` data on keys and values (`...MapClonableForMeta`)

There is also a `HashMap3LWithMeta` class which `extends HashMap3L` and allows to `metaPut(...)` metaKey/metaValue's for each key (doesn't matter on which level).

You even can attach metaKey/metaValues's to values of the HashMap3L

Also you can find keys (`findAll...ByMeta(...)`) which are in the HashMap3L which have a specific metakey or a specific metakey with a specific metaValue (`findAll...ByMetaValue(...)`).

#### `HashMap3LWithMetaSameKeys`

For keeping the constructor more simple, there are helper classes which make instantiation of `HashMap3LWithMeta` more simple if you're using the same type of map instances and same type of keys throught the MapOfMapsWithMeta.

As Map of Maps for `HashMap3L and its MetaData contain of at least 3 different Datatypes:
- Key Types
- Value Type
- MetaValue Type

There are `...MapCloneable` Map instances subtypes `...MapCloneableForMeta` which implement `MapCloneableForMeta<K, V, MV>` needed `cloneXX()` methods to be able to do something like this:

```java
    HashMap3LWithMeta<String, String, String, String, String, String> h3m;
    h3m = new HashMap3LWithMetaSameKeys<String, String, String>(
                new TreeMapCloneableForMeta<String, String, String>()
    );
```

or, with individual Map instances on each key-level and for metadata:

```java
    HashMap3LWithMeta<String, String, String, String, String, String> h3m;
    h3m = new HashMap3LWithMetaSameKeys<String, String, String>(
                new HashMapCloneableForMeta<>(),
                new LinkedHashMapCloneableForMeta<>(),
                new TreeMapCloneableForMeta<>(),
                new TreeMapCloneableForMeta<>()
    );
```

usage as usual:

```java
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
```

</br>
</br>
</br>
</br>

THIS SOFTWARE IS PROVIDED BY THE AUTHOR `AS IS` AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
