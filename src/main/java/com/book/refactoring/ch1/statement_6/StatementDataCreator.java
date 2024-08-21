package com.book.refactoring.ch1.statement_6;

import com.book.refactoring.ch1.data.Invoice;
import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.data.Type;
import com.book.refactoring.ch1.data._5.PerformanceV2;
import com.book.refactoring.ch1.data._5.StatementData;

import java.util.Arrays;
import java.util.Map;

public class StatementDataCreator {
    private Map<String, Play> plays;

    public StatementDataCreator(Map<String, Play> plays) {
        this.plays = plays;
    }

    public StatementData createStatementData(Invoice invoices, Map<String, Play> plays) {
        this.plays = plays;

        PerformanceV2[] performanceV2 = Arrays.stream(invoices.performances())
                .map((perf) -> enrichPerformance(perf))
                .toArray(PerformanceV2[]::new);

        return new StatementData(
                invoices.customer(),
                performanceV2,
                totalAmount(performanceV2),
                totalVolumeCredits(performanceV2)
        );
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
