package com.book.refactoring.ch1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Statement_1Test {
    private Statement_1 statement1 = new Statement_1();
    private Statement_4_AmountFor statement4AmountFor = new Statement_4_AmountFor();

    @Test
    void test() {
        String result = statement1.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

    @Test
    void test4() {
        String result = statement4AmountFor.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

}