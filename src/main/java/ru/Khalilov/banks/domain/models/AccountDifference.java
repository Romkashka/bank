package ru.Khalilov.banks.domain.models;

import java.math.BigDecimal;

/**
 * Used to pass differences in account's balance and accumulator after some calculations
 * @param balanceDiff - difference in balance
 * @param accumulatedDiff - difference in accumulator
 */
public record AccountDifference(BigDecimal balanceDiff, BigDecimal accumulatedDiff) {
}
