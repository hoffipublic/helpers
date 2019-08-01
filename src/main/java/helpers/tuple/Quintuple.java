package helpers.tuple;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class Quintuple<R, L2, L3, L4, V>
        implements Comparable<Quintuple<R, L2, L3, L4, V>>, Serializable {
    private static final long serialVersionUID = 1L;

    public R root;
    public L2 l2;
    public L3 l3;
    public L4 l4;
    public V leaf;
    
    public static final Quintuple<?, ?, ?, ?, ?>[] EMPTY_ARRAY = new Quintuple[0];

    @SuppressWarnings("rawtypes")
    private static final Quintuple NULL = of(null, null, null, null, null);


    @SuppressWarnings("unchecked")
    public static <R, L2, L3, L4, V> Quintuple<R, L2, L3, L4, V>[] emptyArray() {
        return (Quintuple<R, L2, L3, L4, V>[]) EMPTY_ARRAY;
    }

    /**
     * Returns an immutable Quintuple of nulls.
     *
     * @param <R>  the left element of this Quintuple. Value is {@code null}.
     * @param <L2> the second(level2) element type
     * @param <L3> the third(level3) element type
     * @param <V>  the right element of this Quintuple. Value is {@code null}.
     * @return an immutable Quintuple of nulls.
     * @since 3.6
     */
    @SuppressWarnings("unchecked")
    public static <R, L2, L3, L4, V> Quintuple<R, L2, L3, L4, V> nullQuintuple() {
        return NULL;
    }


    private Quintuple() {

    }

    public static <R, L2, L3, L4, V> Quintuple<R, L2, L3, L4, V> of(R root, L2 l2, L3 l3, L4 l4, V leaf) {
        Quintuple<R, L2, L3, L4, V> instance = new Quintuple<>();
        instance.root = root;
        instance.l2 = l2;
        instance.l3 = l3;
        instance.l4 = l4;
        instance.leaf = leaf;
        return instance;
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Compares the Quintuple based on the root element, followed by the level2 element, followed by
     * the level3 element, finally the leaf/value element. The types must be {@code Comparable}.
     * </p>
     *
     * @param other the other Quintuple, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final Quintuple<R, L2, L3, L4, V> other) {
        return new CompareToBuilder().append(root, other.root)
                .append(l2, other.l2).append(l3, other.l3).append(l4, other.l4)
                .append(leaf, other.leaf).toComparison();
    }

    /**
     * <p>
     * Compares this Quintuple to another based on the four elements.
     * </p>
     *
     * @param obj the object to compare to, null returns false
     * @return true if the elements of the Quintuple are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Quintuple<?, ?, ?, ?, ?>) {
            final Quintuple<?, ?, ?, ?, ?> other = (Quintuple<?, ?, ?, ?, ?>) obj;
            return Objects.equals(root, other.root)
                    && Objects.equals(l2, other.l2)
                    && Objects.equals(l3, other.l3)
                    && Objects.equals(l4, other.l4)
                    && Objects.equals(leaf, other.leaf);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + root + "," + l2 + "," + l3 + "," + l4 + "," + leaf + ")";
    }

    public String toString(final String format) {
        return String.format(format, root, l2, l3, l4, leaf);
    }
}