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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static com.alexkasko.unsafe.offheap.OffHeapUtils.free;
import static org.junit.Assert.assertArrayEquals;

/**
* User: alexkasko
* Date: 2/20/13
*/
public class OffHeapIntSorterTest {
//    private static final int THRESHOLD = 1 << 20;
    private static final int THRESHOLD = 1 << 16;

    @Test
    public void test() throws Exception {
        OffHeapIntArray la = null;
        try {
            int[] heap = gendata();
            int[] unsafe = heap.clone();
            Arrays.sort(heap);

            la = new OffHeapIntArray(THRESHOLD);
            for (int i = 0; i < THRESHOLD; i++) {
                la.set(i, unsafe[i]);
            }
            OffHeapIntSorter.sort(la, 0, THRESHOLD);
            for (int i = 0; i < THRESHOLD; i++) {
                unsafe[i] = la.get(i);
            }
            assertArrayEquals(heap, unsafe);
            la.free();
        } finally {
            free(la);
        }
    }

    @Test
    public void testComp() throws Exception {
        OffHeapIntArray la = null;
        try {
            int[] heap = gendata();
            Integer[] heapBoxed = new Integer[heap.length];
            for (int i = 0; i < heap.length; i++) {
                heapBoxed[i] = heap[i];
            }
            int[] unsafe = heap.clone();
            Arrays.sort(heapBoxed, new HeapReverseComp());

            la = new OffHeapIntArray(THRESHOLD);
            for (int i = 0; i < THRESHOLD; i++) {
                la.set(i, unsafe[i]);
            }
            OffHeapIntSorter.sort(la, 0, THRESHOLD, new OffHeapReverseComp());
            for (int i = 0; i < THRESHOLD; i++) {
                unsafe[i] = la.get(i);
            }
            int[] heapUnboxed = new int[heap.length];
            for(int i = 0; i < heap.length; i++) {
                heapUnboxed[i] = heapBoxed[i];
            }
            assertArrayEquals(heapUnboxed, unsafe);
            la.free();
        } finally {
            free(la);
        }
    }

    private static int[] gendata() throws Exception {
        Random random = new Random(42);
        int[] res = new int[THRESHOLD];
        for (int i = 0; i < THRESHOLD; i++) {
            res[i] = random.nextInt();
        }
        return res;
    }

    private static class HeapReverseComp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o2) return -1;
            if (o1 < o2) return 1;
            return 0;
        }
    }

    private static class OffHeapReverseComp implements OffHeapIntComparator {
        @Override
        public int compare(int l1, int l2) {
            if (l1 > l2) return -1;
            if (l1 < l2) return 1;
            return 0;
        }
    }
}
