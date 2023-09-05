package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.AccountException;
import ru.Khalilov.banks.domain.exceptions.TariffException;
import ru.Khalilov.banks.domain.models.AccountDifference;
import ru.Khalilov.banks.domain.models.AccountId;
import ru.Khalilov.banks.domain.models.TransactionEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Account contains balance and provides all necessary methods to change it
 */
public class Account implements ClockSubscriber {
    @NonNull @Getter
    private BigDecimal accumulator;
    @NonNull
    private LocalDateTime previousUpdateTime;
    @Getter
    private final int monthlyUpdateTime;
    @NonNull
    private final List<UUID> transactionEntityIDs;
    @NonNull @Getter
    private BigDecimal balance;
    @NonNull @Getter
    private final AccountId accountID;
    @NonNull @Getter
    private final LocalDateTime creationTime;
    @NonNull @Getter
    private final AccountBalanceHandler accountBalanceHandler;

    /**
     * Constructs account with given parameters. Not mentioned fields are set empty.
     * @param monthlyUpdateTime day in month, when proceeds monthly update of balance (mostly accrual of accumulator)
     * @param accountID {@link UUID} of this account
     * @param creationTime date and time of creation
     * @param accountBalanceHandler handler with chosen tariff
     * @throws AccountException if monthly update day is unreal (less 1 or more 31)
     */
    public Account(@NonNull AccountId accountID, @NonNull LocalDateTime creationTime, @NonNull AccountBalanceHandler accountBalanceHandler, int monthlyUpdateTime) throws AccountException {
        if (monthlyUpdateTime < 1 || monthlyUpdateTime > 31) {
            throw AccountException.unrealMonthlyUpdateDay(monthlyUpdateTime);
        }
        this.monthlyUpdateTime = monthlyUpdateTime;
        this.accountID = accountID;
        this.creationTime = creationTime;
        this.accountBalanceHandler = accountBalanceHandler;
        this.previousUpdateTime = accountBalanceHandler.getTariff().getClock().getDateTime();
        this.accumulator = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
        this.transactionEntityIDs = new ArrayList<>();
    }

    /**
     * Add transaction sum to current balance
     * @param transactionSum sum of transaction. Positive if balance is increasing, negative otherwise
     * @return {@link TransactionEntity} object fo this transaction
     * @throws AccountException if final balance is less than minimal balance specified by tariff
     */
    public TransactionEntity addTransactionSum(@NonNull BigDecimal transactionSum) throws AccountException, TariffException {
        AccountDifference calculatedDifference = accountBalanceHandler.handleTransaction(balance, transactionSum);
        if (balance.add(calculatedDifference.balanceDiff()).compareTo(accountBalanceHandler.getTariff().getTariffStats().minimalBalance()) < 0) {
            throw AccountException.notEnoughMoney();
        }

        balance = balance.add(calculatedDifference.balanceDiff());
        accumulator = accumulator.add(calculatedDifference.accumulatedDiff());

        UUID transactionID = UUID.randomUUID();
        transactionEntityIDs.add(transactionID);
        return new TransactionEntity(this, transactionSum, transactionID);
    }

    @Override
    public void updateThroughTime() {
        AccountDifference accountDifference = calculateUpdates(Duration.between(previousUpdateTime, accountBalanceHandler.getTariff().getClock().getDateTime()));
        balance = balance.add(accountDifference.balanceDiff());
        accumulator = accumulator.add(accountDifference.accumulatedDiff());
    }

    /**
     * Calculates the balance after some time, given that transactions have not been processed
     * @param duration period of time to simulate
     * @return the balance after some time, given that transactions have not been processed
     */
    public BigDecimal predict(@NonNull Duration duration) {
        AccountDifference accountDifference = calculateUpdates(duration);
        return balance.add(accountDifference.balanceDiff());
    }

    /**
     * Check if transaction with given id was completed and still isn't canceled
     * @param id id to check
     * @return true if contains, false otherwise
     */
    public boolean isContainsTransactionEntity(@NonNull UUID id) {
        return transactionEntityIDs.contains(id);
    }

    /**
     * Cancel transaction that hasn't been canceled yet
     * @param id id of transaction
     * @param sum sum to return to balance
     * @throws AccountException if given transaction wasn't completed or already canceled
     */
    public void cancelTransaction(@NonNull UUID id, @NonNull BigDecimal sum) throws AccountException {
        if (!isContainsTransactionEntity(id)) {
            throw AccountException.noSuchTransaction();
        }

        balance = balance.subtract(sum);
        transactionEntityIDs.remove(id);
    }

    @Override
    public String toString() {
        return "Account id: " + accountID + "\nTariff: " + accountBalanceHandler.getTariff().getTariffStats().name() + "\nBalance: " + balance;
    }

    private AccountDifference calculateUpdates(@NonNull Duration duration) throws AccountException {
        if (duration.isNegative()) {
            throw AccountException.negativeUpdateTimeSpan();
        }

        BigDecimal predictedBalance = BigDecimal.ZERO;
        BigDecimal predictedAccumulator = accumulator;

        //used to simulate time in calculations day by day
        LocalDateTime tmp = previousUpdateTime.minus(duration.minus(Duration.ofDays(duration.toDays())));

        int dayCounter = tmp.getDayOfYear() - previousUpdateTime.getDayOfYear();
        do {
            if (tmp.getDayOfMonth() == monthlyUpdateTime || tmp.getMonth() != previousUpdateTime.getMonth()) {
                AccountDifference accountDifference = accountBalanceHandler.handleMonthlyUpdate(predictedBalance, predictedAccumulator);
                predictedBalance = accountDifference.balanceDiff().add(predictedBalance).add(accountDifference.accumulatedDiff());
                predictedAccumulator = BigDecimal.ZERO;
            }

            if (previousUpdateTime.getDayOfYear() != tmp.getDayOfYear()) {
                AccountDifference accountDifference = accountBalanceHandler.handleDailyUpdate(predictedBalance);
                predictedBalance = predictedBalance.add(accountDifference.balanceDiff());
                predictedAccumulator = predictedAccumulator.add(accountDifference.accumulatedDiff());
            }

            dayCounter++;
            tmp = tmp.plusDays(1);
        } while (dayCounter < duration.toDays());

        previousUpdateTime = previousUpdateTime.plus(duration);
        return new AccountDifference(predictedBalance.subtract(balance), predictedAccumulator.subtract(accumulator));
    }
}
