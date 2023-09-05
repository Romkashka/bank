package ru.Khalilov.banks.domain.exceptions;

public class BalanceIntervalException extends AccountException{
    /**
     * Constructs BalanceIntervalException with the specified detail message
     *
     * @param message - the detail message
     */
    protected BalanceIntervalException(String message) {
        super(message);
    }

    /**
     * Creates BalanceIntervalException indicating attempt to create BalanceInterval object with lower bound higher than upper
     * @return BalanceIntervalException indicating attempt to create BalanceInterval object with lower bound higher than upper
     */
    public static BalanceIntervalException LowerBoundHigherThanUpper()
    {
        return new BalanceIntervalException("Lower bond must be les than upper");
    }
}
