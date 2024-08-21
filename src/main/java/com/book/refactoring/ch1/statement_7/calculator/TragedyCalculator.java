package com.book.refactoring.ch1.statement_7.calculator;

import com.book.refactoring.ch1.data.Performance;
import com.book.refactoring.ch1.data.Play;
import com.book.refactoring.ch1.statement_7.PerformanceCalculator;

public class TragedyCalculator extends PerformanceCalculator {

    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    public int amount() {
        int result = 40000;
        if (super.performance.audience() > 30) {
            result += 1000 * (this.performance.audience() - 30);
        }
        return result;
    }
}
