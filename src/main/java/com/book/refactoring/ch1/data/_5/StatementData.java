package com.book.refactoring.ch1.data._5;

import com.book.refactoring.ch1.data.Performance;

/**
 * @see com.book.refactoring.ch1.Statement_5
 * 5부터 등장
 */
public record StatementData(String customer, PerformanceV2[] performances, int totalAmount, int totalVolumeCredits) {
}
