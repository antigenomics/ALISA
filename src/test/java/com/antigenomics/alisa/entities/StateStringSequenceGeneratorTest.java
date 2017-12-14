package com.antigenomics.alisa.entities;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateStringSequenceGeneratorTest {
    @Test
    public void testPerformance() {
        StateStringSequence sag = new StateStringSequence(20, 5);

        long startTime = System.currentTimeMillis();
        assertEquals(sag.parallelStream().count(), sag.getCharacteristicSize());
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("5AA generation parallel (~3 mln variants): "
                + endTime + "ms");

        startTime = System.currentTimeMillis();
        assertEquals(sag.stream().count(), sag.getCharacteristicSize());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("5AA generation single: "
                + endTime + "ms");

        System.out.println("^ Note that no heavy computation takes place here, so just measure parallel overhead");
    }

    @Test
    public void testConsistency() {
        StateStringSequence sag = new StateStringSequence(7, 4);
        Set<StateString> res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getCharacteristicSize());

        sag = new StateStringSequence(1, 4);
        res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getCharacteristicSize());

        sag = new StateStringSequence(10, 2);
        res = sag.stream().collect(Collectors.toSet());
        assertEquals(res.size(), sag.getCharacteristicSize());
    }

    @Test
    public void testIterator() {
        StateStringSequence sag = new StateStringSequence(2, 8, 100);

        int count = 0;
        for (StateString s : sag) {
            //System.out.println(Arrays.toString(arr));
            count++;
        }

        assertEquals(256, count);
        assertEquals(256, sag.getCharacteristicSize());

        sag = new StateStringSequence(2, 12, 100);

        count = 0;
        for (StateString s : sag) {
            count++;
        }

        assertEquals(4096, count);
    }

    @Test
    public void testStream() {
        StateStringSequence sag = new StateStringSequence(2, 10, 100);

        assertEquals(1024, sag.stream().count());

        sag = new StateStringSequence(2, 13, 10000);

        assertEquals(8192, sag.stream().count());
    }

    @Test
    public void testParallel() {
        StateStringSequence sag = new StateStringSequence(2, 10, 100);

        assertEquals(1024, sag.parallelStream().count());

        sag = new StateStringSequence(2, 13, 10000);

        assertEquals(8192, sag.parallelStream().count());
    }
}
