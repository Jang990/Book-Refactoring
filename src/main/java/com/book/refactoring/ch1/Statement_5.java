package com.book.refactoring.ch1;

import com.book.refactoring.ch1.data.*;
import com.book.refactoring.ch1.data._5.PerformanceV2;
import com.book.refactoring.ch1.data._5.StatementData;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/*
OOO: 은 OOO함수의 변경사항 이라는 의미. 오해 소지가 있는 부분만 작성

복잡하게 얽힌 덩어리를 잘게 쪼개었고, 이제 HTML 버전으로 만드는 작업을 살펴본다.
TEXT버전과 HTML버전 모두 기존 함수들을 사용했으면 좋겠다.

다양한 해결책 중 저자가 가장 선호하는 방식은 단계 쪼개기다.
statement()의 로직을 두 단계로 나눈다.
    1. statement에 필요한 데이터 처리
    2. 앞서 처리한 결과를 텍스트나 HTML로 표현

1. 본문 전체를 별도 함수로 추출한다. -> renderPlainText
2. 중간 데이터인 StatementData 추가 후 renderPlainText로 전달
    계산코드는 statement로 renderPlainText는 data로 전달된 데이터만 처리하도록 만들 것이다.
3. statement: 고객정보를 StatementData로 옮기기
4. statement: 공연정보를 StatementData로 옮기기
5. renderPlainText: invoices 제거하기
6. enrichPerformance() 생성: playFor(), amountFor(), volumeCreditsFor() 대신 StatementData를 사용하도록 만들기
    책과 다르게 자바이기 때문에 PerformanceV2를 만들어서 play, amount, volumeCredits 값을 먼저 초기화할 때 넣어줌.
7. totalAmount(), totalVolumeCredits()대신 StatementData를 사용하도록 만들기.
    이 부분도 책과 다르게 자바이기 떄문에 초기화 값으로 넣어준다.
8. total 부분 반복문을 파이프라인화 - stream

6,7은 자바스크립트 코드를 약간 변형해서 옮겼다. 다음 단계에서 클래스를 분리하며 자연스럽게 만들어보겠다.
 */
public class Statement_5 {
    public Map<String, Play> plays;
    public String statement(Invoice invoices, Map<String, Play> plays) {
        this.plays = plays;

        PerformanceV2[] performanceV2 = Arrays.stream(invoices.performances())
                .map((perf) -> enrichPerformance(perf))
                .toArray(PerformanceV2[]::new);

        StatementData statementData = new StatementData(
                invoices.customer(),
                performanceV2,
                totalAmount(performanceV2),
                totalVolumeCredits(performanceV2)
        );
        return renderPlainText(statementData, plays);
    }

    private PerformanceV2 enrichPerformance(Performance perf) {
        return new PerformanceV2(
                perf.playId(),
                perf.audience(),
                playFor(perf),
                amountFor(perf),
                volumeCreditsFor(perf)
        );
    }

    private String renderPlainText(StatementData data, Map<String, Play> plays) {
        String result = "청구 내역 (고객명: %s)\n".formatted(data.customer());

        for (PerformanceV2 perf : data.performances()) {
            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(perf.play().name(), usd(perf.amount()), perf.audience());
        }

        result += "총액: %s\n".formatted(usd(data.totalAmount()));
        result += "적립 포인트: %d점\n".formatted(data.totalVolumeCredits());
        return result;
    }

    private int totalAmount(PerformanceV2[] data) {
        return Arrays.stream(data)
                .map(PerformanceV2::amount)
                .mapToInt(Integer::valueOf).sum();
    }

    private int totalVolumeCredits(PerformanceV2[] data) {
        return Arrays.stream(data)
                .map(PerformanceV2::volumeCredits)
                .mapToInt(Integer::valueOf).sum();
    }

    private String usd(int aNumber) {
        NumberFormat usd = NumberFormat.getCurrencyInstance(Locale.US);
        usd.setMinimumFractionDigits(2);
        return usd.format((double)aNumber / 100);
    }

    // 포인트 적립
    private int volumeCreditsFor(Performance perf) {
        int result = 0;
        result += Math.max(perf.audience() - 30, 0);
        if(playFor(perf).type() == Type.COMEDY)
            result += perf.audience() / 5;
        return result;
    }

    private Play playFor(Performance perf) {
        return this.plays.get(perf.playId());
    }

    // 요금 계산
    private int amountFor(Performance perf) {
        int result = 0; // thisAmount -> result 변수명 변경
        switch (playFor(perf).type()) {
            case TRAGEDY -> { // 비극
                result = 40000;
                if (perf.audience() > 30) {
                    result += 1000 * (perf.audience() - 30);
                }
            }
            case COMEDY -> { // 희극
                result = 30000;
                if (perf.audience() > 20) {
                    result += 10000 + 500 * (perf.audience() - 20);
                }
                result += 300 * perf.audience();
            }
            default -> throw new IllegalArgumentException("알 수 없는 장르" + playFor(perf));
        }
        return result;
    }
}
