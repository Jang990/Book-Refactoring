package com.book.refactoring.ch1;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data.Type;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/*
OOO: 은 OOO함수의 변경사항 이라는 의미. 오해 소지가 있는 부분만 작성

포인트 적립 함수 추출 + format 임시 변수 추출

play 변수 제거 결과로 적립 포인트 계산 부분이 추출하기 쉬워짐.
 포인트 적립 함수 추출
1. 포인트 적립 함수 추출 - volumeCreditsFor
2. volumeCreditsFor: 반환 변수명을 result로 변경

format같은 임시변수는 제거하는 편이 좋다.
 format 변수 제거
3. 함수 추출 - format(aNumber)
4. 함수의 핵심은 화폐맞추기이기 때문에 이름을 화폐단위로 정함. 함수명 변경 - format(aNumber) -> usd(aNumber)
formatAsUSD는 변수명이 너무 장황함.
5. usd: '/100' 단위 변환 로직을 함수안으로 이동시키기

 */
public class Statement_3 {
    public Map<String, Play> plays;
    public String statement(Invoice invoices, Map<String, Play> plays) {
        this.plays = plays;

        int totalAmount = 0;
        int volumeCredits = 0;
        String result = "청구 내역 (고객명: %s)\n".formatted(invoices.customer());

        for (Performance perf : invoices.performances()) {
            volumeCredits += volumeCreditsFor(perf);

            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(playFor(perf).name(), usd(amountFor(perf)), perf.audience());
            totalAmount += amountFor(perf);
        }

        result += "총액: %s\n".formatted(usd(totalAmount));
        result += "적립 포인트: %d점\n".formatted(volumeCredits);
        return result;
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
