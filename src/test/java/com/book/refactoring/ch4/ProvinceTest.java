package com.book.refactoring.ch4;

import com.book.refactoring.ch4.dto.ProducerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProvinceTest {
    Province asia;

    @BeforeEach
    void beforeEach() {
        asia = sampleProvinceData();
    }

    static public Province sampleProvinceData() {
        return new Province(
                "Asia",
                List.of(
                        new ProducerData("Byzantium", 10, 9),
                        new ProducerData("Attalia", 12, 10),
                        new ProducerData("Sinope", 10, 6)
                ),
                30,
                20
        );
    }

    @Test
    void getShortfall() {
        assertEquals(5, asia.getShortFall());
    }

    @Test
    void getProfit() {
        assertEquals(230, asia.getProfit());
    }

//    @Test
    void invalidTest() {
        // 이렇게 하면 안된다. 각각의 독립 테스트가 하나의 픽처스를 공유하여 문제가 발생할 수 있다.
        // beforeEach를 사용하자.
        Province asia = sampleProvinceData();
        assertEquals(5, asia.getShortFall());
        assertEquals(230, asia.getProfit());
    }

    @Test
    void changeProduction() {
        asia.getProducers().get(0).setProduction(20);
        assertThat(asia.getShortFall()).isEqualTo(-6);
        assertThat(asia.getProfit()).isEqualTo(292);
    }

}