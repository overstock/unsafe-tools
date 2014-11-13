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

import com.alexkasko.unsafe.offheap.OffHeapDisposableIterable;

/**
 * Interface for off-heap collections with int elements.
 *
 * @author alexkasko
 * Date: 3/3/13
 */
public interface OffHeapIntAddressable extends OffHeapDisposableIterable<Integer> {
    /**
     * Gets the element at position {@code index}
     *
     * @param index collection index
     * @return long value
     */
    int get(long index);

    /**
     * Sets the element at position {@code index} to the given value
     *
     * @param index collection index
     * @param value long value
     */
    void set(long index, int value);
    
    /**
     * Returns number of elements in collection
     *
     * @return number of elements in collection
     */
    long size();
}
