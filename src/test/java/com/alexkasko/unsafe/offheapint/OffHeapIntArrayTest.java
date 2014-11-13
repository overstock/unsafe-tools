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

/**
 * User: alexkasko
 * Date: 2/24/13
 */
public class OffHeapIntArrayTest {
    /**
     * 
     */
    @Test
    public void test() {
        OffHeapIntArray arr = null;
        try {
            arr = new OffHeapIntArray(4);
            arr.set(0, 41);
            arr.set(1, 42);
            arr.set(2, Integer.MIN_VALUE);
            arr.set(3, Integer.MAX_VALUE);
            assertEquals("Length fail", 4, arr.size());
            assertEquals("Contents fail", 41, arr.get(0));
            assertEquals("Contents fail", 42, arr.get(1));
            assertEquals("Contents fail", Integer.MIN_VALUE, arr.get(2));
            assertEquals("Contents fail", Integer.MAX_VALUE, arr.get(3));
        } finally {
            free(arr);
        }
    }
}
