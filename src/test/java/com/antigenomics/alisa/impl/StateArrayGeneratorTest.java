package com.antigenomics.alisa.impl;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateArrayGeneratorTest {
    @Test
    public void testPerformance() {
        StateArrayGenerator sag = new StateArrayGenerator(20, 5);

        long startTime = System.currentTimeMillis();
        assertEquals(sag.parallelStream().count(), sag.getSizeEstimate());
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("5AA generation parallel: "
                + endTime + "ms");

        startTime = System.currentTimeMillis();
        assertEquals(sag.stream().count(), sag.getSizeEstimate());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("5AA generation single: "
                + endTime + "ms");
    }

    @Test
    public void testConsistency() {
        StateArrayGenerator sag = new StateArrayGenerator(7, 4);
        Set<int[]> res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getSizeEstimate());

        sag = new StateArrayGenerator(1, 4);
        res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getSizeEstimate());

        sag = new StateArrayGenerator(10, 2);
        res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getSizeEstimate());
    }

    @Test
    public void testIterator() {
        StateArrayGenerator sag = new StateArrayGenerator(2, 8, 100);

        int count = 0;
        for (int[] arr : sag) {
            //System.out.println(Arrays.toString(arr));
            count++;
        }

        assertEquals(256, count);
        assertEquals(256, sag.getSizeEstimate());

        sag = new StateArrayGenerator(2, 12, 100);

        count = 0;
        for (int[] arr : sag) {
            count++;
        }

        assertEquals(4096, count);
    }

    @Test
    public void testStream() {
        StateArrayGenerator sag = new StateArrayGenerator(2, 10, 100);

        assertEquals(1024, sag.stream().count());

        sag = new StateArrayGenerator(2, 13, 10000);

        assertEquals(8192, sag.stream().count());
    }

    @Test
    public void testParallel() {
        StateArrayGenerator sag = new StateArrayGenerator(2, 10, 100);

        assertEquals(1024, sag.parallelStream().count());

        sag = new StateArrayGenerator(2, 13, 10000);

        assertEquals(8192, sag.parallelStream().count());
    }
}
