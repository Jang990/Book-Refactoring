package com.book.refactoring.ch1;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data.Type;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement_1 {
    public String statement(Invoice invoices, Map<String, Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        String result = "청구 내역 (고객명: %s)\n".formatted(invoices.customer());

        // 미국 달러 형식으로 숫자를 포맷하는 NumberFormat 객체 생성
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        format.setMinimumFractionDigits(2);

        for (Performance perf : invoices.performances()) {
            final Play play = plays.get(perf.playId());
            int thisAmount = 0;

            switch (play.type()) {
                case TRAGEDY -> { // 비극
                    thisAmount = 40000;
                    if (perf.audience() > 30) {
                        thisAmount += 1000 * (perf.audience() - 30);
                    }
                }
                case COMEDY -> { // 희극
                    thisAmount = 30000;
                    if (perf.audience() > 20) {
                        thisAmount += 10000 + 500 * (perf.audience() - 20);
                    }
                    thisAmount += 300 * perf.audience();
                }
                default -> throw new IllegalArgumentException("알 수 없는 장르" + play.type());
            }

            // 포인트를 적립한다.
            volumeCredits += Math.max(perf.audience() - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if(play.type() == Type.COMEDY) volumeCredits += perf.audience() / 5;

            // 청구 내역을 출력한다.
            result += "  %s: %s (%d석)\n".formatted(play.name(), format.format((double)thisAmount / 100), perf.audience());
            totalAmount += thisAmount;
        }

        result += "총액: %s\n".formatted(format.format((double) totalAmount / 100));
        result += "적립 포인트: %d점\n".formatted(volumeCredits);
        return result;
    }
}
