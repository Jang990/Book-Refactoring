package com.book.refactoring.ch1.statement_6;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data.Type;
import com.book.refactoring.ch1.data._5.PerformanceV2;
import com.book.refactoring.ch1.data._5.StatementData;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/*
OOO: 은 OOO함수의 변경사항 이라는 의미. 오해 소지가 있는 부분만 작성

1. Statement와 StatementData 생성과정 분리
    책에서는 static 메서드처럼 외부 코드를 그냥 불러오는 식으로 사용했지만 어려울 것 같아서 객체로 진행한다.
 */
public class Statement_6 {

    public String statement(Invoice invoices, Map<String, Play> plays) {
        StatementDataCreator creator = new StatementDataCreator(plays);
        return renderPlainText(creator.createStatementData(invoices, plays));
    }

    private String renderPlainText(StatementData data) {
        String result = "청구 내역 (고객명: %s)\n".formatted(data.customer());

        for (PerformanceV2 perf : data.performances()) {
            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(perf.play().name(), usd(perf.amount()), perf.audience());
        }

        result += "총액: %s\n".formatted(usd(data.totalAmount()));
        result += "적립 포인트: %d점\n".formatted(data.totalVolumeCredits());
        return result;
    }

    private String usd(int aNumber) {
        NumberFormat usd = NumberFormat.getCurrencyInstance(Locale.US);
        usd.setMinimumFractionDigits(2);
        return usd.format((double)aNumber / 100);
    }
}
