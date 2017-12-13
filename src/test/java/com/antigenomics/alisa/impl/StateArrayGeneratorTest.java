package com.antigenomics.alisa.impl;

import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateArrayGeneratorTest {
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
