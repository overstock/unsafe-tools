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

/**
 * <h1>Long sized off-heap collections of ints with sorting and searching support</h1>
 *
 * <p>This package contains implementations of fixed-sized array of ints ({@link com.alexkasko.unsafe.offheapint.OffHeapIntArray}),
 * and growing array list of ints ({@link com.alexkasko.unsafe.offheaplong.OffHeapIntArrayList}). Both classes are implemented on top of
 * {@link com.alexkasko.unsafe.offheap.OffHeapMemory}.
 *
 * <h2>Features</h2>
 * <ul>
 *  <li>long indexes (size is not bounded by {@code Integer.MAX_VALUE})</li>
 *  <li>GC doesn't know about allocated memory so you can allocate gigabytes of memory without additional load on GC</li>
 *  <li>memory may be freed to OS eagerly (it's not mandatory, {@link com.alexkasko.unsafe.offheap.OffHeapMemory}
 *  will free allocated memory when it's instance will be garbage collected)</li>
 * </ul>
 *
 * <h2>Iterators</h2>
 * <p>Both collections implements {@link java.util.Iterable}, but {@code foreach} must be used with caution, because
 * in java 6/7 iterators causes new autoboxed {@link java.lang.Integer} object creation for each call to
 * {@link java.util.Iterator#next()}.
 *
 * <h2>Operations</h2>
 * <ul>
 *     <li>sorting using {@link com.alexkasko.unsafe.offheaplong.OffHeapIntSorter}: implementation of Dual-Pivot quicksort algorithm
 *      adapted to off-heap collections</li>
 *     <li>sorting with order defined by {@code com.alexkasko.unsafe.offheapint.OffHeapIntComparator}</li>
 *     <li>binary search over sorted collections using {@link com.alexkasko.unsafe.offheapint.OffHeapIntBinarySearch}</li>
 *     <li>binary search returning ranges of equal values:
 *     {@link com.alexkasko.unsafe.offheapint.OffHeapIntBinarySearch#binarySearchRange(com.alexkasko.unsafe.offheap.OffHeapAddressable, int, com.alexkasko.unsafe.offheaplong.OffHeapIntBinarySearch.IndexRange)}</li>
 * </ul>
 *
 * <h2>Usage example in tests (github links)</h2>
 * <ul>
 *  <li><a href="https://github.com/alexkasko/unsafe-tools/blob/master/src/test/java/com/alexkasko/unsafe/offheapint/OffHeapIntArrayTest.java">array</a></li>
 *  <li><a href="https://github.com/alexkasko/unsafe-tools/blob/master/src/test/java/com/alexkasko/unsafe/offheapint/OffHeapIntArrayListTest.java">array list</a></li>
 *  <li><a href="https://github.com/alexkasko/unsafe-tools/blob/master/src/test/java/com/alexkasko/unsafe/offheapint/OffHeapIntSorterTest.java">sorting</a></li>
 *  <li><a href="https://github.com/alexkasko/unsafe-tools/blob/master/src/test/java/com/alexkasko/unsafe/offheapint/OffHeapIntBinarySearchTest.java">binary search</a></li>
 * </ul>
 *
 */

package com.alexkasko.unsafe.offheapint;