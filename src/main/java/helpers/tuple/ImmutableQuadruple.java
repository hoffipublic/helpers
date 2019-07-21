/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package helpers.tuple;

/**
 * <p>
 * An immutable quadrupel consisting of four {@code Object} elements.
 * </p>
 *
 * <p>
 * Although the implementation is immutable, there is no restriction on the objects that may be
 * stored. If mutable objects are stored in the quadruple, then the quadruple itself effectively becomes
 * mutable. The class is also {@code final}, so a subclass can not add undesirable behaviour.
 * </p>
 *
 * <p>
 * #ThreadSafe# if all four objects are thread-safe
 * </p>
 *
 * @param <R> the first(root) element type
 * @param <L1> the second(level2) element type
 * @param <L2> the second(level2) element type
 * @param <V> the fourth(leaf/value) element type
 *
 * @since 3.2
 */
public final class ImmutableQuadruple<R, L2, L3, V> extends Quadruple<R, L2, L3, V> {

    /**
     * An empty array.
     * <p>
     * Consider using {@link #emptyArray()} to avoid generics warnings.
     * </p>
     *
     * @since 3.10.
     */
    public static final ImmutableQuadruple<?, ?, ?, ?>[] EMPTY_ARRAY = new ImmutableQuadruple[0];

    /**
     * An immutable quadruple of nulls.
     */
    // This is not defined with generics to avoid warnings in call sites.
    @SuppressWarnings("rawtypes")
    private static final ImmutableQuadruple NULL = of(null, null, null, null);

    /** Serialization version */
    private static final long serialVersionUID = 1L;

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
    public static <R, L2, L3, V> ImmutableQuadruple<R, L2, L3, V>[] emptyArray() {
        return (ImmutableQuadruple<R, L2, L3, V>[]) EMPTY_ARRAY;
    }

    /**
     * Returns an immutable quadruple of nulls.
     *
     * @param <R>  the left element of this quadruple. Value is {@code null}.
     * @param <L2> the second(level2) element type
     * @param <L3> the third(level3) element type
     * @param <V>  the right element of this quadruple. Value is {@code null}.
     * @return an immutable quadruple of nulls.
     * @since 3.6
     */
    @SuppressWarnings("unchecked")
    public static <R, L2, L3, V> ImmutableQuadruple<R, L2, L3, V> nullQuadruple() {
        return NULL;
    }

    /**
     * <p>
     * Obtains an immutable quadruple of four objects inferring the generic types.
     * </p>
     *
     * <p>
     * This factory allows the quadruple to be created using inference to obtain the generic types.
     * </p>
     *
     * @param <R>    the first(root) element type
     * @param <L2>   the second(level2) element type
     * @param <L3>   the third(level3) element type
     * @param <V>    the fourth(leaf/value) element type
     * @param root   the first(root) element, may be null
     * @param level2 the second(level2) element, may be null
     * @param level3 the third(level3) element, may be null
     * @param leaf   the fourth(leaf/value) element, may be null
     * @return a quadruple formed from the four parameters, not null
     */
    public static <R, L2, L3, V> ImmutableQuadruple<R, L2, L3, V> of(final R root, final L2 level2,
            final L3 level3, final V leaf) {
        return new ImmutableQuadruple<>(root, level2, level3, leaf);
    }

    /** first(root) object */
    public final R root;
    /** second(level2) object */
    public final L2 level2;
    /** third(level3) object */
    public final L3 level3;
    /** fourth(leaf/value) object */
    public final V leaf;

    /**
     * Create a new quadruple instance.
     *
     * @param root   the first(root) element, may be null
     * @param level2 the second(level2) element, may be null
     * @param level3 the third(level3) element, may be null
     * @param leaf   the fourth(leaf/value) element, may be null
     */
    public ImmutableQuadruple(final R root, final L2 level2, final L3 level3, final V leaf) {
        super();
        this.root = root;
        this.level2 = level2;
        this.level3 = level3;
        this.leaf = leaf;
    }

    // -----------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public R getRoot() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L2 getL2() {
        return level2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public L3 getL3() {
        return level3;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public V getLeaf() {
        return leaf;
    }
}
