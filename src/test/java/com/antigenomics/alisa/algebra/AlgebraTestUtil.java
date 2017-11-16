package com.antigenomics.alisa.algebra;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgebraTestUtil {
    static <O extends LinearSpaceObject<O>> void assertLSOValuesEqual(LinearSpaceObject<O> v1,
                                                                      LinearSpaceObject<O> v2) {
        assertTrue(v1.add(v2.multiply(-1.0)).norm2() < 1e-16);
    }
}
