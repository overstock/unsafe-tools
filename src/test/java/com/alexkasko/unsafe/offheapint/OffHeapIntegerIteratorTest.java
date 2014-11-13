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

import org.junit.Test;

import java.util.Iterator;

import static com.alexkasko.unsafe.offheap.OffHeapUtils.free;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: alexkasko
 * Date: 7/3/13
 */
public class OffHeapIntegerIteratorTest {
    @Test
    public void testArray() {
        OffHeapIntArray arr = null;
        try {
            arr = new OffHeapIntArray(4);
            arr.set(0, 41);
            arr.set(1, 42);
            arr.set(2, Integer.MIN_VALUE);
            arr.set(3, Integer.MAX_VALUE);
            Iterator<Integer> iter = arr.iterator();
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", 41,  iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", 42,  iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", Integer.MIN_VALUE, iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", Integer.MAX_VALUE, iter.next().intValue());
            assertFalse("hasNext fail", iter.hasNext());
        } finally {
            free(arr);
        }
    }

    @Test
    public void testArrayList() {
        OffHeapIntArrayList arr = null;
        try {
            arr = new OffHeapIntArrayList();
            arr.add(41);
            arr.add(42);
            arr.add(Integer.MIN_VALUE);
            arr.add(Integer.MAX_VALUE);
            Iterator<Integer> iter = arr.iterator();
            // next values must be ignored by iterator
            arr.add(43);
            arr.add(44);
            arr.add(45);
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", 41, iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", 42, iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", Integer.MIN_VALUE, iter.next().intValue());
            assertTrue("hasNext fail", iter.hasNext());
            assertEquals("Contents fail", Integer.MAX_VALUE, iter.next().intValue());
            assertFalse("hasNext fail", iter.hasNext());
        } finally {
            free(arr);
        }
    }
}
