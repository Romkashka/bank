package ru.Khalilov.banks.domain.models;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Describes parameters of tariff in a bank
 * @param depositPercentages - list of annual deposit interest rates
 * @param name - name of tariff
 * @param accountType - type of tariff ('deposit', 'credit', 'debit' etc). Can be omitted or not totally correct
 * @param balanceInterest - annual interest rate on account balance
 * @param negativeBalanceOperationTax - fixed tax for operations with a negative balance
 * @param minimalBalance - lower bound of balance value
 * @param addOnlyPeriod - period of time since account creation during which balance can be only increased
 * @param id - {@link UUID} of this tariff stats
 */
public record TariffStats(
        List<BalanceInterval> depositPercentages,
        String name,
        String accountType,
        BigDecimal balanceInterest,
        BigDecimal negativeBalanceOperationTax,
        BigDecimal minimalBalance,
        Duration addOnlyPeriod,
        UUID id) {

    /**
     * Returns string with general information about tariff
     * @return string with general information about tariff
     */
    public String toStringStatsOnly()
    {
        return "Balance interest: " + balanceInterest + "\n" +
            "Tax for operation while negative balance: " + negativeBalanceOperationTax + "\n" +
            "Minimal balance: " + minimalBalance + "\n" +
            "Add-only period: " + addOnlyPeriod;
    }
}
