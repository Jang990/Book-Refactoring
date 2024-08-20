package com.book.refactoring.ch1;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data.Type;

import java.util.Map;

public class TestConst {
    protected static final String CUSTOMER_NAME = "BigCo";
    protected static final String HAMLET = "hamlet",
            AS_LIKE = "as-like",
            OTHELLO = "othello";

    protected static final Map<String, Play> PLAYS = Map.of(
            HAMLET, new Play("Hamlet", Type.TRAGEDY),
            AS_LIKE, new Play("As You Like It", Type.COMEDY),
            OTHELLO, new Play("Othello", Type.TRAGEDY)
    );

    protected static final Invoice INVOICES = new Invoice(CUSTOMER_NAME, new Performance[]{
            new Performance(HAMLET, 55),
            new Performance(AS_LIKE, 35),
            new Performance(OTHELLO, 40)
    });

    protected static final String RESULT = """
            청구 내역 (고객명: BigCo)
              Hamlet: $650.00 (55석)
              As You Like It: $580.00 (35석)
              Othello: $500.00 (40석)
            총액: $1,730.00
            적립 포인트: 47점
            """;
}
