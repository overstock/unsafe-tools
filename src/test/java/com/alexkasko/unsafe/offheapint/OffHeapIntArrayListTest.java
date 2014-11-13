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

import static com.alexkasko.unsafe.offheap.OffHeapUtils.free;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: alexkasko
 * Date: 3/1/13
 */
public class OffHeapIntArrayListTest {
    @Test
    public void test() {
        OffHeapIntArrayList list = null;
        try {
            list = new OffHeapIntArrayList();
            for (int i = 0; i < 42; i++) {
                list.add(42);
            }
            list.add(Integer.MIN_VALUE);
            list.add(Integer.MAX_VALUE);
            assertEquals("Size fail", 44, list.size());
            assertTrue("Capacity fail", list.capacity() >= 44);
            assertEquals("Contents fail", 42, list.get(0));
            assertEquals("Contents fail", 42, list.get(41));
            assertEquals("Contents fail", Integer.MIN_VALUE, list.get(42));
            assertEquals("Contents fail", Integer.MAX_VALUE, list.get(43));
        } finally {
            free(list);
        }
    }
}
