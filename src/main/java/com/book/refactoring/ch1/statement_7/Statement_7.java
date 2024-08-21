package com.book.refactoring.ch1.statement_7;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data._5.PerformanceV2;
import com.book.refactoring.ch1.data._5.StatementData;
import com.book.refactoring.ch1.statement_7.calculator.ComedyCalculator;
import com.book.refactoring.ch1.statement_7.calculator.TragedyCalculator;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/*
OOO: 은 OOO함수의 변경사항 이라는 의미. 오해 소지가 있는 부분만 작성

amountFor, volumeCreditsFor 다형성 적용하기

1. StatementDataCreator.enrichPerformance: amountFor, volumeCreditsFor 계산 과정 객체로 분리하기
    1-1. Performance와 Play를 생성자로 받는 PerformanceCalculator.class 생성
    1-2. amountFor, volumeCreditsFor 로직 이동시키기
        Before
            return new PerformanceV2(
                    perf.playId(), perf.audience(),playFor(perf),
                    amountFor(perf), volumeCreditsFor(perf)
            );
        After
            PerformanceCalculator calculator = new PerformanceCalculator(perf, playFor(perf));
            return new PerformanceV2(
                    perf.playId(), perf.audience(), playFor(perf),
                    calculator.amount(), calculator.volumeCredits()
            );
2. 조건부(switch) 로직을 다형성으로 바꾸기.
    2-1. 팩토리 메소드 만들기 - Statement_7.createPerformanceCalculator(aPerformance, aPlay)
    2-2. TragedyCalculator, ComedyCalculator 서브클래스 생성
    2-3. amount(), volumeCredits()를 서브클래스로.
    2-4. PerformanceCalculator: amount()는 서브 클래스에서만 사용하도록 막기
 */
public class Statement_7 {

    static PerformanceCalculator createPerformanceCalculator(Performance aPerformance, Play aPlay) {
        return switch (aPlay.type()) {
            case TRAGEDY -> new TragedyCalculator(aPerformance, aPlay);
            case COMEDY -> new ComedyCalculator(aPerformance, aPlay);
            default -> throw new IllegalArgumentException("알 수 없는 장르" + aPlay.type());
        };
    }

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
