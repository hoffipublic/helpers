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
 * A mutable quadruple consisting of four {@code Object} elements.
 * </p>
 *
 * <p>
 * Not #ThreadSafe#
 * </p>
 *
 * @param <R>  the first(root) element type
 * @param <L1> the second(level2) element type
 * @param <L2> the second(level2) element type
 * @param <V>  the fourth(leaf/value) element type
 *
 * @since 3.2
 */
public class MutableQuadruple<R, L2, L3, V> extends Quadruple<R, L2, L3, V> {

    /**
     * The empty array singleton.
     * <p>
     * Consider using {@link #emptyArray()} to avoid generics warnings.
     * </p>
     *
     * @since 3.10.
     */
    public static final MutableQuadruple<?, ?, ?, ?>[] EMPTY_ARRAY = new MutableQuadruple[0];

    /** Serialization version */
    private static final long serialVersionUID = 1L;

    /**
     * Returns the empty array singleton that can be assigned without compiler warning.
     *
     * @param <R>  the first(root) element type
     * @param <L1> the second(level2) element type
     * @param <L2> the second(level2) element type
     * @param <V>  the fourth(leaf/value) element type
     * @return the empty array singleton that can be assigned without compiler warning.
     *
     * @since 3.10.
     */
    @SuppressWarnings("unchecked")
    public static <R, L2, L3, V> MutableQuadruple<R, L2, L3, V>[] emptyArray() {
        return (MutableQuadruple<R, L2, L3, V>[]) EMPTY_ARRAY;
    }

    /**
     * <p>
     * Obtains a mutable quadruple of four objects inferring the generic types.
     * </p>
     *
     * <p>
     * This factory allows the quadruple to be created using inference to obtain the generic types.
     * </p>
     *
     * @param <R>    the first(root) element type
     * @param <L1>   the second(level2) element type
     * @param <L2>   the second(level2) element type
     * @param <V>    the fourth(leaf/value) element type
     * @param root   the first(root) element, may be null
     * @param level2 the second(level2) element, may be null
     * @param level3 the third(level3) element, may be null
     * @param leaf   the fourth(leaf/value) element, may be null
     * @return a quadruple formed from the four parameters, not null
     */
    public static <R, L2, L3, V> MutableQuadruple<R, L2, L3, V> of(final R root, final L2 level2, final L3 level3, final V leaf) {
        return new MutableQuadruple<>(root, level2, level3, leaf);
    }

    /** first(root) object */
    public R root;
    /** second(level2) object */
    public L2 level2;
    /** third(level3) object */
    public L3 level3;
    /** fourth(leaf/value) object */
    public V leaf;

    /**
     * Create a new quadruple instance of four nulls.
     */
    public MutableQuadruple() {
        super();
    }

    /**
     * Create a new quadruple instance.
     *
     * @param root   the root value, may be null
     * @param level2 the level2 value, may be null
     * @param right  the right value, may be null
     */
    public MutableQuadruple(final R root, final L2 level2, final L3 level3, final V leaf) {
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

    /**
     * Sets the root element of the quadruple.
     *
     * @param root the new value of the root element, may be null
     */
    public void setRoot(final R root) {
        this.root = root;
    }

    /**
     * Sets the level2 element of the quadruple.
     *
     * @param level2 the new value of the level2 element, may be null
     */
    public void setL2(final L2 level2) {
        this.level2 = level2;
    }

    /**
     * Sets the level3 element of the quadruple.
     *
     * @param level3 the new value of the level3 element, may be null
     */
    public void setL3(final L3 level3) {
        this.level3 = level3;
    }

    /**
     * Sets the leaf/value element of the quadruple.
     *
     * @param leaf the new value of the leaf/value element, may be null
     */
    public void setLeaf(final V leaf) {
        this.leaf = leaf;
    }
}
