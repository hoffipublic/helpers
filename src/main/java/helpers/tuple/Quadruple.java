package helpers.tuple;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * <p>
 * A Quadruple consisting of four elements.
 * </p>
 *
 * <p>
 * This class is an abstract implementation defining the basic API. It refers to the elements as
 * 'root', 'level2', 'level3' and 'leaf'.
 * </p>
 *
 * <p>
 * Subclass implementations may be mutable or immutable. However, there is no restriction on the
 * type of the stored objects that may be stored. If mutable objects are stored in the Quadruple, then
 * the Quadruple itself effectively becomes mutable.
 * </p>
 *
 * @param <R>  the first(root) element type
 * @param <L2> the second(level2) element type
 * @param <L3> the third(level3) element type
 * @param <V>  the fourth(leaf/value) element type
 *
 * @since 3.2
 */
public abstract class Quadruple<R, L2, L3, V> implements Comparable<Quadruple<R, L2, L3, V>>, Serializable {

    private static final class QuadrupleAdapter<R, L2, L3, V> extends Quadruple<R, L2, L3, V> {

        private static final long serialVersionUID = 1L;

        @Override
        public R getRoot() {
            return null;
        }

        @Override
        public L2 getL2() {
            return null;
        }

        @Override
        public L3 getL3() {
            return null;
        }

        @Override
        public V getLeaf() {
            return null;
        }
    }

    /** Serialization version */
    private static final long serialVersionUID = 1L;

    /**
     * An empty array.
     * <p>
     * Consider using {@link #emptyArray()} to avoid generics warnings.
     * </p>
     *
     * @since 3.10.
     */
    public static final Quadruple<?, ?, ?, ?>[] EMPTY_ARRAY = new QuadrupleAdapter[0];

    /**
     * Returns the empty array singleton that can be assigned without compiler warning.
     *
     * @param <R>  the first(root) element type
     * @param <L2> the second(level2) element type
     * @param <L3> the third(level3) element type
     * @param <V>  the fourth(leaf/value) element type
     * @return the empty array singleton that can be assigned without compiler warning.
     *
     * @since 3.10.
     */
    @SuppressWarnings("unchecked")
    public static <R, L2, L3, V> Quadruple<R, L2, L3, V>[] emptyArray() {
        return (Quadruple<R, L2, L3, V>[]) EMPTY_ARRAY;
    }

    /**
     * <p>
     * Obtains an immutable Quadruple of four objects inferring the generic types.
     * </p>
     *
     * <p>
     * This factory allows the Quadruple to be created using inference to obtain the generic types.
     * </p>
     *
     * @param <R>    the first(root) element type
     * @param <L2>   the second(level2) element type
     * @param <L3>   the third(level3) element type
     * @param <V>    the fourth(leave/leaf) element type
     * @param root   the first(root) element, may be null
     * @param level2 the second(level2) element, may be null
     * @param level3 the third(level3) element, may be null
     * @param leaf  the fourth(leaf/value) element, may be null
     * @return a Quadruple formed from the four parameters, not null
     */
    public static <R, L2, L3, V> Quadruple<R, L2, L3, V> of(final R root, final L2 level2, final L3 level3, final V leaf) {
        return new ImmutableQuadruple<>(root, level2, level3, leaf);
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Compares the Quadruple based on the root element, followed by the level2 element,
     * followed by the level3 element, finally the leaf/value element. The types must be {@code Comparable}.
     * </p>
     *
     * @param other the other Quadruple, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final Quadruple<R, L2, L3, V> other) {
        return new CompareToBuilder()
                .append(getRoot(), other.getRoot())
                .append(getL2(), other.getL2())
                .append(getL3(), other.getL3())
                .append(getLeaf(), other.getLeaf())
                .toComparison();
    }

    /**
     * <p>
     * Compares this Quadruple to another based on the four elements.
     * </p>
     *
     * @param obj the object to compare to, null returns false
     * @return true if the elements of the Quadruple are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Quadruple<?, ?, ?, ?>) {
            final Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
            return Objects.equals(getRoot(), other.getRoot())
                    && Objects.equals(getL2(), other.getL2())
                    && Objects.equals(getL3(), other.getL3())
                    && Objects.equals(getLeaf(), other.getLeaf());
        }
        return false;
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the first(root) element from this Quadruple.
     * </p>
     *
     * @return the first(root) element, may be null
     */
    public abstract R getRoot();

    /**
     * <p>
     * Gets the second(level2) element from this Quadruple.
     * </p>
     *
     * @return the second(level2) element, may be null
     */
    public abstract L2 getL2();

    /**
     * <p>
     * Gets the third(level3) element from this Quadruple.
     * </p>
     *
     * @return the third(level3) element, may be null
     */
    public abstract L3 getL3();

    /**
     * <p>
     * Gets the fourth(leaf) element from this Quadruple.
     * </p>
     *
     * @return the fourth(leaf) element, may be null
     */
    public abstract V getLeaf();

    /**
     * <p>
     * Returns a suitable hash code.
     * </p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (getRoot() == null ? 0 : getRoot().hashCode())
                ^ (getL2() == null ? 0 : getL2().hashCode())
                ^ (getL3() == null ? 0 : getL3().hashCode())
                ^ (getLeaf() == null ? 0 : getLeaf().hashCode());
    }

    /**
     * <p>
     * Returns a String representation of this Quadruple using the format
     * {@code ($root,$level2,$level3,$leaf)}.
     * </p>
     *
     * @return a string describing this object, not null
     */
    @Override
    public String toString() {
        return "(" + getRoot() + "," + getL2() + "," + getL3() + "," + getLeaf() + ")";
    }

    /**
     * <p>
     * Formats the receiver using the given format.
     * </p>
     *
     * <p>
     * This uses {@link java.util.Formattable} to perform the formatting. Three variables may be
     * used to embed the root, level2, level3 and leaf elements. Use {@code %1$s} for the root element,
     * {@code %2$s} for the level2, {@code %3$s} for the level3 and {@code %4$s} for the leaf element. The default format used
     * by {@code toString()} is {@code (%1$s,%2$s,%3$s,%4$s)}.
     * </p>
     *
     * @param format the format string, optionally containing {@code %1$s}, {@code %2$s} {@code %3$s} and
     *               {@code %4$s}, not null
     * @return the formatted string, not null
     */
    public String toString(final String format) {
        return String.format(format, getRoot(), getL2(), getL3(), getLeaf());
    }

}
