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

statement에 누적값을 저장하는 volumeCredits 제거하기
누적값은 리펙터링하기 더 까다롭다. 반복문을 쪼개서 누적되는 부분을 따로 빼내야 한다.

1. statement: volumeCredits만 사용하는 반복문 쪼개기
2. statement: volumeCredits 변수를 반복문 바로 앞으로 옮기기
    int volumeCredits = 0;
    for (Performance perf : invoices.performances()) {
        volumeCredits += volumeCreditsFor(perf);
    }
3. 함수 추출 -> totalVolumeCredits
4. statement: volumeCredits 변수 인라인

반복문을 2번 돌리는게 성능에 미치는 영향이 미미할 때가 많음. 하드웨어는 생각보다 빠름.

만약 성능 저하가 발생한다면 리팩터링 후에 시간내어 성능을 개선하기.
이 과정에서 다시 되돌릴 수도 있음. 하지만 대체로 성능이 더 빨라짐.
즉 성능+ 코드-가독성+

5. totalAmount도 똑같이 쪼개주기. -> totalAmount()
 */
public class Statement_4 {
    public Map<String, Play> plays;
    public String statement(Invoice invoices, Map<String, Play> plays) {
        this.plays = plays;

        String result = "청구 내역 (고객명: %s)\n".formatted(invoices.customer());

        for (Performance perf : invoices.performances()) {
            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(playFor(perf).name(), usd(amountFor(perf)), perf.audience());
        }

        result += "총액: %s\n".formatted(usd(totalAmount(invoices)));
        result += "적립 포인트: %d점\n".formatted(totalVolumeCredits(invoices));
        return result;
    }

    private int totalAmount(Invoice invoices) {
        int totalAmount = 0;
        for (Performance perf : invoices.performances()) {
            totalAmount += amountFor(perf);
        }
        return totalAmount;
    }

    private int totalVolumeCredits(Invoice invoices) {
        int result = 0;
        for (Performance perf : invoices.performances()) {
            result += volumeCreditsFor(perf);
        }
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
