package com.book.refactoring.ch1.statement_7;

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
        PerformanceCalculator calculator = Statement_7.createPerformanceCalculator(perf, playFor(perf));
        return new PerformanceV2(
                perf.playId(),
                perf.audience(),
                playFor(perf),
                calculator.amount(),
                calculator.volumeCredits()
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

    private Play playFor(Performance perf) {
        return this.plays.get(perf.playId());
    }


}
