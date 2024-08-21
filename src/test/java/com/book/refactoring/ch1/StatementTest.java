package com.book.refactoring.ch1;

import com.book.refactoring.ch1.statement_6.StatementDataCreator;
import com.book.refactoring.ch1.statement_6.Statement_6;
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

    @Test
    void test4() {
        Statement_4 statement = new Statement_4();
        String result = statement.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }


    @Test
    void test5() {
        Statement_5 statement = new Statement_5();
        String result = statement.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

    @Test
    void test6() {
        Statement_6 statement = new Statement_6();
        String result = statement.statement(TestConst.INVOICES, TestConst.PLAYS);
        assertThat(result).isEqualTo(TestConst.RESULT);
    }

}