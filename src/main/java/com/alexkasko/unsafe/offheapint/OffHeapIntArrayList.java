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
 * <p>Implementation of array-list of long using {@link com.alexkasko.unsafe.offheap.OffHeapMemory}.
 * Memory area will be allocated another time and copied on elements adding. This class doesn't support elements removing.
 * {@link #get(long)} and {@link #set(long, long)} access operations indexes are checked using {@code assert} keyword
 * (indexes between size and capacity will be rejected).
 *
 * <p>Default implementation uses {@code sun.misc.Unsafe}, with all operations guarded with {@code assert} keyword.
 * With assertions enabled in runtime ({@code -ea} java switch) {@link AssertionError}
 * will be thrown on illegal index access. Without assertions illegal index will crash JVM.
 *
 * <p>Allocated memory may be freed manually using {@link #free()} (thread-safe
 * and may be called multiple times) or it will be freed after {@link OffHeapLongArray}
 * will be garbage collected.
 *
 * <p>Note: while class implements Iterable, iterator will create new autoboxed Long object
 * <b>on every</b> {@code next()} call, this behaviour is inevitable with iterators in java 6/7.
 *
 * @author alexkasko
 *         Date: 3/1/13
 */
public class OffHeapIntArrayList implements OffHeapIntAddressable, OffHeapDisposable, Iterable<Integer> {
    private static final int MIN_CAPACITY_INCREMENT = 24;
    private static final int ELEMENT_LENGTH = 4;

    private OffHeapMemory ohm;
    private long size;
    private long capacity;

    /**
     * Constructor, {@code 12} is used as initial capacity
     */
    public OffHeapIntArrayList() {
        this(MIN_CAPACITY_INCREMENT);
    }

    /**
     * Constructor
     *
     * @param capacity initial capacity
     */
    public OffHeapIntArrayList(long capacity) {
        this.capacity = capacity;
        this.ohm = OffHeapMemory.allocateMemory(capacity * ELEMENT_LENGTH);
    }

    /**
     * Adds element to the end of this list. Memory area will be allocated another time and copied
     * on capacity exceed.
     *
     * @param value value to add
     */
    public void add(int value) {
        OffHeapMemory oh = ohm;
        long s = size;
        if (s == capacity) {
            long len = s + (s < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : s >> 1);
            OffHeapMemory newOhm = OffHeapMemory.allocateMemory(len * ELEMENT_LENGTH);
            // maybe it's better to use Unsafe#reallocateMemory here
            oh.copy(0, newOhm, 0, oh.length());
            oh.free();
            ohm = newOhm;
            capacity = len;
        }
        size = s + 1;
        set(s, value);
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
     * Gets the element at position {@code index} from {@code 0} to {@code size-1}
     *
     * @param index list index
     * @return long value
     */
    @Override
    public int get(long index) {
        assert index < size : index;
        return ohm.getInt(index * ELEMENT_LENGTH);
    }

    /**
     * Sets the element at position {@code index} (from {@code 0} to {@code size-1}) to the given value
     *
     * @param index list index
     * @param value int value
     */
    @Override
    public void set(long index, int value) {
        assert index < size : index;
        ohm.putInt(index * ELEMENT_LENGTH, value);
    }

    /**
     * Returns number of elements in list
     *
     * @return number of elements in list
     */
    @Override
    public long size() {
        return size;
    }

    /**
     * Returns number of elements list may contain without additional memory allocation
     *
     * @return number of elements list may contain without additional memory allocation
     */
    public long capacity() {
        return capacity;
    }

    /**
     * Frees allocated memory, may be called multiple times from any thread
     */
    @Override
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
     * Resets the collection setting size to 0.
     * Actual memory contents stays untouched.
     */
    public void reset() {
        this.size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OffHeapIntArrayList");
        sb.append("{size=").append(size());
        sb.append(", capacity=").append(capacity);
        sb.append(", unsafe=").append(isUnsafe());
        sb.append('}');
        return sb.toString();
    }
}