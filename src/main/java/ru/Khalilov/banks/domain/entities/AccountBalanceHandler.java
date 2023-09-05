package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.AccountException;
import ru.Khalilov.banks.domain.exceptions.TariffException;
import ru.Khalilov.banks.domain.models.AccountDifference;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class is used to calculate differences in balance and cashback. All calculations are based on tariff
 */
public class AccountBalanceHandler {
    @NonNull @Getter
    private Tariff tariff;

    public AccountBalanceHandler(@NonNull Tariff tariff) {
        this.tariff = tariff;
    }

    /**
     * Calculates differences in balance after transaction
     * @param balance balance before transaction
     * @param sum sum of transaction. Positive if money adds to the account, negative otherwise
     * @return pair of balance diff and accumulator diff, wrapped into {@link AccountDifference}
     * @throws TariffException - if final balance is less than minimal balance
     */
    public AccountDifference handleTransaction(BigDecimal balance, BigDecimal sum) throws TariffException {
        BigDecimal balanceDiffResult = sum;
        BigDecimal accumulatedDiffResult = BigDecimal.ZERO;

        if (balance.compareTo(BigDecimal.ZERO) < 0 && sum.compareTo(BigDecimal.ZERO) < 0)
        {
            balanceDiffResult = balanceDiffResult.add(tariff.getTariffStats().negativeBalanceOperationTax());
        }

        if (balance.add(balanceDiffResult).compareTo(tariff.getTariffStats().minimalBalance()) < 0)
        {
            throw AccountException.tooLowFinalBalance();
        }

        return new AccountDifference(balanceDiffResult, accumulatedDiffResult);
    }

    /**
     * Calculates difference in balance and accumulator after end of day
     * @param balance current balance
     * @return pair of balance diff and accumulator diff, wrapped into {@link AccountDifference}
     */
    public AccountDifference handleDailyUpdate(BigDecimal balance) {
        BigDecimal balanceDiffResult = BigDecimal.ZERO;
        BigDecimal accumulatedDiffResult = balance.multiply(tariff.getTariffStats().balanceInterest())
                .divide(BigDecimal.valueOf(tariff.getClock().getDaysInYear()), RoundingMode.HALF_UP);

        return new AccountDifference(balanceDiffResult, accumulatedDiffResult);
    }

    /**
     * Calculates differences in balance and accumulator after end of month
     * @param balance current balance
     * @param accumulator current accumulator
     * @return pair of balance diff and accumulator diff, wrapped into {@link AccountDifference}
     */
    public AccountDifference handleMonthlyUpdate(BigDecimal balance, BigDecimal accumulator) {
        return new AccountDifference(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
