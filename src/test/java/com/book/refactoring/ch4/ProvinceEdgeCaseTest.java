package com.book.refactoring.ch4;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProvinceEdgeCaseTest {
    Province noProducers() {
        return new Province(
            "No Producers", Collections.EMPTY_LIST, 30, 20
        );
    }

    @Test
    void getShortfall() {
        assertEquals(30, noProducers().getShortFall());
    }

    @Test
    void getProfit() {
        assertEquals(0, noProducers().getProfit());
    }

    @Test
    void zeroDemand() {
        Province asia = ProvinceTest.sampleProvinceData();
        asia.setDemand(0);

        assertEquals(-25, asia.getShortFall());
        assertEquals(0, asia.getProfit());
    }

    @Test
    void minusDemand() {
        Province asia = ProvinceTest.sampleProvinceData();
        asia.setDemand(-1);

        assertEquals(-26, asia.getShortFall());
        assertEquals(-10, asia.getProfit());
    }
}
