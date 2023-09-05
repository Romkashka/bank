package ru.Khalilov.banks.domain.models;

import ru.Khalilov.banks.domain.exceptions.BalanceIntervalException;

import java.math.BigDecimal;

/**
 * Represents interval of balance with equal interest on balance rate. Used in deposit account to calculate income based
 * on current account balance.
 *
 * @param lowerBound   Lower bound of account balance
 * @param upperBound   Upper bound of account balance
 * @param interestRate Annual interest rate applied to balance if it is within [lowerBound, upperBound)
 */
public record BalanceInterval(BigDecimal lowerBound, BigDecimal upperBound, BigDecimal interestRate) {
    public BalanceInterval {
        if (lowerBound.compareTo(upperBound) >= 0)
            throw BalanceIntervalException.LowerBoundHigherThanUpper();
    }

    /**
     * Checks if 2 balance intervals are intersecting each other
     * @param other - second interval
     * @return true if intervals intersects, false otherwise
     */
    public boolean isIntersects(BalanceInterval other) {
        return !(lowerBound.compareTo(other.upperBound) > 0 || upperBound.compareTo(other.lowerBound) < 0);
    }

}
