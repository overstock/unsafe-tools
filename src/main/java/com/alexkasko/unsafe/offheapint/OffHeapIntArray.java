/*
 * Copyright 2013 Alex Kasko (alexkasko.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexkasko.unsafe.offheapint;

import com.alexkasko.unsafe.offheap.OffHeapDisposable;
import com.alexkasko.unsafe.offheap.OffHeapDisposableIterator;
import com.alexkasko.unsafe.offheap.OffHeapMemory;

/**
 * <p>Implementation of array of long using {@link com.alexkasko.unsafe.offheap.OffHeapMemory}.
 *
 * <p>Default implementation uses {@code sun.misc.Unsafe}, with all operations guarded with {@code assert} keyword.
 * With assertions enabled in runtime ({@code -ea} java switch) {@link AssertionError}
 * will be thrown on illegal index access. Without assertions illegal index will crash JVM.
 *
 * <p>Array won't be zeroed after creation (will contain garbage by default).
 * Allocated memory may be freed manually using {@link #free()} (thread-safe
 * and may be called multiple times) or it will be freed after {@link OffHeapIntArray}
 * will be garbage collected.
 *
 * <p>Note: while class implements Iterable, iterator will create new autoboxed Long object
 * <b>on every</b> {@code next()} call, this behaviour is inevitable with iterators in java 6/7.
 *
 * @author alexkasko
 * Date: 2/22/13
 */
public class OffHeapIntArray implements OffHeapIntAddressable, OffHeapDisposable, Iterable<Integer> {
    private static final int ELEMENT_LENGTH = 4;

    private final OffHeapMemory ohm;

    /**
     * Constructor
     *
     * @param size number of elements in array
     */
    public OffHeapIntArray(long size) {
        this.ohm = OffHeapMemory.allocateMemory(size * ELEMENT_LENGTH);
    }

    /**
     * Private constructor for {@link #clone()} support
     *
     * @param ohm cloned memory instance
     */
    private OffHeapIntArray(OffHeapMemory ohm) {
        this.ohm = ohm;
    }

    /**
     * Whether unsafe implementation of {@link OffHeapMemory} is used
     *
     * @return whether unsafe implementation of {@link OffHeapMemory} is used
     */
    public boolean isUnsafe() {
        return ohm.isUnsafe();
    }

    /**
     * Gets the element at position {@code index}
     *
     * @param index array index
     * @return long value
     */
    @Override
    public int get(long index) {
        return ohm.getInt(index * ELEMENT_LENGTH);
    }

    /**
     * Sets the element at position {@code index} to the given value
     *
     * @param index array index
     * @param value long value
     */
    @Override
    public void set(long index, int value) {
        ohm.putInt(index * ELEMENT_LENGTH, value);
    }

    /**
     * Returns number of elements in array
     *
     * @return number of elements in array
     */
    @Override
    public long size() {
        return ohm.length() / ELEMENT_LENGTH;
    }

    /**
     * Frees allocated memory, may be called multiple times from any thread
     */
    public void free() {
        ohm.free();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffHeapDisposableIterator<Integer> iterator() {
        return new OffHeapIntegerIterator(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffHeapIntArray clone() {
        OffHeapMemory cloned = ohm.clone();
        return new OffHeapIntArray(cloned);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OffHeapIntArray");
        sb.append("{size=").append(size());
        sb.append(", unsafe=").append(isUnsafe());
        sb.append('}');
        return sb.toString();
    }
}
