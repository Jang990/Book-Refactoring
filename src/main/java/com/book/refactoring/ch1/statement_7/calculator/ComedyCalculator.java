package com.book.refactoring.ch1.statement_7.calculator;

import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.statement_7.PerformanceCalculator;

public class ComedyCalculator extends PerformanceCalculator {

    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    public int amount() {
        int result = 30000;
        if (this.performance.audience() > 20) {
            result += 10000 + 500 * (this.performance.audience() - 20);
        }
        result += 300 * this.performance.audience();
        return result;
    }

    public int volumeCredits() {
        return super.volumeCredits() + this.performance.audience() / 5;
    }
}
