package com.book.refactoring.ch1.data._5;

import com.book.refactoring.ch1.data.Play;

public record PerformanceV2(String playId, int audience, Play play, int amount, int volumeCredits) {
}
