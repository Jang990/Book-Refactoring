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

1. switch 구문 함수 추출 -> amountFor
(함수 추출하면 꼭 명확하게 표한할 수 있는 간단한 방법은 없는지 검토)
2. amountFor: thisAmount -> result 변수명 변경 (저자는 항상 반환할 값은 result로 함)
3. amountFor: play 변수 제거 (JS 내부 함수 개념이라 자바에는 맞지 않지만 전역변수로 선언해서 따라가 보도록 한다.)
    3-0. plays 전역변수 선언(자바이기 때문...)
    3-1. playFor 메서드 추출
    3-2. playFor: plays를 전역변수로 사용하도록 수정
    3-3. statement: playFor 변수 인라인하기
    3-4. amountFor: playFor를 사용하도록 수정하기
4. statement: amountFor를 받는 thisAmount 변수 인라인하기
 */
public class Statement_4_AmountFor {
    public Map<String, Play> plays;
    public String statement(Invoice invoices, Map<String, Play> plays) {
        this.plays = plays;

        int totalAmount = 0;
        int volumeCredits = 0;
        String result = "청구 내역 (고객명: %s)\n".formatted(invoices.customer());

        // 미국 달러 형식으로 숫자를 포맷하는 NumberFormat 객체 생성
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        format.setMinimumFractionDigits(2);

        for (Performance perf : invoices.performances()) {
            // 포인트를 적립한다.
            volumeCredits += Math.max(perf.audience() - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if(playFor(perf).type() == Type.COMEDY) volumeCredits += perf.audience() / 5;

            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(playFor(perf).name(), format.format((double)amountFor(perf) / 100), perf.audience());
            totalAmount += amountFor(perf);
        }

        result += "총액: %s\n".formatted(format.format((double) totalAmount / 100));
        result += "적립 포인트: %d점\n".formatted(volumeCredits);
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
            default -> throw new IllegalArgumentException("알 수 없는 장르 : " + playFor(perf).type());
        }
        return result;
    }
}
