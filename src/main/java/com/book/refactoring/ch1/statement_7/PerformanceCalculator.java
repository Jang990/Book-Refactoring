package com.book.refactoring.ch1.statement_7;

import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;

public class PerformanceCalculator {
    protected Performance performance;
    protected Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public int amount() {
        throw new IllegalStateException("서브 클래스에서 처리하도록 설계되었습니다.");
    }

    public int volumeCredits() {
        return Math.max(this.performance.audience() - 30, 0);
    }
}
