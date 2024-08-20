package com.book.refactoring.ch1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatementTest {

    @Test
    void test() {
        Statement_1 statement1 = new Statement_1();
        String result = statement1.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

    @Test
    void test2() {
        Statement_2 statement2 = new Statement_2();
        String result = statement2.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

    @Test
    void test3() {
        Statement_3 statement3 = new Statement_3();
        String result = statement3.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

}